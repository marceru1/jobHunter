package com.jobhunter.job_hunter.scraper;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class ScraperFactory {
    
    private final Map<String, JobScraper> scrapers;

    public ScraperFactory(List<JobScraper> scraperList) {
        this.scrapers = scraperList.stream()
            .collect(Collectors.toMap(JobScraper::getSourceName, scraper -> scraper));
    }

    public JobScraper getScraper(String sourceName) {
        JobScraper scraper = scrapers.get(sourceName.toLowerCase());
        if (scraper == null) {
            throw new IllegalArgumentException("Nenhum scraper encontrado para: " + sourceName);
        }
        return scraper;
    }



}
