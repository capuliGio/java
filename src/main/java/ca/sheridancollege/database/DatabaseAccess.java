package ca.sheridancollege.database;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.Toy;
import ca.sheridancollege.beans.User;

@Repository
public class DatabaseAccess {
	
	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	public void addToy(Toy toy) {
		//Map<key,Value>
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "INSERT INTO contacts_list (name, price, quantity)"
				+ "VALUES (:name, :price, :quantity)";
		parameters.addValue("name", toy.getName());
		parameters.addValue("price", toy.getPrice());
		parameters.addValue("quantity", toy.getQuantity());
		jdbc.update(query, parameters);
		
	}
	
	public void editToy(Toy toy) {
		//Map<key,Value>
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String query = "UPDATE contacts_list SET name=:name, price=:price, quantity=:quantity WHERE id=:id";
		parameters.addValue("id", toy.getId());
		parameters.addValue("name", toy.getName());
		parameters.addValue("price", toy.getPrice());
		parameters.addValue("quantity", toy.getQuantity());
		jdbc.update(query, parameters);
		
	}
	
	//Retrieve from Database table
	public ArrayList<Toy> getToys(){
		ArrayList<Toy> toys = new ArrayList<Toy>();
		
		String query = "SELECT * FROM contacts_list";
		
		List<Map<String, Object>> rows = jdbc.queryForList(query, new HashMap<String, Object>());
		
		for(Map<String, Object> row: rows) {
			Toy d = new Toy();
			d.setId((Integer)row.get("id"));
			d.setName((String)row.get("name"));
			d.setPrice((Double)row.get("price"));
			d.setQuantity((Integer)row.get("quantity"));
			toys.add(d);
		}
		return toys;
	}
	
	public Toy getToyById(int id){
		ArrayList<Toy> toys = new ArrayList<Toy>();
		
		String query = "SELECT * FROM contacts_list WHERE id=:id";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id", id);
		List<Map<String, Object>> rows = jdbc.queryForList(query,parameters);
		
		for(Map<String, Object> row: rows) {
			Toy d = new Toy();
			d.setId((Integer)row.get("id"));
			d.setName((String)row.get("name"));
			d.setPrice((Double)row.get("price"));
			d.setQuantity((Integer)row.get("quantity"));
			toys.add(d);
		}
		if( toys.size() > 0)
			return toys.get(0);
		
		return null;
	}
	
	public void deleteToy(int id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "DELETE FROM contacts_list WHERE id=:id";
		parameters.addValue("id", id);
		jdbc.update(query, parameters);
	}
	
	//Security methods
	
	public User findUserAccount(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user WHERE userName=:userName";
		parameters.addValue("userName", userName);
		ArrayList<User> users = (ArrayList<User>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<User>(User.class));
		if (users.size() >0) 
			return users.get(0);
		return null;
	}
	
	public List<String> getRolesById(Long userId){
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT user_role.userId, sec_role.roleName "
				+ "FROM user_role, sec_role "
				+ "WHERE user_role.roleId=sec_role.roleId "
				+ "AND userId=:userId";
		parameters.addValue("userId", userId);
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		
		for(Map<String, Object> row: rows) {
			roles.add((String)row.get("roleName"));
		}
		return roles;
	}
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public void createNewUser(String username, String password) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "insert into SEC_User (userName, encryptedPassword, ENABLED) " + 
				"values (:name, :pass, 1)"; 
		parameters.addValue("name", username);
		parameters.addValue("pass", passwordEncoder.encode(password));
		jdbc.update(query, parameters); // adds the values to the table
	}
	
	public void addRoles(long userId, long roleId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();//name parameter query
		String query = "insert into user_role (userId, roleId) " + 
				"values (:userId, :roleId)"; 
		parameters.addValue("userId", userId);
		parameters.addValue("roleId", roleId);
		jdbc.update(query, parameters);
					
	}
}