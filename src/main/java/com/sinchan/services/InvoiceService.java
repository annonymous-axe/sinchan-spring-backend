package com.sinchan.services;

import java.util.List;

import com.sinchan.entities.Invoice;

public interface InvoiceService {

	void save(Invoice invoice, int userId);
	
	Invoice findById(int id, int userId);
	
	List<Invoice> listInvoice(int userId);
	
	void delete(int id, int userId);
	
	void update(Invoice invoice, int userId);

    List<Invoice> listInvoiceOfFarmers(int farmerId, int userId);
}