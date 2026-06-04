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

    @Column(name = "profile_picture", unique = true)
    private String profilePicture;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    //BEGINNER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressLevel education = ProgressLevel.BEGINNER;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressLevel physical = ProgressLevel.BEGINNER;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressLevel hobby = ProgressLevel.BEGINNER;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressLevel professional = ProgressLevel.BEGINNER;

    // -all points are starting from 0
    @Column(name = "education_points", nullable = false)
    private int educationPoints = 0;
    @Column(name = "physical_points", nullable = false)
    private int physicalPoints = 0;
    @Column(name = "hobby_points", nullable = false)
    private int hobbyPoints = 0;
    @Column(name = "professional_points", nullable = false)
    private int professionalPoints = 0;
    // prosperity is calculated from all points in the range 0-100 in percents
    @Column(name = "prosperity", nullable = false)
    private int prosperity = 0;

   //history OF progress
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SkillProgress> progressEntries = new ArrayList<>();
}
