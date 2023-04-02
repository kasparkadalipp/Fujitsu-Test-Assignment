package ee.fujitsu.deliveryfeeservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String phenomenon;
    private int wmoCode;
    private double windSpeed;
    private double airTemperature;
    private Timestamp timestamp;
}