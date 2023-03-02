package com.github.raphaelfontoura.dscatalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.raphaelfontoura.dscatalog.dto.ProductDTO;
import com.github.raphaelfontoura.dscatalog.fixture.ProductFixture;
import com.github.raphaelfontoura.dscatalog.services.ProductService;
import com.github.raphaelfontoura.dscatalog.services.exceptions.DatabaseException;
import com.github.raphaelfontoura.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductResource.class)
class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper mapper;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private long existingId;
    private long nonExistingId;

    private long dependentId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 99L;
        dependentId = 2L;

        productDTO = ProductFixture.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        when(service.findAll(any(Pageable.class))).thenReturn(page);
        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        doNothing().when(service).deleteProduct(existingId);
        doThrow(ResourceNotFoundException.class).when(service).deleteProduct(nonExistingId);
        doThrow(DatabaseException.class).when(service).deleteProduct(dependentId);
        when(service.createNewProduct(any(ProductDTO.class))).thenReturn(productDTO);
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnProductWhenIdExist() throws Exception {
        mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(productDTO.getName()));
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdNotExist() throws Exception {
        mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void insertShouldReturnCreated() throws Exception {
        ProductDTO newProduct = ProductFixture.createProductDTO();
        newProduct.setId(null);
        String jsonBody = mapper.writeValueAsString(newProduct);

        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/products/1"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(productDTO.getName()))
                .andExpect(jsonPath("$.description").value(productDTO.getDescription()));
    }

    @Test
    void updateShouldReturnProductWhenIdExist() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);
        mockMvc.perform(put("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(productDTO.getName()))
                .andExpect(jsonPath("$.description").value(productDTO.getDescription()));
    }

    @Test
    void updateShouldReturnNotFoundWhenIdNotExist() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);
        mockMvc.perform(put("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deleteShouldReturnNoContentWhenIdExist() throws Exception {
        mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdNotExist() throws Exception {
        mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deleteShouldReturnBadRequestWhenIdIsDepedent() throws Exception {
        mockMvc.perform(delete("/products/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
