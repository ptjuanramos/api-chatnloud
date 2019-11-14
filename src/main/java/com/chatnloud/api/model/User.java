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

package com.chatnloud.api.model;

import com.chatnloud.api.constants.ConstraintMessage;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonIgnore
    @Column(name = "image_url_path")
    private String imageUrlPath;

    @Column(unique =  true)
    @NotBlank(message = ConstraintMessage.CONSTRAINT_NOT_EMPTY_USERNAME)
    private String username;

    @NotBlank(message = ConstraintMessage.CONSTRAINT_NOT_EMPTY_NAME)
    private String name;

    @Column(name = "last_name")
    @JsonProperty(value = "last_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String salt; //TODO generate in controller or service level

    @Email
    @Column(unique =  true)
    @NotBlank(message = ConstraintMessage.CONSTRAINT_NOT_EMPTY_EMAIL)
    private String email;

    @ManyToMany(mappedBy = "users")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties("users")
    @Size(max = 5)
    private List<ChatGroup> chatGroups;
}
