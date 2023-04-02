package ee.fujitsu.deliveryfeeservice.repository;

import ee.fujitsu.deliveryfeeservice.model.Station;
import ee.fujitsu.deliveryfeeservice.state.Region;
import org.springframework.data.repository.CrudRepository;


public interface ProductRepository extends CrudRepository<Station, Long> {

    default Station findLatestWeatherData(Region region) {
        return findFirstByNameOrderByTimestampDesc(region.getName());
    }

    Station findFirstByNameOrderByTimestampDesc(String regionName);
}
