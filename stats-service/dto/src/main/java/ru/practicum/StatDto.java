package ru.practicum;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatDto {
    private String app;
    private String uri;
    private long hits;
}
