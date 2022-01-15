# Library Application

## How to run

1. Import the project in your preferred IDE
2. Run LibraryApplication.java to start the Spring Boot application

## Requirements fulfilled

a) returns all users who have actually borrowed at least one book

`curl --request GET --url http://localhost:8080/users/borrowers`

b) returns all non-terminated users who have not currently borrowed anything

`curl --request GET --url http://localhost:8080/users/active-non-borrowers`

## Notes

- Important queries are in UserRepository.java
- Test cases are in UserRepositoryIntegrationTest.java