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
import com.chatnloud.api.model.User;
import com.chatnloud.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private GroupService groupService;

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     *
     * @param newUser
     * @return
     */
    @Transactional
    public User createNewUser(User newUser) {
        if(isUserExists(newUser)) {
            String errorMessage = "Username or email already exists: %s or %s";
            throw new UserServiceExceptions.UserNotFoundException(String.format(errorMessage, newUser.getUsername(), newUser.getEmail()));
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
    public boolean isUsernameOrEmailRegistered(String username, String email) {
        return userRepository.isUsernameRegistered(username, email);
    }

    /**
     *
     * @param username
     */
    @Transactional
    public void deleteUser(String username) {
        Optional<User> possibleUserToDelete = userRepository.getUserByUsername(username);
        if(!possibleUserToDelete.isPresent()) {
            throw new UserServiceExceptions.UserNotFoundException("User doesn't exists: " + username);
        }

        User userToDelete = possibleUserToDelete.get();

        //Where he is the chat owner
        userToDelete.getChatGroups()
                .stream()
                .filter(cg -> cg.getCreator().equals(userToDelete))
                .forEach(cg -> {
                    groupService.deleteGroup(cg);
                });

        userRepository.delete(possibleUserToDelete.get());
    }

    private boolean isUserExists(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        return isUsernameOrEmailRegistered(username, email);
    }

}
