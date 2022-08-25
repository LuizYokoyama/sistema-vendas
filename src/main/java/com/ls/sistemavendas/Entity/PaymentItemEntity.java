package com.ls.sistemavendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="TB_payment_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "type_id")
    @EqualsAndHashCode.Include
    private UUID id;

    private String type;

    private double value;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentEntity;

}
