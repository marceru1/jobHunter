package com.jobhunter.job_hunter.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jobhunter.job_hunter.model.Job;
import com.jobhunter.job_hunter.repository.JobRepository;
import com.jobhunter.job_hunter.scraper.JobScraper;
import com.jobhunter.job_hunter.scraper.ScraperFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScraperTestRunner implements CommandLineRunner {


    private final JobRepository jobRepository;
    private final ScraperFactory scraperFactory;

    public ScraperTestRunner(JobRepository jobRepository, ScraperFactory scraperFactory) {
        this.jobRepository = jobRepository;
        this.scraperFactory = scraperFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("===============================================");
        log.info("🧪 INICIANDO TESTE DO SCRAPER NA INICIALIZAÇÃO e colocando no banco de dados");
        log.info("===============================================");
        try {
            
            JobScraper linkedinScraper = scraperFactory.getScraper("linkedin");// 1. Usamos a Factory para buscar a estratégia correta de forma dinâmica
        
    
            List<Job> vagasEncontradas = linkedinScraper.scrape();
            

            int vagasNovas = 0;
            
            
            for (Job vaga : vagasEncontradas) {

                boolean vagaExiste = jobRepository.existsBySourceAndExternalId(
                    vaga.getSource(),
                    vaga.getExternalId()
                    
                );

                if (!vagaExiste) {
                    jobRepository.save(vaga);
                    vagasNovas++;
                    log.info("💾 Nova Vaga Salva: {} na empresa {}", vaga.getTitle(), vaga.getCompany());
                    
                }
                    
            };





        } catch (Exception e) {
            log.error("Erro durante o teste: {}", e.getMessage());
        }
        
        log.info("===============================================");
    }
}
