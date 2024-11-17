package ru.practicum.evm.compilation.dto.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.evm.compilation.dto.NewCompilationDto;
import ru.practicum.evm.compilation.model.Compilation;

@Component
public class NewCompilationDtoToCompilationConverter implements Converter<NewCompilationDto, Compilation> {

    @Override
    public Compilation convert(NewCompilationDto source) {
        return Compilation.builder().pinned(source.pinned()).title(source.title()).build();
    }
}
