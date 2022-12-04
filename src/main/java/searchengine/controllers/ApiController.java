package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.model.PageRepository;
import searchengine.model.Site;
import searchengine.model.SiteRepository;
import searchengine.model.Status;
import searchengine.services.PageNode;
import searchengine.services.PageProcessor;
import searchengine.services.SiteBuilder;
import searchengine.services.StatisticsService;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private SiteRepository siteRepository;
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

            String domain = sites[i].substring(9);
            Date now = new Date();
            Site site = new Site();
            site.setUrl(sites[i]);
            site.setName(domain);
            site.setStatus(Status.INDEXING);
            site.setStatusTime(now);
            siteRepository.save(site);

            PageNode root = new PageNode(sites[i]);
            PageProcessor processor = new PageProcessor(root, domain);
            SiteBuilder builder = new SiteBuilder(processor, pageRepository);
            new Thread(builder).start();

            site.setLastError("");
            site.setStatusTime(now);
            siteRepository.save(site);
            //TODO make adequate site saving
        }
    }
}
