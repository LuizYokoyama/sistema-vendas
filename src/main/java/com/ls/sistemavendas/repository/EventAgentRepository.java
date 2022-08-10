package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.EventAgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventAgentRepository extends JpaRepository<EventAgentEntity, UUID> {

}
