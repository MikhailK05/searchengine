package searchengine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "indexing-settings")
public class SitesList {
    private List<Site> sites;

    public List<Site> getSites() {
        return sites;
    }
    public void setSites(List<Site> sites) {
        this.sites = sites;
    }
    public SitesList(List<Site> sites) {
        this.sites = sites;
    }

    public static class Site {
        private String url;
        private String name;

        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Site(String url, String name) {
            this.url = url;
            this.name = name;
        }
    }
}
