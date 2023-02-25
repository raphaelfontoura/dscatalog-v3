package com.github.raphaelfontoura.dscatalog.services;

import com.github.raphaelfontoura.dscatalog.dto.CategoryDTO;
import com.github.raphaelfontoura.dscatalog.entities.Category;
import com.github.raphaelfontoura.dscatalog.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
}
