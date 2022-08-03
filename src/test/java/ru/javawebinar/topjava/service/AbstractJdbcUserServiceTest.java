package ru.javawebinar.topjava.service;

import org.junit.Before;

public abstract class AbstractJdbcUserServiceTest extends AbstractUserServiceTest {

    @Before
    public void setup() {
        cacheManager.getCache("users").clear();
    }
}