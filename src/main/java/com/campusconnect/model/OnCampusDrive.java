package com.campusconnect.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "on_campus_drives")
public class OnCampusDrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String companyName;
    private String jobProfile;
    private String eligibilityCriteria;
    private LocalDate interviewDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getJobProfile() { return jobProfile; }
    public void setJobProfile(String jobProfile) { this.jobProfile = jobProfile; }
    public String getEligibilityCriteria() { return eligibilityCriteria; }
    public void setEligibilityCriteria(String eligibilityCriteria) { this.eligibilityCriteria = eligibilityCriteria; }
    public LocalDate getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDate interviewDate) { this.interviewDate = interviewDate; }
}

