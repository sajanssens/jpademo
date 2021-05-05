package org.example.post.onetoone.bidi;

import org.example.AppInit;

import static java.time.LocalDateTime.now;
import static org.example.Config.*;

public class App extends AppInit {

    // See https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/

    // The point here is that bidi cannot load lazily.
    // Use alternatives.

    private void start() {
        PostDetails details = PostDetails.builder().createdBy("Me").createdOn(now()).build();
        Post post = Post.builder().title("first").build();
        post.setDetails(details);
        persist(em, post);

        // results in two SELECTs, even though details is configured LAZY :-(
        find(em, post.getId(), Post.class);

        // Why? Hibernate fetches the child entity as well, so, instead of only one query,
        // Hibernate requires two select statements, like if it's EAGER fetching,
        // because the only way to find the associated post_details primary key is
        // to execute a secondary query:

        // Improvements: use uni with @MapsId, see uni.App

    }

    public App() { super(oneToOneBidi); }

    public static void main(String[] args) { new App().start(); }

}
