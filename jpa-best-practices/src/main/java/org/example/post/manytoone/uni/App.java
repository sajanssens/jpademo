package org.example.post.manytoone.uni;

import org.example.AppInit;

import java.util.List;

import static org.example.Config.manyToOneUni;
import static org.example.Config.persist;

public class App extends AppInit {

    // Preferred way.

    public void start() {
        Post post = Post.builder().title("first").build();

        PostComment my_first_review = PostComment.builder().title("My first review").post(post).build();
        PostComment my_second_review = PostComment.builder().title("My second review").post(post).build();
        PostComment my_third_review = PostComment.builder().title("My third review").post(post).build();

        persist(em, my_first_review); // two inserts: postcomment 1 and post (cascade)
        persist(em, my_second_review);// one insert:  postcomment 2
        persist(em, my_third_review); // one insert:  postcomment 3
        //                            -> four inserts in total :-)

        // Since it's a uni relation, we don't already have the postcomments in a post, but
        // getting these is easy; we can write a simple query for that:
        List<PostComment> comments = em.createQuery(
                "select pc " +
                        "from PostComment pc " +
                        "where pc.post.id = :postId", PostComment.class)
                .setParameter("postId", post.getId())
                .getResultList();

        comments.forEach(System.out::println);
    }

    public App() { super(manyToOneUni); }

    public static void main(String[] args) { new App().start(); }

}
