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

import com.chatnloud.api.exception.ChatRoomServiceExceptions;
import com.chatnloud.api.generators.AccessCodeSequenceGenerator;
import com.chatnloud.api.model.ChatGroup;
import com.chatnloud.api.model.GroupInfo;
import com.chatnloud.api.model.User;
import com.chatnloud.api.service.GroupService;
import com.chatnloud.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@RequestMapping("group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ChatGroup createGroup(@RequestBody ChatGroup newGroup) {
        User creator = userService.getUserByUsername("username").get(); //TODO get username from Authentication and user from userservice object later

        newGroup.setCreator(creator);

        String generatedAccessCode = AccessCodeSequenceGenerator.generate();
        newGroup.setAccessCode(generatedAccessCode);
        return groupService.createNewGroup(newGroup);
    }

    @DeleteMapping
    public void deleteGroup(@RequestBody GroupInfo info) {

    }

    @PostMapping("join")
    public ChatGroup joinGroup(@RequestBody GroupInfo info) {
        Optional<ChatGroup> existentGroup = groupService.findChatGroupByAccessCode(info.getAccessCode());
        if(!existentGroup.isPresent()) {
            String notFoundMessage = "Group not found for %s access code";
            new ChatRoomServiceExceptions.ChatRoomNotExistException(String.format(notFoundMessage, info.getAccessCode()));
        }

        User userToJoin = userService.getUserByUsername("username").get(); //TODO get username from Authentication and user from userservice object later

        ChatGroup chatGroupWithNewUser = groupService.addUserToGroup(userToJoin, existentGroup.get());
        return chatGroupWithNewUser;
    }

    @DeleteMapping("leave")
    public void leaveGroup() {

    }
}
