package org.nfe.web.db.mongo.repository;

import java.util.List;

import org.nfe.web.db.mongo.entidade.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
	public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);
}
