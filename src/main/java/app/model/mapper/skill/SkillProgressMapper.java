package app.model.mapper.skill;

import app.model.dto.activity.ActivitySelectDto;
import app.model.dto.skill.SkillProgressDto;
import app.model.dto.skill.SkillProgressLogDto;
import app.model.entity.skill.SkillProgress;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

    public static ActivitySelectDto toActivitySelect(SkillProgressDto skillProgressDto, UUID categoryId) {
        if (skillProgressDto == null) {
            return null;
        }
        return ActivitySelectDto.builder()
                .id(skillProgressDto.getActivityId())
                .categoryId(categoryId)
                .build();
    }

    public static SkillProgressLogDto toLogDto(SkillProgress entry) {
        if (entry == null) {
            return null;
        }
        return SkillProgressLogDto.builder()
                .id(entry.getId())
                .activityName(entry.getActivity().getName())
                .hours(entry.getHours())
                .description(entry.getDescription())
                .build();
    }
}