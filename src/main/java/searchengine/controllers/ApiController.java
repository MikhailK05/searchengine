package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.model.PageRepository;
import searchengine.model.Site;
import searchengine.model.SiteRepository;
import searchengine.model.Status;
import searchengine.services.PageNode;
import searchengine.services.PageProcessor;
import searchengine.services.SiteBuilder;
import searchengine.services.StatisticsService;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private SitesList list;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private SiteRepository siteRepository;
    private final StatisticsService statisticsService;
    private Thread[] threads;

    public ApiController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public void startIndexing(){
        pageRepository.deleteAll();
        siteRepository.deleteAll();
        ArrayList<searchengine.config.Site> sites = (ArrayList) list.getSites();
        for(int i = 0; i < sites.size(); i++){
            System.out.println(sites.get(i).getUrl());
            String domain = sites.get(i).getUrl().substring(8);
            Date now = new Date();
            Site site = new Site();
            site.setUrl(sites.get(i).getUrl());
            site.setName(domain);
            site.setStatus(Status.INDEXING);
            site.setStatusTime(now);
            site.setLastError("-");
            siteRepository.save(site);

            PageNode root = new PageNode(sites.get(i).getUrl());
            PageProcessor processor = new PageProcessor(root, domain);
            SiteBuilder builder = new SiteBuilder(processor, pageRepository, site);
            Thread thread = new Thread(builder);
            thread.start();
            threads[i] = thread;

            site.setLastError("");
            site.setStatusTime(now);
            site.setStatus(Status.INDEXED);
            siteRepository.save(site);
        }
    }

    @GetMapping("/stopIndexing")
    public void stopIndexing(){
        for (int i = 0; i < threads.length; i++){
            Thread thread = threads[i];
            thread.interrupt();
        }
    }
}
