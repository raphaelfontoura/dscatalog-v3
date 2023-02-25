package com.github.raphaelfontoura.dscatalog.dto;

import com.github.raphaelfontoura.dscatalog.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CategoryDTO implements Serializable {
    private Long id;
    private String name;

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
    }
}
