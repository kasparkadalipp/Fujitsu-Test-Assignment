package ee.fujitsu.deliveryfeeservice.state;

import ee.fujitsu.deliveryfeeservice.exception.InvalidDataException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public enum AirTemperature {
    COLD(new BigDecimal("1")),
    NORMAL(new BigDecimal("0.5")),
    WARM(new BigDecimal("0"));

    private final BigDecimal fee;

    public static BigDecimal calcAirTemperatureExtraFee(double airTemperature) {
        if (airTemperature < -217.15 || 100 < airTemperature)
            throw new InvalidDataException("Invalid air temperature provided");
        if (airTemperature < -10)
            return COLD.getFee();
        if (-10 <= airTemperature && airTemperature <= 10)
            return NORMAL.getFee();
        return WARM.getFee();
    }
}
