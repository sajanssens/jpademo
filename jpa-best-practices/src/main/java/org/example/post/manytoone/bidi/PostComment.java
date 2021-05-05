package org.example.post.manytoone.bidi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PostComment {

    @Id @GeneratedValue
    private long id;

    private String title;

    @ManyToOne
    private Post post;

    public PostComment(String title) {
        this.title = title;
    }
}
