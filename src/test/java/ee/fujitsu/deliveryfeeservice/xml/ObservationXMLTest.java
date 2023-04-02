package ee.fujitsu.deliveryfeeservice.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ObservationXMLTest {

    private static final XmlMapper xmlMapper = new XmlMapper();

    @Nested
    class XmlParse {
        private static final String xml_str = """
                     <observations timestamp="1648981916">
                     <station>
                         <name>Station 1</name>
                         <wmocode>12345</wmocode>
                         <phenomenon>Cloudy</phenomenon>
                         <airtemperature>20.0</airtemperature>
                         <windspeed>5.0</windspeed>
                     </station>
                     <station>
                         <name>Station 2</name>
                         <wmocode>67890</wmocode>
                         <phenomenon/>
                         <airtemperature>25.0</airtemperature>
                         <windspeed>10.0</windspeed>
                     </station>
                 </observations>
                """;


        @Test
        void timestampProvided() {
            ObservationXML observationRequest = assertDoesNotThrow(() -> xmlMapper.readValue(xml_str, ObservationXML.class));
            assertEquals(1648981916L, observationRequest.getTimestamp());
            assertNotNull(observationRequest.getStationXMLS());

        }

        @Test
        void stationDataParsed() {
            ObservationXML observationRequest = assertDoesNotThrow(() -> xmlMapper.readValue(xml_str, ObservationXML.class));
            List<ObservationXML.StationXML> stationXMLS = observationRequest.getStationXMLS();
            assertEquals(2, stationXMLS.size());
            ObservationXML.StationXML station1 = stationXMLS.get(0);
            ObservationXML.StationXML station2 = stationXMLS.get(1);

            assertEquals("Station 1", station1.getName());
            assertEquals("Station 2", station2.getName());

            assertEquals(12345, station1.getWmoCode());
            assertEquals(67890, station2.getWmoCode());

            assertEquals("Cloudy", station1.getPhenomenon());
            assertEquals("", station2.getPhenomenon());

            assertEquals(20.0, station1.getAirTemperature());
            assertEquals(25.0, station2.getAirTemperature());

            assertEquals(5.0, station1.getWindSpeed());
            assertEquals(10.0, station2.getWindSpeed());
        }
    }
}