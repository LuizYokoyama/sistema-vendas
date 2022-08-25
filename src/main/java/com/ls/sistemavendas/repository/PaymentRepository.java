package com.ls.sistemavendas.repository;

import com.ls.sistemavendas.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("paymentRepository")
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {

    PaymentEntity findByParticipantCode(String participantCode);

}
