package app.model.entity.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginRequestDto {
    @Size(min = 8, message = "Username must be at least 8 characters")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
