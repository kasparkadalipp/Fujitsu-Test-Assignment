package ee.fujitsu.deliveryfeeservice.config;

import ee.fujitsu.deliveryfeeservice.state.Vehicle;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

public class VehicleConverter implements Converter<String, Vehicle> {

    @Override
    public Vehicle convert(@NotNull String value) {
        return Vehicle.valueOf(value.toUpperCase());
    }
}
