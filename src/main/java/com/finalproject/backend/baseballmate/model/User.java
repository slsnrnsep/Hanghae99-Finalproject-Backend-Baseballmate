package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "user_id")
    private Long id;

    private String userid;

    private String username;

    private String password;

//    @OneToMany(mappedBy = "user")
//    private List<Group> groupList = new ArrayList<Group>();

    public User(String userid, String username, String password){
        this.userid = userid;
        this.username = username;
        this.password = password;
    }



}
