package app.model.entity.dto.skill;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
@Data
@Builder
public class SkillProgressDto {
    private UUID id;
    private LocalDate date;
    private int hours;
    private String description;
    private UUID activityId;
    private String activityName;
}
