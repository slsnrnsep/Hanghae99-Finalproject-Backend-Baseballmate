package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CommunityLikes {

    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "community_id")
    @ManyToOne
    private Community communitylikes;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public CommunityLikes(Community community, User user)
    {
        this.communitylikes = community;
        this.user = user;
    }


}
