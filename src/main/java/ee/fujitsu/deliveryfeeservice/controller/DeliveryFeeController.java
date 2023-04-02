package ee.fujitsu.deliveryfeeservice.controller;

import ee.fujitsu.deliveryfeeservice.exception.AdverseWeatherConditionsException;
import ee.fujitsu.deliveryfeeservice.exception.InvalidDataException;
import ee.fujitsu.deliveryfeeservice.service.DeliveryFeeService;
import ee.fujitsu.deliveryfeeservice.state.Region;
import ee.fujitsu.deliveryfeeservice.state.Vehicle;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Spring Boot REST API Documentation"))
@Tag(name = "Delivery Service")
@RequestMapping("/api")
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryService;

    @Operation(description = """
            <p>The delivery fee is calculated by taking the base fee of the region and the fee of the vehicle.</p>
            <p>Additional fees are added based on current weather conditions of the region.</p>
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Returns the delivery fee for the selected region and vehicle in euros (€)",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "4.5 €")
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Returns an error message when weather conditions are unfavorable",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "Usage of selected vehicle type is forbidden")
            )
    )
    @GetMapping("/delivery-fee")
    public ResponseEntity<String> deliveryFeeRequest(
            @Parameter(
                    name = "city",
                    schema = @Schema(type = "string", allowableValues = {"Tallinn", "Tartu", "Pärnu"}),
                    required = true)
            @RequestParam Region city,
            @Parameter(
                    name = "vehicle",
                    schema = @Schema(type = "string", allowableValues = {"Car", "Scooter", "Bike"}),
                    required = true)
            @RequestParam Vehicle vehicle) {
        return deliveryService.deliveryFeeRequest(city, vehicle);
    }

    @ExceptionHandler(AdverseWeatherConditionsException.class)
    public ResponseEntity<String> handleMyException(AdverseWeatherConditionsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleMyException(InvalidDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
