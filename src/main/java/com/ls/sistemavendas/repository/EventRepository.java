package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.dto.HomeScreenEventDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query("SELECT new com.ls.sistemavendas.dto.HomeScreenEventDto(id, name, photo, description) FROM EventEntity ")
    public List<HomeScreenEventDto> getAllEventsShortList();

}
