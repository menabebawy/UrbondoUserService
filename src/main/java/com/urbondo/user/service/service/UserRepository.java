package com.urbondo.user.service.service;

import com.urbondo.user.service.model.UserEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
@EnableScan
public interface UserRepository extends CrudRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
}
