package ru.javaops.topjava.web.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.repository.VoteRepository;
import ru.javaops.topjava.util.UserUtil;


@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected VoteRepository voteRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        log.info("delete {}", id);
        voteRepository.deleteByUserId(id);
        userRepository.deleteExisted(id);
    }

    protected User prepareAndSave(User user) {
        return userRepository.save(UserUtil.prepareToSave(user));
    }
}