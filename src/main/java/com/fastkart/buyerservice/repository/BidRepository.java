package com.fastkart.buyerservice.repository;

import com.fastkart.buyerservice.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, String> {

    public Bid save(Bid bid);

    public Bid findById(Long productId);

}
