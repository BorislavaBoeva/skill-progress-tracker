package app.model.dto.skill;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillProgressDto {
    private UUID activityId;
    private String activityName;
    private UUID categoryId;
    private String categoryName;

    @NotNull(message = "Hours cannot be empty")
    @Positive(message = "Hours must be positive")
    private Integer hours;

    @NotNull(message = "Description cannot be empty")
    @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")
    private String description;
}