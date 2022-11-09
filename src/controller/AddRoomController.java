package controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import entity.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import repository.RoomRepository;
import service.Message;
import service.NotificationWindow;
import service.SwitchToScene;
import upload.FileUpload;

public class AddRoomController extends FrameController {

	private String linkImage = null;

	public static Integer id = null;

	private ObservableList<String> allStatus = FXCollections.observableArrayList();

	private RoomRepository roomRepository = new RoomRepository();

	private FileUpload FileUpload = new FileUpload();

	@FXML
	private ImageView image;

	@FXML
	private TextField txt_id;

	@FXML
	private TextField txt_name;

	@FXML
	private TextField txt_price;

	@FXML
	private TextField txt_capacity;

	@FXML
	private ChoiceBox<String> txt_status;

	@FXML
	void openImage(MouseEvent event) throws MalformedURLException {
		FileChooser fc = new FileChooser();
		File f = fc.showOpenDialog(null);
		if (f != null) {
			String s = f.getName();
			Path urlFile = f.toPath();
			linkImage = urlFile.toString();
			File file = new File(linkImage);
			String path = file.toURI().toURL().toString();
			Image image = new Image(path);
			this.image.setImage(image);

			System.out.println(linkImage);

		}
	}

	@FXML
	void addOrUpdate(ActionEvent event) {
		
		if (txt_id.getText().equals("")) {
			Room room = new Room();
			room.setCapacity(Integer.valueOf(txt_capacity.getText()));
			room.setPrice(Double.valueOf(txt_price.getText()));
			room.setRoomName(txt_name.getText());
			room.setRoomStatus(txt_status.getValue());
			if (room.getRoomStatus().equals("normal")) {
				room.setActived(1);
			} else {
				room.setActived(0);
			}
			if (linkImage != null) {
				String link = FileUpload.upload(linkImage);
				System.out.println(link);
				room.setLinkImage(link);
				System.out.println();
				if (roomRepository.save(room)) {
					NotificationWindow.showNotification("성공", "객실 추가 성공!");
				}
			}
			else {
				Message.getMess("사진 선택해야합니다!");
			}
		}
		
		else {
			Room room = roomRepository.findById(Integer.valueOf(txt_id.getText()));
			room.setCapacity(Integer.valueOf(txt_capacity.getText()));
			room.setPrice(Double.valueOf(txt_price.getText()));
			room.setRoomName(txt_name.getText());
			room.setRoomStatus(txt_status.getValue());
			if (room.getRoomStatus().equals("normal")) {
				room.setActived(1);
			} else {
				room.setActived(0);
			}
			if (linkImage != null) {
				String link = FileUpload.upload(linkImage);
				System.out.println(link);
				room.setLinkImage(link);
			}
			if (roomRepository.update(room)) {
				NotificationWindow.showNotification("성공", "객실 수정 성공!");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		allStatus.add("normal");
		allStatus.add("correcting");
		txt_status.setItems(allStatus);
		txt_status.setValue("normal");
		if (id != null) {
			Room room = roomRepository.findById(id);
			System.out.println("==> "+room);
			txt_capacity.setText(room.getCapacity().toString());
			txt_id.setText(room.getId().toString());
			txt_name.setText(room.getRoomName());
			txt_price.setText(room.getPrice().toString());
			txt_status.setValue(room.getRoomStatus());
			image.setImage(new Image(room.getLinkImage()));
		}
		System.out.println("==== id: "+id);
	}

}
