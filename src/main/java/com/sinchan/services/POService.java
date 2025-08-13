package com.sinchan.services;

import java.util.List;

import com.sinchan.entities.PurchaseOrder;

public interface POService {

    void save(PurchaseOrder purchaseOrder, int userId);

    void update(PurchaseOrder purchaseOrder, int userId);

    PurchaseOrder findById(int id, int userId);

    List<PurchaseOrder> listPurchaseOrders(int userId);

    void delete(int id, int userId);

}
