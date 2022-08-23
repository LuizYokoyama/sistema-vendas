package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.dto.ParticipantSummaryDto;
import com.ls.sistemavendas.dto.PurchasedProductsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, String> {

    @Query("SELECT new com.ls.sistemavendas.dto.PurchasedProductsDto" +
            "(p.description, p.price, i.quantity, i.dateTime)" +
            "FROM TransactionItemEntity i " +
            "INNER JOIN ProductEntity p ON " +
            "i.product = p.id " +
            "GROUP BY i.id, p.id " +
            "HAVING i.participantCode = :participantCode")
    public List<PurchasedProductsDto> getPurchasedProducts(@Param("participantCode") String participantCode);

    @Query("SELECT new com.ls.sistemavendas.dto.ParticipantSummaryDto(p.code, p.name, p.entryDateTime)" +
            "FROM ParticipantEntity p " +
            "WHERE p.code = :participantCode")
    public ParticipantSummaryDto getParticipantSummaryById(@Param("participantCode") String participantCode);

}
