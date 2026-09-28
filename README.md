# Network Intrusion Detection System (NIDS)
### DBMS Course Project | Vasavi College of Engineering

---

## Tech Stack
| Layer    | Technology                  |
|----------|-----------------------------|
| Frontend | HTML, CSS, Bootstrap 5, JS  |
| Backend  | Spring Boot 3 (Java 17)     |
| Database | MySQL 8                     |
| Template | Thymeleaf                   |
| Config   | XML (DTD-validated)         |

---

## Project Structure
```
nids/
├── sql/
│   └── schema.sql              ← DDL, Trigger, Stored Procedures, Seed Data
├── src/main/
│   ├── java/com/nids/
│   │   ├── NidsApplication.java
│   │   ├── model/              ← 5 JPA Entities
│   │   ├── repository/         ← Spring Data JPA + custom JPQL queries
│   │   ├── service/            ← Business logic + @Transactional
│   │   └── controller/         ← 5 controllers (one per entity)
│   └── resources/
│       ├── templates/          ← Thymeleaf HTML pages
│       ├── static/css/style.css
│       ├── static/js/app.js
│       ├── nids-config.xml     ← XML with DTD (FSD requirement)
│       └── application.properties
└── pom.xml
```

---

## Setup & Run

### Step 1 — Database
```sql
mysql -u root -p
source /path/to/nids/sql/schema.sql
```

### Step 2 — Configure DB Password
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=your_mysql_password
```

### Step 3 — Run
```bash
cd nids
mvn spring-boot:run
```

### Step 4 — Open
```
http://localhost:8080
```

---

## DBMS Concepts Demonstrated

| Concept                  | Where                                          |
|--------------------------|------------------------------------------------|
| ER Model → Schema        | `schema.sql` DDL + 5 entity classes            |
| Normalization (BCNF)     | No redundancy across all 5 tables              |
| PK / FK Integrity        | All tables in `schema.sql`                     |
| Trigger                  | `trg_check_intrusion` fires on LOG insert      |
| Stored Procedure         | `sp_alert_summary`, `sp_device_threats`        |
| Indexing                 | On protocol, port, log_time, rule_id           |
| Transactions             | `@Transactional` on `saveLog()`                |
| Weak Entity              | `ALERT_REVIEW` (composite PK with ALERT)       |
| JOIN                     | Q1, Q3 — multi-table joins                     |
| GROUP BY / HAVING        | Q1 — count by severity                         |
| Subquery (NOT IN)        | Q4 — unreviewed alerts                         |
| ORDER BY + Aggregation   | Q2 — top triggered rules                       |
| M:N Resolution           | ALERT bridges NETWORK_LOG ↔ INTRUSION_RULE     |

---

## Demo Flow (Viva)
1. Register a device → `NETWORK_DEVICE` populated
2. Add a rule (e.g. TCP port 80 threshold 100) → `INTRUSION_RULE` populated
3. Insert log with 250 TCP packets on port 80 → **trigger fires** → `ALERT` auto-populated
4. Insert log with 10 packets → no alert generated
5. Submit review on alert → `ALERT_REVIEW` (weak entity) populated
6. Open Query Dashboard → show all 4 live queries on real data
