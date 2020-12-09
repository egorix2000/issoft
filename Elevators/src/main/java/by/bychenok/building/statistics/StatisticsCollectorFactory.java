package by.bychenok.building.statistics;

import by.bychenok.building.configuration.BuildingConfig;

import static com.google.common.base.Preconditions.checkArgument;

public class StatisticsCollectorFactory {
    private static StatisticsCollector statisticsCollector = null;

    public static void initFactory(BuildingConfig config) {
        statisticsCollector = new StatisticsCollector(config);
    }

    public static StatisticsCollector getStatisticsCollector() {
        checkArgument(statisticsCollector != null,
                "Factory must be initialized before getting statistics collector");
        return statisticsCollector;
    }
}
