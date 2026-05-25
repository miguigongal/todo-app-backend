package com.miguigongal.todo_app_backend.model.domain;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


public interface UserDao extends CrudRepository<User, Long> {
    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String userName);

}
