package com.yunseong.second_project.product.commend.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "referee_id"))
public class Referee extends BaseUserEntity {

    @Version
    private Integer version;

    private String username;
    private String nickname;

    public Referee(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
