package org.example.post.manytoone.bidi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Post {

    @Id @GeneratedValue
    private long id;

    private String title;

    @OneToMany(mappedBy = "post", cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @Builder.Default // Builder should take default value (new ArrayList), otherwise builder sets comments to null
    private List<PostComment> comments = new ArrayList<>();

    // Update both sides of the bidirectional relationship
    public void addComment(PostComment pc) {
        this.comments.add(pc);
        pc.setPost(this);
    }

    public void removeComment(PostComment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
    }

}

