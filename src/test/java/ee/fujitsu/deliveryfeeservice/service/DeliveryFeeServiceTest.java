package ee.fujitsu.deliveryfeeservice.service;

import ee.fujitsu.deliveryfeeservice.exception.AdverseWeatherConditionsException;
import ee.fujitsu.deliveryfeeservice.model.Station;
import ee.fujitsu.deliveryfeeservice.repository.ProductRepository;
import ee.fujitsu.deliveryfeeservice.state.Region;
import ee.fujitsu.deliveryfeeservice.state.Vehicle;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeliveryFeeServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DeliveryFeeService service;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateDocumentation() throws Exception {
        String docksApiURL = "/v3/api-docs";
        String swaggerApiURL = "/swagger-ui/index.html";
        mockMvc.perform(get(docksApiURL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("openapi")));
        mockMvc.perform(get(swaggerApiURL))
                .andExpect(status().isOk());
    }

    @Test
    void calculateDeliveryFeeSuccessfully() {
        Region region = Region.TALLINN;
        Vehicle vehicle = Vehicle.CAR;
        Station station = new Station();
        when(productRepository.findLatestWeatherData(region)).thenReturn(station);

        ResponseEntity<String> response = service.deliveryFeeRequest(region, vehicle);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("4.5 €", response.getBody());
    }
}
