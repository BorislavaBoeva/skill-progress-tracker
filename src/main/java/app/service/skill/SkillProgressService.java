package app.service.skill;

import app.model.dto.activity.ActivitySelectDto;
import app.model.entity.activity.Activity;
import app.model.dto.skill.SkillProgressDto;
import app.model.entity.sklill.SkillProgress;
import app.model.entity.user.ProgressLevel;
import app.model.entity.user.User;
import app.model.mapper.skill.SkillProgressMapper;
import app.repository.skill.SkillProgressRepository;
import app.service.activity.ActivityService;
import app.service.user.UserService;
import jakarta.transaction.Transactional;
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
        User user = userService.getEntityById(skillProgress.getUser().getId());
        skillProgress.setUser(user);

        accumulatePoints(user, skillProgress.getActivity().getCategory().getName(), skillProgress.getHours());
        recalculateLevels(user);

        skillProgressRepository.save(skillProgress);
        userService.save(user);
    }

    private void accumulatePoints(User user, String categoryName, int points) {
        switch (categoryName) {
            case "education"    -> user.setEducationPoints(user.getEducationPoints() + points);
            case "physical"     -> user.setPhysicalPoints(user.getPhysicalPoints() + points);
            case "hobby"        -> user.setHobbyPoints(user.getHobbyPoints() + points);
            case "professional" -> user.setProfessionalPoints(user.getProfessionalPoints() + points);
            default -> throw new IllegalStateException("Unknown category: " + categoryName);
        }
    }

    private void recalculateLevels(User user) {
        user.setEducation(determineLevel(user.getEducationPoints()));
        user.setPhysical(determineLevel(user.getPhysicalPoints()));
        user.setHobby(determineLevel(user.getHobbyPoints()));
        user.setProfessional(determineLevel(user.getProfessionalPoints()));
    }

    private ProgressLevel determineLevel(int points) {
        if (points < 100) return ProgressLevel.BEGINNER;
        if (points < 200) return ProgressLevel.INTERMEDIATE;
        if (points < 300) return ProgressLevel.ADVANCED;
        return ProgressLevel.MASTER;
    }

   public void saveLog(SkillProgressDto dto, UUID userId) {
        User user = userService.getEntityById(userId);
        Activity activity = activityService.getById(dto.getActivityId());

        SkillProgress skillProgress = new SkillProgress();
        skillProgress.setUser(user);
        skillProgress.setActivity(activity);
        skillProgress.setHours(dto.getHours());
        skillProgress.setDescription(dto.getDescription());

        addSkillProgress(skillProgress);
    }

    public SkillProgressDto buildLogDto(ActivitySelectDto activitySelectDto) {
        return SkillProgressMapper.fromActivitySelect(activitySelectDto);
    }

    public ActivitySelectDto buildSelectDto(SkillProgressDto skillProgressDto, UUID categoryId) {
        return SkillProgressMapper.toActivitySelect(skillProgressDto, categoryId);
    }
}