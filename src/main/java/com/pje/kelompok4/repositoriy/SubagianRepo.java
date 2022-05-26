package com.pje.kelompok4.repositoriy;

import com.pje.kelompok4.model.Subagian;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubagianRepo extends CrudRepository<Subagian,Long> {
    Subagian findSubagianById(Long id);
    Boolean existsById(String id);
    Boolean existsByName(String name);

    @Query(nativeQuery = true,value = "select * from subagian where name = ?1 and id != ?2")
    Subagian validateName(String name,Long id);
}
