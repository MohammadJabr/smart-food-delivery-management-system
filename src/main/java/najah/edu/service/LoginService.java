package najah.edu.service;

import najah.edu.domain.User;


public class LoginService {
	User user;
	public LoginService(User user) {
		this.user=user;
	}
	
public boolean login(String name,String password) {
	if( user.getName().equals(name)&&user.getpassword().equals(password)) {
		return true;
	}

System.out.println("Invalid username or password");
return false;
}
}


