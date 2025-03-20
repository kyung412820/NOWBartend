package com.nowbartend.domain.customer.collection.entity;

import com.nowbartend.domain.common.BaseEntity;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.customer.collection.dto.request.CollectionUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "collections",
        indexes = {
                @Index(name = "idx_fk_user_id", columnList = "user_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Collection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Collection(
            Long id,
            String name,
            String description,
            boolean isPublic,
            User user
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.user = user;
    }

    public void updateCollectionInfo(CollectionUpdateRequest collectionUpdateRequest) {
        if (collectionUpdateRequest.name() != null) {
            this.name = collectionUpdateRequest.name();
        }

        if (collectionUpdateRequest.description() != null) {
            this.description = collectionUpdateRequest.description();
        }

        if (collectionUpdateRequest.isPublic() != null) {
            this.isPublic = collectionUpdateRequest.isPublic();
        }
    }
}
