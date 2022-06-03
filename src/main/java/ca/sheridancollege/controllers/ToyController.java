package ca.sheridancollege.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.beans.Toy;
import ca.sheridancollege.database.DatabaseAccess;
import ca.sheridancollege.mail.EmailServiceImpl;

@Controller
public class ToyController {
	
	@Autowired
	@Lazy
	private DatabaseAccess da;
	
	@Autowired
	private EmailServiceImpl esi;

	@GetMapping("/add") //localhost:8080
	public String goNewToys(Authentication authentication, Model model) {
		//Get the user's roles
				List<String> roleNames = new ArrayList<String>();
				for (GrantedAuthority ga : authentication.getAuthorities()) {
					roleNames.add(ga.getAuthority());
				}
				
		model.addAttribute("toy", new Toy());
		return "newContacts.html";
	}
	
	@GetMapping("/")
	public String goHome() {
		return "home.html"; 
	}
	
	@GetMapping("/submit")
	public String saveSong(Model model, @ModelAttribute Toy toy) {
		da.addToy(toy);
		return "redirect:/add";
	}
	
	@GetMapping("/email")
	public String sendEmail(Authentication authentication) {
		
		//Get the user's roles
		List<String> roleNames = new ArrayList<String>();
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			roleNames.add(ga.getAuthority());
		}

		try {
			System.out.println("Bravo");
			esi.sendMailWithThymeleaf("prog32758.summer2020@gmail.com", "Finals", 
					"Hernald Gio Capuli", "Class Number - 1205_11184", "Thank you so much!", da.getToys());	
		} catch (MessagingException e) {
			System.out.println(e);
		}
		return "email.html";
	}
		
	@GetMapping("/print")
	public String viewContact(Model model) {
		model.addAttribute("toys", da.getToys());
		
		
		System.out.println("Boo");
		
		return "showContacts.html";
	}
	
	@GetMapping("/edit/{id}")
	public String editToy(Authentication authentication, @PathVariable int id, Model model) {
		//Get the user's roles
		List<String> roleNames = new ArrayList<String>();
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			roleNames.add(ga.getAuthority());
		}
		
		Toy toy = da.getToyById(id);
		model.addAttribute("toy", toy);
		return "modify.html";
	}

	@GetMapping("/delete/{id}")
	public String deleteToy(Authentication authentication, @PathVariable int id, Model model) {
		//Get the user's roles
		List<String> roleNames = new ArrayList<String>();
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			roleNames.add(ga.getAuthority());
		}
		
		da.deleteToy(id);
		return "redirect:/print";
	}

	@GetMapping("/modify")
	public String editToy(Model model, @ModelAttribute Toy toy, Authentication authentication) {
		da.editToy(toy);
		
		//Get the user's roles
		List<String> roleNames = new ArrayList<String>();
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			roleNames.add(ga.getAuthority());
		}
		
		model.addAttribute("toys", da.getToys());
		
		return "showContacts.html";
	}
	
	@GetMapping("/member")
	public String goOwnerHome(Authentication authentication) { // added auth to get information about the user
		//Get the user's name
		String name = authentication.getName();
		//Get the user's roles
		List<String> roleNames = new ArrayList<String>();
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			roleNames.add(ga.getAuthority());
		}
		return "redirect:/add"; 
	}
	
	@GetMapping("/login")
	public String goLoginPage() {
		return "login.html"; 
	}
	
	@GetMapping("/access-denied")
	public String goAccessDenied() {
		return "/error/access-denied.html"; 
	}
	
	@GetMapping("/register")
	public String goRegistration() {
		return "registration.html";
	}
	
	@PostMapping("/register")
	public String processRegistration(@RequestParam String name, @RequestParam String password, 
			@RequestParam(required=false, defaultValue="not selected") String role1,
			@RequestParam(required=false, defaultValue="not selected") String role2) {
		da.createNewUser(name, password);
		long userId = da.findUserAccount(name).getUserId();
		
		if(!role1.equals("not selected")) {
			da.addRoles(userId, 1);
		}
		if(!role2.equals("not selected")) {
			da.addRoles(userId, 2);
		}
		
		return "redirect:/register";
	}
	
}
