package app.service.skill;

import app.model.entity.activity.Activity;
import app.model.entity.category.Category;
import app.model.entity.dto.skill.SkillProgressDto;
import app.model.entity.sklill.SkillProgress;
import app.model.entity.user.ProgressLevel;
import app.model.entity.user.User;
import app.repository.skill.SkillProgressRepository;
import app.service.activity.ActivityService;
import app.service.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class SkillProgressService {
    private final SkillProgressRepository skillProgressRepository;
    private final ActivityService activityService;
    private final UserService userService;

    @Autowired
    public SkillProgressService(SkillProgressRepository skillProgressRepository, ActivityService activityService, UserService userService) {
        this.skillProgressRepository = skillProgressRepository;
        this.activityService = activityService;
        this.userService = userService;
    }

    public void addSkillProgress(SkillProgress skillProgress) {

        // 1) Зареждаме user
        User user = userService.getEntityById(skillProgress.getUser().getId());
        skillProgress.setUser(user);

        // 2) Записваме SkillProgress
        skillProgressRepository.save(skillProgress);

        // 3) ВЗИМАМЕ КАТЕГОРИЯТА ОТ ACTIVITY TYPE
        Category category = skillProgress.getActivity().getCategory();
        String categoryName = category.getName();

        int points = skillProgress.getHours();

        // 4) Добавяме точки към правилната категория
        switch (categoryName) {
            case "education" -> user.setEducationPoints(user.getEducationPoints() + points);
            case "physical" -> user.setPhysicalPoints(user.getPhysicalPoints() + points);
            case "hobby" -> user.setHobbyPoints(user.getHobbyPoints() + points);
            case "professional" -> user.setProfessionalPoints(user.getProfessionalPoints() + points);
            default -> throw new IllegalStateException("Unknown category: " + categoryName);
        }

        // 5) Обновяваме нивата
        user.setEducation(increaseLevel(user.getEducationPoints()));
        user.setPhysical(increaseLevel(user.getPhysicalPoints()));
        user.setHobby(increaseLevel(user.getHobbyPoints()));
        user.setProfessional(increaseLevel(user.getProfessionalPoints()));

        userService.save(user);
    }

    private ProgressLevel increaseLevel(int points) {
        if (points < 100) return ProgressLevel.BEGINNER;
        if (points < 200) return ProgressLevel.INTERMEDIATE;
        if (points < 300) return ProgressLevel.ADVANCED;
        return ProgressLevel.MASTER;
    }

     public UUID getCategoryIdByActivity(UUID activityId) {
        Activity activity = activityService.getById(activityId);
        return activity.getCategory().getId();
    }

    public void saveLog(@Valid SkillProgressDto dto, UUID userId) {
        User user = userService.getEntityById(userId);
        Activity activity = activityService.getById(dto.getActivityId());

        SkillProgress skillProgress = new SkillProgress();
        skillProgress.setUser(user);
        skillProgress.setActivity(activity);
        skillProgress.setHours(dto.getHours());
        skillProgress.setDescription(dto.getDescription());

        // reuse the addSkillProgress method
        addSkillProgress(skillProgress);
    }
}

