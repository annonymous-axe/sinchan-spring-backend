package com.sinchan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Items {

    private int id;

    private int categoryId;

    private String categoryName;

    private String itemName;

    private String measurementType;

    private float gstRate;

    private List<ItemManufacturerDetails> itemManufacturerDetailsList = new ArrayList<>();

}