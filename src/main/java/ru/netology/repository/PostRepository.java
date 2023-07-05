package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {

    private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long, LinkedList<String>> postsHistory = new ConcurrentHashMap<>();
    static AtomicLong nextId = new AtomicLong(1);

    public List<Post> all() {
        return posts.values().stream().collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        if (isExisted(id)) {
            //if (posts.containsKey(id)) {
            return Optional.ofNullable(posts.get(id));
        } else {
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
        } else {
            addHistory(posts.get(post.getId()));
        }
        posts.put(post.getId(), post);
        return posts.get(post.getId());
    }

    public void removeById(long id) {
        if (isExisted(id)) {
            addHistory(posts.get(id));
            posts.remove(id);
        }
    }

    private void addHistory(Post post) {
        if (post != null) { //postsHistory.size() == 0 &&
            LinkedList<String> history = new LinkedList<>();
            if (postsHistory.containsKey(post.getId())) {
                history = postsHistory.get(post.getId());
            }
            history.add(post.getContent());
            postsHistory.put(post.getId(), history);
            System.out.println(postsHistory.toString());
        }
    }

    private boolean isExisted(long id) {
        boolean result = false;
        if (!posts.containsKey(id)) {
            throw new NotFoundException("Не найден пост с id=" + id);
        } else {
            result = true;
        }
        return result;
    }
}
