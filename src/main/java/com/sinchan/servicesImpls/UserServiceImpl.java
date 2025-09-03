package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sinchan.dao.UserDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sinchan.entities.User;
import com.sinchan.services.UserService;

@Repository
public class UserServiceImpl implements UserService {
	
	private final JdbcTemplate jdbcTemplate;
	
	public UserServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public User findUserByEmail(String email) {

		User user = null;

		try {
			
			String sql = "select * from users where email = '"+email+"'";

			user = jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
				@Override
				public User extractData(ResultSet rs) throws SQLException {
					
					User savedUser = new User();
					
					if(rs.next()) {
						
						savedUser.setUserId(rs.getInt("user_id"));
						savedUser.setActive(rs.getBoolean("active"));
						savedUser.setEmail(rs.getString("email"));
						savedUser.setFirmNameEn(rs.getString("firm_name_en"));
						savedUser.setFullNameEn(rs.getString("full_name_en"));
						savedUser.setAddressEn(rs.getString("address_en"));
						savedUser.setContactNumber(rs.getString("contact_number"));
						savedUser.setGstNumber(rs.getString("gst_number"));
					}
					return savedUser;
				}
			});
			
		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
		
		return user;
	}

	@Override
	@Transactional
	public void update(UserDAO user, int userId) {

		try {

			String sql = "  update users set address_en = ?, firm_name_en = ?, full_name_en = ?, gst_number = ? "
				+ " where user_id = "+userId;

			jdbcTemplate.update(sql, user.getAddressEn(), user.getFirmNameEn(), user.getFullNameEn(), user.getGstNumber());

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	@Transactional
	public void delete(UserDAO user, int userId) {

		try {

			String sql = " delete from users where user_id = ?";

			jdbcTemplate.update(sql, userId);

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		try {

			String sql = "select user.user_id, user.email, user.password, user.active, role.role as role from users user "
					+ " join roles role on role.email = user.email "
					+ " where user.email = '"+email+"' ";

			System.out.println("sql : "+sql);

			return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
				@Override
				public User extractData(ResultSet rs) throws SQLException {

					if(rs.next()) {

						if(rs.getBoolean("active")){

							User user = new User();
							user.setUserId(rs.getInt("user_id"));
							user.setEmail(rs.getString("email"));
							user.setPassword(rs.getString("password"));
							user.setActive(rs.getBoolean("active"));

							List<SimpleGrantedAuthority> roles = new ArrayList<>();

							for(String role : rs.getString("role").split(",")){
								roles.add(new SimpleGrantedAuthority(role));
							}

							user.setRole(roles);

							return user;

						}
					}

					throw new UsernameNotFoundException("User not found with given username.");

				}
			});

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
	}

	@Override
	public UserDAO loadUserDAOByUsername(String email) throws UsernameNotFoundException {

		try {

			String sql = "select email, firm_name_en, full_name_en, address_en, contact_number, gst_number, active from users "
					+ " where email = '"+email+"' ";

			System.out.println("sql : "+sql);

			return jdbcTemplate.query(sql, new ResultSetExtractor<UserDAO>() {
				@Override
				public UserDAO extractData(ResultSet rs) throws SQLException {

					if(rs.next()) {

						if(rs.getBoolean("active")){

							UserDAO user = new UserDAO();
							user.setEmail(rs.getString("email"));
							user.setFirmNameEn(rs.getString("firm_name_en"));
							user.setFullNameEn(rs.getString("full_name_en"));
							user.setAddressEn(rs.getString("address_en"));
							user.setGstNumber(rs.getString("gst_number"));
							user.setContactNumber(rs.getString("contact_number"));

							return user;

						}
					}

					throw new UsernameNotFoundException("User not found with given username.");

				}
			});

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}
	}


}
