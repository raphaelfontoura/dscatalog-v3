package com.github.raphaelfontoura.dscatalog.services.validation;

import com.github.raphaelfontoura.dscatalog.dto.UserUpdateDTO;
import com.github.raphaelfontoura.dscatalog.repositories.UserRepository;
import com.github.raphaelfontoura.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();
        var user = repository.findByEmail(dto.getEmail());
        if (user.isPresent() && user.get().getId() != userId) {
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
