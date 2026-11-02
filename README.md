# CampusConnect — integrated full-stack project

This package combines the provided CampSkil/CampusConnect HTML pages with the Spring Boot Java backend and the project proposal's core features: on-campus drive tracker, off-campus hiring directory endpoint, CV/drive PDF support, and role-based skill mapping.

## Requirements
- JDK 17 (or newer)
- Maven 3.8+
- MySQL 8.x
- VS Code with Extension Pack for Java (or IntelliJ IDEA)

## 1. Create the database
Open MySQL Workbench or a MySQL terminal and run:
```sql
CREATE DATABASE campus_connect;
```

## 2. Configure the database
Open `src/main/resources/application.properties`. Set the correct MySQL username/password, or use environment variables:
- `DB_URL` (default `jdbc:mysql://localhost:3306/campus_connect?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- `DB_USERNAME` (default `root`)
- `DB_PASSWORD` (default blank)

For example, set `spring.datasource.password=YOUR_MYSQL_PASSWORD` directly in `application.properties` for local development. Do not commit real passwords.

## 3. Run it in VS Code
1. Extract this ZIP.
2. In VS Code choose **File → Open Folder…** and select the extracted `CampusConnect-Full` folder (the one containing `pom.xml`).
3. Install **Extension Pack for Java** if VS Code asks.
4. Make sure `java -version` reports JDK 17 or later and `mvn -version` works in the VS Code terminal.
5. Start MySQL and run the database SQL above.
6. Open Terminal → New Terminal and run:
   ```bash
   mvn spring-boot:run
   ```
7. Wait for `Started CampusConnectApplication` in the terminal.
8. Open **http://localhost:8080/**. The frontend is served by Spring Boot, so use this URL rather than opening HTML files directly.
9. Test **http://localhost:8080/api/health**. It should return JSON with status `ok`.

## Pages
- `/` — landing page (`index.html`)
- `/test.html` — sign-in / registration UI
- `/dashboard.html` — dashboard
- `/campus-drives.html` — drive tracker; live database drives appear in a backend panel
- `/cv-builder.html` — CV builder page
- `/credentials.html` — student profile
- `/prep-guide.html` — preparation guide plus backend skill mapping

## API endpoints
- `GET /api/health`
- `GET /api/drives`
- `POST /api/admin/drives` — add a drive (JSON)
- `GET /api/drives/{id}/pdf` — download a drive PDF
- `GET /api/skills/roles`
- `GET /api/skills/roles/{title}`
- `POST /api/admin/skills/roles`
- `GET /api/off-campus-jobs` — sample external job directory links

Example add-drive JSON:
```json
{
  "companyName": "Example Ltd",
  "jobProfile": "Graduate Engineer",
  "eligibilityCriteria": "Final-year students; Java and SQL",
  "interviewDate": "2026-11-15"
}
```
Send it to `POST http://localhost:8080/api/admin/drives` with `Content-Type: application/json`.

## Important notes
- On first run, sample drives and role/skill records are added automatically if their tables are empty.
- The supplied login/profile/onboarding interactions are largely client-side and use browser localStorage; this starter project does not claim production-grade authentication. The current Spring Security configuration permits requests for easy classroom demo/testing. Before deployment, implement real registration/login, password hashing, authorization for admin endpoints, and external job verification.
- Third-party font/icon/CDN assets need an internet connection. Core pages are served locally by Spring Boot.
- If MySQL login fails, check that MySQL is running and your DB username/password match `application.properties`.
