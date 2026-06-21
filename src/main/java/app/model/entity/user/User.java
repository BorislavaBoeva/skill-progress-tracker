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
    @Column(name = "username", nullable = false, unique = true,length = 20)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "profile_picture")
    private String profilePicture;
    @Column(name = "email", nullable = false, unique = true,length = 50)
    private String email;
    @Column(name = "first_name", nullable = false,length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false,length = 50)
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    //BEGINNER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgressLevel education;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgressLevel physical;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgressLevel hobby;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
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
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SkillProgress> progressEntries = new ArrayList<>();
}