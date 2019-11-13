/*
 * Copyright 2019
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.chatnloud.api.service;

import com.chatnloud.api.exception.UserServiceExceptions;
import com.chatnloud.api.model.ChatGroup;
import com.chatnloud.api.model.User;
import com.chatnloud.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @param newUser
     * @return
     */
    public User createNewUser(User newUser) {
        if(isUserExists(newUser)) {
            throw new UserServiceExceptions.UserNotFoundException("Username already exists: " + newUser.getUsername());
        }

        return userRepository.save(newUser);
    }

    /**
     *
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        Optional<User> possibleUserResult = userRepository.getUserByUsername(username);
        return possibleUserResult.orElseThrow(() -> new UserServiceExceptions.UserNotFoundException("Couldn't found any user"));
    }

    /**
     *
     * @param username
     * @return
     */
    public boolean isUsernameRegistered(String username) {
        return userRepository.isUsernameRegistered(username);
    }

    /**
     *
     * @param user
     */
    public void deleteUser(User user) {
        if(!isUserExists(user)) {
            throw new UserServiceExceptions.UserNotFoundException("User doesn't exists: " + user.getUsername());
        }

        userRepository.delete(user);
    }

    private boolean isUserExists(User user) {
        String username = user.getUsername();
        return isUsernameRegistered(username);
    }

}
