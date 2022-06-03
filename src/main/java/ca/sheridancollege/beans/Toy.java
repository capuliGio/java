package ca.sheridancollege.beans;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Toy implements java.io.Serializable {

	private static final long serialVersionUID = -3237853041937337689L;
	private int id;
	private String name;
	private double price; 
	private int quantity;
	private String role;
	private String [] roles = {"Boss", "Worker"};

	
}
