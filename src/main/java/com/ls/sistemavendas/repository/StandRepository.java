package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.StandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("standRepository")
public interface StandRepository extends JpaRepository<StandEntity, UUID> {

}
