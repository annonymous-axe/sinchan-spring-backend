package com.sinchan.servicesImpls;

import com.sinchan.entities.ItemManufacturerDetails;
import com.sinchan.services.ManufacturersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sinchan.entities.Items;
import com.sinchan.services.ItemService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemServiceImpl implements ItemService{

    private final JdbcTemplate jdbcTemplate;

    public ItemServiceImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void save(Items item, int userId) {

        int id = getNextSeq(userId);

        try {

            String sql = "  insert into items(id, user_id, category_id, name, unit, gst_rate) "
                        + " values(?, ?, ?, ?, ?, ?) ";

            jdbcTemplate.update(sql, id, userId, item.getCategoryId(), item.getItemName(), item.getMeasurementType(),
                                item.getGstRate());

            saveItemManufacturerDetails(item, id, userId);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }


    @Override
    @Transactional
    public void update(Items item, int userId) {

        try {

            String sql = " update items set category_id = ?, name = ?, unit = ?, gst_rate = ? "
                    + " where id = "+item.getId()+" and user_id = "+userId;

            jdbcTemplate.update(sql, item.getCategoryId(), item.getItemName(), item.getMeasurementType(),
                    item.getGstRate());

            saveItemManufacturerDetails(item, item.getId(), userId);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }

    @Override
    public List<Items> listItems(int userId) {

        List<Items> itemsList = null;

        try {

            String sql = "  select distinct item.id, item.category_id, item.name, item.unit, cat.name as category_name from items item "
                    + " left join categories cat on cat.id = item.category_id "
                    + " where item.user_id = '"+userId+"'";

            itemsList = jdbcTemplate.query(sql, new RowMapper<Items>() {
                @Override
                public Items mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Items savedItems = new Items();
                    savedItems.setId(rs.getInt("id"));
                    savedItems.setItemName(rs.getString("name"));
                    savedItems.setCategoryId(rs.getInt("category_id"));
                    savedItems.setCategoryName(rs.getString("category_name"));
                    savedItems.setMeasurementType(rs.getString("unit"));

                    return savedItems;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return itemsList;
    }

    @Override
    public Items findById(int id, int userId) {

        Items item = null;

        try {

            String sql = "  select * from items "
                    + " where id = '"+id+"'"
                    + " and user_id = '"+userId+"'";

            item = jdbcTemplate.query(sql, new ResultSetExtractor<Items>() {
                @Override
                public Items extractData(ResultSet rs) throws SQLException {

                    Items savedItems = new Items();

                    if(rs.next()) {

                        savedItems.setId(id);
                        savedItems.setItemName(rs.getString("name"));
                        savedItems.setCategoryId(rs.getInt("category_id"));
                        savedItems.setGstRate(rs.getInt("gst_rate"));
                        savedItems.setMeasurementType(rs.getString("unit"));

                        savedItems.setItemManufacturerDetailsList(listItemManufacturerDetails(id, userId));
                    }
                    return savedItems;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return item;

    }

    @Override
    @Transactional
    public void delete(int id, int userId) {

        try {

            String sql = " delete from items where id = "+id+" and user_id =  " + userId ;

            jdbcTemplate.update(sql);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }

    @Transactional
    public void saveItemManufacturerDetails(Items items, int itemId, int userId) {

        try {
        	
        	clearItemManufacturerDetails(itemId, userId);

            String sql = "  insert into item_manufacturer_details(item_id, user_id, manufacturer_id, rate, cml_number, quontity) "
                    + " values(?, ?, ?, ?, ?, ?) ";

            List<Object[]> objArryList = new ArrayList<>();

            for(ItemManufacturerDetails itemManufacturerDetails  : items.getItemManufacturerDetailsList()){
            	if(itemManufacturerDetails.getManufacturerId() > 0 && itemManufacturerDetails.getQuantity() > 0) {
                    objArryList.add(new Object[]{
                            itemId, userId, itemManufacturerDetails.getManufacturerId(),
                            itemManufacturerDetails.getRate(), itemManufacturerDetails.getCmlNumber(),
                            itemManufacturerDetails.getQuantity()
                    });
                }
            }

            jdbcTemplate.batchUpdate(sql, objArryList);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }
    
    @Transactional
    public void clearItemManufacturerDetails(int itemId, int userId) {

        try {

            String sql = "delete from item_manufacturer_details "
            			+ " where item_id = "+itemId+" and user_id = "+userId;

            jdbcTemplate.update(sql);

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

    }    

//    @Override
    public List<ItemManufacturerDetails> listItemManufacturerDetails(int itemId, int userId) {

        List<ItemManufacturerDetails> listItemManufacturerDetails = null;

        try {

            String sql = "  select * from item_manufacturer_details "
                    + " where item_id = "+itemId+" and user_id = "+userId;

            listItemManufacturerDetails = jdbcTemplate.query(sql, new RowMapper<ItemManufacturerDetails>() {
                @Override
                public ItemManufacturerDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

                    ItemManufacturerDetails savedItemsManufacturerDetails = new ItemManufacturerDetails();
                    savedItemsManufacturerDetails.setId(rs.getInt("id"));
                    savedItemsManufacturerDetails.setItemId(rs.getInt("item_id"));
                    savedItemsManufacturerDetails.setManufacturerId(rs.getInt("manufacturer_id"));
                    savedItemsManufacturerDetails.setRate(rs.getFloat("rate"));
                    savedItemsManufacturerDetails.setQuantity(rs.getInt("quontity"));
                    savedItemsManufacturerDetails.setCmlNumber(rs.getString("cml_number"));

                    return savedItemsManufacturerDetails;
                }
            });

        }catch(Exception e) {
            throw new RuntimeException("Exception : "+e);
        }

        return listItemManufacturerDetails;
    }

    private Integer getNextSeq(int userId) {

        Integer seq = 0;

        try {

            String sql = "  select id seq from items "
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