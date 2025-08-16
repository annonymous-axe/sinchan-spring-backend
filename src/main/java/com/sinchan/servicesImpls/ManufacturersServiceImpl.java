package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinchan.entities.Manufacturers;
import com.sinchan.services.ManufacturersService;


@Repository
public class ManufacturersServiceImpl implements ManufacturersService {
	
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public ManufacturersServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Manufacturers> listManufacturers(int userId) {

		List<Manufacturers> manufacturersList = null;

		try {

			String sql = "  select id, name_en, name_mh from manufacturers "
					+ " where user_id = :userId";

			HashMap<String, Object> params = new HashMap<>();
			params.put("userId", userId);

			manufacturersList = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Manufacturers.class));

//			manufacturersList = jdbcTemplate.query(sql, new RowMapper<Manufacturers>() {
//				@Override
//				public Manufacturers mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//					Manufacturers savedManufacturers = new Manufacturers();
//					savedManufacturers.setId(rs.getInt("id"));
//					savedManufacturers.setNameEn(rs.getString("name_en"));
//					savedManufacturers.setNameMh(rs.getString("name_mh"));
//
//					return savedManufacturers;
//				}
//			});

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

		return manufacturersList;

	}

	@Override
	public void save(Manufacturers manufacturer, int userId) {

		try {

			String sql = "insert into manufacturers (user_id, name_en, name_mh) "
					+ " values (:userId, :name_ne, :name_mh) ";

			HashMap<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			params.put("name_ne", manufacturer.getNameEn());
			params.put("name_mh", manufacturer.getNameMh());

			namedParameterJdbcTemplate.update(sql, params);

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public void update(Manufacturers manufacturer, int userId) {

		try {

			String sql = "update manufacturers set name_en = :manufacturer_en_name, name_mh = :manufacturer_mh_name "
					+ " where id = :man_id and user_id = :user_id ";

			HashMap<String, Object> params = new HashMap<>();
			params.put("manufacturer_en_name", manufacturer.getNameEn());
			params.put("manufacturer_mh_name", manufacturer.getNameMh());
			params.put("man_id", manufacturer.getId());
			params.put("user_id", userId);

			namedParameterJdbcTemplate.update(sql, params);

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}
	
	@Override
	public void delete(int manufacturerId, int userId) {

		try {

			String sql = "delete from manufacturers "
					+ " where id = :man_id and user_id = :userId";

			HashMap<String, Object> params = new HashMap<>();
			params.put("man_id", manufacturerId);
			params.put("userId", userId);


			namedParameterJdbcTemplate.update(sql, params);

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public Manufacturers findById(int manufacturerId, int userId) {
		Manufacturers result = null;

		try {

			String sql = "select id, name_en, name_mh from manufacturers "
					+ " where id = :manufacturerId and user_id = :userId";

			HashMap<String, Object> params = new HashMap<>();
			params.put("manufacturerId", manufacturerId);
			params.put("userId", userId);

			result = namedParameterJdbcTemplate.queryForObject(sql, params, new SimplePropertyRowMapper<>(Manufacturers.class));

//			result = jdbcTemplate.query(sql, new ResultSetExtractor<Manufacturers>() {
//				@Override
//				public Manufacturers extractData(ResultSet rs) throws SQLException {
//
//					Manufacturers savedManufacturer = new Manufacturers();
//
//					if(rs.next()) {
//						savedManufacturer.setId(rs.getInt("id"));
//						savedManufacturer.setNameEn(rs.getString("name_en"));
//						savedManufacturer.setNameMh(rs.getString("name_mh"));
//					}
//
//					return savedManufacturer;
//				}
//			});


		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

		return result;
	}

	@Override
	public boolean exist(String manufacturerName, int userId) {

		Integer counts = 0;

		try {

			String sql = "select count(name_en) names from manufacturers "
					+ " where name_en = :manufacturerName and user_id = :userId";

			HashMap<String, Object> params = new HashMap<>();
			params.put("manufacturerName", manufacturerName);
			params.put("userId", userId);

			counts = namedParameterJdbcTemplate.queryForObject(sql, params, new SimplePropertyRowMapper<>(Integer.class));


//			counts = jdbcTemplate.query(sql, new ResultSetExtractor<Integer>() {
//				@Override
//				public Integer extractData(ResultSet rs) throws SQLException {
//
//					int counts = 0;
//
//					if(rs.next()) {
//						counts = rs.getInt("names");
//					}
//
//					return counts;
//				}
//			});
			

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
		return counts != null;
	}	

}
