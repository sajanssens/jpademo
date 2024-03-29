package org.example.forum.onetoone.uni;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Post { // parent side

    @Id @GeneratedValue
    private long id;

    private String title;

}
