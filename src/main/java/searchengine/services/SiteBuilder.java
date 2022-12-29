package searchengine.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.model.PageRepository;

import java.io.IOException;

public class SiteBuilder implements Runnable{
    private PageProcessor processor;
    @Autowired
    private PageRepository pageRepository;

    public SiteBuilder(PageProcessor processor, PageRepository pageRepository){
        this.processor = processor;
        this.pageRepository = pageRepository;
    }

    void appendToDB(PageNode rootNode) throws IOException {
        pageRepository.save(rootNode.convertToPage());
        //TODO save page ↑ (i think it saves already, idk why i put this todo
        // here in the first place, it was awhile ago)
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
