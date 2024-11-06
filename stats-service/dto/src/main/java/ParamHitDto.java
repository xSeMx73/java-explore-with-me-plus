import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ParamHitDto {
    private String app;
    private String url;
    private long hits;
}

