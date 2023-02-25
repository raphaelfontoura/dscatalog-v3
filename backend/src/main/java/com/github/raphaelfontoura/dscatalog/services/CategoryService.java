package com.github.raphaelfontoura.dscatalog.services;

import com.github.raphaelfontoura.dscatalog.entities.Category;
import com.github.raphaelfontoura.dscatalog.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> findAll() {
        return repository.findAll();
    }
}
