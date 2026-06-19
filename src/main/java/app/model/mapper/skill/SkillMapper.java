package app.model.mapper.skill;

import app.model.entity.dto.skill.SkillDto;
import app.model.entity.sklill.SkillProgress;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SkillMapper {

    public static SkillDto toDto(SkillProgress skillProgress) {
        if (skillProgress == null) {
            return null;
        }

        return SkillDto.builder()
                .id(skillProgress.getId())
                .hours(skillProgress.getHours())
                .description(skillProgress.getDescription())
                .activityId(skillProgress.getActivity().getId())
                .activityName(skillProgress.getActivity().getName())
                .categoryId(skillProgress.getActivity().getCategory().getId())
                .categoryName(skillProgress.getActivity().getCategory().getName())
                .build();
    }
}
