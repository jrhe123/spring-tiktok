package com.imooc.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.imooc.mo.MessageMO;

@Repository
public interface MessageRepository extends MongoRepository<MessageMO, String> {

	List<MessageMO> findAllByToUserIdOrderByCreateTimeDesc(
			String toUserId,
			Pageable pageable
		);
}
