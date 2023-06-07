package com.nhn.ep.etc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Car {
    private String name;
    private String color;
    private int value;
}

public class Owner {
    public static void main(String[] args) {
        Car myCar = new Car();
        myCar.setName("소나타");
        myCar.setColor("black");
        myCar.setValue(30000000);

        System.out.println("제 차의 이름은 " + myCar.getName());
        System.out.println("구매 가격은 " + myCar.getValue() + "입니다.");
    }
}