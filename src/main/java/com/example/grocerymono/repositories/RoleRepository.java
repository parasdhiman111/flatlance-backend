package com.example.grocerymono.repositories;

import com.example.grocerymono.models.Role;
import com.example.grocerymono.models.enums.RoleEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role,String> {
    Optional<Role> findByName(RoleEnum role);
}
