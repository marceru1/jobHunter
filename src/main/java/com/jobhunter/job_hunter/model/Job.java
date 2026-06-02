package com.jobhunter.job_hunter.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    
    
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime scrapedAt;

    @Column
    private Integer matchScore;

    @Column (length = 255)    
    private String company;

    @Column (columnDefinition = "TEXT")
    private String requirements;

    @Column (length = 255)
    private String salary;

    @Column (nullable = false, length = 255)
    private String url;

    @Column (length = 255)
    private String postedDate;

    @Column (columnDefinition = "TEXT")
    private String keywords;

    @Column (nullable = false, length = 50)    
    private String source;

    @Column
    private String externalId;

    @Column 
    private String notifiedAt;

}
