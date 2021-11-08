package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class GroupLikes {
    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "group_id")
    @ManyToOne
    private Group group;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public GroupLikes(Group group,User user)
    {
        this.group = group;
        this.user = user;
    }

}
