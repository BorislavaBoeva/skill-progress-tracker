package app.model.entity.dto.skill;

import app.model.entity.activity.SkillCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
@Data
@Builder
public class SkillProgressDto {
    private UUID id;
    private SkillCategory category;
    private LocalDate date;
    private int hours;
    private String description;
    private UUID activityTypeId;
    private String activityName;

}
