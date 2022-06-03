package ca.sheridancollege;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import ca.sheridancollege.beans.Toy;
import ca.sheridancollege.database.DatabaseAccess;

@SpringBootTest
@AutoConfigureMockMvc
class ToyApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private DatabaseAccess da;
	
	@Test
	public void testLoadingHomePage() throws Exception{
		this.mockMvc.perform(get("/"))
		.andExpect(status().isOk()) //Status Code 200
		.andExpect(view().name("home.html"));
	}
	
	@Test
	public void testSubmitToy() throws Exception{
		this.mockMvc.perform(get("/submit")
				.flashAttr("toy", new Toy())) //Testing the Model Attribute
		.andExpect(status().isFound()) //Status Code 302 for Redirect
		.andExpect(redirectedUrl("/add")); 
	}
	
	@Test
	public void testViewToy() throws Exception{
		this.mockMvc.perform(get("/print"))
		.andExpect(status().isOk()) //Status Code 200 for normal html
		.andExpect(model().attributeExists("toys")) 
		.andExpect(view().name("showContacts.html")); 
	}
	
	@Test
	public void testDefaultGetToys(){
		ArrayList<Toy> toys = da.getToys();
		int initialsize = toys.size();
		assertThat(initialsize).isEqualTo(10);
	}
	
	@Test
	public void testAddToy(){
		ArrayList<Toy> toys = da.getToys();
		int initialsize = toys.size();
		da.addToy(new Toy());
		int newSize = da.getToys().size();
		assertThat(initialsize+1).isEqualTo(newSize);
	}
	
	@Test
	public void testGetToyById(){
		Toy toy = da.getToyById(2);
		assertThat(toy).isNotEqualTo(null);
	}
	
	@Test
	public void testGetBadToyById(){
		Toy toy = da.getToyById(22);
		assertThat(toy).isEqualTo(null);
	}
}
