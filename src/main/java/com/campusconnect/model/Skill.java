package com.campusconnect.model;
import jakarta.persistence.*;
@Entity @Table(name="skills")
public class Skill {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String skillName; private String category;
 public Skill(){} public Skill(String n,String c){skillName=n;category=c;}
 public Long getId(){return id;} public void setId(Long id){this.id=id;}
 public String getSkillName(){return skillName;} public void setSkillName(String s){skillName=s;}
 public String getCategory(){return category;} public void setCategory(String c){category=c;}
}
