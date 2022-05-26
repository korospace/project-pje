package com.pje.kelompok4.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import com.pje.kelompok4.dao.UserDao;
import com.pje.kelompok4.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_CHECK_USERNAME = "SELECT username FROM users WHERE username = ? AND id != ?";
    private static final String SQL_CHECK_NIK = "SELECT nik FROM users WHERE nik = ? AND id != ?";

    @Override
    public Boolean usernameExist(String username, String uniqueId) {
        User user = null;
        try { 
            user = (User) jdbcTemplate.queryForObject(SQL_CHECK_USERNAME, new Object []{username,uniqueId}, new RowMapper<User>(){
                public User mapRow(ResultSet rs, int rowNum)throws SQLException{
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    return user;
                }
            });

            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean nikExist(String nik, String uniqueId) {
        User user = null;
        try { 
            user = (User) jdbcTemplate.queryForObject(SQL_CHECK_NIK, new Object []{nik,uniqueId}, new RowMapper<User>(){
                public User mapRow(ResultSet rs, int rowNum)throws SQLException{
                    User user = new User();
                    user.setNik(rs.getString("nik"));
                    return user;
                }
            });

            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
}
