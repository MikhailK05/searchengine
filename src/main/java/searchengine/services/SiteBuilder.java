package searchengine.services;

import lombok.SneakyThrows;
import searchengine.model.PageRepository;

import java.io.IOException;

public class SiteBuilder implements Runnable{
    private PageProcessor processor;
    private PageRepository pageRepository;

    public SiteBuilder(PageProcessor processor, PageRepository pageRepository){
        this.processor = processor;
        this.pageRepository = pageRepository;
    }

    void appendToDB(PageNode rootNode) throws IOException {
        pageRepository.save(rootNode.convertToPage());
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
