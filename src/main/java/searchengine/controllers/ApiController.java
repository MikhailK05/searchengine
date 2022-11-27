package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.PageNode;
import searchengine.services.PageProcessor;
import searchengine.services.SiteBuilder;
import searchengine.services.StatisticsService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;

    public ApiController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    public void startIndexing(){
        String[] sites = null; //TODO get sites from application.yaml
        for(int i = 0; i < sites.length; i++){
            PageNode root = new PageNode(sites[i]);
            PageProcessor processor = new PageProcessor(root, sites[i].substring(9));
            SiteBuilder builder = new SiteBuilder(processor);
            new Thread(builder).start();
        }
    }
}
