package com.urbondo.user.service.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
class UserRepositoryImpl implements UserRepository {
    private final DynamoDBMapper dynamoDBMapper;

    UserRepositoryImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public UserDAO findById(final String id) {
        return dynamoDBMapper.load(UserDAO.class, id);
    }

    @Override
    public boolean isEmailExist(final String email) {
        Map<String, AttributeValue> attributesValues = new HashMap<>();
        attributesValues.put(":email", new AttributeValue().withS(email));

        Map<String, String> attributesNames = new HashMap<>();
        attributesNames.put("#email", "email");

        DynamoDBQueryExpression<UserDAO> queryExpression = new DynamoDBQueryExpression<UserDAO>().withIndexName("email")
                .withKeyConditionExpression("#email = :email")
                .withExpressionAttributeNames(attributesNames)
                .withExpressionAttributeValues(attributesValues)
                .withConsistentRead(false);

        return !dynamoDBMapper.query(UserDAO.class, queryExpression).isEmpty();
    }

    @Override
    public void save(final UserDAO userDAO) {
        dynamoDBMapper.save(userDAO);
    }

    @Override
    public void delete(final UserDAO userDAO) {
        dynamoDBMapper.delete(userDAO);
    }
}
