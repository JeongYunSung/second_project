package com.yunseong.second_project.product.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Referee extends BaseUserEntity {

    private String username;
    private String nickname;

    public Referee(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
