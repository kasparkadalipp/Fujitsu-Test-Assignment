package ee.fujitsu.deliveryfeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FujitsuInternshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(FujitsuInternshipApplication.class, args);
    }

}
