package com.campusconnect.service;

import com.campusconnect.model.*;
import com.campusconnect.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DemoDataLoader implements CommandLineRunner {
    private final DriveRepository drives;
    private final JobRoleRepository roles;
    private final UserRepository users;
    private final RoleRepository roleRepo;

    public DemoDataLoader(DriveRepository d, JobRoleRepository r, UserRepository u, RoleRepository roleRepo) {
        this.drives = d;
        this.roles = r;
        this.users = u;
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... args) {
        Role studentRole = roleRepo.findByName("ROLE_STUDENT").orElseGet(() -> roleRepo.save(new Role("ROLE_STUDENT")));
        Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> roleRepo.save(new Role("ROLE_ADMIN")));
        Role tpoRole = roleRepo.findByName("ROLE_TPO").orElseGet(() -> roleRepo.save(new Role("ROLE_TPO")));

        if (drives.count() == 0) {
            OnCampusDrive a = new OnCampusDrive();
            a.setCompanyName("Demo Technologies");
            a.setJobProfile("Graduate Software Engineer");
            a.setEligibilityCriteria("Final-year students; Java, SQL and problem-solving basics");
            a.setInterviewDate(LocalDate.now().plusDays(14));
            drives.save(a);

            OnCampusDrive b = new OnCampusDrive();
            b.setCompanyName("Campus Analytics");
            b.setJobProfile("Junior Data Analyst");
            b.setEligibilityCriteria("Final-year students; SQL, Excel and analytical thinking");
            b.setInterviewDate(LocalDate.now().plusDays(21));
            drives.save(b);
        }

        if (roles.count() == 0) {
            JobRole a = new JobRole();
            a.setRoleTitle("Software Developer");
            a.setSkills(Set.of(new Skill("Java", "Technical"), new Skill("Data Structures", "Technical"), new Skill("SQL", "Technical"), new Skill("Communication", "Soft skill")));
            roles.save(a);

            JobRole b = new JobRole();
            b.setRoleTitle("Data Analyst");
            b.setSkills(Set.of(new Skill("SQL", "Technical"), new Skill("Excel", "Technical"), new Skill("Statistics", "Technical"), new Skill("Communication", "Soft skill")));
            roles.save(b);
        }

        if (users.count() == 0) {
            User demoStudent = new User();
            demoStudent.setEmail("student@campusconnect.com");
            demoStudent.setPassword("password123");
            demoStudent.setFullName("Demo Student");
            demoStudent.setPhoneNumber("9876543210");
            demoStudent.setDegree("B.Tech");
            demoStudent.setBranch("CSE");
            demoStudent.setCgpa(8.8);
            demoStudent.setGraduationYear(2026);
            demoStudent.setTechnicalSkills("Java, Spring Boot, MySQL, React");
            demoStudent.setProjectsDescription("CampusConnect placement management system");
            demoStudent.setRoles(Set.of(studentRole));
            users.save(demoStudent);

            User demoAdmin = new User();
            demoAdmin.setEmail("admin@campusconnect.com");
            demoAdmin.setPassword("admin123");
            demoAdmin.setFullName("Super Admin");
            demoAdmin.setPhoneNumber("9876543211");
            demoAdmin.setDegree("Administration");
            demoAdmin.setRoles(Set.of(adminRole));
            users.save(demoAdmin);
        }
    }
}
