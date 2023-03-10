package com.github.raphaelfontoura.dscatalog.dto;

import com.github.raphaelfontoura.dscatalog.services.validation.UserInsertValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@UserInsertValid
public class UserInsertDTO extends UserDTO {
    @NotBlank
    @Size(min = 6)
    private String password;

}
