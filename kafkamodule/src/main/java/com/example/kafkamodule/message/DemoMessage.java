package com.example.kafkamodule.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoaa
 * @date 2023/6/17 11:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemoMessage {

    public static final String TOPIC = "DEMO_01";

    private Integer id;

}
