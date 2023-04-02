package ee.fujitsu.deliveryfeeservice.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public enum Vehicle {
    CAR(new BigDecimal("3")),
    SCOOTER(new BigDecimal("2.5")),
    BIKE(new BigDecimal("2"));

    private final BigDecimal fee;
}
