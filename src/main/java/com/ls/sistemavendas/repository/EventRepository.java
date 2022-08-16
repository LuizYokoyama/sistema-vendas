package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.dto.HomeScreenEventDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    @Query("SELECT new com.ls.sistemavendas.dto.HomeScreenEventDto(id, name, photo, description) FROM EventEntity ")
    public List<HomeScreenEventDto> getAllEventsShortList();

    @Query("SELECT e.name FROM EventEntity e WHERE e.name = ?1")
    public String getEventByName(String name);

    @Query("SELECT e.firstOccurrenceDateTime FROM EventEntity e WHERE e.firstOccurrenceDateTime = ?1")
    public LocalDateTime getEventByDate(LocalDateTime dateTime);

}
