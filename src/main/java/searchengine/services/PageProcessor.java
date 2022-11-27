package searchengine.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class PageProcessor extends RecursiveTask<PageNode> {
    private PageNode node;
    private String domain;
    private static Set<String> uniqueURLS = new HashSet<>();

    public PageProcessor(PageNode node, String domain) {
        this.node = node;
        this.domain = domain;
        uniqueURLS.add(node.getUrl());
    }

    @Override
    protected PageNode compute() {
        try {
                node = searchForLinks(node, domain);

        } catch (Exception exception) {
            //exception.printStackTrace();
        }

        List<PageProcessor> tasks = new ArrayList<>();
        node.getChildNodes().forEach(childNode -> {
            PageProcessor task = new PageProcessor(childNode, domain);
            task.fork();
            tasks.add(task);
        });

        for (PageProcessor task : tasks) {
            node.addChild(task.join());
        }

        return node;
    }

    public static PageNode searchForLinks(PageNode initNode, String domain) throws IOException, InterruptedException {
        Document doc = Jsoup.connect(initNode.getUrl()).userAgent("Firefox").get();
        Elements links = doc.select("a");

        links.stream().map((link) -> link.attr("abs:href")).forEachOrdered((subUrl) -> {
            if (subUrl.contains(domain) && subUrl.endsWith("/")) {
                boolean added = uniqueURLS.add(subUrl);

                if (added) {
                    try {
                        initNode.addChild(subUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return initNode;
    }

    public PageNode getNode() {
        return node;
    }
}