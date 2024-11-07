package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.StatDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {


    @Query(value = "select new ru.practicum.StatDto" +
                   "(h.app, h.uri, count(distinct h.ip)) from Hit as h " +
                   "where h.timestamp between ?1 and ?2 " +
                   "and h.uri in ?3 " +
                   "group by h.app, h.uri " +
                   "order by count(distinct h.ip) desc")
    List<StatDto> getWithUriAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);


    @Query(value = "select new ru.practicum.StatDto" +
                   "(h.app, h.uri, count(h.uri)) from Hit as h " +
                   "where h.timestamp between ?1 and ?2 " +
                   "group by h.app, h.uri " +
                   "order by count(h.uri) desc ")
    List<StatDto> getWithoutUriAndUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.StatDto" +
                   "(h.app, h.uri, count(h.uri)) from Hit as h " +
                   "where h.timestamp between ?1 and ?2 " +
                   "and h.uri in ?3 " +
                   "group by h.app, h.uri " +
                   "order by count(h.uri) desc ")
    List<StatDto> getWithUriAndNotUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.StatDto" +
                   "(h.app, h.uri, count(h.uri)) from Hit as h " +
                   "where h.timestamp between ?1 and ?2 " +
                   "group by h.app, h.uri " +
                   "order by count(h.uri) desc ")
    List<StatDto> getWithoutUriAndNotUniqueIp(LocalDateTime start, LocalDateTime end);
}
