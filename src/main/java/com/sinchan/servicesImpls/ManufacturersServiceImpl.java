package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.sinchan.entities.Items;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sinchan.entities.Manufacturers;
import com.sinchan.services.ManufacturersService;


@Repository
public class ManufacturersServiceImpl implements ManufacturersService {
	
	private final JdbcTemplate jdbcTemplate;
	
	public ManufacturersServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Manufacturers> listManufacturers(int userId) {

		List<Manufacturers> manufacturersList = null;

		try {

			String sql = "  select id, name from manufacturers "
					+ " where user_id = '"+userId+"'";

			manufacturersList = jdbcTemplate.query(sql, new RowMapper<Manufacturers>() {
				@Override
				public Manufacturers mapRow(ResultSet rs, int rowNum) throws SQLException {

					Manufacturers savedManufacturers = new Manufacturers();
					savedManufacturers.setId(rs.getInt("id"));
					savedManufacturers.setName(rs.getString("name"));

					return savedManufacturers;
				}
			});

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

		return manufacturersList;

	}

	@Override
	public void save(Manufacturers manufacturer, int userId) {

		try {

			String sql = "insert into manufacturers (user_id, name) "
					+ " values (? , ?) ";
			
			jdbcTemplate.update(sql, userId, manufacturer.getName());

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public void update(Manufacturers manufacturer, int userId) {

		try {

			String sql = "update manufacturers set name = ? "
					+ " where id = ? and user_id = ? ";

			jdbcTemplate.update(sql, manufacturer.getName(), manufacturer.getId(), userId );

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}
	
	@Override
	public void delete(int manufacturerId, int userId) {

		try {

			String sql = "delete from manufacturers "
					+ " where id = '"+manufacturerId+"' and user_id = "+userId;

			jdbcTemplate.update(sql);

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public Manufacturers findById(int manufacturerId, int userId) {
		Manufacturers result = null;

		try {

			String sql = "select id, name from manufacturers "
					+ " where id = "+manufacturerId+" and user_id = "+userId;

			result = jdbcTemplate.query(sql, new ResultSetExtractor<Manufacturers>() {
				@Override
				public Manufacturers extractData(ResultSet rs) throws SQLException {

					Manufacturers savedManufacturer = new Manufacturers();

					if(rs.next()) {
						savedManufacturer.setId(rs.getInt("id"));
						savedManufacturer.setName(rs.getString("name"));
					}

					return savedManufacturer;
				}
			});


		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

		return result;
	}

	@Override
	public boolean exist(String manufacturerName, int userId) {

		int counts = 0;

		try {

			String sql = "select count(name) names from manufacturers "
					+ " where name = '"+manufacturerName+"' and user_id = "+userId;

			counts = jdbcTemplate.query(sql, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException {
					
					int counts = 0;
					
					if(rs.next()) {
						counts = rs.getInt("names");
					}
					
					return counts;
				}
			});
			

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
		return counts == 0;
	}	

}
