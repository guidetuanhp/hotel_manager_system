package service;

import javafx.scene.control.Alert;

public class Message {

	public static void getMess(String str) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(str);
		alert.setTitle("메시지");
		alert.setHeaderText("경보");
		alert.show();
	}
}
