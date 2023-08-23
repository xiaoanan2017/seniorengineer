package com.example.kafkamodule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author xiaoaa
 * @date 2023/6/28 21:56
 **/
public class TestDemo {

    public static void main(String[] args) {
        List<Entity> list = new ArrayList<>();
        Entity entity = new Entity();
        entity.setOrderId("T1000");
        entity.setDistance(300D );

        Entity entity1 = new Entity();
        entity1.setOrderId("T1001");
        entity1.setDistance(300D);

        Entity entity2 = new Entity();
        entity2.setOrderId("T1002");
        entity2.setDistance(200D);

        list.add(entity);
        list.add(entity1);
        list.add(entity2);

        Entity result = list.stream()
                .sorted(Comparator.comparing(Entity::getDistance, Comparator.nullsLast(Double::compare))
                        .thenComparing(Comparator.comparing(Entity::getOrderId, Comparator.nullsLast(String::compareTo))))
                .findFirst()
                .orElse(null);


        System.out.println(result);



    }
}
