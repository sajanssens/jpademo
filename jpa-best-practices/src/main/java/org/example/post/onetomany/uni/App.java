package org.example.post.onetomany.uni;

import org.example.AppInit;
import org.hibernate.LazyInitializationException;

import java.util.List;

import static org.example.Config.*;

public class App extends AppInit {

    // See https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/

    // OneToMany.uni is evil (see below), so better not use this.

    private void start() {
        Post post = Post.builder().title("first").build();

        post.addComment(PostComment.builder().title("My first review").build());
        post.addComment(PostComment.builder().title("My second review").build());
        post.addComment(PostComment.builder().title("My third review").build());

        persist(em, post);

        // This results in:
        // - three tables
        // - seven INSERTs: WHAT?!?!

        // Possible improvements:
        // 1) Use a @JoinColumn on Post.comments to prevent additional table.
        // 2) Make relationship bidi (see onetomany.bidi) to prevent additional table and to reduce number of queries to four INSERTs.
        // 3) Use ManyToOne uni (see manytoone.uni) to prevent additional table and to reduce number of queries to four INSERTs, and then use a simple query to get all comments from post.

        // What happens when we query this model? What SQL statements will be generated?
        findWithComments(post);
    }

    private void findWithComments(Post managedPost) {
        // Get all postcomments is easy in this solution, since collection is already available,
        // but this only works when post is managed, since comments are lazily loaded:
        Post find = find(em, managedPost.getId(), Post.class); // results in SELECT from post
        find.getComments().forEach(System.out::println);//        results in SELECT from comment
        // So this results in TWO SELECTs :-(

        // When post is detached, like in Spring/JEE, then an exception occurs:
        try {
            Post detachedPost = find(em, managedPost.getId(), Post.class);
            em.detach(detachedPost);
            detachedPost.getComments().forEach(System.out::println);
        } catch (LazyInitializationException e) {
            System.out.println(e.getMessage());
        }

        // We can fetch the comments of a detached post with a JPQL query instead:
        em.detach(managedPost);
        Post findWithDetails = findWithDetailsFetched(em, managedPost.getId(), Post.class);
        // And this results in one SELECT (with JOINs) only!
        findWithDetails.getComments().forEach(System.out::println);

        // -------------------------------------------------------------
        // But what happens when we find all posts with details fetched?
        List<Post> allWithDetailsFetched = findAllWithDetailsFetched(em, Post.class);
        allWithDetailsFetched.forEach(System.out::println);
        // Although there is only one post, we get three,.. what?!

        // This is caused by the join on comment. We have to filter out duplicates:
        List<Post> allDistinctWithDetailsFetched = findAllDistinctWithDetailsFetched(em, Post.class);
        allDistinctWithDetailsFetched.forEach(System.out::println);

    }

    public App() { super(oneToManyUni); }

    public static void main(String[] args) { new App().start(); }

}
