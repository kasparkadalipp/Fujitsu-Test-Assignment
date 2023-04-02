package ee.fujitsu.deliveryfeeservice.service;

import ee.fujitsu.deliveryfeeservice.exception.AdverseWeatherConditionsException;
import ee.fujitsu.deliveryfeeservice.exception.InvalidDataException;
import ee.fujitsu.deliveryfeeservice.model.Station;
import ee.fujitsu.deliveryfeeservice.state.Region;
import ee.fujitsu.deliveryfeeservice.state.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryFeeUtilTest {
    private static final RandomEnum<Vehicle> vehicles = new RandomEnum<>(Vehicle.class);
    private static final RandomEnum<Region> regions = new RandomEnum<>(Region.class);
    private Vehicle vehicle;
    private Region region;

    @BeforeEach
    void setUp() {
        vehicle = vehicles.random();
        region = regions.random();
    }

    @Nested
    class calculateDeliveryFee {
        private static Stream<Arguments> provideInvalidWindSpeed() {
            return Stream.of(-1000, -1, 1001, 5000).map(windSpeed -> Arguments.of(
                    Station.builder().windSpeed(windSpeed).build()));
        }

        private static Stream<Arguments> provideInvalidAirTemperature() {
            return Stream.of(-1000, -220, 101, 1000).map(temperature -> Arguments.of(
                    Station.builder().airTemperature(-1000).build()));
        }

        private static Stream<Arguments> provideInvalidPhenomenon() {
            return Stream.of("Unknown", "Storm", "Tornado").map(phenomenon -> Arguments.of(
                    Station.builder().phenomenon(phenomenon).build()));
        }

        private static Stream<Arguments> provideValidParameters() {
            return Stream.of(
                    Arguments.of("", -20, 0),
                    Arguments.of("Moderate snowfall", -5, 5),
                    Arguments.of("Heavy shower", 5, 10),
                    Arguments.of("Variable clouds", 15, 20)
            );
        }


        @ParameterizedTest
        @MethodSource("provideInvalidWindSpeed")
        void invalidWindSpeed(Station station) {
            var response = assertThrows(InvalidDataException.class,
                    () -> DeliveryFeeUtil.calculateDeliveryFee(station, region, vehicle));
            assertEquals("Invalid wind speed provided", response.getMessage());
        }

        @ParameterizedTest
        @MethodSource("provideInvalidAirTemperature")
        void invalidAirTemperature(Station station) {
            var response = assertThrows(InvalidDataException.class,
                    () -> DeliveryFeeUtil.calculateDeliveryFee(station, region, vehicle));
            assertEquals("Invalid air temperature provided", response.getMessage());
        }

        @ParameterizedTest
        @MethodSource("provideInvalidPhenomenon")
        void invalidPhenomenon(Station station) {
            var response = assertThrows(InvalidDataException.class,
                    () -> DeliveryFeeUtil.calculateDeliveryFee(station, region, vehicle));
            assertEquals("Unknown weather phenomenon", response.getMessage());
        }

        @ParameterizedTest
        @MethodSource("provideValidParameters")
        void validRequest(String phenomenon, double airTemperature, double windSpeed) {
            Station station = Station.builder()
                    .phenomenon(phenomenon)
                    .airTemperature(airTemperature)
                    .windSpeed(windSpeed)
                    .build();
            assertDoesNotThrow(() -> DeliveryFeeUtil.calculateDeliveryFee(station, region, vehicle));
        }
    }

    @Nested
    class unsuitableDrivingConditions {
        @Test
        void phenomenon() {
            Station station = Station.builder().phenomenon("Thunderstorm").build();
            testUnsuitableDrivingConditions(station);
        }

        @Test
        void windSpeed() {
            Station station = Station.builder().windSpeed(25).build();
            testUnsuitableDrivingConditions(Station.builder().windSpeed(25).build());
        }

        private void testUnsuitableDrivingConditions(Station station) {
            var response = assertThrows(AdverseWeatherConditionsException.class, () ->
                    DeliveryFeeUtil.calculateDeliveryFee(station, region, vehicle));
            assertEquals("Usage of selected vehicle type is forbidden", response.getMessage());
        }
    }
}
