package com.example.grocerymono.repositories;

import com.example.grocerymono.models.Property;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends MongoRepository<Property,String> {

    List<Property> findByCheckPropertyOwner(boolean checkPropertyOwner);
    List<Property> findByCheckPropertyOwnerAndUserId(boolean checkPropertyOwner,String userId);

}
