package edu.telegrambot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Xolair {

    String drugForm;
    String adress;
    String price;

    public boolean isSolution(){
        return getDrugForm().equals("ксолар раствор 150мг/мл шприц 1мл №1");
    }

    @Override
    public String toString() {
        return "{drugForm='" + drugForm + '\'' +
                ", adress='" + adress + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
