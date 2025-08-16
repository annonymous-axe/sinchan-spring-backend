package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.sinchan.entities.Dictionary;
import com.sinchan.entities.District;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sinchan.entities.Farmer;
import com.sinchan.services.FarmerService;

@Repository
public class FarmerServiceImpl implements FarmerService {

	private JdbcTemplate jdbcTemplate;
	
	
	public FarmerServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	@Transactional
	public void save(Farmer farmer, int userId) {
		
		int id = getNextSeq(userId);
		
		try {
			
			String sql = "  insert into farmers(id, user_id, contact_no, email, farmer_name_en, farmer_name_mh,"
						+ "	address_en, address_mh, sanch, district_id, tehsil_id, aadhar_id, farmer_id) "
						+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			jdbcTemplate.update(sql, id, userId, farmer.getContactNo(), 
							   farmer.getEmail(), farmer.getFarmerNameEn(), farmer.getFarmerNameMh(), farmer.getAddressEn(),
							   farmer.getAddressMh(),farmer.getSanch(), farmer.getDistrict(), farmer.getTehsil(),
							   farmer.getAadharId(), farmer.getFarmerId());
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
	}

	private Integer getNextSeq(int userId) {

		Integer seq = 0;

		try {

			String sql = "  select id seq from farmers "
					+" where user_id = '"+userId+"' "
					+" order by id desc limit 1";

			seq = jdbcTemplate.query(sql, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException {

					int seq = 0;

					if(rs.next()) {

						System.out.println(seq);

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

	@Override
	@Transactional
	public void update(Farmer farmer, int userId) {
		
		try {
			
			String sql = "  update farmers set contact_no = ?, email = ?, farmer_name_en = ?, farmer_name_mh = ?, "
						+ "	address_en = ?, address_mh = ?, sanch = ?, district_id = ?, tehsil_id = ?, aadhar_id = ?, farmer_id = ? "
						+ " where id = "+farmer.getId()+" and user_id = "+userId;
			
			jdbcTemplate.update(sql, farmer.getContactNo(), farmer.getEmail(), farmer.getFarmerNameEn(), farmer.getFarmerNameMh(),
							   farmer.getAddressEn(), farmer.getAddressMh(), farmer.getSanch(), farmer.getDistrict(),
							   farmer.getTehsil(), farmer.getAadharId(), farmer.getFarmerId());
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public List<Farmer> listFarmers(int userId) {
		
		List<Farmer> farmers = null;
		String sql = "";
		
		try {

			sql = " select id, farmer_name_en, farmer_name_mh, email, contact_no, address_en, address_mh from farmers "
				+ " where user_id = '"+userId+"'";
			
			farmers = jdbcTemplate.query(sql, new RowMapper<Farmer>() {
				@Override
				public Farmer mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					Farmer savedFarmer = new Farmer();
					savedFarmer.setId(rs.getInt("id"));
					savedFarmer.setContactNo(rs.getString("contact_no"));
					savedFarmer.setEmail(rs.getString("email"));
					savedFarmer.setFarmerNameEn(rs.getString("farmer_name_en"));
					savedFarmer.setFarmerNameMh(rs.getString("farmer_name_mh"));
					savedFarmer.setAddressEn(rs.getString("address_en"));
					savedFarmer.setAddressMh(rs.getString("address_mh"));

					return savedFarmer;
				}
			});
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
		return farmers;
	}

	@Override
	public Farmer findById(int id, int userId) {
		
		Farmer farmer = null;
		
		try {
			
			String sql = "  select * from farmers "
						+ " where id = '"+id+"'"
						+ " and user_id = '"+userId+"'";
			
			farmer = jdbcTemplate.query(sql, new ResultSetExtractor<Farmer>() {
				@Override
				public Farmer extractData(ResultSet rs) throws SQLException {
					
					Farmer savedFarmer = new Farmer();
					
					if(rs.next()) {
						
						savedFarmer.setId(id);
						savedFarmer.setContactNo(rs.getString("contact_no"));
						savedFarmer.setEmail(rs.getString("email"));
						savedFarmer.setFarmerNameEn(rs.getString("farmer_name_en"));
						savedFarmer.setFarmerNameMh(rs.getString("farmer_name_mh"));
						savedFarmer.setAddressEn(rs.getString("address_en"));
						savedFarmer.setAddressMh(rs.getString("address_mh"));
						savedFarmer.setSanch(rs.getString("sanch"));
						savedFarmer.setAadharId(rs.getString("aadhar_id"));
						savedFarmer.setFarmerId(rs.getString("farmer_id"));
						savedFarmer.setDistrict(rs.getInt("district_id"));
						savedFarmer.setTehsil(rs.getInt("tehsil_id"));
					}
					return savedFarmer;
				}
			});
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
		return farmer;
	}

	@Override
	@Transactional
	public void delete(Farmer farmerBean, int userId) {
		
		try {
			
			String sql = " delete from farmers where id = ? and user_id = ?";
			
			jdbcTemplate.update(sql, farmerBean.getId(), userId);
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
	}

	// for rest-apis

	@Override
	@Transactional
	public void delete(int farmerId, int userId) {

		try {

			String sql = " delete from farmers where id = ? and user_id = ?";

			jdbcTemplate.update(sql, farmerId, userId);

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public List<Dictionary> listSanch(int userId) {

		List<Dictionary> sanchList = null;
		String sql = "";

		try {

			sql = " select id, name_en, name_mh from sanch "
					+ " where user_id = '"+userId+"'";

			sanchList = jdbcTemplate.query(sql, new RowMapper<Dictionary>() {
				@Override
				public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {

					Dictionary savedSanch = new Dictionary();
					savedSanch.setIntKey(rs.getInt("id"));
					savedSanch.setStringValue(rs.getString("name_en"));
					savedSanch.setStringValue2(rs.getString("name_mh"));

					return savedSanch;
				}
			});

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

		return sanchList;
	}

}