import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StatDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uris;
    private boolean unique;
}
