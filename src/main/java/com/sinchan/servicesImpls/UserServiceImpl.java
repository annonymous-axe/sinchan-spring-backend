package com.sinchan.servicesImpls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sinchan.user.credentials.SinchanAuthToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sinchan.entities.Farmer;
import com.sinchan.entities.User;
import com.sinchan.services.UserService;

import lombok.extern.slf4j.Slf4j;

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
						savedUser.setFirmName(rs.getString("firm_name"));
						savedUser.setFirstName(rs.getString("first_name"));
						savedUser.setLastName(rs.getString("last_name"));
						savedUser.setAddress(rs.getString("address"));
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
	public void update(User user) {

		try {

			String sql = "  update users set address = ?, firm_name = ?, first_name = ?, last_name = ?, gst_number = ? "
				+ " where user_id = "+user.getUserId();

			jdbcTemplate.update(sql, user.getAddress(), user.getFirmName(), user.getFirstName(), user.getLastName(), user.getGstNumber());

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	@Transactional
	public void delete(User user) {

		try {

			String sql = " delete from users where user_id = ?";

			jdbcTemplate.update(sql, user.getUserId());

		}catch(Exception e) {
			throw new RuntimeException("Exception : "+e);
		}

	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = new User();

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


}
