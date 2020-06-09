package com.barbutov.network_of_giving.data.contracts;

import com.barbutov.network_of_giving.data.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
