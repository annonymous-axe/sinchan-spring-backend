package com.sinchan.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemManufacturerDetails {

    private int id;

    private int manufacturerId;

    private String cmlNumber;

    private float rate;
    
    private int quantity;

    private int itemId;

}
