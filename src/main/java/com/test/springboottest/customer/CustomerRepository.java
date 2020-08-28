package com.test.springboottest.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    @Query("SELECT c FROM CustomerEntity c WHERE c.phoneNumber = ?1")
    Optional<Customer> selectCustomerByPhoneNumber(String phoneNumber);

}
