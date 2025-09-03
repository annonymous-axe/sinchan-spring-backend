package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sinchan.entities.District;
import com.sinchan.entities.Tehsil;
import com.sinchan.services.LocationServices;

@Repository
public class LocationServicesImpls implements LocationServices {
	
	private JdbcTemplate jdbcTemplate;
	
	public LocationServicesImpls(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<District> districtList() {

		List<District> districts = null;
		
		try {
			
			String sql = "  select * from districts ";
			
			districts = jdbcTemplate.query(sql, new RowMapper<District>() {
				@Override
				public District mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					District savedDistrict = new District();
					savedDistrict.setDistrictId(rs.getInt("district_id"));
					savedDistrict.setDistrictNameEn(rs.getString("district_name_en"));
					savedDistrict.setDistrictNameMh(rs.getString("district_name_mh"));
					
					return savedDistrict;
				}
			});
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
		return districts;
		
	}

	@Override
	public List<Tehsil> tehsilList(int districtId) {
		
		List<Tehsil> tehsils = null;
		
		try {
			
			String sql = "  select * from tehsils where district_id =  "+districtId;
			
			tehsils = jdbcTemplate.query(sql, new RowMapper<Tehsil>() {
				@Override
				public Tehsil mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					Tehsil savedTehsil = new Tehsil();
					savedTehsil.setDistrictId(rs.getInt("district_id"));
					savedTehsil.setTehsilId(rs.getInt("tehsil_id"));
					savedTehsil.setTehsilNameEn(rs.getString("tehsil_name_en"));
					
					return savedTehsil;
				}
			});
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
		return tehsils;
	}

}