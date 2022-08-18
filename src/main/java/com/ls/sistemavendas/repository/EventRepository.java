package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.dto.HomeScreenEventDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository("eventRepository")
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    @Query("SELECT new com.ls.sistemavendas.dto.HomeScreenEventDto(id, name, photo, description) FROM EventEntity ")
    public List<HomeScreenEventDto> getAllEventsShortList();

    @Query("SELECT e.name FROM EventEntity e WHERE e.name = ?1")
    public String getEventByName(String name);

    @Query(value = "with vd(init_date, end_date) as" +
            "       (" +
            "           values (cast(?1 as timestamptz)," +
            "           (cast(?1 as timestamptz) + cast(concat(?2, ':00') as time) ) )" +
            "       ) " +
            "SELECT CASE WHEN EXISTS" +
            "   (select e.first_occurrence_date_time, e.first_occurrence_date_time +  cast(concat(e.duration, ':00') as Time) from tb_event e" +
            "       inner join vd on" +
            "       (e.first_occurrence_date_time  between vd.init_date and vd.end_date) or " +
            "       (e.first_occurrence_date_time +  cast(concat(e.duration, ':00') as Time) between vd.init_date and vd.end_date)" +
            "   )" +
            "THEN CAST(1 AS Boolean)" +
            "ELSE CAST(0 AS boolean) END",
            nativeQuery = true)
    public boolean existsByPeriod(LocalDateTime dateTime, int duration);

    public Boolean existsByName(String name);

}
