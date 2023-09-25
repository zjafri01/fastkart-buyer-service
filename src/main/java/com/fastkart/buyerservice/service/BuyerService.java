package com.fastkart.buyerservice.service;

import com.fastkart.buyerservice.model.Bid;
import com.fastkart.buyerservice.model.Product;
//import com.fastkart.buyerservice.model.User;
import com.fastkart.buyerservice.model.User;
import com.fastkart.buyerservice.repository.BidRepository;
import com.fastkart.buyerservice.repository.ProductRepository;
//import com.fastkart.buyerservice.repository.UserDetailsRepository;
import com.fastkart.buyerservice.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BuyerService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BidRepository bidRepository;

    public List<Product> viewProducts() throws Exception {
        List<Product> userProducts = null;
        userProducts = productRepository.findAll();

        if(userProducts.isEmpty()){
            throw new Exception("There are no products to be bought at the moment, try your luck after some time!");
        }

        Stream<Product> stream = userProducts.parallelStream();
        List<Product> productListSortedDescendingOrder = stream.collect(reverseStream()).toList();
        return productListSortedDescendingOrder;
    }

    public Product placeBid(Product product) throws Exception {

        try{
            Product productDetails = productRepository.findProductById(product.getId());
            List<Bid> bidList = productDetails.getBid();

            if(product.getCurrent_bid_amt()<=productDetails.getCurrent_bid_amt()){
                throw new Exception("Bid amount too low, minimum bid is $"+productDetails.getCurrent_bid_amt());
            }
            productDetails.setCurrent_bid_amt(product.getCurrent_bid_amt());

            User userDetails = userDetailsRepository.findByUsername(product.getUsername());

            Bid bid = new Bid(userDetails, userDetails.getUsername(), productDetails.getCurrent_bid_amt());
            bidRepository.save(bid);
            bidList.add(bid);
            productDetails.setBid(bidList);
            productRepository.save(productDetails);

            Stream<Bid> stream = productDetails.getBid().parallelStream();
            productDetails.setBid(null);

            List<Bid> bidListSortedDescendingOrder = stream.collect(reverseStream()).toList();
            productDetails.setBid(bidListSortedDescendingOrder);

            return productDetails;
        }
        catch (NullPointerException e){
            return null;
        }
    }

    public static <T> Collector<T, ?, Stream<T> > reverseStream()
    {
        return Collectors
                .collectingAndThen(Collectors.toList(),
                        list -> {
                            Collections.reverse(list);
                            return list.stream();
                        });
    }
}
