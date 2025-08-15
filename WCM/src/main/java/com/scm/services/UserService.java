
package com.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.entities.User;

public interface UserService {

    User saveUser(User user); // save user to db

    Optional<User> getUserById(String id); // get user by id from db

    Optional<User> updateUser(User user); // get user by email from db

    void deleteUser(String id); // delete user by id from db

    boolean isUserExist(String userId); // check if user exists in db

    boolean isUserExistByEmail(String email); // check if user exists by email in db

    List<User> getAllUsers(); // get all users from db

    User getUserByEmail(String email); // get user by email from db

    //add more methods here related user service[logics] 

}
