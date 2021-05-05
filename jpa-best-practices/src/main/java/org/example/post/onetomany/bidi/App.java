package org.example.post.onetomany.bidi;

import org.example.AppInit;

import static org.example.Config.*;

public class App extends AppInit {

    // See https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/

    // Same as manytoone.bidi
    private void start() {
        Post post = Post.builder().title("first").build();

        post.addComment(new PostComment("My first review"));
        post.addComment(new PostComment("My second review"));
        post.addComment(new PostComment("My third review"));

        persist(em, post);
        // This results in:
        // - just two tables
        // - just one INSERT for each persisted PostComment entity

        // Same logic for update/remove:
        post.removeComment(post.getComments().get(0));
        merge(em, post);
        // This results in:
        // - only one DELETE on postcomment (because orphanRemoval=true)
    }

    public App() { super(oneToManyBidi); }

    public static void main(String[] args) { new App().start(); }

}
