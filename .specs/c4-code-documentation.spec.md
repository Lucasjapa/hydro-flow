# C4 Code Level Documentation Specification

## 🎯 Objective

Generate code-level diagrams (C4 Level 4) based on backend source code.

Focus on structure and relationships, NOT implementation details.

---

## 📂 Source Scope

Analyze:

- /src/main/java

Focus on packages:

- controller
- service
- repository
- domain
- dto
- security

---

## 🧠 Diagram Types

### 1. Class Diagrams (REQUIRED)

Generate modular class diagrams grouped by domain.

Each diagram must include:

- Controllers
- Services
- Repositories
- Domain entities
- DTOs (if relevant)

---

### 2. Entity Relationship Diagram (REQUIRED if JPA exists)

From domain/entities:

- Identify:
    - @Entity
    - @OneToMany
    - @ManyToOne
    - @ManyToMany

Generate:

/docs/c4/code-entities.puml

---

### 3. Sequence Diagrams (OPTIONAL)

Only for important flows:

- Authentication
- Main business operation

Limit to max 2–3 diagrams

---

## 🧩 Grouping Strategy

Group by domain/module:

Examples:

- user
- auth
- scheduling
- payment

---

## 🧱 Required Outputs

Generate:

/docs/c4/code/
user-classes.puml
auth-classes.puml
...
entities.puml
(optional) sequence-*.puml

---

## 🎨 Class Diagram Rules

Use PlantUML:

- class
- interface

Include:

- Class name
- Key attributes (only important ones)
- Relationships

DO NOT include:
- getters/setters
- trivial methods

---

## 🔗 Relationships

Use:

- --> (dependency)
- -->|> (inheritance)
- o-- (aggregation)
- *-- (composition)

---

## 📛 Naming

Use real class names:

- UserController
- AuthService
- UserEntity

---

## 🚫 Constraints

- DO NOT generate a single massive diagram
- DO NOT include irrelevant classes
- DO NOT infer non-existent relationships

---

## 🔄 Update Strategy

- Detect new classes
- Detect removed classes
- Update only affected diagrams

---

## ✅ Quality Rules

- Max ~15 classes per diagram
- Prefer multiple small diagrams
- Keep diagrams readable