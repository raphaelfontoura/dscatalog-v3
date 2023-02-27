package com.github.raphaelfontoura.dscatalog.services;

import com.github.raphaelfontoura.dscatalog.dto.ProductDTO;
import com.github.raphaelfontoura.dscatalog.entities.Product;
import com.github.raphaelfontoura.dscatalog.fixture.ProductFixture;
import com.github.raphaelfontoura.dscatalog.repositories.CategoryRepository;
import com.github.raphaelfontoura.dscatalog.repositories.ProductRepository;
import com.github.raphaelfontoura.dscatalog.services.exceptions.DatabaseException;
import com.github.raphaelfontoura.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 99L;
        dependentId = 2L;
        product = ProductFixture.createProduct();
        page = new PageImpl<>(List.of(product));

        doNothing().when(repository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(repository.save(any())).thenReturn(product);
        when(repository.findById(existingId)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(repository.getReferenceById(existingId)).thenReturn(product);
        doThrow(EntityNotFoundException.class).when(repository).getReferenceById(nonExistingId);
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

    @Test
    void deleteShouldThrowsDatabaseExceptionWhenIdHasDependency() {
        assertThrows(DatabaseException.class, () -> service.deleteProduct(dependentId));
    }

    @Test
    void findAllShouldReturnProductPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void fingByIdShouldReturnProductDTOWhenIdExist() {
        ProductDTO result = service.findById(existingId);
        assertNotNull(result);
        verify(repository, times(1)).findById(existingId);
    }

    @Test
    void findByIdShouldThrowsResourceNotFoundExceptionWhenIdNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
        verify(repository, times(1)).findById(nonExistingId);
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExist() {
        ProductDTO productDTO = ProductFixture.createProductDTO();

        ProductDTO result = service.update(existingId, productDTO);

        assertNotNull(result);
        verify(repository, times(1)).save(any());
        verify(repository, times(1)).getReferenceById(existingId);
    }

    @Test
    void updateShoulThrowsResourceNotFoundExceptionWhenIdNotExist() {
        ProductDTO productDTO = ProductFixture.createProductDTO();
        productDTO.setId(nonExistingId);

        assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, productDTO));

        verify(repository, times(1)).getReferenceById(nonExistingId);
        verify(repository, never()).save(any());
    }
}
