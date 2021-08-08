package com.test.demo.repositories;


import com.test.demo.model.Card;
import com.test.demo.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /*@Query("SELECT c FROM Card c WHERE c.currency = 'USD'")*/
    Set<Card> getAllByCurrency(Currency currency);

    @Query("SELECT c FROM Card c")
    Set<Card> getAllCards();

}
