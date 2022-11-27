package searchengine.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageNode {
    private String url;
    private List<PageNode> childNodes = new ArrayList<>();

    public PageNode(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public List<PageNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<PageNode> childNodes) {
        this.childNodes = childNodes;
    }

    public void addChild(String url) throws IOException {
        childNodes.add(new PageNode(url));
    }

    public void addChild(PageNode node) {
        childNodes.add(node);
    }
}
