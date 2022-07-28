package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.StandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandRepository extends JpaRepository<StandEntity, Long> {

}
