package com.jobhunter.job_hunter.scraper;
import com.jobhunter.job_hunter.model.Job;
import java.util.List;


public interface JobScraper {
    
    /**
     * Roda o scraper e retorna as vagas encontradas.
     * 
     * @return Uma lista de vagas (Job) estruturadas
     */
    List<Job> scrape();
    /**
     * Identificador do scraper (ex: "linkedin", "gupy").
     */
    String getSourceName();
}