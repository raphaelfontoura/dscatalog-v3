package com.github.raphaelfontoura.dscatalog.services;

import com.github.raphaelfontoura.dscatalog.dto.CategoryDTO;
import com.github.raphaelfontoura.dscatalog.entities.Category;
import com.github.raphaelfontoura.dscatalog.repositories.CategoryRepository;
import com.github.raphaelfontoura.dscatalog.services.exceptions.DatabaseException;
import com.github.raphaelfontoura.dscatalog.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> page = repository.findAll(pageable);
        return page.map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = Category.builder()
                .name(dto.getName())
                .build();
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Categoria não localizada. Id: " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Categoria não localizada. Id: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao deleter registro do banco");
        }
    }
}
