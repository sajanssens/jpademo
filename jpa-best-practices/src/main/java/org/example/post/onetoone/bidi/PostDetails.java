package org.example.post.onetoone.bidi;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor @ToString(exclude = "post")
public class PostDetails { // child side
    @Id // @GeneratedValue? no, since we use @MapsId and the postId is FK and PK
    private Long id;

    private LocalDateTime createdOn;

    private String createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // The id property serves as both Primary Key and Foreign Key. More logical in the database.
    private Post post;

}
