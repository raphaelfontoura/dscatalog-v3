package com.github.raphaelfontoura.dscatalog.services.validation;

import com.github.raphaelfontoura.dscatalog.dto.UserInsertDTO;
import com.github.raphaelfontoura.dscatalog.entities.User;
import com.github.raphaelfontoura.dscatalog.repositories.UserRepository;
import com.github.raphaelfontoura.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        Optional<User> user = repository.findByEmail(dto.getEmail());

        if (user.isPresent()) {
            list.add(new FieldMessage("email", "Email já existe"));
        }

        list.forEach(fieldMessage -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldMessage.getMessage())
                    .addPropertyNode(fieldMessage.getFieldName())
                    .addConstraintViolation();
        });

        return list.isEmpty();
    }
}
