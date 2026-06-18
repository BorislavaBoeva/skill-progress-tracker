package app.model.entity.dto.user;

import app.model.entity.dto.skill.SkillProgressDto;
import app.model.entity.user.ProgressLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;
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
    private ProgressLevel education = ProgressLevel.BEGINNER;
    private ProgressLevel physical = ProgressLevel.BEGINNER;
    private ProgressLevel hobby = ProgressLevel.BEGINNER;
    private ProgressLevel professional = ProgressLevel.BEGINNER;
    private int educationPoints;
    private int physicalPoints;
    private int hobbyPoints;
    private int professionalPoints;

    private int prosperity;
    private List<SkillProgressDto> progressEntries;
}
