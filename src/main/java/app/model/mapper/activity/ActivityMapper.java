package app.model.mapper.activity;

import app.model.entity.activity.Activity;
import app.model.entity.dto.activity.ActivityDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ActivityMapper {

    public static Activity toEntity(ActivityDto activityDto) {
        if (activityDto == null) {
            return null;
        }
        return Activity.builder()
                .id(activityDto.getId())
                .name(activityDto.getName())
                .build();
    }
    //set Category only in service layer
    public static ActivityDto toDto(Activity entity) {
        if (entity == null) {
            return null;
        }
        return ActivityDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .categoryId(entity.getCategory().getId())
                .build();
    }
}
