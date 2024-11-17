package ru.practicum.evm.compilation.adminCompilation;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.evm.compilation.dto.CompilationDto;
import ru.practicum.evm.compilation.dto.NewCompilationDto;
import ru.practicum.evm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.evm.compilation.model.Compilation;
import ru.practicum.evm.compilation.repository.CompilationRepository;
import ru.practicum.evm.event.EventRepository;
import ru.practicum.evm.exception.NotFoundException;
import ru.practicum.evm.exception.ParameterNotValidException;

import java.sql.SQLException;

@RequiredArgsConstructor
@Service
public class CompilationAdminService  {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    @Qualifier("mvcConversionService")
    private final ConversionService converter;

    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {

        if (newCompilationDto == null || newCompilationDto.title().isBlank()) {
            throw new ParameterNotValidException("Compilation", "Запрос составлен некорректно");
        }
        Compilation comp = converter.convert(newCompilationDto, Compilation.class);
        try {
            assert comp != null;
            comp.setEvents(eventRepository.findAllById(newCompilationDto.events()));
            return converter.convert(compilationRepository.save(comp), CompilationDto.class);
        } catch (PersistenceException e) {
            throw new ConstraintViolationException("Compilation", new SQLException(), "Нарушение целостности данных");
        }
    }

    public void deleteCompilation(Long compId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка событий не найдена"));
        compilationRepository.delete(comp);
    }

    public CompilationDto updateCompilation(UpdateCompilationRequest upComp, Long compId) {
       Compilation newComp = compilationRepository.findById(compId)
                .orElseThrow(() -> (new NotFoundException("Подборка с ID: " + compId + " отсутствует")));

      if (!upComp.events().isEmpty()) newComp.setEvents(eventRepository.findAllById(upComp.events()));
      if (!ObjectUtils.isEmpty(upComp.pinned())) newComp.setPinned(upComp.pinned());
      if (!upComp.title().isEmpty()) newComp.setTitle(upComp.title());
      return converter.convert(compilationRepository.save(newComp), CompilationDto.class);
    }
}
