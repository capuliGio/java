package ca.sheridancollege.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DatabaseConfig {
	
	//Used in our DatabaseAccess class. Used to submit JDBC query strings
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTEmplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	//Create a connection to our database
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:testdb");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;		
	}
	
	//Load any default SQL files
	@Bean
	public DataSource loadSchema() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:schema.sql")
				.addScript("classpath:newsecuritysetup.sql")
				.build();
	}

}
