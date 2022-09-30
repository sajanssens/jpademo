package org.example.forum.onetomany.bidi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Post {

    @Id @GeneratedValue
    private long id;

    private String title;

    @OneToMany(mappedBy = "post", cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @Builder.Default // Builder should take default value (new ArrayList), otherwise builder sets comments to null (recommended in Uni, mandatory in BiDi)
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

