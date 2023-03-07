package com.github.raphaelfontoura.dscatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserInsertDTO extends UserDTO {
    @NotBlank
    @Size(min = 6)
    private String password;

}
