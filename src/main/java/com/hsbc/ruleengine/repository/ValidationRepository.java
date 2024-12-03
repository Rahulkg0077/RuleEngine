package com.hsbc.ruleengine.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hsbc.ruleengine.entity.Validations;

public interface ValidationRepository extends MongoRepository<Validations, String> {


}
