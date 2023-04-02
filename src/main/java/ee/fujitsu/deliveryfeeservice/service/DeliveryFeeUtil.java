package ee.fujitsu.deliveryfeeservice.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ee.fujitsu.deliveryfeeservice.exception.InvalidDataException;
import ee.fujitsu.deliveryfeeservice.model.Station;
import ee.fujitsu.deliveryfeeservice.state.Region;
import ee.fujitsu.deliveryfeeservice.state.Vehicle;
import ee.fujitsu.deliveryfeeservice.xml.ObservationXML;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static ee.fujitsu.deliveryfeeservice.state.AirTemperature.calcAirTemperatureExtraFee;
import static ee.fujitsu.deliveryfeeservice.state.Phenomenon.calcWeatherPhenomenonExtraFee;
import static ee.fujitsu.deliveryfeeservice.state.WindSpeed.calcWindSpeedExtraFee;

@Slf4j
@UtilityClass
public class DeliveryFeeUtil {
    private static final String WEATHER_STATION_API = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final Set<String> stationNames = Region.getStationNames();

    public static List<Station> getStationWeatherData() {
        log.info("Request weather data for {}", stationNames);
        try {
            return requestStationWeatherData();
        } catch (InvalidDataException | IOException e) {
            log.error("Error fetching weather data: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private static List<Station> requestStationWeatherData() throws IOException {
        URL url = new URL(WEATHER_STATION_API);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        ObservationXML observationRequest = xmlMapper.readValue(url, ObservationXML.class);
        List<ObservationXML.StationXML> stationData = observationRequest.getStationXMLS();
        if (stationData == null) {
            throw new InvalidDataException("Unable to parse weather API response");
        }
        return mapStationXmlForDb(stationData, observationRequest.getTimestamp());
    }

    private static List<Station> mapStationXmlForDb(List<ObservationXML.StationXML> stationXml, long timestamp) {
        return stationXml.stream()
                .filter(station -> stationNames.contains(station.getName()))
                .map(station -> Station.builder()
                        .timestamp(Timestamp.from(Instant.ofEpochSecond(timestamp)))
                        .airTemperature(station.getAirTemperature())
                        .windSpeed(station.getWindSpeed())
                        .phenomenon(station.getPhenomenon())
                        .name(station.getName())
                        .wmoCode(station.getWmoCode())
                        .build()
                ).toList();
    }

    static BigDecimal calculateDeliveryFee(Station station, Region region, Vehicle vehicle) {
        log.info("Calculate fee for {} {}", region, vehicle);
        BigDecimal regionBaseFee = region.getFee();
        BigDecimal vehicleBaseFee = vehicle.getFee();
        BigDecimal windSpeedExtraFee = calcWindSpeedExtraFee(station.getWindSpeed());
        BigDecimal temperatureExtraFee = calcAirTemperatureExtraFee(station.getAirTemperature());
        BigDecimal phenomenonExtraFee = calcWeatherPhenomenonExtraFee(station.getPhenomenon());
        return Stream.of(regionBaseFee, vehicleBaseFee, windSpeedExtraFee, temperatureExtraFee, phenomenonExtraFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
