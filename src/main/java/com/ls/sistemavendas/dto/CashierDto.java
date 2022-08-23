package com.ls.sistemavendas.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CashierDto {

    @EqualsAndHashCode.Include
    private ParticipantSummaryDto participantSummary;

    private List<PurchasedProductsDto> purchasedProducts;

    private double accountTotal;

}
