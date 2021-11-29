package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CommunityLikes {

    @Id
    @GeneratedValue
    private Long id;

    @JsonBackReference
    @JoinColumn(name = "community_id")
    @ManyToOne
    private Community communitylikes;

    @JsonBackReference
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public CommunityLikes(Community community, User user)
    {
        this.communitylikes = community;
        this.user = user;
    }


}
