package app.model.dto.user;


import app.model.entity.user.ProgressLevel;
import app.model.entity.user.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class UserDto {
    private UUID id;
    private String username;
    private String profilePicture;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private boolean enabled;
    private ProgressLevel education;
    private ProgressLevel physical;
    private ProgressLevel hobby;
    private ProgressLevel professional;
    private int educationPoints;
    private int physicalPoints;
    private int hobbyPoints;
    private int professionalPoints;
    private int prosperity;
 }