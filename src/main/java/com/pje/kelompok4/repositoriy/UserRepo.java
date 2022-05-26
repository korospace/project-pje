package com.pje.kelompok4.repositoriy;

import com.pje.kelompok4.model.User;
import com.pje.kelompok4.payload.response.UserRes;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepo extends CrudRepository<User,Long> {
    public User findById(String id);
    public User findByUsername(String username);
    Boolean existsById(String id);
    Boolean existsByUsername(String username);
    Boolean existsByNik(String nik);

    @Query(nativeQuery = true,value = "select * from users where nik = ?1 and id != ?2")
    User validateNik(String nik,String id);

    @Query(nativeQuery = true,value = "select * from users where username = ?1 and id != ?2")
    User validateUsername(String username,String id);

    @Query(nativeQuery = true,value = "select i.id as id,i.username as username from users as i where i.id != ?1")
    Iterable<UserRes> getAllUsersExcept(String uniqueid);

    @Query(nativeQuery = true,value = "select * from users where id = ?1 and id != ?2")
    User findByIdExcept(String id,String uniqueid);

    @Query(nativeQuery = true,value = "select * from users where id = ?1 and id != ?2")
    User existsByIdExcept(String id,String uniqueid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from users where id = ?1")
    void deleteUserById(String id);
}
