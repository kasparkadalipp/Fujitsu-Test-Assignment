package ee.fujitsu.deliveryfeeservice.config;

import ee.fujitsu.deliveryfeeservice.state.Region;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class CityStationConverter implements Converter<String, Region> {

    @Override
    public Region convert(@NotNull String city) {
        Pattern nonAscii = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        return Region.valueOf(Normalizer.normalize(city, Normalizer.Form.NFD)
                .replaceAll(nonAscii.toString(), "")
                .toUpperCase());
    }
}
