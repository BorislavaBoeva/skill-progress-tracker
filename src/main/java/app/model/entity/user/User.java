package app.model.entity.user;

import app.model.entity.sklill.SkillProgress;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "profile_picture")
    private String profilePicture;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "Last_name", nullable = false)
    private String lastName;

    //BEGINNER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('BEGINNER', 'INTERMEDIATE', 'ADVANCED') default 'BEGINNER'")
    private ProgressLevel education;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('BEGINNER', 'INTERMEDIATE', 'ADVANCED') default 'BEGINNER'")
    private ProgressLevel physical;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('BEGINNER', 'INTERMEDIATE', 'ADVANCED') default 'BEGINNER'")
    private ProgressLevel hobby;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('BEGINNER', 'INTERMEDIATE', 'ADVANCED') default 'BEGINNER'")
    private ProgressLevel professional;
    // -all points are starting from 0
    @Column(name = "education_points", nullable = false, columnDefinition = "int default 0")
    private int educationPoints;
    @Column(name = "physical_points", nullable = false, columnDefinition = "int default 0")
    private int physicalPoints;
    @Column(name = "hobby_points", nullable = false, columnDefinition = "int default 0")
    private int hobbyPoints;
    @Column(name = "professional_points", nullable = false, columnDefinition = "int default 0")
    private int professionalPoints;
    // prosperity is calculated from all points in the range 0-100 in percents
    @Column(name = "prosperity", nullable = false, columnDefinition = "int default 0")
    private int prosperity;

   //history OF progress
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SkillProgress> progressEntries = new ArrayList<>();
}
