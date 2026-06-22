package app.service.activity;

import app.exception.ActivityNotFoundException;
import app.exception.DuplicateResourceException;
import app.exception.UnauthorizedActionException;
import app.model.entity.activity.Activity;
import app.model.entity.category.Category;
import app.model.dto.activity.ActivityDto;
import app.model.entity.user.User;
import app.model.mapper.activity.ActivityMapper;
import app.repository.activity.ActivityRepository;
import app.service.category.CategoryService;
import app.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
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
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
    }

    public void createActivity(ActivityDto activityDto, UUID userId) {
        User user = userService.getEntityById(userId);

        //1.check if activity type already exists
        if (activityRepository.existsByNameAndUserIdAndActiveTrue(activityDto.getName(), userId)) {
            throw new DuplicateResourceException("Activity already exists");
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
        ActivityMapper.toDto(newActivity);
    }

    public List<ActivityDto> getActivitiesByCategoryNameAndUser(String categoryName, UUID userId) {
        Category category = categoryService.getByName(categoryName);

        return activityRepository.findAllByCategoryIdAndUserIdAndActiveTrue(category.getId(), userId)
                .stream()
                .map(ActivityMapper::toDto)
                .toList();
    }

    public void deleteActivity(UUID id, UUID userId) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
        // Check if it is this activity belong to the user
        if (!activity.getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException("You cannot delete another user's activity");
        }

        activity.setActive(false);
        activityRepository.save(activity);
    }

    public void deleteAllByUser(UUID id) {
        activityRepository.deleteAllByUserId(id);
    }
}