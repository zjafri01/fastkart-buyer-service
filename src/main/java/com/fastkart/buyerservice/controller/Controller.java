package com.fastkart.buyerservice.controller;

import com.fastkart.buyerservice.model.Product;
import com.fastkart.buyerservice.service.BuyerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/buyer")
@CrossOrigin
@Slf4j
public class Controller {

    @Autowired
    private BuyerService buyerService;

    private LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();

    @PostMapping("/viewAvailableProducts")
    public ResponseEntity<List<Product>> viewAvailableProducts(){
        log.info("Request received to view available products");

        return ResponseEntity.status(HttpStatus.OK).body(buyerService.viewProducts());
    }

    @PostMapping("/placeBid")
    public ResponseEntity placeBid(@RequestBody Product product){
        try {
            responseMap.clear();
            log.info("Request received to place a bid for product");
            return ResponseEntity.status(HttpStatus.OK).body(buyerService.placeBid(product));
        }
        catch (Exception e){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", e.getMessage());
            responseMap.put("errorMessage",e.getMessage());
            responseMap.put("statusCode",HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(responseMap);
        }
    }
}
