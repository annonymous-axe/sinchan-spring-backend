package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
			
			String sql = "  insert into farmers(id, user_id, contact_no, email, farmer_name,"
						+ "	address, sanch, district_id, tehsil_id, aadhar_id, farmer_id) "
						+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			jdbcTemplate.update(sql, id, userId, farmer.getContactNo(), 
							   farmer.getEmail(), farmer.getFarmerName(), farmer.getAddress(),
							   farmer.getSanch(), farmer.getDistrict(), farmer.getTehsil(),
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
			
			String sql = "  update farmers set contact_no = ?, email = ?, farmer_name = ?,"
						+ "	address = ?, sanch = ?, district_id = ?, tehsil_id = ?, aadhar_id = ?, farmer_id = ? "
						+ " where id = "+farmer.getId()+" and user_id = "+userId;
			
			jdbcTemplate.update(sql, farmer.getContactNo(), farmer.getEmail(), farmer.getFarmerName(),
							   farmer.getAddress(), farmer.getSanch(), farmer.getDistrict(), 
							   farmer.getTehsil(), farmer.getAadharId(), farmer.getFarmerId());
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public List<Farmer> listFarmers(int userId) {
		
		List<Farmer> farmers = null;
		
		try {
			
			String sql = "  select id, farmer_name, email, contact_no, address from farmers "
						+ " where user_id = '"+userId+"'";
			
			farmers = jdbcTemplate.query(sql, new RowMapper<Farmer>() {
				@Override
				public Farmer mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					Farmer savedFarmer = new Farmer();
					savedFarmer.setId(rs.getInt("id"));
					savedFarmer.setContactNo(rs.getString("contact_no"));
					savedFarmer.setEmail(rs.getString("email"));
					savedFarmer.setFarmerName(rs.getString("farmer_name"));
					savedFarmer.setAddress(rs.getString("address"));
					
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
						savedFarmer.setFarmerName(rs.getString("farmer_name"));
						savedFarmer.setAddress(rs.getString("address"));
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

}