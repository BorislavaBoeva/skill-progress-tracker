package app.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditRequestDto {
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    private String lastName;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    private String email;
    private String profilePicture;
}