package app.model.dto.activity;

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
public class ActivitySelectDto {
    @NotNull(message = "Activity is required")
    private UUID id;
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
    @NotNull(message = "Category is required")
    private UUID categoryId;
}