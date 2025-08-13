package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.sinchan.entities.Manufacturers;
import lombok.extern.slf4j.Slf4j;
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
	public List<Category> listCategory(int userId) {

		List<Category> categoryListList = null;

		try {

			String sql = "  select id, name from categories "
						+ " where user_id = '"+userId+"'";

			categoryListList = jdbcTemplate.query(sql, new RowMapper<Category>() {
				@Override
				public Category mapRow(ResultSet rs, int rowNum) throws SQLException {

					Category savedCategories = new Category();
					savedCategories.setId(rs.getInt("id"));
					savedCategories.setName(rs.getString("name"));

					return savedCategories;
				}
			});

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

		return categoryListList;
		
	}



}
