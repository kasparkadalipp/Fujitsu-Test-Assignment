package ee.fujitsu.deliveryfeeservice.scheduled;

import ee.fujitsu.deliveryfeeservice.model.Station;
import ee.fujitsu.deliveryfeeservice.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static ee.fujitsu.deliveryfeeservice.service.DeliveryFeeUtil.getStationWeatherData;

@Slf4j
@EnableAsync
@RequiredArgsConstructor
@Component
public class ScheduledTasks {
    private final ProductRepository productRepository;

    @Async
    @PostConstruct
    @Scheduled(cron = "${scheduled.cron.weather-station}")
    public void requestDataFromAllStations() {
        log.info("Scheduled request for weather data");
        List<Station> stationWeatherData = getStationWeatherData();
        productRepository.saveAll(stationWeatherData);
    }
}
