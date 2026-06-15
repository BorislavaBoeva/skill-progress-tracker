package app.service.skill;

import app.model.entity.category.Category;
import app.model.entity.dto.user.UserDto;
import app.model.entity.sklill.SkillProgress;
import app.model.entity.user.ProgressLevel;
import app.model.entity.user.User;
import app.repository.skill.SkillProgressRepository;
import app.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillProgressService {
    private final SkillProgressRepository skillProgressRepository;
    private final UserService userService;

    @Autowired
    public SkillProgressService(SkillProgressRepository skillProgressRepository, UserService userService) {
        this.skillProgressRepository = skillProgressRepository;
        this.userService = userService;
    }

    public void addSkillProgress(SkillProgress skillProgress) {

        // 1) Зареждаме user
        User user = userService.getEntityById(skillProgress.getOwner().getId());
        skillProgress.setOwner(user);

        // 2) Записваме SkillProgress
        skillProgressRepository.save(skillProgress);

        // 3) ВЗИМАМЕ КАТЕГОРИЯТА ОТ ACTIVITY TYPE
        Category category = skillProgress.getActivity().getCategory();
        String categoryName = category.getName();

        int points = skillProgress.getHours();

        // 4) Добавяме точки към правилната категория
        switch (categoryName) {
            case "EDUCATION" -> user.setEducationPoints(user.getEducationPoints() + points);
            case "PHYSICAL" -> user.setPhysicalPoints(user.getPhysicalPoints() + points);
            case "HOBBY" -> user.setHobbyPoints(user.getHobbyPoints() + points);
            case "PROFESSIONAL" -> user.setProfessionalPoints(user.getProfessionalPoints() + points);
        }

        // 5) Обновяваме нивата
        user.setEducation(increaseLevel(user.getEducationPoints()));
        user.setPhysical(increaseLevel(user.getPhysicalPoints()));
        user.setHobby(increaseLevel(user.getHobbyPoints()));
        user.setProfessional(increaseLevel(user.getProfessionalPoints()));

        // 6) Обновяваме общия прогрес
        user.setProsperity(calculateProsperity(user));

        // 7) Записваме user
        userService.save(user);
    }

    private ProgressLevel increaseLevel(int points) {
        if (points < 100) return ProgressLevel.BEGINNER;
        if (points < 200) return ProgressLevel.INTERMEDIATE;
        if (points < 300) return ProgressLevel.ADVANCED;
        return ProgressLevel.MASTER;
    }

    private int calculateProsperity(User user) {
        int sum =
                levelToPoints(user.getEducation()) +
                        levelToPoints(user.getPhysical()) +
                        levelToPoints(user.getHobby()) +
                        levelToPoints(user.getProfessional());
        return (sum * 100) / 16;
    }

    private int levelToPoints(ProgressLevel level) {
        return switch (level) {
            case BEGINNER -> 1;
            case INTERMEDIATE -> 2;
            case ADVANCED -> 3;
            case MASTER -> 4;
        };
    }
}

