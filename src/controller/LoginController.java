package controller;

import entity.Account;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import repository.AccountRepository;
import service.Contains;
import service.Message;
import service.SwitchToScene;

public class LoginController {
	
	private AccountRepository accountRepo = new AccountRepository();
	
	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	void login(ActionEvent event) {
		Account account = accountRepo.checkLogin(username.getText(), password.getText());
		if (account != null) {
			SwitchToScene sw = new SwitchToScene();
			Contains.account = account;
			System.out.println(Contains.account);
			sw.switchToAddRoom(event, sw.listRoom);
		}
		else {
			Message.getMess("login failure");
		}
	}
}
