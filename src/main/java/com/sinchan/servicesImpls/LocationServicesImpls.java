package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sinchan.entities.District;
import com.sinchan.entities.Farmer;
import com.sinchan.entities.Tehsil;
import com.sinchan.services.LocationServices;

import lombok.extern.slf4j.Slf4j;

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
					savedDistrict.setDistrictName(rs.getString("district_name"));
					
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
					savedTehsil.setTalukaId(rs.getInt("tehsil_id"));
					savedTehsil.setTalukaName(rs.getString("tehsil_name"));
					
					return savedTehsil;
				}
			});
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
		return tehsils;
	}

}