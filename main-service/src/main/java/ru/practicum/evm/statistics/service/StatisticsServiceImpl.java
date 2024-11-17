package ru.practicum.evm.statistics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.StatRequestDto;
import ru.practicum.StatWebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    private final StatWebClient statsClient;

    @Override
    public void saveStats(HttpServletRequest request) {
        statsClient.hit(new HitDto(
                "ewm-main-service",
                request.getRemoteAddr(),
                request.getRequestURI(),
                LocalDateTime.now())
        );
    }

    @Override
    public Map<Long, Long> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) {

        ObjectMapper mapper = new ObjectMapper();
        List<StatDto> stats;

        StatRequestDto requestDTO = new StatRequestDto(
                uris,
                start,
                end,
                true);

        List<StatDto> response = statsClient.get(requestDTO).collectList().block();

        if (response != null) {
            if (response.isEmpty()) {
            return uris.stream().map(this::getEventIdFromUri)
                    .collect(Collectors.toMap(Function.identity(), s -> 0L));
            } else {
                stats = response.stream()
                        .map(e -> mapper.convertValue(e, StatDto.class))
                        .collect(Collectors.toList());
                log.info("Данные статистики --> {}", stats);
                return stats.stream()
                        .collect(Collectors.toMap(ViewStats -> getEventIdFromUri(ViewStats.getUri()),
                                StatDto::getHits));
            }
        } else {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Long getEventIdFromUri(String uri) {
        String[] parts = uri.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}