# C4 Backend Documentation Specification

## 🎯 Objective
Generate and maintain C4 Model documentation focusing ONLY on the backend system.

The backend is a Spring-based application with layered architecture.

---

## 📂 Source Scope

The agent must analyze ONLY:

- /src/main/java
- /src/test
- /gradle
- /infra/docker-compose.yml
- build.gradle / settings.gradle

Ignore:
- frontend (if exists)
- unrelated folders

---

## 🧠 Architecture Assumption

The backend follows a layered architecture:

- controller → entry point (REST)
- service → business logic
- repository → data access
- domain → core entities
- dto → data transfer objects
- configuration → framework setup
- security → authentication/authorization

---

## 🧩 Package Mapping Rules

Map packages to components:

- controller → API Layer
- service → Business Layer
- repository → Persistence Layer
- domain → Domain Model
- security → Security Components
- configuration → Infrastructure Components

---

## 🌍 Context Diagram (C1)

Identify:

### Users:
- Default: "User"

### External Systems:
Infer from:
- HTTP clients (RestTemplate, WebClient, Feign)
- Messaging (Kafka, RabbitMQ)
- Auth providers (JWT, OAuth, Keycloak)

If none found:
- Only include User → Backend System

---

## 🧱 Container Diagram (C2)

Since this is backend-only:

Represent ONE main container:

- "Backend API" (Spring Boot)

Also include:

- Database (if repository exists)
  - Infer type from:
    - docker-compose
    - dependencies

- External systems (if detected)

---

## 🧠 Component Diagram (C3 - MAIN FOCUS)

Inside Backend API:

### Controllers
- Each controller class → Component
- Label: "REST Controller"

### Services
- Each service class → Component
- Label: "Business Service"

### Repositories
- Each repository interface → Component
- Label: "Repository"

### Security
- JWT filters, config classes → Components

### Configuration
- Only include if relevant (e.g., Beans, configs)

---

## 🔗 Relationships

Infer:

- Controller → Service
- Service → Repository
- Service → External APIs
- Security → Controllers

Use:
Rel(A, B, "calls")
Rel(A, B, "uses")

---

## 🧱 Required Outputs

Generate:

/docs/c4/backend-context.puml  
/docs/c4/backend-containers.puml  
/docs/c4/backend-components.puml  

---

## 🎨 PlantUML Standard

Use:

- C4-PlantUML
- LAYOUT_WITH_LEGEND()

Includes:

- !include <C4/C4_Context.puml>
- !include <C4/C4_Container.puml>
- !include <C4/C4_Component.puml>

---

## 📛 Naming Rules

Use real class/module names when possible:

- "UserController"
- "AuthService"
- "UserRepository"

Group logically when too many components:
- e.g., "User Module"

---

## 🚫 Constraints

- DO NOT invent frontend
- DO NOT create fake microservices
- DO NOT assume multiple containers
- DO NOT include unused layers

---

## 🔄 Update Strategy

- Detect new classes in:
  - controller
  - service
  - repository
- Update diagram incrementally

- Remove deleted components

---

## ✅ Quality Rules

- Avoid clutter:
  - Max ~15–20 components visible
- Group when needed
- Keep relationships readable