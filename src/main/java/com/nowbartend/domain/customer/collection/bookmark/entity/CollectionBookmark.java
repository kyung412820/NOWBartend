package com.nowbartend.domain.customer.collection.bookmark.entity;

import com.nowbartend.domain.customer.bookmark.entity.Bookmark;
import com.nowbartend.domain.customer.collection.entity.Collection;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "collection_bookmarks",
        indexes = {
                @Index(name = "idx_fk_collection_id", columnList = "collection_id"),
                @Index(name = "idx_fk_bookmark_id", columnList = "bookmark_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionBookmark {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Bookmark bookmark;

    @Builder
    public CollectionBookmark(Collection collection, Bookmark bookmark) {
        this.collection = collection;
        this.bookmark = bookmark;
    }
}
