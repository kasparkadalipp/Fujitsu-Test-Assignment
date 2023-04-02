package ee.fujitsu.deliveryfeeservice.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Region {
    TALLINN(new BigDecimal("1"), "Tallinn-Harku"),
    TARTU(new BigDecimal("0.5"), "Tartu-Tõravere"),
    PARNU(new BigDecimal("0"), "Pärnu");

    private final BigDecimal fee;
    private final String name;

    public static Set<String> getStationNames() {
        return Arrays.stream(Region.values()).map(Region::getName).collect(Collectors.toSet());
    }
}
