package com.pje.kelompok4.repositoriy;

import com.pje.kelompok4.model.Bagian;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BagianRepo extends CrudRepository<Bagian,Long> {
    Bagian findBagianById(Long id);
    Boolean existsById(String id);
    Boolean existsByName(String name);

    @Query(nativeQuery = true,value = "select * from bagian where name = ?1 and id != ?2")
    Bagian validateName(String name,Long id);
}
