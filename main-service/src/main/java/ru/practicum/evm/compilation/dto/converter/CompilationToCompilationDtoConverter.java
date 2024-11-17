package ru.practicum.evm.compilation.dto.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.evm.compilation.dto.CompilationDto;
import ru.practicum.evm.compilation.model.Compilation;
import ru.practicum.evm.event.converter.EventToEventShortResponseDtoConverter;

@RequiredArgsConstructor
@Component
public class CompilationToCompilationDtoConverter implements Converter<Compilation, CompilationDto> {

    private final EventToEventShortResponseDtoConverter converter;

    @Override
    public CompilationDto convert(Compilation source) {
        return CompilationDto.builder()
                .id(source.getId())
                .title(source.getTitle())
                .pinned(source.getPinned())
                .events(source.getEvents().stream().map(converter::convert).toList())
                .build();
    }
}
