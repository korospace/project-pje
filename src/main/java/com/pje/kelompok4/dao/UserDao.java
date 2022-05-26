package com.pje.kelompok4.dao;

public interface UserDao {
    public Boolean usernameExist(String username,String uniqueId);

    public Boolean nikExist(String nik,String uniqueId);
}
