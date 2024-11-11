package ru.practicum;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatController {

    private final StatWebClient statClient;

    @PostMapping("/hit")
    public Mono<ResponseEntity<HitDto>> hit(@RequestBody HitDto hitDto) {
        log.info("Новая запись в сервисе статистики {}", hitDto);
        return statClient.hit(hitDto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/stats")
    public Mono<ResponseEntity<List<StatDto>>> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        if (start.isAfter(end)) {
            throw new ValidationException("Неверные параметры запроса");
        }
        StatRequestDto dto = new StatRequestDto(uris, start, end, unique);
        log.info("Запрос статистики");
        return statClient.get(dto)
                .collectList()
                .map(response -> ResponseEntity.ok().body(response));
    }
}
