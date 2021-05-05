package org.example.post.onetoone.uni;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PostDetails { // child side
    @Id // @GeneratedValue? no, since we use @MapsId and the postId is FK and PK
    private Long id;

    private LocalDateTime createdOn;

    private String createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // The id property serves as both Primary Key and Foreign Key.
    //         This way, you don’t need a bidirectional association since you can
    //         always fetch the PostDetails entity by using the Post entity identifier.
    private Post post;
}
