package org.example.forum.enumeration;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    // single valued enum
    @Enumerated // (EnumType.STRING) // EnumType is optional: string is not the most efficient way.
    // @Convert(converter = TagConverter.class) // OR use a converter INSTEAD of @Enumerated
    private Tag rootTag;

    // collection valued enum
    @ElementCollection @Singular
    // Optionally, add:
    // @CollectionTable(name = "PostTag")       // choose the table name yourself
    // @Convert(converter = TagConverter.class) // use a converter TOGETHER WITH @ElementCollection
    private Set<Tag> tags;

    // onetomany to an entity (instead of enum) with hard coded database values
    @OneToMany @Singular
    // Optionally, add:
    // @JoinTable(name = "PostCategory")
    private Set<Category> categories = new HashSet<>();
}

