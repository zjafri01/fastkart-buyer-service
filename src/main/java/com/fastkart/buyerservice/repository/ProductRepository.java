package com.fastkart.buyerservice.repository;

import com.fastkart.buyerservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public Product save(Product product);

    //public Product findProductBy(Product product);

    public Product findProductById(Long id);

    public List<Product> findAll();

}