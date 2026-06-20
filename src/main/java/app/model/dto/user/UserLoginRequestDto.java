package app.model.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginRequestDto {
    @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
    private String username;
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}