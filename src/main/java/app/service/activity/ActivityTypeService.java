package app.service.activity;

import app.model.entity.activity.ActivityType;
import app.model.entity.dto.activity.ActivityTypeDto;
import app.model.mapper.activity.ActivityTypeMapper;
import app.repository.activity.ActivityTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivityTypeService {
    private final ActivityTypeRepository activityTypeRepository;

    @Autowired
    public ActivityTypeService(ActivityTypeRepository activityTypeRepository) {
        this.activityTypeRepository = activityTypeRepository;
    }

    public ActivityType getById(UUID id) {
        return activityTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity type not found"));
    }

    public ActivityTypeDto createActivityType(ActivityTypeDto activityTypeDto) {
        //1.check if activity type already exists
        if (activityTypeRepository.existsByName(activityTypeDto.getName())) {
            throw new IllegalArgumentException("Activity type already exists");
        }
        //2.DTO → Entity
        ActivityType newActivityType = ActivityTypeMapper.toEntity(activityTypeDto);
        //3.Entity → DB
        activityTypeRepository.save(newActivityType);
        //4.Return the created activity typeEntity → DTO
        return ActivityTypeMapper.toDto(newActivityType);
    }
}
