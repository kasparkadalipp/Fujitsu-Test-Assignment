package ee.fujitsu.deliveryfeeservice.state;

import ee.fujitsu.deliveryfeeservice.exception.AdverseWeatherConditionsException;
import ee.fujitsu.deliveryfeeservice.exception.InvalidDataException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum Phenomenon {
    SNOWY(new BigDecimal("1")),
    RAINY(new BigDecimal("0.5")),
    MILD(new BigDecimal("0")),
    UNSPECIFIED(new BigDecimal("0"));
    private static final List<String> snowyWeather = Arrays.asList(
            "Light snow shower", "Moderate snow shower", "Heavy snow shower",
            "Light sleet", "Moderate sleet",
            "Light snowfall", "Moderate snowfall", "Heavy snowfall",
            "Blowing snow", "Drifting snow");
    private static final List<String> rainyWeather = Arrays.asList(
            "Light shower", "Moderate shower", "Heavy shower",
            "Light rain", "Moderate rain", "Heavy rain");
    private static final List<String> hazardousWeather = Arrays.asList(
            "Glaze", "Hail", "Thunder", "Thunderstorm");
    private static final List<String> mildWeather = Arrays.asList(
            "Few clouds", "Variable clouds", "Cloudy with clear spells", "Overcast", "Clear", "Fog");

    private final BigDecimal fee;

    public static BigDecimal calcWeatherPhenomenonExtraFee(String phenomenon) {
        if (snowyWeather.contains(phenomenon))
            return SNOWY.getFee();
        if (rainyWeather.contains(phenomenon))
            return RAINY.getFee();
        if (hazardousWeather.contains(phenomenon))
            throw new AdverseWeatherConditionsException();
        if (mildWeather.contains(phenomenon))
            return MILD.getFee();
        if(phenomenon == null || phenomenon.isBlank())
            return UNSPECIFIED.getFee();
        throw new InvalidDataException("Unknown weather phenomenon");
    }
}
