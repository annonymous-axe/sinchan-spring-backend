package com.sinchan.servicesImpls;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sinchan.entities.ItemManufacturerDetails;
import com.sinchan.entities.Items;
import com.sinchan.entities.PurchaseOrder;
import com.sinchan.entities.PurchaseOrderItems;
import com.sinchan.services.ItemService;
import com.sinchan.services.POService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class POServiceImpl implements POService{

    private final JdbcTemplate jdbcTemplate;
    private final ItemService itemService;

    public POServiceImpl(JdbcTemplate jdbcTemplate, ItemService itemService){
        this.jdbcTemplate = jdbcTemplate;
        this.itemService = itemService;
    }

    @Override
    public void save(PurchaseOrder purchaseOrder, int userId) {

        int id = getNextSeq(userId);

        updateItem(purchaseOrder, userId);

        try {

            String sql = "  insert into purchase_orders(id, user_id, po_number, supplier_name, purchase_date, bill_number) "
                    + " values(?, ?, ?, ?, ?, ?) ";

            jdbcTemplate.update(sql, id, userId, purchaseOrder.getPoNumber(), purchaseOrder.getSupplierNameEn(),
            					purchaseOrder.getPurchaseDate(),purchaseOrder.getBillNumber());

            savePurchaseOrderItemDetails(purchaseOrder, id, userId);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }

    @Override
    public void update(PurchaseOrder purchaseOrder, int userId) {

        try {

            String sql = "update table purchase_orders set po_number = ?, supplier_name = ?, "
            		+ " purchase_date = ?, bill_number = ? "
                    + " where id = "+purchaseOrder.getId()+"and user_id = "+userId;

            jdbcTemplate.update(sql, purchaseOrder.getId(), userId, purchaseOrder.getPoNumber(), purchaseOrder.getSupplierNameEn(), purchaseOrder.getPurchaseDate(),
                                purchaseOrder.getBillNumber());

            savePurchaseOrderItemDetails(purchaseOrder, purchaseOrder.getId(), userId);

            updateItem(purchaseOrder, userId);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }

    @Override
    public PurchaseOrder findById(int id, int userId) {

        PurchaseOrder purchaseOrder = null;

        try {

            String sql = "select * from purchase_orders "
                    + " where id = '"+id+"'"
                    + " and user_id = '"+userId+"'";

            purchaseOrder = jdbcTemplate.query(sql, new ResultSetExtractor<PurchaseOrder>() {
                @Override
                public PurchaseOrder extractData(ResultSet rs) throws SQLException {

                    PurchaseOrder savedPurchaseOrder = new PurchaseOrder();

                    if(rs.next()) {

                        savedPurchaseOrder.setId(id);
                        savedPurchaseOrder.setPoNumber(rs.getString("po_number"));
                        savedPurchaseOrder.setSupplierNameEn(rs.getString("supplier_name"));
                        savedPurchaseOrder.setPurchaseDate(rs.getDate("purchase_date"));
                        savedPurchaseOrder.setBillNumber(rs.getString("bill_number"));
                        savedPurchaseOrder.setCreatedAt(rs.getDate("created_at"));

                        savedPurchaseOrder.setPurchaseOrderItemsList(listPurchaseOrderItemDetails(savedPurchaseOrder.getId(), userId));
                    }
                    return savedPurchaseOrder;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return purchaseOrder;
    }

    @Override
    public List<PurchaseOrder> listPurchaseOrders(int userId) {

        List<PurchaseOrder> itemsList = null;

        try {

            String sql = " select id, po_number, supplier_name, purchase_date, bill_number from purchase_orders "
                    + " where user_id = '"+userId+"'";

            itemsList = jdbcTemplate.query(sql, new RowMapper<PurchaseOrder>() {
                @Override
                public PurchaseOrder mapRow(ResultSet rs, int rowNum) throws SQLException {

                    PurchaseOrder savedPurchaseOrder = new PurchaseOrder();
                    savedPurchaseOrder.setId(rs.getInt("id"));
                    savedPurchaseOrder.setPoNumber(rs.getString("po_number"));
                    savedPurchaseOrder.setSupplierNameEn(rs.getString("supplier_name"));
                    savedPurchaseOrder.setPurchaseDate(rs.getDate("purchase_date"));
                    savedPurchaseOrder.setBillNumber(rs.getString("bill_number"));

                    return savedPurchaseOrder;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return itemsList;
    }

    
    public void updateItem(PurchaseOrder purchaseOrder, int userId) {

    	for(PurchaseOrderItems poItem : purchaseOrder.getPurchaseOrderItemsList()) {

    		Items item = itemService.findById(poItem.getItemId(), userId);

    		List<ItemManufacturerDetails> newItemManufacturerDetailList = new ArrayList<>();
    		
    		boolean isManufacturedNotExists = true;
    		
    		for(ItemManufacturerDetails imd : item.getItemManufacturerDetailsList()) {

    			if(poItem.getManufacturerId() == imd.getManufacturerId()) {

	    			int updateQuantity = imd.getQuantity() + poItem.getQuantity();

	    			imd.setQuantity(updateQuantity);

	    			imd.setRate(poItem.getRate());

	    			imd.setCmlNumber(poItem.getCmlNumber());
	    			
	    			isManufacturedNotExists = false;

    			}
    		}
    		
    		if(isManufacturedNotExists) {
	    		ItemManufacturerDetails newItemManufacturerDetails = new ItemManufacturerDetails();
	    		newItemManufacturerDetails.setItemId(poItem.getItemId());
	    		newItemManufacturerDetails.setManufacturerId(poItem.getManufacturerId());
	    		newItemManufacturerDetails.setQuantity(poItem.getQuantity());
	    		newItemManufacturerDetails.setRate(poItem.getRate());
	    		newItemManufacturerDetails.setCmlNumber(poItem.getCmlNumber());
	    		
	    		item.getItemManufacturerDetailsList().add(newItemManufacturerDetails);    			
    		}
    		
    		item.getItemManufacturerDetailsList().addAll(newItemManufacturerDetailList);

    		itemService.update(item, userId);
    		
    	}

    }

    @Override
    public void delete(int id, int userId) {

        try {

            String sql = " delete from purchase_orders where id = "+id+" and user_id =  " + userId ;

            jdbcTemplate.update(sql);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }

    public void savePurchaseOrderItemDetails(PurchaseOrder purchaseOrder, int purchaseOrderId,
    										int userId) {

        try {
        	
        	clearPurchaseItemDetails(purchaseOrderId, userId);

            String sql = "insert into purchase_order_items(purchase_order_id, item_id, user_id, "
            			+ "	manufacturer_id, rate, category_id, cml_number, quantity, total_amount) "
            			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            List<Object[]> objArryList = new ArrayList<>();

            for(PurchaseOrderItems poItemDetails  : purchaseOrder.getPurchaseOrderItemsList()){
                if(poItemDetails.getItemId() > 0 && poItemDetails.getCategoryId() > 0) {
                    objArryList.add(new Object[]{
                            purchaseOrderId, poItemDetails.getItemId(), userId, poItemDetails.getManufacturerId(),
                            poItemDetails.getRate(), poItemDetails.getCategoryId(), poItemDetails.getCmlNumber(),
                            poItemDetails.getQuantity(), poItemDetails.getTotalAmount()
                    });
                }
            }

            jdbcTemplate.batchUpdate(sql, objArryList);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }
    
    @Transactional
    public void clearPurchaseItemDetails(int purchaseOrderId, int userId) {

        try {

            String sql = "delete from purchase_order_items "
            			+ " where purchase_order_id = "+purchaseOrderId+" and user_id = "+userId;

            jdbcTemplate.update(sql);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }        

    //    @Override
    public List<PurchaseOrderItems> listPurchaseOrderItemDetails(int purchaseOrderId, int userId) {

        List<PurchaseOrderItems> listPurchaseOrderItemDetails = null;

        try {

            String sql = "  select * from purchase_order_items "
                    + " where purchase_order_id = "+purchaseOrderId+" and user_id = "+userId;

            listPurchaseOrderItemDetails = jdbcTemplate.query(sql, new RowMapper<PurchaseOrderItems>() {
                @Override
                public PurchaseOrderItems mapRow(ResultSet rs, int rowNum) throws SQLException {

                    PurchaseOrderItems savedPoItem = new PurchaseOrderItems();
                    savedPoItem.setId(rs.getInt("id"));
                    savedPoItem.setPurchaseOrderId(purchaseOrderId);
                    savedPoItem.setItemId(rs.getInt("item_id"));
                    savedPoItem.setCategoryId(rs.getInt("category_id"));
                    savedPoItem.setManufacturerId(rs.getInt("manufacturer_id"));
                    savedPoItem.setRate(rs.getFloat("rate"));
                    savedPoItem.setQuantity(rs.getInt("quantity"));
                    savedPoItem.setCmlNumber(rs.getString("cml_number"));
                    savedPoItem.setTotalAmount(rs.getString("total_amount"));

                    return savedPoItem;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return listPurchaseOrderItemDetails;
    }


    private Integer getNextSeq(int userId) {

        Integer seq = 0;

        try {

            String sql = "  select count(id) seq from purchase_orders where user_id = '"+userId+"'";

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