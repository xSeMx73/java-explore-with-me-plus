import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ParamDto {
    @NotBlank
    private String app;
    @NotBlank
    private String url;
    @NotBlank
    private String ip;
    @NotBlank
    private LocalDateTime timestamp;
}
