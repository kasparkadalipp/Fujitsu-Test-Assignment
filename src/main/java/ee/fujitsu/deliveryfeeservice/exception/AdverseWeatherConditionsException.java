package ee.fujitsu.deliveryfeeservice.exception;

public class AdverseWeatherConditionsException extends RuntimeException {

    public AdverseWeatherConditionsException() {
        super("Usage of selected vehicle type is forbidden");
    }
}
