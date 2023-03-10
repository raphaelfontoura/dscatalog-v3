package com.github.raphaelfontoura.dscatalog.resources;

import com.github.raphaelfontoura.dscatalog.dto.UserDTO;
import com.github.raphaelfontoura.dscatalog.dto.UserInsertDTO;
import com.github.raphaelfontoura.dscatalog.dto.UserUpdateDTO;
import com.github.raphaelfontoura.dscatalog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService service;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(
            @PageableDefault(value = 12) Pageable pageable
    ) { // {server address}/categories?size=12&page=0&sort=name,asc
        return ResponseEntity.ok(service.findAllPaged(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO insertDTO) {
        UserDTO dto = service.insert(insertDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
