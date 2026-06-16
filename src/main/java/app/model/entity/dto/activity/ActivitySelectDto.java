package app.model.entity.dto.activity;

import jakarta.validation.constraints.NotBlank;
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
    private UUID id;
    @NotBlank(message = "Name is required")
    private String name;
    private UUID categoryId;
}

