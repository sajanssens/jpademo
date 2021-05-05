package org.example.post.enumeration;

import lombok.*;

import javax.persistence.*;
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
    @ElementCollection
    // @Convert(converter = TagConverter.class) // AND use a converter TOGETHER WITH @ElementCollection
    @Singular
    private Set<Tag> tags;

    // onetomany to an entity (instead of enum) with hard coded database values
    @OneToMany
    @Singular
    private Set<Category> categories = new HashSet<>();

}

