User
 ├── username, password (BCrypt), email, firstName, lastName
 ├── profilePicture
 ├── educationPoints, physicalPoints, hobbyPoints, professionalPoints
 ├── education, physical, hobby, professional  (ProgressLevel)
 ├── prosperity  (int, 0–100)
 └── progressEntries  → List<SkillProgress>

Category          ← seeded: education, physical, hobby, professional
 └── name

Activity
 ├── name
 ├── category  → Category
 └── user      → User  (optional — global or personal)

SkillProgress
 ├── date        (defaults to today)
 ├── hours
 ├── description
 ├── owner       → User
 └── activity    → Activity
