package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.StandAgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandAgentRepository extends JpaRepository<StandAgentEntity, String> {

}
