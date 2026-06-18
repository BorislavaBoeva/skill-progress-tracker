package app.model.mapper.skill;

import app.model.entity.dto.activity.ActivityEntryDto;
import app.model.entity.dto.skill.SkillProgressDto;
import app.model.entity.sklill.SkillProgress;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SkillProgressMapper {
    // Стария метод — остава (ако се ползва другаде)
    public static SkillProgressDto toDto(SkillProgress skillProgress) {
        if (skillProgress == null) return null;
        return SkillProgressDto.builder()
                .activityId(skillProgress.getActivity().getId())
                .hours(skillProgress.getHours())
                .description(skillProgress.getDescription())
                .build();
    }

    // Новият метод — за progress страницата
    public static ActivityEntryDto toActivityEntryDto(SkillProgress skillProgress) {
        if (skillProgress == null) return null;
        return ActivityEntryDto.builder()
                .activityName(skillProgress.getActivity().getName())
                .hours(skillProgress.getHours())
                .build();
    }

}
