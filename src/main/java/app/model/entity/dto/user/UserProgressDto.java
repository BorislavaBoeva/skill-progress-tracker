package app.model.entity.dto.user;

import app.model.entity.dto.skill.SkillProgressDto;
import app.model.entity.user.ProgressLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressDto {
    private UUID id;
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
