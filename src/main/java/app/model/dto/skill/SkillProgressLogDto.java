package app.model.dto.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillProgressLogDto {
    private UUID id;
    private String activityName;
    private int hours;
    private String description;
}
