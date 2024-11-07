package ru.practicum;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StatDto {
    private String app;
    private String uri;
    private long hits;
}
