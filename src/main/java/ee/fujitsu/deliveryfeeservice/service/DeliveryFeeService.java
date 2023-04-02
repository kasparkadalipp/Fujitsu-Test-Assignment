package ee.fujitsu.deliveryfeeservice.service;


import ee.fujitsu.deliveryfeeservice.exception.AdverseWeatherConditionsException;
import ee.fujitsu.deliveryfeeservice.exception.InvalidDataException;
import ee.fujitsu.deliveryfeeservice.model.Station;
import ee.fujitsu.deliveryfeeservice.repository.ProductRepository;
import ee.fujitsu.deliveryfeeservice.state.Region;
import ee.fujitsu.deliveryfeeservice.state.Vehicle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static ee.fujitsu.deliveryfeeservice.service.DeliveryFeeUtil.calculateDeliveryFee;


@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class DeliveryFeeService {
    private final ProductRepository productRepository;

    /**
     Retrieves the delivery fee for a given region and vehicle type.

     @param region The region where the delivery will take place.
     @param vehicle The type of vehicle that will be used for the delivery.

     @return A ResponseEntity containing the delivery fee as a String and a status of 200 OK.

     @throws InvalidDataException if requested weather data isn't in supported format
     @throws AdverseWeatherConditionsException if the weather conditions aren't suitable for delivery.
     */
    public ResponseEntity<String> deliveryFeeRequest(Region region, Vehicle vehicle) {
        log.info("Query latest data for {}", region);
        Station station = productRepository.findLatestWeatherData(region);
        BigDecimal deliveryFee = calculateDeliveryFee(station, region, vehicle);
        return ResponseEntity.ok().body(deliveryFee.toString() + " €");
    }
}

