package app.model.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterRequestDto {
    @Size(min = 8, message = "Username must be at least 8 characters")
    private String username;
    private String profilePicture;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Full name cannot be empty")
    private String fullName;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
