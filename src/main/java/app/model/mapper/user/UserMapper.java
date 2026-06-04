package app.model.mapper.user;

import app.model.entity.dto.skill.SkillProgressDto;
import app.model.entity.dto.user.UserDto;
import app.model.entity.dto.user.UserRegisterRequestDto;
import app.model.entity.user.ProgressLevel;
import app.model.entity.user.User;
import app.model.mapper.skill.SkillProgressMapper;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
public class UserMapper {
    //DTO → Entity /POST, PUT request from user
    public static User toUserEntity(UserRegisterRequestDto userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        return User.builder()
                .username(userRegisterRequest.getUsername())
                .password(userRegisterRequest.getPassword())
                .email(userRegisterRequest.getEmail())
                .fullName(userRegisterRequest.getFullName())
                .profilePicture(userRegisterRequest.getProfilePicture())
                .education(ProgressLevel.BEGINNER)
                .physical(ProgressLevel.BEGINNER)
                .hobby(ProgressLevel.BEGINNER)
                .professional(ProgressLevel.BEGINNER)
                .educationPoints(0)
                .physicalPoints(0)
                .hobbyPoints(0)
                .professionalPoints(0)
                .prosperity(0)
                .progressEntries(new ArrayList<>())
                .build();
    }

    //DTO → Entity / GET request to user
    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        // list with all activity -> skill progress/{id=1, hours=2, activityName="Yoga"} {id=2, hours=1, activityType=READING}....
        List<SkillProgressDto> progressDto = user.getProgressEntries()
                .stream()
                .map(SkillProgressMapper::toDto)
                .toList();

       return UserDto.builder()
                .username(user.getUsername())
                .profilePicture(user.getProfilePicture())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .education(user.getEducation())
                .physical(user.getPhysical())
                .hobby(user.getHobby())
                .professional(user.getProfessional())
                .educationPoints(user.getEducationPoints())
                .physicalPoints(user.getPhysicalPoints())
                .hobbyPoints(user.getHobbyPoints())
                .professionalPoints(user.getProfessionalPoints())
                .prosperity(user.getProsperity())
                .progressEntries(progressDto)
                .build();
    }
}
