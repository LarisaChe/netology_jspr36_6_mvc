package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {

    private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    static AtomicLong nextId = new AtomicLong(1) ;

    public List<Post> all() {
        return posts.values().stream().collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        if (posts.containsKey(id)) {
            return Optional.ofNullable(posts.get(id));
        }
        else {
            return Optional.empty();
        }
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            while (posts.containsKey(nextId.longValue())) {
                nextId.incrementAndGet();
            }
            post.setId(nextId.longValue());
            nextId.incrementAndGet();
        }
        posts.put(post.getId(), post);
        return posts.get(post.getId());
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
