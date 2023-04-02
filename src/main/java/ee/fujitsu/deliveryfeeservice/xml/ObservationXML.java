package ee.fujitsu.deliveryfeeservice.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.List;

@Getter
@JacksonXmlRootElement(localName = "observations")
public class ObservationXML {
    @JacksonXmlProperty(isAttribute = true, localName = "timestamp")
    private long timestamp;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "station")
    private List<StationXML> stationXMLS;

    @Getter
    public static class StationXML {
        @JacksonXmlProperty(localName = "name")
        private String name;
        @JacksonXmlProperty(localName = "wmocode")
        private int wmoCode;
        @JacksonXmlProperty(localName = "phenomenon")
        private String phenomenon;
        @JacksonXmlProperty(localName = "airtemperature")
        private double airTemperature;
        @JacksonXmlProperty(localName = "windspeed")
        private double windSpeed;
    }
}
