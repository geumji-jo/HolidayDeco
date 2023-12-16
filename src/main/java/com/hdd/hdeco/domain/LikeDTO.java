package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private int userNo;
    private int itemNo;
    private int likeCheck;
}
