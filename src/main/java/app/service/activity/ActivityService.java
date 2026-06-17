package app.service.activity;

import app.model.entity.activity.Activity;
import app.model.entity.category.Category;
import app.model.entity.dto.activity.ActivityDto;
import app.model.entity.user.User;
import app.model.mapper.activity.ActivityMapper;
import app.repository.activity.ActivityRepository;
import app.service.category.CategoryService;
import app.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, CategoryService categoryService, UserService userService) {
        this.activityRepository = activityRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public Activity getById(UUID userId) {
        return activityRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
    }

    public ActivityDto createActivity(ActivityDto activityDto, UUID userId) {
        User user = userService.getEntityById(userId);


        //1.check if activity type already exists
        if (activityRepository.existsByNameAndUserId(activityDto.getName(), userId)) {
            throw new IllegalArgumentException("Activity already exists");
        }
        //2.DTO → Entity
        Activity newActivity = ActivityMapper.toEntity(activityDto);

        //3.Category via CategoryService
        Category category = categoryService.getById(activityDto.getCategoryId());
        newActivity.setCategory(category);
        newActivity.setUser(user);

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

    public List<ActivityDto> getActivitiesByCategoryNameAndUser(String categoryName, UUID userId) {
        Category category = categoryService.getByName(categoryName);

        return activityRepository.findAllByCategoryIdAndUserId(category.getId(), userId)
                .stream()
                .map(ActivityMapper::toDto)
                .toList();
    }

    public void deleteActivity(UUID id, UUID userId) {
        Activity activity = activityRepository.findById(UUID.fromString(id.toString()))
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

        // Проверка дали activity принадлежи на user-а
        if (!activity.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You cannot delete another user's activity");
        }

        activityRepository.delete(activity);
    }
}
