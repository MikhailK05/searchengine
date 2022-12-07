package searchengine;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import searchengine.config.SitesList;

@Configuration
@ComponentScan
@EnableConfigurationProperties
public class Tester {

    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(Tester.class).run(args);
        SitesList list = context.getBean(SitesList.class);
        list.getSites().forEach(site -> System.out.println(site.getName() + " - " + site.getUrl()));
    }
}
