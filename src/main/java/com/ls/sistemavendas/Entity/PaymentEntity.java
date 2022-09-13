package com.ls.sistemavendas.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="TB_PAYMENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "participant_id", nullable = false)
    private String participantCode;

    @Column(nullable = false)
    private double totalPayment;

    private String comment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Set<PaymentItemEntity> paymentItems;

}
