# UrbondoUserService

Social network for condominium residents

## Table of Contents

1. [Overview](#description)
2. [Technologies](#tech-stack)
3. [Getting Started](#getting-started)
4. [Building](#build)
5. [REST APIs](#rest-apis)
6. [User](#user)

## Description

A server side project in purpose of exposing APIs that are used to CRUD users.

## Tech stack

* Java 17
* Spring Boot
* AWS Dynamo DB
* JUnit5
* Mockito

## Getting Started

Create the particular tables on AWS DynamoDB and make sure to provide your AWS credentials in order to run successfully
the app.

### Build

From the root folder, just execute the following command which will build/run unit and integration tests.

`./gradlew clean build`

### REST APIs

#### User

1. Fetch user by id `GET /user/{id}`

   Response `200`

    ```json
    {
      "id": "e3b42f92-37ea-4a6c-813c-46155e23197c",
      "firstName": "Foo",
      "lastName": "Bar",
      "email": "foo.bar@test.com",
      "phone": "12345678910"
    }
    ```

   Response `404 NOT FOUND`
   ```json
    {
      "message": "user id:{id} is not found"
    }
    ```

2. Add new user `POST /user`

   Request body
   ```json
    {
      "firstName": "Json",
      "lastName": "Jakarta",
      "email": "json.jakarta@test.com",
      "phone": "1334545677"
    }
    ```
   Response `201`
   ```json
   {
      "id": "e0716138-442d-43a8-a6e1-1e846c678d5a"
   }
   ```
   A bad request gets returned when either one of field gets blank or email/phone is not valid. Message's value
   depends on the invalid field.
   Response `400 BAD REQUEST`
   ```json
   {
     "message": "{validation's message}"
   }
   ```

3. Update an existing user `PUT /user/{id}`

   Request body
   ```json
    {
      "firstName": "Json",
      "lastName": "jakarta",
      "email": "json.jakarta@test.com",
      "phone": "01234563495"
    }
    ```

   Response `200`
    ```json
    {
      "id": "e0716138-442d-43a8-a6e1-1e846c678d5a",
      "firstName": "Json",
      "lastName": "jakarta",
      "email": "json.jakarta@test.com",
      "phone": "01234563495"
    }
    ```

   Response `404 NOT FOUND`
   ```json
   {
     "message": "user id:{id} is not found"
   }
   ```
   
   A bad request gets returned when either one of field gets blank or email/phone is not valid. Message's value 
   depends on the invalid field.
   Response `400 BAD REQUEST`
   ```json
   {
     "message": "{validation's message}"
   }
   ```

4. Delete an existing user `DELETE /user/{id}`

   Response `204`

   Response `404 NOT FOUND`
   ```json
   {
     "message": "user id:{id} is not found"
   }
   ```
