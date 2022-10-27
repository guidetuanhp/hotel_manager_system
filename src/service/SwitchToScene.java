package service;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SwitchToScene {

	public static String addRoom = "../view/addroom.fxml";
	public static String listRoom = "../view/listroom.fxml";
	public static String login = "../view/login.fxml";
	public static String booking = "../view/booking.fxml";
	public static String service = "../view/listservice.fxml";
	public static String pay = "../view/checkout.fxml";
	public static String addService = "../view/addserviceinvocie.fxml";
	public static String listCustomer = "../view/listcustomer.fxml";
	public static String account = "../view/account.fxml";
	public static String listInvoice = "../view/listinvoice.fxml";
	public static String statistic = "../view/statitic.fxml";
	
	public void switchToAddRoom(Event event, String path) {
		if(path.equals(addRoom) || path.equals(account) || path.equals(statistic)) {
			if(RoleService.hasRole("ADMIN") == false) {
				Message.getMess("401: access denied");
				return;
			}
			else {
				Parent root;
				Scene scene;
				Stage stage;
				try {
					root = FXMLLoader.load(getClass().getResource(path));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("../view/style.css").toExternalForm());
					stage.setScene(scene);
					Screen screen = Screen.getPrimary();
					Rectangle2D bounds = screen.getVisualBounds();
					stage.setX(bounds.getMinX());
					stage.setY(bounds.getMinY());
					stage.setWidth(bounds.getWidth());
					stage.setHeight(bounds.getHeight());
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			Parent root;
			Scene scene;
			Stage stage;
			try {
				root = FXMLLoader.load(getClass().getResource(path));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("../view/style.css").toExternalForm());
				stage.setScene(scene);
				Screen screen = Screen.getPrimary();
				Rectangle2D bounds = screen.getVisualBounds();
				stage.setX(bounds.getMinX());
				stage.setY(bounds.getMinY());
				stage.setWidth(bounds.getWidth());
				stage.setHeight(bounds.getHeight());
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void switchToLogin(Event event) {
		Parent root;
		Scene scene;
		Stage stage;
		try {
			root = FXMLLoader.load(getClass().getResource(login));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../view/style.css").toExternalForm());
			stage.setScene(scene);
			Screen screen = Screen.getPrimary();
			stage.setX(300);
			stage.setY(200);
			stage.setWidth(600);
			stage.setHeight(300);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
