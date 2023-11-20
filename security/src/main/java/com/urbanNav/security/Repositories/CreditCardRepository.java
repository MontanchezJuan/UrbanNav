package com.urbanNav.security.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.urbanNav.security.Models.CreditCard;

public interface CreditCardRepository extends MongoRepository<CreditCard, String> {
    @Query("{'cardNumber':?0}")
    Optional<CreditCard> getCreditCard(String cardNumber);
}
