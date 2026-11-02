package com.campusconnect.model;
import jakarta.persistence.*;
import java.util.*;
@Entity @Table(name="job_roles")
public class JobRole {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String roleTitle;
 @ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
 @JoinTable(name="role_skills",joinColumns=@JoinColumn(name="job_role_id"),inverseJoinColumns=@JoinColumn(name="skill_id"))
 private Set<Skill> skills=new HashSet<>();
 public Long getId(){return id;} public void setId(Long id){this.id=id;}
 public String getRoleTitle(){return roleTitle;} public void setRoleTitle(String s){roleTitle=s;}
 public Set<Skill> getSkills(){return skills;} public void setSkills(Set<Skill> s){skills=s;}
}
