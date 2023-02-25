package com.github.raphaelfontoura.dscatalog.resources;

import com.github.raphaelfontoura.dscatalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryResource {

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        List<Category> list = Arrays.asList(
                new Category(1L, "Books"),
                new Category(2L, "Electronics")
        );
        return ResponseEntity.ok(list);
    }
}
