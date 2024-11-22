package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.StatRequestDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatsController {

   private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<HitDto> hit(@RequestBody HitDto hitDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statsService.hit(hitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatDto>> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        StatRequestDto dto = new StatRequestDto(uris, start, end, unique);
        return ResponseEntity.ok().body(statsService.getStats(dto));
    }
}