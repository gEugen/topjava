package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.UserUtils.users;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> repository;

    private final AtomicInteger counter = new AtomicInteger();

    public InMemoryUserRepository() {
        this.repository = new ConcurrentHashMap<>();
        users.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            log.info("add {}", user);
            repository.put(user.getId(), user);
            return user;
        }
        // handle case: update, but not present in storage
        log.info("update {}", user);
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return sortByName(repository.values());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny()
                .orElse(null);
    }

    private List<User> sortByName(Collection<User> users) {
        Comparator<User> nameComparator = Comparator.comparing(User::getName);
        Comparator<User> mailComparator = Comparator.comparing(User::getEmail);
        return users.stream()
                .sorted(nameComparator.thenComparing(mailComparator))
                .collect(Collectors.toList());
    }
}
