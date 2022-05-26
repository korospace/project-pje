package com.pje.kelompok4.repositoriy;

import com.pje.kelompok4.model.Information;
import com.pje.kelompok4.payload.response.InformationRes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InformationRepo extends JpaRepository<Information,Long> {
    Information findInformationById(Long id);
    Information findInformationByName(String name);
    Boolean existsById(String id);
    Boolean existsByName(String name);

    @Query(nativeQuery = true,value = "select i.id as id,i.name as name from information as i")
    Iterable<InformationRes> getAllInformations();

    @Query(nativeQuery = true,value = "select * from information where name = ?1 and id != ?2")
    Information validateName(String name,Long id);
}
