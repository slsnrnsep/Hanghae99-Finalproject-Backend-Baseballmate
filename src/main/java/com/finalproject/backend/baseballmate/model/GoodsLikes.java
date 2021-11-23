package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class GoodsLikes {

    @Id
    @GeneratedValue
    private Long id;

    @JsonBackReference
    @JoinColumn(name = "goods_id")
    @ManyToOne
    private Goods goods;

    @JsonBackReference
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column
    private Long userIdGoods;

    public GoodsLikes(Goods goods, User user, Long userIdGoods){
        this.goods = goods;
        this.user = user;
        this.userIdGoods = userIdGoods;
    }
}
