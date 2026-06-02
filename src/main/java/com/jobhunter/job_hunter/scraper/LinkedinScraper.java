package com.jobhunter.job_hunter.scraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.jobhunter.job_hunter.model.Job;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementação do scraper para o LinkedIn utilizando o padrão Strategy (JobScraper).
 * 
 * Convenções Spring Boot utilizadas:
 * - @Slf4j: Injeta automaticamente um Logger estático para evitarmos o uso de System.out.println.
 * - @Component: Transforma esta classe num Bean gerenciado pelo Spring (Singleton), 
 *   permitindo que ela seja injetada automaticamente na nossa ScraperFactory.
 */
@Slf4j
@Component
public class LinkedinScraper implements JobScraper {
    
    // variaveis fixas precisam ser constantes pra economizar memoria
    private static final String BASE_URL = "https://br.linkedin.com/jobs/search";


    // headers pra enganar o linkedin dizendo q somos um navegador
    // User-Agent falso para evitar bloqueios (HTTP 403 Forbidden)
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    // ========================================================================
    // MÉTODOS DO SCRAPER
    // ========================================================================
    

    @Override
    public List<Job> scrape() {
        List<Job> jobs = new ArrayList<>();

        log.info("Iniciando scraping no LinkedIn..."); 
 
        // 1. CONFIGURAÇÃO DOS PARÂMETROS
        // OBS: Num cenário ideal (Fase 3), esses valores deveriam vir via parâmetro da interface (ex: scrape(keyword, location))
        String keyword = "java backend";
        String location = "Brazil";

        // Montagem limpa da URL usando String.format
        String url = String.format("%s?keywords=%s&location=%s", BASE_URL, keyword, location);

        try {
            // 2. CONEXÃO JSOUP
            // Conecta na URL configurando o timeout para 10s (evita travar a thread se a rede cair)
            Document doc = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(10000)     
                .get();

            // 3. EXTRAÇÃO DOS DADOS (SELETORES CSS)
            // A classe 'jobs-search__results-list' engloba as vagas na página pública do LinkedIn
            Elements jobCards = doc.select("ul.jobs-search__results-list > li");

            for (Element card : jobCards) {
                // Extrai os textos limpando os espaços em branco com .trim()
                String title = card.select("h3.base-search-card__title").text().trim(); 
                String company = card.select("h4.base-search-card__subtitle").text().trim(); 
                
                // Para links, pegamos o valor do atributo 'href' ao invés do texto visual
                String jobUrl = card.select("a.base-card__full-link").attr("href"); 

                // 4. MAPEAMENTO PARA A ENTIDADE (MODEL)
                // Criamos o objeto que futuramente será salvo no banco SQLite via JPA
                Job job = new Job();
                job.setTitle(title);
                job.setCompany(company);
                job.setUrl(jobUrl);
                job.setSource(getSourceName());
                job.setScrapedAt(LocalDateTime.now());
                job.setExternalId(jobUrl);

                jobs.add(job);
            }

            log.info("Scraping concluído! O LinkedIn retornou {} vagas.", jobs.size());
            
        } catch (IOException e) {
            // 5. TRATAMENTO DE ERRO
            // Se falhar (ex: bloqueio ou sem internet), apenas logamos o erro (nível ERROR), 
            // mas o sistema não quebra. Retorna a lista vazia graciosamente.
            log.error("Falha ao comunicar com o LinkedIn na URL: {}. Motivo: {}", url, e.getMessage(), e);
        }
        
        return jobs;
    }

    @Override
    public String getSourceName() {
        return "linkedin";
    }
}
