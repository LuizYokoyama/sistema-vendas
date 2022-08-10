package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.StandAgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StandAgentRepository extends JpaRepository<StandAgentEntity, UUID> {

}
