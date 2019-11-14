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

import com.chatnloud.api.exception.ChatGroupServiceExceptions;
import com.chatnloud.api.generators.AccessCodeSequenceGenerator;
import com.chatnloud.api.model.ChatGroup;
import com.chatnloud.api.model.User;
import com.chatnloud.api.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService {

    private final static int GROUPS_MAX_NUMBER = 5;

    @Autowired
    private GroupRepository groupRepository;

    /**
     *
     * @param group
     * @return
     */
    public ChatGroup createNewGroup(ChatGroup group) {
        User creator = group.getCreator();
        if(creator.getChatGroups().size() == GROUPS_MAX_NUMBER) {
            String errorMessage = "The number maximum of users is %s";
            throw new ChatGroupServiceExceptions.TooManyChatGroupsException(String.format(errorMessage, GROUPS_MAX_NUMBER));
        }

        String generatedAccessCode = AccessCodeSequenceGenerator.generate();
        group.setAccessCode(generatedAccessCode);
        group = addUserToGroup(creator, group);

        return groupRepository.save(group);
    }

    /**
     *
     * @param group
     */
    public void deleteGroup(ChatGroup group) {
        groupRepository.delete(group);
    }

    /**
     *
     * @param id
     * @return
     */
    public ChatGroup findChatGroupById(UUID id) {
        Optional<ChatGroup> returnedChatGroup = groupRepository.findById(id);
        if(!returnedChatGroup.isPresent()) {
            String notFoundMessage = "Chat Group not found";
            throw new ChatGroupServiceExceptions.ChatRoomNotExistException(notFoundMessage);
        }

        return returnedChatGroup.get();
    }

    /**
     *
     * @param accessCode
     * @return
     */
    public ChatGroup findChatGroupByAccessCode(String accessCode) {
        Optional<ChatGroup> existentGroup = groupRepository.findByAccessCode(accessCode);
        if(!existentGroup.isPresent()) {
            String notFoundMessage = "Chat Group not found for %s access code";
            throw new ChatGroupServiceExceptions.ChatRoomNotExistException(String.format(notFoundMessage, accessCode));
        }

        return existentGroup.get();
    }

    /**
     *
     * @param user
     * @param group
     * @return
     */
    public ChatGroup addUserToGroup(User user, ChatGroup group) {
        if(group.getUsers() == null) {
            group.setUsers(new ArrayList<>());
        }

        if(group.getUsers().contains(user)) {
            String errorMessage = "User %s already in group";
            throw new ChatGroupServiceExceptions.UserAlreadyInGroupException(String.format(errorMessage, user.getUsername()));
        }

        group.getUsers().add(user);
        return group;
    }
}
