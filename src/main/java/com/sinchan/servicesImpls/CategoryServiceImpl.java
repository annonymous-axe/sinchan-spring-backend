package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sinchan.entities.Category;
import com.sinchan.services.CategoryService;


@Repository
public class CategoryServiceImpl implements CategoryService{

	private final JdbcTemplate jdbcTemplate;
	
	public CategoryServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Category> listCategory(int userId, String locale) {

		List<Category> categoryListList = null;
		String sql = "";

		try {

			sql = "  select id, name_en, name_mh from categories "
					+ " where user_id = '" + userId + "'";


			categoryListList = jdbcTemplate.query(sql, new RowMapper<Category>() {
				@Override
				public Category mapRow(ResultSet rs, int rowNum) throws SQLException {

					Category savedCategories = new Category();
					savedCategories.setId(rs.getInt("id"));
					savedCategories.setNameEn(rs.getString("name_en"));
					savedCategories.setNameMh(rs.getString("name_mh"));

					return savedCategories;
				}
			});

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

		return categoryListList;
		
	}



}
