package com.github.raphaelfontoura.dscatalog.services;

import com.github.raphaelfontoura.dscatalog.repositories.CategoryRepository;
import com.github.raphaelfontoura.dscatalog.repositories.ProductRepository;
import com.github.raphaelfontoura.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;
    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 99L;

        doNothing().when(repository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> service.deleteProduct(existingId));

        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    void deleteShouldThrowsResourceNotFoundExceptionWhenIdNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> service.deleteProduct(nonExistingId));

        verify(repository, times(1)).deleteById(nonExistingId);
    }
}