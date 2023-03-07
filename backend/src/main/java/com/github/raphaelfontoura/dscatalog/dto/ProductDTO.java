package com.github.raphaelfontoura.dscatalog.dto;

import com.github.raphaelfontoura.dscatalog.entities.Category;
import com.github.raphaelfontoura.dscatalog.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ProductDTO implements Serializable {
    private Long id;
    @Size(min = 3, max = 60, message = "Deve ter entre 3 e 60 caracteres")
    @NotBlank(message = "Campo requerido")
    private String name;
    @NotBlank(message = "Campo requerido")
    private String description;
    @Positive(message = "Valor deve ser um número positivo")
    private Double price;
    private String imgUrl;
    @PastOrPresent(message = "A data do produto não pode ser futura")
    private Instant date;
    private final Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();                                                  
        imgUrl = entity.getImgUrl();
        date = entity.getDate();
    }

    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(category -> this.categories.add(new CategoryDTO(category)));
    }
}
