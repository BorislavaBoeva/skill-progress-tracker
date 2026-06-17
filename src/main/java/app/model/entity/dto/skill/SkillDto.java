package app.model.entity.dto.skill;

import app.model.entity.activity.Activity;
import app.model.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillDto {
    private UUID id;
    private LocalDate date = LocalDate.now();
    @NotNull(message = "Hours cannot be empty")
    @Positive(message = "Hours must be positive")
    private int hours;
    @NotNull(message = "Description cannot be empty")
    @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")
    private String description;
    private User owner;
    private Activity activityId;
}
