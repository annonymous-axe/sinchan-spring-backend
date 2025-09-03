package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sinchan.entities.Invoice;
import com.sinchan.entities.InvoiceItems;
import com.sinchan.entities.ItemManufacturerDetails;
import com.sinchan.entities.Items;
import com.sinchan.services.InvoiceService;
import com.sinchan.services.ItemService;

@Repository("invoice")
public class InvoiceServiceImpl implements InvoiceService {
	
	private final JdbcTemplate jdbcTemplate;
	private final ItemService itemService;
	
	public InvoiceServiceImpl(JdbcTemplate jdbcTemplate, ItemService itemService) {
		this.jdbcTemplate = jdbcTemplate;
		this.itemService = itemService;
	}

	@Override
	@Transactional
	public void save(Invoice invoice, int userId) {

        int id = getNextSeq(userId);
        
		float total = 0f;
		
		for(InvoiceItems invoiceItem : invoice.getInvoiceItemList()) {
			total += invoiceItem.getTotal();
		}
		
		invoice.setGrandTotal(total);

        updateItem(invoice, userId);

        try {

            String sql = " insert into invoice(id, user_id, farmer, email, phone, "
            		+ " address, manufacturer_id, grand_total, district_id, sanch, tehsil_id, "
            		+ " aadhar_id, farmer_id) "
                    + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            jdbcTemplate.update(sql, id, userId, invoice.getFarmer(), invoice.getEmail(),
            					invoice.getContactNo(), invoice.getAddress(),
            					invoice.getManufacturerId(), invoice.getGrandTotal(),
            					invoice.getDistrict(), invoice.getSanch(), invoice.getTehsil(),
            					invoice.getAadharId(), invoice.getFarmerId());

            saveInvoiceItemDetails(invoice, id, userId);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

	}
	
	@Override
	@Transactional
	public void update(Invoice invoice, int userId) {
        
		float total = 0f;
		
		for(InvoiceItems invoiceItem : invoice.getInvoiceItemList()) {
			total += invoiceItem.getTotal();
		}
		
		invoice.setGrandTotal(total);        

        try {

            String sql = "update table invoice set farmer = ?, email = ?, phone = ?, "
            		+ " address = ?, manufacturer_id = ?, grand_total = ?, district_id = ?,"
            		+ " sanch = ?, tehsil_id = ?, aadhar_id = ?, farmer_id = ?) "
            		+ " where invoice_id = "+invoice.getId()+" and user_id = "+userId;

            jdbcTemplate.update(sql, invoice.getFarmer(), invoice.getEmail(),
            		invoice.getContactNo(), invoice.getAddress(), invoice.getManufacturerId(),
            		invoice.getGrandTotal(), invoice.getDistrict(), invoice.getSanch(), 
            		invoice.getTehsil(), invoice.getAadharId(), invoice.getFarmerId());

            saveInvoiceItemDetails(invoice, invoice.getId(), userId);

//    		if(!invoice.getType()) {
//    			updateItem(invoice);
//    		}

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

	}

    @Override
    public List<Invoice> listInvoiceOfFarmers(int farmerId, int userId) {
        List<Invoice> listInvoice = null;

        try {

            String sql = " select inv.id, inv.farmer, inv.phone, inv.grand_total, farmer.farmer_name_en as farmer_name from invoice inv "
                    + " left join farmers farmer on farmer.id = inv.farmer "
                    + " where inv.user_id = '"+userId+"'";

            listInvoice = jdbcTemplate.query(sql, new RowMapper<Invoice>() {
                @Override
                public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Invoice savedInvoice = new Invoice();
                    savedInvoice.setId(rs.getInt("id"));
                    savedInvoice.setFarmer(rs.getInt("farmer"));
                    savedInvoice.setFarmerNameEn(rs.getString("farmer_name"));
                    savedInvoice.setContactNo(rs.getString("phone"));
                    savedInvoice.setGrandTotal(rs.getFloat("grand_total"));

                    return savedInvoice;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return listInvoice;
    }


    @Override
	@Transactional
	public void delete(int id, int userId) {

        try {

            String sql = " delete from invoice where id = "+id+" and user_id =  " + userId ;

            jdbcTemplate.update(sql);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

	}	

	@Override
	public Invoice findById(int id, int userId) {

        Invoice invoice = null;

        try {

            String sql = "select inv.*, farmer.farmer_name_en as farmer_name_en, farmer.farmer_name_mh as farmer_name_mh, "
                    + " dist.district_name_en, tehsil.tehsil_name_en, man.name_en as manufacturer_name_en "
                    + " from invoice inv  "
                    + " left join districts dist on dist.district_id = inv.district_id "
                    + " left join tehsils tehsil on tehsil.tehsil_id = inv.tehsil_id "
                    + " left join farmers farmer on farmer.id = inv.farmer and farmer.user_id = inv.user_id "
                    + " left join manufacturers man on man.id = inv.manufacturer_id and man.user_id = inv.user_id"
                    + " where inv.id = '"+id+"'"
                    + " and inv.user_id = '"+userId+"'";

            invoice = jdbcTemplate.query(sql, new ResultSetExtractor<Invoice>() {
                @Override
                public Invoice extractData(ResultSet rs) throws SQLException {

                	Invoice savedInvoice = new Invoice();

                    if(rs.next()) {

                    	savedInvoice.setId(id);
                    	savedInvoice.setFarmer(rs.getInt("farmer"));
                        savedInvoice.setFarmerNameEn(rs.getString("farmer_name_en"));
                        savedInvoice.setFarmerNameMh(rs.getString("farmer_name_mh"));
                    	savedInvoice.setEmail(rs.getString("email"));
                    	savedInvoice.setContactNo(rs.getString("phone"));
                    	savedInvoice.setAddress(rs.getString("address"));
                    	savedInvoice.setManufacturerId(rs.getInt("manufacturer_id"));
                        savedInvoice.setManufacturerName(rs.getString("manufacturer_name_en"));
                    	savedInvoice.setGrandTotal(rs.getFloat("grand_total"));
                    	savedInvoice.setDistrict(rs.getInt("district_id"));
                    	savedInvoice.setSanch(rs.getString("sanch"));
                    	savedInvoice.setTehsil(rs.getInt("tehsil_id"));
                    	savedInvoice.setAadharId(rs.getString("aadhar_id"));
                    	savedInvoice.setFarmerId(rs.getString("farmer_id"));                    	
                    	savedInvoice.setCreatedAt(rs.getDate("created_at"));
                        savedInvoice.setDistrictName(rs.getString("district_name_en"));
                        savedInvoice.setTehsilName(rs.getString("tehsil_name_en"));

                    	savedInvoice.setInvoiceItemList(listInvoiceItemDetails(savedInvoice.getId(), userId));
                    }
                    return savedInvoice ;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return invoice;
        
	}

	@Override
	public List<Invoice> listInvoice(int userId) {

        List<Invoice> listInvoice = null;

        try {

            String sql = " select inv.id, inv.farmer, inv.phone, inv.grand_total, farmer.farmer_name_en as farmer_name_en, "
                    + " farmer.farmer_name_mh as farmer_name_mh from invoice inv "
                    + " left join farmers farmer on farmer.id = inv.farmer "
                    + " where inv.user_id = '"+userId+"'";

            listInvoice = jdbcTemplate.query(sql, new RowMapper<Invoice>() {
                @Override
                public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {

                	Invoice savedInvoice = new Invoice();
                	savedInvoice.setId(rs.getInt("id"));
                	savedInvoice.setFarmer(rs.getInt("farmer"));
                    savedInvoice.setFarmerNameEn(rs.getString("farmer_name_en"));
                    savedInvoice.setFarmerNameMh(rs.getString("farmer_name_mh"));
                	savedInvoice.setContactNo(rs.getString("phone"));
                	savedInvoice.setGrandTotal(rs.getFloat("grand_total"));

                    return savedInvoice;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return listInvoice;
	}

	public void updateItem(Invoice invoice, int userId) {
    	for(InvoiceItems invoiceItem : invoice.getInvoiceItemList()) {
    		
    		Items item = itemService.findById(invoiceItem.getItemId(), userId);

    		for(ItemManufacturerDetails imd : item.getItemManufacturerDetailsList()) {

    			if(invoice.getManufacturerId() == imd.getManufacturerId()
                    && invoiceItem.getQuantity() <= imd.getQuantity() // recheck quantity entered are valid
                    ) {

	    			int updateQuantity = imd.getQuantity() - invoiceItem.getQuantity();

	    			imd.setQuantity(updateQuantity);

    			}
    		}

    		itemService.update(item, userId);

    	}    	
    }
	
    public void saveInvoiceItemDetails(Invoice invoice, int invoiceId, int userId) {

        try {
        	
        	clearInvoiceItemDetails(invoiceId, userId);

            String sql = "insert into invoice_items(invoice_id, user_id, item_id, category_id, "
            			+ "	quantity, unit, rate, cml_number, total) "
            			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            List<Object[]> objArryList = new ArrayList<>();

            for(InvoiceItems invoiceItems : invoice.getInvoiceItemList()){

                if(invoiceItems.getItemId() > 0 && invoiceItems.getCategoryId() > 0) {

                    objArryList.add(new Object[]{
                            invoiceId, userId, invoiceItems.getItemId(), invoiceItems.getCategoryId(),
                            invoiceItems.getQuantity(), invoiceItems.getUnit(), 
                            invoiceItems.getRate(), invoiceItems.getCmlNumber(),
                            invoiceItems.getTotal()
                    });
                }
            }

            jdbcTemplate.batchUpdate(sql, objArryList);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }

    @Transactional
    public void clearInvoiceItemDetails(int invoiceId, int userId) {

        try {

            String sql = "delete from invoice_items "
            			+ " where invoice_id = "+invoiceId+" and user_id = "+userId;

            jdbcTemplate.update(sql);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }     

    //    @Override
    public List<InvoiceItems> listInvoiceItemDetails(int invoiceId, int userId) {

        List<InvoiceItems> listInvoiceItemDetails = null;

        try {

            String sql = " select distinct inv_item.*, item.name_en as item_name_en, item.name_mh as item_name_mr, "
                    + " cat.name_en as category_name_en, cat.name_mh as category_name_mr from invoice_items inv_item  "
                    + " left join items item on item.id = inv_item.item_id "
                    + " left join categories cat on cat.id = inv_item.category_id "
                    + " where inv_item.invoice_id = "+invoiceId+" and inv_item.user_id = "+userId;

            listInvoiceItemDetails = jdbcTemplate.query(sql, new RowMapper<InvoiceItems>() {
                @Override
                public InvoiceItems mapRow(ResultSet rs, int rowNum) throws SQLException {

                	InvoiceItems savedInvoiceItems = new InvoiceItems();
                	savedInvoiceItems.setId(rs.getInt("id"));
                	savedInvoiceItems.setInvoiceId(invoiceId);
                	savedInvoiceItems.setItemId(rs.getInt("item_id"));
                	savedInvoiceItems.setCategoryId(rs.getInt("category_id"));
                	savedInvoiceItems.setUnit(rs.getString("unit"));
                	savedInvoiceItems.setRate(rs.getFloat("rate"));
                	savedInvoiceItems.setQuantity(rs.getInt("quantity"));
                	savedInvoiceItems.setCmlNumber(rs.getString("cml_number"));
                	savedInvoiceItems.setTotal(rs.getFloat("total"));
                    savedInvoiceItems.setItemNameEn(rs.getString("item_name_en"));
                    savedInvoiceItems.setItemNameMr(rs.getString("item_name_mr"));
                    savedInvoiceItems.setCategoryNameEn(rs.getString("category_name_en"));
                    savedInvoiceItems.setCategoryNameMr(rs.getString("category_name_mr"));

                    return savedInvoiceItems;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return listInvoiceItemDetails;
    }


    private Integer getNextSeq(int userId) {

        Integer seq = 0;

        try {

            String sql = "  select id seq from invoice "
                    +" where user_id = '"+userId+"' "
                    +" order by id desc limit 1";

            seq = jdbcTemplate.query(sql, new ResultSetExtractor<Integer>() {
                @Override
                public Integer extractData(ResultSet rs) throws SQLException {

                    int seq = 0;

                    if(rs.next()) {

                        seq = rs.getInt("seq");

                    }
                    return seq;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        seq += 1;

        return seq;

    }	

}