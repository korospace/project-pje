package com.pje.kelompok4.repositoriy;

import com.pje.kelompok4.model.ERole;
import com.pje.kelompok4.model.Role;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, String> {
  Role findByName(ERole name);
}
