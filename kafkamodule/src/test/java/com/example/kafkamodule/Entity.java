package com.example.kafkamodule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoaa
 * @date 2023/6/28 21:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity {

    String orderId;

    Double distance;
}
