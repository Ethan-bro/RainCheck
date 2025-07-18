package use_case.weather.hourly;

import java.util.List;
import java.util.Map;

public class HourlyWeatherOutputData {

    private final List<Map<String, String>> summaries;

    public HourlyWeatherOutputData(List<Map<String, String>> summaries) {
        this.summaries = summaries;
    }

    public List<Map<String, String>> getSummaries() {
        return summaries;
    }
}
