package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        batchInsert(user.getRoles(), user.getId());
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
            setRoles(user, id, jdbcTemplate);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
//        return DataAccessUtils.singleResult(users);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            setRoles(user, user.getId(), jdbcTemplate);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        Map<Integer, List<Role>> usersRoles =
                jdbcTemplate.query("SELECT * FROM user_roles", new ResultSetExtractor<>() {
                    @Override
                    public Map<Integer, List<Role>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        Map<Integer, List<Role>> result = new HashMap<>();
                        while (rs.next()) {
                            Integer id = rs.getInt("user_id");
                            if (!result.containsKey(id)) {
                                result.put(id, new ArrayList<Role>());
                            }
                            List<Role> roles = result.get(id);
                            roles.add(Role.valueOf(rs.getString(2)));
                            result.replace(id, roles);
                        }
                        return result;
                    }
                });
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        for (User user :
                users) {
            user.setRoles(usersRoles.get(user.getId()));
        }
        return users;
    }

    private void setRoles(User user, int id, JdbcTemplate jdbcTemplate) {
        user.setRoles(jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", Role.class, id));
    }

    private int[] batchInsert(Set<Role> roleSet, int userId) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", userId);
        List<Role> roles = new ArrayList<>(roleSet);

        return this.jdbcTemplate.batchUpdate(
                "insert into user_roles (user_id, role) values(?,?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, userId);
                        ps.setString(2, String.valueOf(roles.get(i)));
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }

                });
    }
}
