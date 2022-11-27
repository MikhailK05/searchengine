package searchengine.services;

public class SiteBuilder implements Runnable{
    private PageProcessor processor;

    public SiteBuilder(PageProcessor processor){
        this.processor = processor;
    }

    private static void appendToDB(PageNode rootNode){
        rootNode.getUrl();//TODO send sql sequence or something
        rootNode.getChildNodes().forEach(node -> {
            try {
                appendToDB(node);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        PageNode node = processor.compute();
        appendToDB(node);
    }
}
