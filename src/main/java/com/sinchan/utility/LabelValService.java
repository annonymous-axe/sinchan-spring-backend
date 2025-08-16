package com.sinchan.utility;

import com.sinchan.entities.Dictionary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sinchan.entities.Items;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class LabelValService {

    private final JdbcTemplate jdbcTemplate;

    public LabelValService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Dictionary> getItemLabelValList(int userId){

        log.info("Entering listItems.");

        List<Dictionary> dictionaryList = null;
        log.info("email : "+userId);

        try {

            String sql = " select id, name from items "
                    + " where user_id = '"+userId+"'";
            log.info("sql :: "+sql);

            dictionaryList = jdbcTemplate.query(sql, new RowMapper<Dictionary>() {
                @Override
                public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Dictionary saveDictionary = new Dictionary();
                    saveDictionary.setIntKey(rs.getInt("id"));
                    saveDictionary.setStringValue(rs.getString("name"));

                    return saveDictionary;
                }
            });

        }catch(Exception e) {
            log.info("Exception :: "+e);
            log.info("Exception Cause :: "+e.getCause());
        }

        log.info("Exiting listItems");

        return dictionaryList;

    }

    public List<Dictionary> getManufacturerLabelValList(int userId){

        log.info("Entering listManufacturers.");

        List<Dictionary> dictionaryList = null;
        log.info("email : "+userId);

        try {

            String sql = "  select id, name from manufacturers "
                    + " where user_id = '"+userId+"'";
            log.info("sql :: "+sql);

            dictionaryList = jdbcTemplate.query(sql, new RowMapper<Dictionary>() {
                @Override
                public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Dictionary saveDictionary = new Dictionary();
                    saveDictionary.setIntKey(rs.getInt("id"));
                    saveDictionary.setStringValue(rs.getString("name"));

                    return saveDictionary;
                }
            });

        }catch(Exception e) {
            log.info("Exception :: "+e);
            log.info("Exception Cause :: "+e.getCause());
        }

        log.info("Exiting listManufacturers");

        return dictionaryList;

    }
    
    public List<Dictionary> getCategoryLabelValList(int userId){

        log.info("Entering getCategoryLabelValList.");

        List<Dictionary> dictionaryList = null;
        log.info("userId : "+userId);

        try {

            String sql = "  select id, name from categories "
                    + " where user_id = '"+userId+"'";
            log.info("sql :: "+sql);

            dictionaryList = jdbcTemplate.query(sql, new RowMapper<Dictionary>() {
                @Override
                public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Dictionary savedDictionary = new Dictionary();
                    savedDictionary.setIntKey(rs.getInt("id"));
                    savedDictionary.setStringValue(rs.getString("name"));

                    return savedDictionary;
                }
            });

        }catch(Exception e) {
            log.info("Exception :: "+e);
            log.info("Exception Cause :: "+e.getCause());
        }

        log.info("Exiting categoryListList");

        return dictionaryList;

    }    
    
    public List<Dictionary> getCategoryLabelValListFromItemName(String itemName, int userId){

        log.info("Entering getCategoryLabelValList.");

        List<Dictionary> dictionaryList = null;
        log.info("userId : "+userId);

        try {

            String sql = "  select id, name from categories cat "
                    + " left join items item on cat.id = item.category_id "
                    + " where item.name = '"+itemName+"' and user_id = '"+userId+"'";
            log.info("sql :: "+sql);

            dictionaryList = jdbcTemplate.query(sql, new RowMapper<Dictionary>() {
                @Override
                public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Dictionary savedDictionary = new Dictionary();
                    savedDictionary.setIntKey(rs.getInt("id"));
                    savedDictionary.setStringValue(rs.getString("name"));

                    return savedDictionary;
                }
            });

        }catch(Exception e) {
            log.info("Exception :: "+e);
            log.info("Exception Cause :: "+e.getCause());
        }

        log.info("Exiting categoryListList");

        return dictionaryList;

    }
    
    public Dictionary getItemDetails(int itemId, int manufacturerId, int userId){

        log.info("Entering getAvlQuantityFromItemNameAndManufacturer.");

        Dictionary dictionary = null;

        try {

            String sql = "select imd.quantity, imd.cml_number, item.unit, imd.rate"
                    + " from item_manufacturer_details imd "
                    + " left join items item on item.id = imd.item_id "
                    + " where imd.item_id = "+itemId+" and imd.manufacturer_id = "+manufacturerId
                    + " and imd.user_id = "+userId;
            log.info("sql :: "+sql);

            dictionary = jdbcTemplate.query(sql, new ResultSetExtractor<Dictionary>() {
                @Override
                public Dictionary extractData(ResultSet rs) throws SQLException {

                    Dictionary savedDictionary = new Dictionary();

                    if(rs.next()) {
                        savedDictionary.setFloatKey(rs.getFloat("quantity"));
                        savedDictionary.setStringValue(rs.getString("cml_number"));
                        savedDictionary.setFloatKey2(rs.getFloat("rate"));
                        savedDictionary.setStringValue2(rs.getString("unit"));
                    }

                    return savedDictionary;
                }
            });

        }catch(Exception e) {
            log.info("Exception :: "+e);
            log.info("Exception Cause :: "+e.getCause());
        }

        log.info("Exiting categoryListList");

        return dictionary;

    }

	public List<Dictionary> getItemFromCategoryLabelValList(int categoryId, int userId) {
        log.info("Entering getItemFromCategoryLabelValList.");

        List<Dictionary> dictionaryList = null;
        log.info("userId : "+userId);

        try {

            String sql = "  select id, name from items where category_id = '"+categoryId+"' and user_id = '"+userId+"'";
            log.info("sql :: "+sql);

            dictionaryList = jdbcTemplate.query(sql, new RowMapper<Dictionary>() {
                @Override
                public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Dictionary savedDictionary = new Dictionary();
                    savedDictionary.setIntKey(rs.getInt("id"));
                    savedDictionary.setStringValue(rs.getString("name"));

                    return savedDictionary;
                }
            });

        }catch(Exception e) {
            log.info("Exception :: "+e);
            log.info("Exception Cause :: "+e.getCause());
        }

        log.info("Exiting getItemFromCategoryLabelValList");

        return dictionaryList;

	}
	
	public List<Dictionary> getItemFromCategoryLabelValItemList(int categoryId, int userId) {
        log.info("Entering getItemFromCategoryLabelValList.");

        List<Dictionary> dictionaryList = null;

        try {

            String sql = "  select id, name_en from items where category_id = '"+categoryId+"' and user_id = '"+userId+"'";
            log.info("sql :: "+sql);

            dictionaryList = jdbcTemplate.query(sql, new RowMapper<Dictionary>() {
                @Override
                public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Dictionary savedDictionary = new Dictionary();
                    savedDictionary.setIntKey(rs.getInt("id"));
                    savedDictionary.setStringValue(rs.getString("name_en"));

                    return savedDictionary;
                }
            });

        }catch(Exception e) {
            log.info("Exception :: "+e);
            log.info("Exception Cause :: "+e.getCause());
        }

        log.info("Entering getItemFromCategoryLabelValList.");

        return dictionaryList;
	}

//
//    public String getCategoryNameFromId(int categoryId) {
//        log.info("Entering getCategoryNameFromId.");
//
//        TypedQuery<String> query = entityManager.createQuery("SELECT cat.name FROM Category cat where cat.id=:categoryId", String.class);
//
//        query.setParameter("categoryId", categoryId);
//
//        String categoryName = query.getSingleResult();
//
//        log.info("categoryName : "+categoryName);
//
//        log.info("Entering getCategoryNameFromId.");
//
//        return categoryName;
//    }
//
//    public String getItemNameFromId(int itemId) {
//        log.info("Entering getItemNameFromId.");
//
//        TypedQuery<String> query = entityManager.createQuery("SELECT item.itemName FROM Items item where item.id=:itemId", String.class);
//
//        query.setParameter("itemId", itemId);
//
//        String itemName = query.getSingleResult();
//
//        log.info("itemName : "+itemName);
//
//        log.info("Entering getItemNameFromId.");
//
//        return itemName;
//    }
}