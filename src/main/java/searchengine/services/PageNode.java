package searchengine.services;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import searchengine.model.Page;

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

    public Page convertToPage() throws IOException {
        Page page = new Page();
        page.setPath(url);
        Connection.Response response = Jsoup.connect(url).execute();
        page.setContent(response.body());
        page.setCode(response.statusCode());

        return page;
    }

    public void addChild(PageNode node) {
        childNodes.add(node);
    }
}
