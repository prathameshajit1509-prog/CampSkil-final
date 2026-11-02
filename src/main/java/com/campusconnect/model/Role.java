package com.campusconnect.model;
import jakarta.persistence.*;
import java.util.Objects;
@Entity @Table(name="roles")
public class Role {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String name;
 public Role() {} public Role(String name){this.name=name;}
 public Long getId(){return id;} public void setId(Long id){this.id=id;}
 public String getName(){return name;} public void setName(String name){this.name=name;}
 @Override public boolean equals(Object o){return o instanceof Role r && name!=null && Objects.equals(name,r.name);}
 @Override public int hashCode(){return Objects.hashCode(name);}
}
