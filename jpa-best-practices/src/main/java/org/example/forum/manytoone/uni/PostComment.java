package org.example.forum.manytoone.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.ConstraintMode.CONSTRAINT;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PostComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "")
    private long id;

    private String title;

    @ManyToOne(cascade = PERSIST)
    // Optionally add a cascading delete to the table definition (only when creating new table, not on exisiting table):
    @JoinColumn(name = "postId", foreignKey = @ForeignKey(value = CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (postId) REFERENCES parent(id) ON DELETE CASCADE"))
    private Post post;
}
