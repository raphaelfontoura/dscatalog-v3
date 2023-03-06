package com.github.raphaelfontoura.dscatalog.repositories;

import com.github.raphaelfontoura.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
