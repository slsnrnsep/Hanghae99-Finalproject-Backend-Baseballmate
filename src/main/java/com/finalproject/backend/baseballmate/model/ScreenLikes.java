package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ScreenLikes {
    @GeneratedValue
    @Id
    private Long screenLikeId;

    @JoinColumn(name = "screen_id")
    @ManyToOne
    private Screen screenlikes;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public ScreenLikes(Screen screen, User user){
        this.screenlikes = screen;
        this.user = user;
    }
}
