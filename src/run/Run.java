package run;

import java.io.IOException;

import org.hibernate.Session;

import connect.HibernateConnect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Run extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("../view/style.css").toExternalForm());
			primaryStage.setScene(scene);
//			FileInputStream file = new FileInputStream("hust-icon.png");
//			primaryStage.getIcons().add(new Image(file));
			primaryStage.show();
			Session session = HibernateConnect.getFactory().openSession();
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
