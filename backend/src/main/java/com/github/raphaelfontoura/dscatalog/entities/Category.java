package com.github.raphaelfontoura.dscatalog.entities;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
public class Category implements Serializable {
    private Long id;
    private String name;
}
