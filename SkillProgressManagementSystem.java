src/main/java/app/
├── Application.java
├── config/          # Bean configuration (e.g. PasswordEncoder)
├── init/            # CategorySeeder – seeds the 4 categories on startup
├── model/
│   ├── entity/      # JPA entities: User, Activity, Category, SkillProgress
│   │   └── dto/     # Request/response DTOs
│   └── mapper/      # Static mapper classes (entity ↔ DTO)
├── repository/      # Spring Data JPA repositories
├── service/         # Business logic services
└── web/             # Spring MVC controllers

src/main/resources/
├── application.properties
├── static/css/      # Per-page stylesheets
├── static/images/   # UI assets
└── templates/       # Thymeleaf HTML templates
