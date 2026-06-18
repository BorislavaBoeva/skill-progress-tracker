package app.model.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditRequestDto {
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private String lastName;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    private String profilePicture;
}

