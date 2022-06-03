package ca.sheridancollege.beans;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class User {

	public Long userId;
	public String userName;
	public String encryptedPassword;
}
