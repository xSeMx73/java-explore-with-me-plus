import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class StatDto {
    @NotBlank
    private LocalDateTime start;
    @NotBlank
    private LocalDateTime end;
    private List<String> uris;
    @NotBlank
    private boolean unique;
}
