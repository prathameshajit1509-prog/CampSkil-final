package com.campusconnect.controller;

import com.campusconnect.model.*;
import com.campusconnect.repository.*;
import com.campusconnect.service.PdfGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CampusController {
    private final DriveRepository drives;
    private final JobRoleRepository roles;
    private final PdfGeneratorService pdf;
    private final UserRepository users;
    private final RoleRepository roleRepo;

    public CampusController(DriveRepository d, JobRoleRepository r, PdfGeneratorService p, UserRepository u, RoleRepository roleRepo) {
        this.drives = d;
        this.roles = r;
        this.pdf = p;
        this.users = u;
        this.roleRepo = roleRepo;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok", "application", "CampusConnect API");
    }

    // ---------- Users Endpoints ----------
    @GetMapping("/users")
    public List<User> getUsers() {
        return users.findAll();
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }
        email = email.toLowerCase().trim();
        if (users.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered"));
        }

        User u = new User();
        u.setEmail(email);
        u.setPassword((String) payload.getOrDefault("password", ""));
        u.setFullName((String) payload.getOrDefault("name", payload.get("fullName")));
        u.setPhoneNumber((String) payload.getOrDefault("phone", payload.get("phoneNumber")));
        u.setBranch((String) payload.getOrDefault("dept", payload.get("branch")));
        u.setDegree((String) payload.getOrDefault("degree", "B.Tech"));

        Object cgpaObj = payload.get("cgpa");
        if (cgpaObj != null && !cgpaObj.toString().isBlank()) {
            try {
                u.setCgpa(Double.parseDouble(cgpaObj.toString()));
            } catch (Exception ignored) {}
        }

        Role studentRole = roleRepo.findByName("ROLE_STUDENT").orElseGet(() -> roleRepo.save(new Role("ROLE_STUDENT")));
        u.setRoles(Set.of(studentRole));

        User saved = users.save(u);
        return ResponseEntity.status(201).body(Map.of(
            "message", "User registered successfully",
            "id", saved.getId(),
            "email", saved.getEmail(),
            "name", saved.getFullName() != null ? saved.getFullName() : ""
        ));
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }
        Optional<User> opt = users.findByEmail(email.toLowerCase().trim());
        if (opt.isEmpty() || !password.equals(opt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
        }
        User u = opt.get();
        return ResponseEntity.ok(Map.of(
            "id", u.getId(),
            "email", u.getEmail(),
            "name", u.getFullName() != null ? u.getFullName() : "",
            "dept", u.getBranch() != null ? u.getBranch() : "",
            "cgpa", u.getCgpa() != null ? u.getCgpa() : ""
        ));
    }

    @PutMapping("/users/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        if (email == null) return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        Optional<User> opt = users.findByEmail(email.toLowerCase().trim());
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        User u = opt.get();
        if (payload.containsKey("phone")) u.setPhoneNumber((String) payload.get("phone"));
        if (payload.containsKey("dept")) u.setBranch((String) payload.get("dept"));
        if (payload.containsKey("cgpa")) {
            try { u.setCgpa(Double.parseDouble(payload.get("cgpa").toString())); } catch (Exception ignored) {}
        }
        if (payload.containsKey("skills")) u.setTechnicalSkills((String) payload.get("skills"));
        if (payload.containsKey("projects")) u.setProjectsDescription((String) payload.get("projects"));
        users.save(u);
        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }

    // ---------- Drives Endpoints ----------
    @GetMapping("/drives")
    public List<OnCampusDrive> getDrives() {
        return drives.findAll();
    }

    @PostMapping("/admin/drives")
    public ResponseEntity<OnCampusDrive> createDrive(@RequestBody OnCampusDrive d) {
        return ResponseEntity.status(201).body(drives.save(d));
    }

    @GetMapping("/drives/{id}/pdf")
    public void drivePdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        OnCampusDrive d = drives.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Drive not found"));
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=drive_details.pdf");
        pdf.generateDriveReport(d, response.getOutputStream());
    }

    // ---------- Skills & Roles Endpoints ----------
    @GetMapping("/skills/roles")
    public List<JobRole> getRoles() {
        return roles.findAll();
    }

    @GetMapping("/skills/roles/{title}")
    public ResponseEntity<JobRole> getRole(@PathVariable String title) {
        JobRole r = roles.findByRoleTitle(title);
        return r == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(r);
    }

    @PostMapping("/admin/skills/roles")
    public ResponseEntity<JobRole> createRole(@RequestBody JobRole role) {
        return ResponseEntity.status(201).body(roles.save(role));
    }

    @GetMapping("/off-campus-jobs")
    public List<Map<String, String>> offCampusJobs() {
        return List.of(
            Map.of("company", "Example Tech", "role", "Graduate Software Engineer", "type", "Off-campus", "url", "https://www.linkedin.com/jobs/"),
            Map.of("company", "Career Portal", "role", "Entry-level Analyst", "type", "Off-campus", "url", "https://www.naukri.com/"),
            Map.of("company", "Internship Portal", "role", "Software Development Intern", "type", "Internship", "url", "https://internshala.com/")
        );
    }
}
