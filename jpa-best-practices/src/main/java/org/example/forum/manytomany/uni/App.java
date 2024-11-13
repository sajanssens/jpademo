package org.example.forum.manytomany.uni;

import lombok.extern.slf4j.Slf4j;
import org.example.AppInit;

import java.util.List;

import static org.example.Config.find;
import static org.example.Config.manyToManyUni;
import static org.example.Config.merge;
import static org.example.Config.persist;

@Slf4j
public class App extends AppInit {

    // See https://thorben-janssen.com/ultimate-guide-association-mappings-jpa-hibernate/#Many-to-Many_Associations

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

        // Since it's a ..ToMany relation, the tags belonging to a post are
        // not eagerly fetched. Getting them is easy, we can write a query for that:
        List<Tag> postTags =
                em.createQuery("SELECT DISTINCT pt " +
                                        "FROM Post p " +
                                        "JOIN FETCH p.tags pt " +
                                        "WHERE p.id=:pId"
                                , Tag.class)
                        .setParameter("pId", post1.getId())
                        .getResultList();
        postTags.forEach(e -> log.info(e.toString()));

        // Since it's a uni relation, we only have the tags belonging to a post,
        // we don't have the posts belonging to a tag.
        // Getting them is easy, we can write a query for that:
        List<Post> posts =
                em.createQuery("SELECT p " +
                                        "FROM Post p " +
                                        "JOIN p.tags pt " +
                                        "WHERE pt.id=:tagId"
                                , Post.class)
                        .setParameter("tagId", java.getId())
                        .getResultList();
        posts.forEach(e -> log.info(e.toString()));
    }

    public App() { super(manyToManyUni); }

    public static void main(String[] args) { new App().start(); }
}
