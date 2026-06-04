package app.model.mapper.activity;

import app.model.entity.activity.ActivityType;
import app.model.entity.dto.activity.ActivityTypeDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ActivityTypeMapper {

    public static ActivityType toEntity(ActivityTypeDto activityTypeDto) {
        if (activityTypeDto == null) { return null;}
        return ActivityType.builder()
                .id(activityTypeDto.getId())
                .category(activityTypeDto.getCategory())
                .name(activityTypeDto.getName())
                .build();
    }

    public static ActivityTypeDto toDto(ActivityType entity) {
        if (entity == null) { return null;}
        return ActivityTypeDto.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .name(entity.getName())
                .build();
    }
}
