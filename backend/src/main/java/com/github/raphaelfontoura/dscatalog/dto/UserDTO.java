package com.github.raphaelfontoura.dscatalog.dto;

import com.github.raphaelfontoura.dscatalog.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserDTO implements Serializable {
    private Long id;
    @NotBlank(message = "Campo Obrigatório")
    private String firstName;
    private String lastName;
    @Email(message = "Entre com um email válido")
    @NotEmpty
    private String email;

    private final Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(User entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        entity.getRoles().forEach(role -> roles.add(new RoleDTO(role)));
    }
}
