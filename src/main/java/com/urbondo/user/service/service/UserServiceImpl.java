package com.urbondo.user.service.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.urbondo.user.service.exception.UserAlreadyFoundException;
import com.urbondo.user.service.exception.UserNotFoundException;
import com.urbondo.user.service.model.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final DynamoDBMapper dynamoDBMapper;

    public UserServiceImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public UserDTO findById(String id) {
        return Mapper.transferToUserDTO(findUserEntityById(id));
    }

    @Override
    public AddUserResponseDTO add(final AddUserRequestDTO requestDTO) {
        Map<String, AttributeValue> attributesValues = new HashMap<>();
        attributesValues.put(":email", new AttributeValue().withS(requestDTO.getEmail()));

        Map<String, String> attributesNames = new HashMap<>();
        attributesNames.put("#email", "email");

        DynamoDBQueryExpression<UserEntity> queryExpression = new DynamoDBQueryExpression<UserEntity>().withIndexName(
                        "email")
                .withKeyConditionExpression("#email = :email")
                .withExpressionAttributeNames(attributesNames)
                .withExpressionAttributeValues(attributesValues)
                .withConsistentRead(false);


        if (!dynamoDBMapper.query(UserEntity.class, queryExpression).isEmpty()) {
            throw new UserAlreadyFoundException(requestDTO.getEmail());
        }

        UserEntity userEntity = new UserEntity(UUID.randomUUID().toString(),
                                               requestDTO.getFirstName(),
                                               requestDTO.getLastName(),
                                               requestDTO.getEmail(),
                                               requestDTO.getPhone());

        dynamoDBMapper.save(userEntity);

        return new AddUserResponseDTO(userEntity.getId());
    }


    @Override
    public UserDTO updateBy(final UpdateUserRequestDTO requestDTO) {
        UserEntity userEntity = findUserEntityById(requestDTO.getId());

        userEntity.setFirstName(requestDTO.getFirstName());
        userEntity.setLastName(requestDTO.getLastName());
        userEntity.setEmail(requestDTO.getEmail());
        userEntity.setPhone(requestDTO.getPhone());

        dynamoDBMapper.save(userEntity);

        return Mapper.transferToUserDTO(userEntity);
    }

    @Override
    public void deleteBy(String id) {
        dynamoDBMapper.delete(findUserEntityById(id));
    }

    private UserEntity findUserEntityById(String id) {
        UserEntity userEntity = dynamoDBMapper.load(UserEntity.class, id);
        if (userEntity == null) {
            throw new UserNotFoundException(id);
        }

        return userEntity;
    }
}
