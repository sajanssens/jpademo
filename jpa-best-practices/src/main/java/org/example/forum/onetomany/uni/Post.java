package org.example.forum.onetomany.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.NONE;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@NamedQueries(value = {
        @NamedQuery(name = "Post.findWithDetailsFetched", query = "SELECT p FROM Post p JOIN FETCH p.comments WHERE p.id=:id"),
        @NamedQuery(name = "Post.findAllWithDetailsFetched", query = "SELECT p FROM Post p JOIN FETCH p.comments"), // could create duplicates, don't use this!
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
