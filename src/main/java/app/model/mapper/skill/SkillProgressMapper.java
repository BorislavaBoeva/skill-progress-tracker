package app.model.mapper.skill;

import app.model.entity.dto.skill.SkillProgressDto;
import app.model.entity.sklill.SkillProgress;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SkillProgressMapper {

    public static SkillProgressDto toDto(SkillProgress skillProgress) {
        if (skillProgress == null) {return null;}

        return SkillProgressDto.builder()
                .id(skillProgress.getId())
                .category(skillProgress.getActivityType().getCategory())
                .date(skillProgress.getDate())
                .hours(skillProgress.getHours())
                .description(skillProgress.getDescription())
                .activityName(skillProgress.getActivityType().getName())
                .build();
    }
}
