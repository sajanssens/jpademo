package org.example.post.manytoone.bidi;

import org.example.AppInit;

import static org.example.Config.*;

public class App extends AppInit {

    // Same as onetomany.bidi
    private void start() {
        Post post = Post.builder().title("first").build();

        post.addComment(new PostComment("My first review"));
        post.addComment(new PostComment("My second review"));
        post.addComment(new PostComment("My third review"));

        persist(em, post); // four inserts: post and postcomment 1, 2, 3 (cascade) :-)

        // Same logic for update/remove:
        post.removeComment(post.getComments().get(0));
        merge(em, post);
        // This results in:
        // - only one delete on postcomment (because orphanRemoval=true)
    }

    public App() { super(manyToOneBidi); }

    public static void main(String[] args) { new App().start(); }
}
