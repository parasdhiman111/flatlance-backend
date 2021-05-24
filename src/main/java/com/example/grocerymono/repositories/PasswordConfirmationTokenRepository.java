package com.example.grocerymono.repositories;

import com.example.grocerymono.models.PasswordConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordConfirmationTokenRepository extends MongoRepository<PasswordConfirmationToken,String> {

    PasswordConfirmationToken findByConfirmationToken(String confirmationToken);

}
