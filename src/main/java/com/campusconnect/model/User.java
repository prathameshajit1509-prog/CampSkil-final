package com.campusconnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------- Login Details ----------
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password; // store BCrypt hash only

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // ---------- CV Builder & Profile Details ----------
    private String fullName;

    @Pattern(regexp = "^[0-9+\\-\\s]{7,15}$", message = "Invalid phone number")
    private String phoneNumber;

    private String degree;
    private String branch;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double cgpa;

    private Integer graduationYear;

    @Column(columnDefinition = "TEXT")
    private String technicalSkills;

    @Column(columnDefinition = "TEXT")
    private String projectsDescription;

    // ---------- equals / hashCode (id-based, JPA-safe) ----------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // Password intentionally excluded
    @Override
    public String toString() {
        return "User{id=" + id + ", email='" + email + "', fullName='" + fullName + "'}";
    }
}