package org.example.forum.manytoone.uni;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PostComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "")
    private long id;

    private String title;

    @ManyToOne(cascade = PERSIST)
    private Post post;

}
