package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCancelDTO {
  private String imp_uid;
  private String reason;
  private int checksum;
}