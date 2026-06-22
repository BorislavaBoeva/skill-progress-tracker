package app.model.dto.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillProgressEditDto {
    @NotNull
    private UUID id;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 2, max = 100, message = "Description must be between 2 and 100 characters")
    private String description;
}
