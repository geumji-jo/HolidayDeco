package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private int orderDetailNo;
    private String itemOrderNo;
    private int userNo;
    private int itemNo;
    private int quantity;
    private String itemTitle;
    private String itemPrice;
    private String itemMainImg;
}
