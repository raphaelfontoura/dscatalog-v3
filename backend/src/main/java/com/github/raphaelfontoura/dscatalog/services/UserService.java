package com.github.raphaelfontoura.dscatalog.services;

import com.github.raphaelfontoura.dscatalog.dto.UserDTO;
import com.github.raphaelfontoura.dscatalog.dto.UserInsertDTO;
import com.github.raphaelfontoura.dscatalog.dto.UserUpdateDTO;
import com.github.raphaelfontoura.dscatalog.entities.Role;
import com.github.raphaelfontoura.dscatalog.entities.User;
import com.github.raphaelfontoura.dscatalog.repositories.RoleRepository;
import com.github.raphaelfontoura.dscatalog.repositories.UserRepository;
import com.github.raphaelfontoura.dscatalog.services.exceptions.DatabaseException;
import com.github.raphaelfontoura.dscatalog.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageRequest) {
        Page<User> userPage = repository.findAll(pageRequest);
        return userPage.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não cadastrado"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = repository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Usuário não localizada. Id: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao deletar registro do banco");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        dto.getRoles().forEach(roleDto -> {
            Role role = roleRepository.getReferenceById(roleDto.getId());
            entity.getRoles().add(role);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).orElseThrow(() -> {
            logger.error("User not found:" + username);
            return new UsernameNotFoundException("Username not found");
        });
    }
}
