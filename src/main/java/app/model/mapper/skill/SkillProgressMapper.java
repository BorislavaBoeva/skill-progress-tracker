package app.model.mapper.skill;

import app.model.dto.skill.SkillProgressDto;
import app.model.entity.sklill.SkillProgress;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SkillProgressMapper {
      public static SkillProgressDto toDto(SkillProgress skillProgress) {
        if (skillProgress == null) return null;
        return SkillProgressDto.builder()
                .activityId(skillProgress.getActivity().getId())
                .hours(skillProgress.getHours())
                .description(skillProgress.getDescription())
                .build();
    }
}
