package ee.fujitsu.deliveryfeeservice.state;

import ee.fujitsu.deliveryfeeservice.exception.AdverseWeatherConditionsException;
import ee.fujitsu.deliveryfeeservice.exception.InvalidDataException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public enum WindSpeed {
    MODERATE(new BigDecimal("0.5")),
    CALM(new BigDecimal("0"));

    private final BigDecimal fee;

    public static BigDecimal calcWindSpeedExtraFee(double windSpeed) {
        if (windSpeed < 0 || 1000 < windSpeed)
            throw new InvalidDataException("Invalid wind speed provided");
        if (10 <= windSpeed && windSpeed <= 20)
            return MODERATE.getFee();
        if (20 < windSpeed)
            throw new AdverseWeatherConditionsException();
        return CALM.getFee();
    }
}
