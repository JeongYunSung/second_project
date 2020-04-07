package com.yunseong.second_project.member.command.domain;

import com.yunseong.second_project.common.domain.BaseEntity;
import com.yunseong.second_project.common.errors.NotMatchPasswordException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "member_id"))
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "username", "nickname" }))
public class Member extends BaseEntity {

    private String username;

    private String password;

    private String nickname;

    private Integer money;

    @Enumerated(value = EnumType.STRING)
    private Grade grade;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberPurchase> purchases = new ArrayList<>();

    public Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.grade = Grade.BRONZE;
    }

    public void changeGrade(Grade grade) {
        this.grade = grade;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.getPassword());
    }

    public void verifyPassword(String password, PasswordEncoder encoder) {
        if (this.getPassword().equals(encoder.encode(password))) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.", password);
        }
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addMoney(Integer money) {
        this.money += money;
    }

    public void subMoney(Integer money) {
        this.money -= money;
    }

    public void addPurchase(MemberPurchase purchase) {
        this.getPurchases().add(purchase);
        purchase.setMember(this);
    }

    public void update(String password, String nickname, Integer money) {
        this.changePassword(password);
        this.changeNickname(nickname);
        this.addMoney(money);
    }

    @Override
    public void delete() {
        super.delete();
        this.getPurchases().forEach(MemberPurchase::delete);
    }
}
