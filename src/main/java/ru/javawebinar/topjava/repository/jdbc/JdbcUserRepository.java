package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.ValidationUtil.beanValidate;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        beanValidate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        boolean isNew = user.isNew();
        if (isNew) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        batchInsert(user, isNew);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            setRoles(user);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            setRoles(user);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        Map<Integer, List<Role>> usersRoles =
                jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
                    Map<Integer, List<Role>> result = new HashMap<>();
                    while (rs.next()) {
                        Integer id = rs.getInt("user_id");
                        List<Role> roles = result.computeIfAbsent(id, k -> new ArrayList<>());
                        roles.add(Role.valueOf(rs.getString("role")));
                    }
                    return result;
                });
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        for (User user : users) {
            user.setRoles(usersRoles.get(user.getId()));
        }
        return users;
    }

    private void setRoles(User user) {
        user.setRoles(jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", Role.class, user.getId()));
    }

    private void batchInsert(User user, boolean isNew) {
        if (!isNew) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<Role> roles = new ArrayList<>(user.getRoles());
            jdbcTemplate.batchUpdate(
                    "INSERT INTO user_roles (user_id, role) VALUES(?,?)", new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, user.getId());
                            ps.setString(2, roles.get(i).name());
                        }

                        public int getBatchSize() {
                            return roles.size();
                        }
                    });
        }
    }
}
