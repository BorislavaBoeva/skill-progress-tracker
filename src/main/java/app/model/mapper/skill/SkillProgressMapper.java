package app.model.mapper.skill;

import app.model.dto.activity.ActivitySelectDto;
import app.model.dto.skill.SkillProgressDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SkillProgressMapper {

    public static SkillProgressDto fromActivitySelect(ActivitySelectDto activitySelectDto) {
        if (activitySelectDto == null) {
            return null;
        }
        return SkillProgressDto.builder()
                .activityId(activitySelectDto.getId())
                .categoryId(activitySelectDto.getCategoryId())
                .build();
    }
}