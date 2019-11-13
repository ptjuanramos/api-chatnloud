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

package com.chatnloud.api.controller;

import com.chatnloud.api.exception.UserServiceExceptions;
import com.chatnloud.api.model.ChatGroup;
import com.chatnloud.api.model.User;
import com.chatnloud.api.service.GroupService;
import com.chatnloud.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @PostMapping
    public User signUp(@RequestBody User newUser) {
        String username = newUser.getUsername();
        if(userService.isUsernameRegistered(username)) {
            throw new UserServiceExceptions.UserNotCreatedException("Username already exists: " + username);
        }

        User newUserResult = userService.createNewUser(newUser);
        if(newUserResult == null) {
            throw new UserServiceExceptions.UserNotCreatedException("Could not create user: " + username);
        }

        return newUserResult;
    }

    @DeleteMapping
    public boolean removeUser(User user) {
        return false;
    }

    @PutMapping
    public User updateUser(User newUserInfo) {
        return null;
    }
}
