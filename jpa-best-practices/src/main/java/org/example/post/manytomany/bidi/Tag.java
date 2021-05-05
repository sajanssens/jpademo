package org.example.post.manytomany.bidi;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@ToString(exclude = "posts") @EqualsAndHashCode(exclude = "posts") // to prevent cycles with toString and HashSet
public class Tag {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags") // mappedBy not needed because we already have JoinTable on Post.tags
    @Builder.Default
    private Set<Post> posts = new HashSet<>();
}
