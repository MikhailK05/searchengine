package searchengine;

import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StatisticsResponse;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        System.out.println(
                statisticsResponse
                .getStatistics()
                .getTotal()
                .getSites());
    }
}