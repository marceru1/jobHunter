package com.jobhunter.job_hunter.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobhunter.job_hunter.model.Job;

@Repository
public interface JobRepository extends JpaRepository <Job, Long>{
    

    boolean existsBySourceAndExternalId(String source, String externalId);

    Optional<Job> findBySourceAndExternalId(String source, String externalId);

    List<Job> findByNotifiedAtIsNull();

    List<Job> findBySource(String source);

    List<Job> findByScrapedAtAfter(LocalDateTime since);
}
