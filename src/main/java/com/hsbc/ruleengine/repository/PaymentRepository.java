package com.hsbc.ruleengine.repository;

import com.hsbc.ruleengine.entity.FileModel;
import com.hsbc.ruleengine.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByStatus(String status);
}
