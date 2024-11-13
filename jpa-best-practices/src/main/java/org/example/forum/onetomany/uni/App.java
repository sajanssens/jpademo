package org.example.forum.onetomany.uni;

import lombok.extern.slf4j.Slf4j;
import org.example.AppInit;

import java.util.List;

import static org.example.Config.find;
import static org.example.Config.findAllDistinctWithDetailsFetched;
import static org.example.Config.findAllWithDetailsFetched;
import static org.example.Config.findComments;
import static org.example.Config.findWithDetailsFetched;
import static org.example.Config.oneToManyUni;
import static org.example.Config.persist;

@Slf4j
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

    private void findWithComments(Post post) {
        // Get all postcomments is easy in this solution, since collection is already available,
        // but this only works when post is managed, since comments are lazily loaded:
        Post managedPost = find(em, post.getId(), Post.class); // results in SELECT from post and SELECT from Post_PostComment
        // This resulted in TWO select queries :-(
        managedPost.getComments().forEach(e -> log.info(e.toString()));//        results in SELECT from comment

        // When post is detached, like in Spring/JEE, then an exception occurs:
        try {
            Post p = find(em, post.getId(), Post.class);
            em.detach(p);
            p.getComments().forEach(e -> log.info(e.toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // We can fetch the comments of a detached post with a JPQL query instead:
        em.detach(post);
        Post findWithDetails = findWithDetailsFetched(em, post.getId(), Post.class);
        // And this results in one SELECT (with JOINs) only!
        findWithDetails.getComments().forEach(e -> log.info(e.toString()));

        List<PostComment> comments = findComments(em, post.getId(), PostComment.class);
        // This also results in one SELECT (with JOINs) only.
        comments.forEach(e -> log.info(e.toString()));

        // -------------------------------------------------------------
        // But what happens when we find all posts with details fetched?
        List<Post> allWithDetailsFetched = findAllWithDetailsFetched(em, Post.class);
        allWithDetailsFetched.forEach(e -> log.info(e.toString()));
        // Although there is only one post, we get three... what?!
        // NOTE: This depends on the persistence provider:
        //       The behavior of JOIN FETCH in JPA has been updated, particularly in Hibernate.
        //       Hibernate introduced the hibernate.query.passDistinctThrough hint.

        // This is caused by the join on comment. We have to filter out duplicates:
        List<Post> allDistinctWithDetailsFetched = findAllDistinctWithDetailsFetched(em, Post.class);
        allDistinctWithDetailsFetched.forEach(e -> log.info(e.toString()));
    }

    public App() { super(oneToManyUni); }

    public static void main(String[] args) { new App().start(); }
}
