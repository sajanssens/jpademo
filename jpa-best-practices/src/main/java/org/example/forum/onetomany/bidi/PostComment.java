package org.example.forum.onetomany.bidi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PostComment {

    @Id @GeneratedValue
    private long id;

    private String title;

    @ManyToOne(fetch = LAZY)
    private Post post;

    public PostComment(String title) {
        this.title = title;
    }
}
