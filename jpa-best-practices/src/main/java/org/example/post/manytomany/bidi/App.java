package org.example.post.manytomany.bidi;

import org.example.AppInit;

import static org.example.Config.*;

public class App extends AppInit {

    // See https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/

    private void start() {
        Tag java = Tag.builder().name("Java").build();
        Tag hib = Tag.builder().name("Hibernate").build();

        Post post1 = Post.builder().title("JPA with Hibernate").build();
        post1.addTag(java);
        post1.addTag(hib);

        Post post2 = Post.builder().title("Core Hibernate").build();
        post2.addTag(hib);

        persist(em, post1);
        persist(em, post2);

        Post post = find(em, post1.getId(), Post.class);
        post.removeTag(hib);
        merge(em, post);
    }

    public App() { super(manyToManyBidi); }

    public static void main(String[] args) { new App().start(); }

}
