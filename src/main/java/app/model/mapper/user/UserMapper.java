package app.model.mapper.user;

import app.model.dto.activity.ActivityEntryDto;
import app.model.dto.category.CategoryProgressDto;
import app.model.dto.user.UserDto;
import app.model.dto.user.UserEditRequestDto;
import app.model.dto.user.UserProgressDto;
import app.model.dto.user.UserRegisterRequestDto;
import app.model.entity.sklill.SkillProgress;
import app.model.entity.user.ProgressLevel;
import app.model.entity.user.User;
import app.model.entity.user.UserRole;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class UserMapper {
    //DTO → Entity  - for registration
    public static User toUserEntity(UserRegisterRequestDto userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        return User.builder()
                .username(userRegisterRequest.getUsername())
                .password(userRegisterRequest.getPassword())
                .profilePicture(userRegisterRequest.getProfilePicture())
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .email(userRegisterRequest.getEmail())
                .role(UserRole.USER)
                .education(ProgressLevel.BEGINNER)
                .physical(ProgressLevel.BEGINNER)
                .hobby(ProgressLevel.BEGINNER)
                .professional(ProgressLevel.BEGINNER)
                .build();
    }

    //Entity → DTO - for login, getById, updateProfile
    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profilePicture(user.getProfilePicture())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .education(user.getEducation())
                .physical(user.getPhysical())
                .hobby(user.getHobby())
                .professional(user.getProfessional())
                .educationPoints(user.getEducationPoints())
                .physicalPoints(user.getPhysicalPoints())
                .hobbyPoints(user.getHobbyPoints())
                .professionalPoints(user.getProfessionalPoints())
                .prosperity(user.getProsperity())
                .build();
    }

    public static UserEditRequestDto toEditRequestDto(UserDto user) {
        if (user == null) {
            return null;
        }
        return UserEditRequestDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .build();
    }
    public static UserProgressDto toUserProgressDto(User user) {
        if (user == null) return null;

        Map<String, List<ActivityEntryDto>> byCategory = user.getProgressEntries()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getActivity().getCategory().getName(),
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(
                                        entry -> entry.getActivity().getName(),
                                        Collectors.summingInt(SkillProgress::getHours)
                                ),
                                map -> map.entrySet().stream()
                                        .map(e -> ActivityEntryDto.builder()
                                                .activityName(e.getKey())
                                                .hours(e.getValue())
                                                .build())
                                        .toList()
                        )
                ));

        List<CategoryProgressDto> categories = List.of(
                buildCategory("education", user.getEducation(), user.getEducationPoints(), byCategory),
                buildCategory("physical", user.getPhysical(), user.getPhysicalPoints(), byCategory),
                buildCategory("hobby", user.getHobby(), user.getHobbyPoints(), byCategory),
                buildCategory("professional", user.getProfessional(), user.getProfessionalPoints(), byCategory)
        );

        return UserProgressDto.builder()
                .id(user.getId())
                .categories(categories)
                .build();
    }

    private static CategoryProgressDto buildCategory(String name,
                                                     ProgressLevel level,
                                                     int points,
                                                     Map<String, List<ActivityEntryDto>> byCategory) {
        return CategoryProgressDto.builder()
                .categoryName(name)
                .level(level)
                .points(points)
                .activities(byCategory.getOrDefault(name, List.of()))
                .build();
    }
}