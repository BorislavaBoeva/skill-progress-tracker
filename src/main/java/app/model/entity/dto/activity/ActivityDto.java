package app.model.entity.dto.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private UUID id;
    //Todo: add validation "Cooking" и "Reading" са под Education, но Cooking логично принадлежи към Hobby.
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Category is required")
    private UUID categoryId;
 }
