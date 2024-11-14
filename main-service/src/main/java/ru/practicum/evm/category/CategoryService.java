package ru.practicum.evm.category;

import ru.practicum.evm.category.dto.CategoryCreateDto;
import ru.practicum.evm.category.dto.CategoryResponseDto;
import ru.practicum.evm.category.dto.CategoryUpdateDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto create(CategoryCreateDto createDto);

    CategoryResponseDto update(CategoryUpdateDto updateDto);

    void remove(long categoryId);

    CategoryResponseDto getById(long categoryId);

    List<CategoryResponseDto> getFromSize(int from, int size);
}
