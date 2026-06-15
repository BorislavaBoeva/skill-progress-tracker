package app.service.activity;

import app.model.entity.activity.Activity;
import app.model.entity.category.Category;
import app.model.entity.dto.activity.ActivityDto;
import app.model.mapper.activity.ActivityMapper;
import app.repository.activity.ActivityRepository;
import app.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final CategoryService categoryService;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, CategoryService categoryService) {
        this.activityRepository = activityRepository;
        this.categoryService = categoryService;
    }

    public Activity getById(UUID id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
    }

    public ActivityDto createActivity(ActivityDto activityDto) {
        //1.check if activity type already exists
        if (activityRepository.existsByName(activityDto.getName())) {
            throw new IllegalArgumentException("Activity already exists");
        }
        //2.DTO → Entity
        Activity newActivity = ActivityMapper.toEntity(activityDto);
        //3.Category via CategoryService
        Category category = categoryService.getById(activityDto.getCategoryId());
        newActivity.setCategory(category);
        //4.Entity → DB
        activityRepository.save(newActivity);
        //5.Return the created activity Entity → DTO
        return ActivityMapper.toDto(newActivity);
    }
    public List<ActivityDto> getActivitiesByCategoryName(String categoryName) {
        Category category = categoryService.getByName(categoryName);
        return activityRepository.findAllByCategoryId(category.getId())
                .stream()
                .map(ActivityMapper::toDto)
                .toList();
    }
}
