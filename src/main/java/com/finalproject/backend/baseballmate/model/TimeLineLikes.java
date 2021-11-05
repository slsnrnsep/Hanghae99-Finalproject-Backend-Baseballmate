package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TimeLineLikes {

    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "timeline_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TimeLine timeLine;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    public TimeLineLikes(TimeLine timeLine,User user)
    {
        this.timeLine = timeLine;
        this.user = user;
    }

}
