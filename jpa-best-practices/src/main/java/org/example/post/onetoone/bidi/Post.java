package org.example.post.onetoone.bidi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Post { // parent side

    @Id @GeneratedValue
    private long id;

    private String title;

    @OneToOne(cascade = ALL, fetch = LAZY, optional = false, // Despite of all these properties, this association behaves like a FetchType.EAGER relationship, see App.java
            mappedBy = "post")
    private PostDetails details;

    public void setDetails(PostDetails details) {
        this.details = details;
        details.setPost(this);
    }
}
