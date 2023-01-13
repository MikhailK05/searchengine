package searchengine.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.model.Page;
import searchengine.model.PageRepository;
import searchengine.model.Site;

import java.io.IOException;

public class SiteBuilder implements Runnable{
    private PageProcessor processor;
    @Autowired
    private PageRepository pageRepository;
    private Site site;

    public SiteBuilder(PageProcessor processor, PageRepository pageRepository, Site site){
        this.processor = processor;
        this.pageRepository = pageRepository;
        this.site = site;
    }

    void appendToDB(PageNode rootNode) throws IOException {
        Page page = rootNode.convertToPage();
        page.setSite(site);
        pageRepository.save(page);
        //TODO save page ↑
        rootNode.getChildNodes().forEach(node -> {
            try {
                appendToDB(node);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @SneakyThrows
    @Override
    public void run() {
        PageNode node = processor.compute();
        appendToDB(node);
    }
}
