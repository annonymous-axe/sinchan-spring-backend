package com.sinchan.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Items {

    private int id;

    private int categoryId;

    private String categoryNameEn;

    private String categoryNameMh;

    private String itemNameEn;

    private String itemNameMh;

    private String measurementType;

    private float gstRate;

    private List<ItemManufacturerDetails> itemManufacturerDetailsList = new ArrayList<>();

}