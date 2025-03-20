package com.nowbartend.domain.common.user.entity;

import com.nowbartend.domain.common.BaseEntity;
import com.nowbartend.domain.customer.coupon.entity.UserCoupon;
import com.nowbartend.domain.customer.post.region.entity.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_email", columnList = "email")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(length = 35)
    private String introduce;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCoupons = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Builder
    public User(
            Long id,
            String email,
            String password,
            String name,
            String nickname,
            String phoneNumber,
            UserRole role
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public void updateProfile(String nickname, String introduce) {
        updateNickname(nickname);
        updateIntroduce(introduce);
    }

    public void updateInfo(String name, String phoneNumber) {
        updateName(name);
        updatePhoneNumber(phoneNumber);
    }

    public void updateRegion(Region region) {
        this.region = region;
    }

    public void resetPassword(String password) {
        this.password = password;
    }

    private void updateNickname(String nickname) {
        if (nickname != null) {
            this.name = nickname;
        }
    }

    private void updateIntroduce(String introduce) {
        if (introduce != null) {
            this.introduce = introduce;
        }
    }

    private void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    private void updatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
    }
}
