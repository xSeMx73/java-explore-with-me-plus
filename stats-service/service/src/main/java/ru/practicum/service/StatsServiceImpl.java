package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

   private final StatsRepository statsRepository;
   private final HitMapper hitMapper;

    @Override
    public HitDto hit(HitDto hitDto) {
        return hitMapper.toHitDto(statsRepository.save(hitMapper.toHit(hitDto)));
    }

    @Override
    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            if (uris != null) {
                return statsRepository.getWithUriAndUniqueIp(start, end, uris);
            } else {
                return statsRepository.getWithoutUriAndUniqueIp(start, end);
            }
        } else {
            if (uris != null) {
                return statsRepository.getWithUriAndNotUniqueIp(start, end, uris);
            } else {
                return statsRepository.getWithoutUriAndNotUniqueIp(start, end);
            }
        }

    }
}