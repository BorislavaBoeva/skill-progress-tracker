package app.model.entity.dto.activity;

import app.model.entity.activity.SkillCategory;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class ActivityTypeDto {
    private UUID id;
    private SkillCategory category;
    private String name;
}
