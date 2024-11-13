package org.example.forum.onetomany.uni;

import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@NamedQuery(name = "PostComment.findComments", query = "SELECT comments FROM Post p JOIN p.comments comments  WHERE p.id=:id")
public class PostComment {

    @Id @GeneratedValue
    private long id;

    private String title;
}
