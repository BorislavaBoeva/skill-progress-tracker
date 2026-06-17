package app.model.mapper.skill;

import app.model.entity.dto.skill.SkillDto;
import app.model.entity.dto.skill.SkillProgressDto;
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
                .date(skillProgress.getDate())
                .hours(skillProgress.getHours())
                .description(skillProgress.getDescription())
                .owner(skillProgress.getOwner())
                .activityId(skillProgress.getActivity())
                .build();
    }
}
