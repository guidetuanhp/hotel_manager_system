package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class NotificationWindow {

	public static void showNotification(String title, String content) {
		FileInputStream file = null;
		try {
			file = new FileInputStream(new File("image/cropped-small-tick.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Image image = new Image(file);
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
		Notifications noti = Notifications.create()
				.title(title)
				.text(content)
				.graphic(imageView)
				.hideAfter(Duration.seconds(5))
				.position(Pos.BOTTOM_RIGHT)
				.onAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("hello");
					}
					
				});
		noti.darkStyle();
		noti.show();
	}
	
}
