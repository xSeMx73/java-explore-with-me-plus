package ru.practicum.evm.category;

import org.mapstruct.Mapper;
import ru.practicum.evm.category.dto.CategoryCreateDto;
import ru.practicum.evm.category.dto.CategoryResponseDto;
import ru.practicum.evm.category.dto.CategoryUpdateDto;
import ru.practicum.evm.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryCreateDto createDto);

    Category toCategory(CategoryUpdateDto updateDto);

    CategoryResponseDto toCategoryDto(Category category);

    CategoryResponseDto toCategoryDto(CategoryUpdateDto updateDto);
}
