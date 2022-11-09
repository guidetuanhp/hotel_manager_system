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
		
		if (username.getText().length() == 0 || password.getText().length() == 0)
		{
			Message.getMess("아이디 비밀번호 입력해야합니다.");
		}
		else
		{
			Account account = accountRepo.checkLogin(username.getText(), password.getText());
			
			if (account != null) {
				SwitchToScene sw = new SwitchToScene();
				Contains.account = account;
				System.out.println(Contains.account);
				sw.switchToAddRoom(event, sw.listRoom);
			}
			else {
				Message.getMess("로그인 실패");
			}
		}
		
	}
}
