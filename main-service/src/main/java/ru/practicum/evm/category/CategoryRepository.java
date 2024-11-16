package ru.practicum.evm.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
