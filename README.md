# Job Application Tracker — Backend

Multi-user job application tracker API. Java 21, Spring Boot 3.2, PostgreSQL, stateless JWT auth.

## Setup

1. Create the database:
   ```bash
   createdb job_tracker
   ```
2. Set env vars (or edit `src/main/resources/application.properties` directly):
   - `DB_USERNAME`, `DB_PASSWORD`
   - `JWT_SECRET` — a base64-encoded string decoding to 256+ bits. Generate one with:
     ```bash
     openssl rand -base64 32
     ```
3. Run:
   ```bash
   mvn spring-boot:run
   ```
   Hibernate is set to `ddl-auto=update` for local dev convenience — swap in Flyway/Liquibase with `ddl-auto=validate` before production use.

## Data isolation model

- `job_applications` and `application_notes` carry `user_id`. Every repository method that reads or writes them takes `userId` as an explicit parameter and filters on it in the query itself (`findByUserIdAndId`, `findByUserId`, etc.) — see [`JobApplicationRepository`](src/main/java/com/jobtracker/repository/JobApplicationRepository.java) and [`ApplicationNoteRepository`](src/main/java/com/jobtracker/repository/ApplicationNoteRepository.java).
- A lookup for an id owned by another user returns empty, which the service layer turns into a 404 via `ResourceNotFoundException` — never a 403, so tenants can't distinguish "not yours" from "doesn't exist."
- `companies` is shared reference data (no `user_id`) — any authenticated user can search/create companies to attach applications to.
- The authenticated user is always resolved from the JWT (`@AuthenticationPrincipal User`), never taken from request bodies.

## API

| Method | Path | Auth |
|---|---|---|
| POST | `/api/auth/register` | none |
| POST | `/api/auth/login` | none |
| GET | `/api/applications?status=&page=&size=` | JWT |
| POST | `/api/applications` | JWT |
| GET | `/api/applications/{id}` | JWT |
| PUT | `/api/applications/{id}` | JWT |
| DELETE | `/api/applications/{id}` | JWT |
| GET | `/api/companies?q=` | JWT |
| POST | `/api/companies` | JWT |
| GET | `/api/applications/{applicationId}/notes` | JWT |
| POST | `/api/applications/{applicationId}/notes` | JWT |
| DELETE | `/api/applications/{applicationId}/notes/{noteId}` | JWT |

Send the JWT as `Authorization: Bearer <token>`.
