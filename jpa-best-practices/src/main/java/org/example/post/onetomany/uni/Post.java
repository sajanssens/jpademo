package org.example.post.onetomany.uni;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@NamedQueries(value = {
        @NamedQuery(name = "Post.findWithDetailsFetched", query = "SELECT p FROM Post p JOIN FETCH p.comments WHERE p.id=:id"),
        @NamedQuery(name = "Post.findAllWithDetailsFetched", query = "SELECT p FROM Post p JOIN FETCH p.comments"), // creates duplicates, don't use this!
        @NamedQuery(name = "Post.findAllDistinctWithDetailsFetched", query = "SELECT DISTINCT(p) FROM Post p JOIN FETCH p.comments")
})
public class Post {

    @Id @GeneratedValue
    private long id;

    private String title;

    @OneToMany(cascade = ALL, fetch = LAZY, orphanRemoval = true)
    // @JoinColumn(name = "postId") // possible improvement
    @Builder.Default // Builder should take default value (new ArrayList), otherwise builder sets comments to null (recommended in Uni, mandatory in BiDi)
    private List<PostComment> comments = new ArrayList<>();

    // Convenience method
    public void addComment(PostComment pc) {
        this.comments.add(pc);
    }

}
