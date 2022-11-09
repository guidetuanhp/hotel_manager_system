package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import repository.RoomRepository;
import service.SwitchToScene;

public class FrameController implements Initializable {

	private RoomRepository roomRepo = new RoomRepository();

	@FXML
	private VBox sub_menu;

	@FXML
	private ImageView avatar;

	@FXML
	private ImageView logo;

	@FXML
	private Button statiitics;
	
    @FXML
    private Button manager;
    
    @FXML
    private Button booking;
    

    @FXML
    private Label time;


	@FXML
	void openSubMenu(MouseEvent event) {
		open_Sub_Menu();
	}

	@FXML
	void closeSubMenu(MouseEvent event) {
		close_Sub_Menu();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		close_Sub_Menu();
		setAvatar();
		setLogo();
		showLogout();
		
		booking.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			switchToRoom();
			sub_menu.setVisible(true);
		});
		manager.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			switchToManager();
			sub_menu.setVisible(true);
		});
		statiitics.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			new SwitchToScene().switchToAddRoom(ev, SwitchToScene.statistic);
			sub_menu.setVisible(true);
		});
		AnimationTimer timer = new AnimationTimer() {
		    @Override
		    public void handle(long now) {
		        time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		    }
		};
		timer.start();
	}

	public void showLogout() {
		avatar.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			PopOver popOver = new PopOver();
			popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
			Button button = new Button("Logout");
			button.setPrefWidth(100);
			button.setPrefHeight(60);
			popOver.setContentNode(button);
			popOver.show((Node) ev.getSource(), -15);

			button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				new SwitchToScene().switchToLogin(ev);
			});
		});
	}

	public void open_Sub_Menu() {
		sub_menu.setVisible(true);
		sub_menu.setPrefWidth(200);
		sub_menu.setMinWidth(200);
		sub_menu.setMaxWidth(200);
	}

	public void close_Sub_Menu() {
		sub_menu.setVisible(false);
		sub_menu.setPrefWidth(0);
		sub_menu.setMinWidth(0);
		sub_menu.setMaxWidth(0);
	}

	public void setAvatar() {
		File is = new File("image/avatar.jpg");
		InputStream ip = null;
		try {
			ip = new FileInputStream(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Image image = new Image(ip);
		avatar.setImage(image);
	}

	public void setLogo() {
		File is = new File("image/logo.jpg");
		InputStream ip = null;
		try {
			ip = new FileInputStream(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Image image = new Image(ip);
		logo.setImage(image);
	}

	void switchToManager() {
		manager.setId("active");
		booking.setId("");
		statiitics.setId("");
		List<Button> list = new ArrayList<Button>();
		Button button = new Button("계정 주가");
		Button button1 = new Button("리스트 계정");
		Button button3 = new Button("리스트 영수증 ");
		Button button5 = new Button("리스트 서비스");
		Button button6 = new Button("리스트 고객");
		list.add(button1);
		list.add(button3);
		list.add(button5);
		list.add(button6);
		setListSubmenu(list, button);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			new SwitchToScene().switchToAddRoom(ev, SwitchToScene.account);
		});
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			new SwitchToScene().switchToAddRoom(ev, SwitchToScene.account);
		});
		button3.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			new SwitchToScene().switchToAddRoom(ev, SwitchToScene.listInvoice);
		});
		button5.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			new SwitchToScene().switchToAddRoom(ev, SwitchToScene.service);
		});
		button6.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			new SwitchToScene().switchToAddRoom(ev, SwitchToScene.listCustomer);
		});
	}

	void switchToRoom() {
		booking.setId("active");
		manager.setId("");
		statiitics.setId("");
		List<Button> list = new ArrayList<Button>();
		Button button = new Button("객실 추가");
		Button button1 = new Button("모두 객실");
		Button button2 = new Button("공실");
		Button button3 = new Button("정리 객실");
		Button button4 = new Button("사용중 객실");
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			AddRoomController.id = null;
			new SwitchToScene().switchToAddRoom(event, SwitchToScene.addRoom);
		});
		button1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			RoomController.list = roomRepo.findAll();
			new SwitchToScene().switchToAddRoom(event, SwitchToScene.listRoom);
			sub_menu.setVisible(true);
		});
		button2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			RoomController.list = roomRepo.findEmptyRoom();
			new SwitchToScene().switchToAddRoom(event, SwitchToScene.listRoom);
			sub_menu.setVisible(true);
		});
		button3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			RoomController.list = roomRepo.findRepairRoom();
			new SwitchToScene().switchToAddRoom(event, SwitchToScene.listRoom);
			sub_menu.setVisible(true);
		});
		button4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			RoomController.list = roomRepo.findInhabitedRoom();
			new SwitchToScene().switchToAddRoom(event, SwitchToScene.listRoom);
			sub_menu.setVisible(true);
		});
		list.add(button1);
		list.add(button2);
		list.add(button3);
		list.add(button4);
		setListSubmenu(list, button);
	}

	void switchToStatitical() {
		
	}

	public void setListSubmenu(List<Button> list, Button buttonAdd) {
		sub_menu.getChildren().clear();
		HBox hBox = new HBox();
		hBox.getChildren().add(buttonAdd);
		buttonAdd.setId("add_content");
		buttonAdd.setPrefWidth(123);
		buttonAdd.setPrefHeight(57);
		hBox.setMargin(buttonAdd, new Insets(10, 0, 0, 10));
		FontAwesomeIconView remove = new FontAwesomeIconView();
		remove.setGlyphName("REMOVE");
		remove.setSize("20");
		FontAwesomeIconView edit = new FontAwesomeIconView();
		edit.setGlyphName("PENCIL_SQUARE_ALT");
		edit.setSize("12");
		buttonAdd.setGraphic(edit);
		hBox.getChildren().add(remove);
		hBox.setMargin(remove, new Insets(30, 0, 0, 30));
		remove.setFill(Color.valueOf("#b7b338"));

		sub_menu.getChildren().add(hBox);
		for (Button b : list) {
			FontAwesomeIconView font = new FontAwesomeIconView();
			font.setSize("14");
			font.setGlyphName("LIST_ALT");
			if (b.getText().contains("add") || b.getText().contains("Add")) {
				font.setGlyphName("PLUS");
			}
			b.setTextFill(Color.valueOf("#837f13"));
			b.setGraphic(font);
			b.setFont(Font.font("System", FontWeight.BOLD, 13));
			b.getStyleClass().add("btn_submenu");
			sub_menu.getChildren().add(b);
			sub_menu.setMargin(b, new Insets(10, 0, 0, 10));
		}
		open_Sub_Menu();
		remove.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			close_Sub_Menu();
		});
		remove.setCursor(Cursor.HAND);
	}
}
