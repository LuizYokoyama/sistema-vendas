package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, String> {

}
