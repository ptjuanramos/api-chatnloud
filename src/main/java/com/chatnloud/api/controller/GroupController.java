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

import com.chatnloud.api.model.ChatGroup;
import com.chatnloud.api.model.User;
import com.chatnloud.api.service.GroupService;
import com.chatnloud.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        User creator = userService.getUserByUsername("username"); //TODO get username from Authentication and user from userservice object later
        newGroup.setCreator(creator);
        return groupService.createNewGroup(newGroup);
    }

    @DeleteMapping
    public void deleteGroup(@RequestBody ChatGroup chatGroup) {
        ChatGroup foundChatGroup = groupService.findChatGroupById(chatGroup.getId());
        groupService.deleteGroup(foundChatGroup);
    }

    @PostMapping("join")
    public ChatGroup joinGroup(@RequestBody ChatGroup info) {
        ChatGroup existentGroup = groupService.findChatGroupByAccessCode(info.getAccessCode());
        User userToJoin = userService.getUserByUsername("username1"); //TODO get username from Authentication and user from userservice object later

        ChatGroup chatGroupWithNewUser = groupService.addUserToGroup(userToJoin, existentGroup);
        return chatGroupWithNewUser;
    }

    @DeleteMapping("leave")
    public void leaveGroup(@RequestBody ChatGroup info) {
        User userToJoin = userService.getUserByUsername("username"); //TODO get username from Authentication and user from userservice object later
    }
}
