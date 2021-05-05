package org.example.post.enumeration;

import org.example.AppInit;

import java.util.List;

import static org.example.Config.enumeration;
import static org.example.Config.persist;

public class App extends AppInit {

    public void start() {
        // Using standard Java enums:
        Post post = Post.builder().title("first")
                .rootTag(Tag.NEWS)
                .tag(Tag.JAVA).tag(Tag.CLOUD)
                .build();

        persist(em, post);

        // Using hard coded database values:
        List<Category> categories = populateCategories();

        Post post2 = Post.builder().title("second")
                .category(find(categories, "IT"))
                .category(find(categories, "Lifestyle"))
                .build();

        persist(em, post2);
    }

    private List<Category> populateCategories() {
        Category it = Category.builder().name("IT").build();
        Category lifestyle = Category.builder().name("Lifestyle").build();
        Category food = Category.builder().name("Food").build();

        persist(em, it);
        persist(em, lifestyle);
        persist(em, food);

        return List.of(it, lifestyle, food);
    }

    public Category find(List<Category> categories, String name) {
        return categories.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst().orElseThrow();
    }

    public App() { super(enumeration); }

    public static void main(String[] args) { new App().start(); }

}
