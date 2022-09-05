package com.imooc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.imooc.mo.MessageMO;

@Repository
public interface MessageRepository extends MongoRepository<MessageMO, String> {

}
