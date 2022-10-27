package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.transaction.Transactional.TxType;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import entity.Customer;
import entity.Invoice;
import entity.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import repository.CustomerRepository;
import repository.InvoiceRepository;
import repository.RoomRepository;
import service.Message;
import service.NotificationWindow;
import service.SwitchToScene;

public class RoomController extends FrameController {

	public static List<Room> list = new ArrayList<Room>();

	private RoomRepository roomRepo = new RoomRepository();
	
	private CustomerRepository customerRepository = new CustomerRepository();
	
	private InvoiceRepository invoiceRepository = new InvoiceRepository();

	private ObservableList<String> listNumber = FXCollections.observableArrayList();

	@FXML
	private VBox listRoom;

	@FXML
	private ImageView empty_room;

	@FXML
	private ImageView not_empty_room;

	@FXML
	private Button booking;

	@FXML
	private Button manager;

	@FXML
	private Button statiitics;

	@FXML
	private ChoiceBox<String> numberOfPeople;

	@FXML
	void btnCheck(ActionEvent event) {
		list.clear();
		list = roomRepo.findAll();
		String value = numberOfPeople.getValue();
		if (value.equalsIgnoreCase("single room")) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCapacity() > 1) {
					list.remove(i);
					--i;
				}
			}
		} else if (value.equalsIgnoreCase("double room")) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCapacity() > 2) {
					list.remove(i);
					--i;
				}
			}
		} else {
			list.clear();
			list = roomRepo.findAll();
		}
		loadListRoom();
	}

	public void loadListRoom() {
		for (int k = 0; k < listRoom.getChildren().size(); k++) {
			if (listRoom.getChildren().get(k) instanceof HBox) {
				listRoom.getChildren().remove(k);
				--k;
			}
		}
		int j = 1;
		HBox hBox = new HBox();
		listRoom.getChildren().add(hBox);
		listRoom.setMargin(hBox, new Insets(0, 0, 20, 0));
		for (int i = 0; i < list.size(); i++) {
			if (j % 6 == 0) {
				j = 1;
				hBox = new HBox();
				listRoom.getChildren().add(hBox);
				listRoom.setMargin(hBox, new Insets(0, 0, 20, 0));
			}
			VBox room = new VBox();
			showListOptionBookRoom(room, 0, list.get(i).getId(),
					list.get(i).getRoomStatus().equalsIgnoreCase("correcting"));
			room.setPrefWidth(150);
			room.setPrefHeight(150);
			Label room_name = new Label("room: " + list.get(i).getRoomName());
			Label price = new Label("price: " + list.get(i).getPrice());
			room_name.setFont(new Font(22));
			price.setFont(new Font(22));

			File file = new File("image/room_not.png");
			if (list.get(i).getIdCardCustomer() == null) {
				file = new File("image/room-active.png");
				room_name.setTextFill(Color.rgb(255, 255, 255));
				price.setTextFill(Color.rgb(255, 255, 255));
				showListOptionBookRoom(room, 1, list.get(i).getId(), list.get(i).getRoomStatus().equalsIgnoreCase("correcting"));
			}
			if(list.get(i).getRoomStatus().equalsIgnoreCase("correcting")) {
				file = new File("image/repair.png");
			}
			FileInputStream is = null;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Image img = new Image(is, 250, 150, false, true);
			BackgroundImage myBI = new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
			room.setBackground(new Background(myBI));
			room.getChildren().add(room_name);
			room.getChildren().add(price);
			hBox.getChildren().add(room);
			hBox.setMargin(room, new Insets(0, 20, 0, 0));
			++j;
		}
	}

	public boolean checkNow(List<Invoice> list) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		for (Invoice i : list) {
			if (i.getBookingDate().getDate() <= now.getDate() && i.getBookingDate().getMonth() == now.getMonth()
					&& i.getBookingDate().getYear() == now.getYear() && i.getReturnDate().getDate()>=now.getDate()) {
				if(i.getActualReturnDate() != null) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	public void showListOptionBookRoom(Node node, int check, Integer idRoom, boolean repair) {
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			Room rooms = roomRepo.findById(idRoom);
			PopOver popOver = new PopOver();
			popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
			VBox vBox = new VBox();
			vBox.setPrefWidth(200);
			vBox.setPrefHeight(300);
			Button button = new Button("예약");
			button.setPrefWidth(200);
			button.setPrefHeight(50);

			Button checkout = new Button("체크아웃");
			checkout.setPrefWidth(200);
			checkout.setPrefHeight(50);

			Button addService = new Button("서비스 추가");
			addService.setPrefWidth(200);
			addService.setPrefHeight(50);
			
			Button update = new Button("객실 수정");
			update.setPrefWidth(200);
			update.setPrefHeight(50);

			Button delete = new Button("객실 삭제");
			delete.setPrefWidth(200);
			delete.setPrefHeight(50);

			Button showInforCustomer = new Button("고객 정보");
			showInforCustomer.setPrefWidth(200);
			showInforCustomer.setPrefHeight(50);

			Button addCustomerCard = new Button("객실 받기");
			addCustomerCard.setPrefWidth(200);
			addCustomerCard.setPrefHeight(50);
			
			if (repair == false) {
				vBox.getChildren().add(button);
			}
			if (check == 0 && repair == false) {
				vBox.getChildren().add(checkout);
				vBox.getChildren().add(addService);
			}
			if (repair == false && rooms.getIdCardCustomer() != null) {
				vBox.getChildren().add(showInforCustomer);
			}
			if(check == 1 && repair == false) {
				vBox.getChildren().add(addCustomerCard);
			}
			vBox.getChildren().add(update);
			vBox.getChildren().add(delete);
			popOver.setContentNode(vBox);
			popOver.show(node, -15);

			update.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
				AddRoomController.id = idRoom;
				popOver.hide();
				new SwitchToScene().switchToAddRoom(ev, SwitchToScene.addRoom);
			});
			
			addCustomerCard.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
				PopOver pop = new PopOver();
				pop.setArrowLocation(ArrowLocation.TOP_CENTER);
				VBox vBox2 = new VBox();
				vBox2.setPrefWidth(200);
				vBox2.setPrefHeight(100);
				TextField idCard = new TextField(); 
				idCard.setPromptText("id customer");
				idCard.setPrefWidth(200);
				idCard.setPrefHeight(50);
				Button updateRoom = new Button("확인");
				updateRoom.setPrefWidth(200);
				updateRoom.setPrefHeight(50);
				
				vBox2.getChildren().add(idCard);
				vBox2.getChildren().add(updateRoom);
				
				popOver.setContentNode(vBox2);
				popOver.show(node, -15);
				
				updateRoom.addEventHandler(MouseEvent.MOUSE_CLICKED, evv -> {
					Room r = roomRepo.findById(idRoom);
					Customer customer = customerRepository.findById(Integer.valueOf(idCard.getText()));
					
					if(customer == null) {
						Message.getMess("고객 정보 확인 못 합니다.");
					}
					else if(customer != null) {
						List<Invoice> listI = new ArrayList<Invoice>();
						listI = invoiceRepository.findByCustomer(customer.getId());
						Integer checks = 0;
						for(Invoice i : listI) {
							if(i.getTotalAmount() == null && i.getCustomer().getId() == Integer.valueOf(idCard.getText()) && i.getRoom().getId() == idRoom) {
								r.setIdCardCustomer(idCard.getText());
								roomRepo.update(r);
								list.clear();
								list = roomRepo.findAll();
								checks = 1;
								new SwitchToScene().switchToAddRoom(ev, SwitchToScene.listRoom);
							}
						}
						if(checks == 0) {
							Message.getMess("예약 정보 못 찾습니다");
						}
					}
					
					pop.hide();
				});
			});

			button.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
				BookingController.idRoom = idRoom;
				popOver.hide();
				new SwitchToScene().switchToAddRoom(ev, SwitchToScene.booking);
			});

			delete.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
				popOver.hide();
				try {
					roomRepo.delete(idRoom);
					NotificationWindow.showNotification("success", "객실 삭제 정공");
				} catch (Exception e) {
					Message.getMess("delete failure");
				}
			});
			showInforCustomer.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
				popOver.hide();
				Customer customer = customerRepository.findById(Integer.valueOf(rooms.getIdCardCustomer()));
				System.out.println("infor customer, room id_ :" + idRoom);
				PopOver pop = new PopOver();
				pop.setArrowLocation(ArrowLocation.TOP_CENTER);
				VBox infor = new VBox();
				infor.setPrefWidth(400);
				infor.setPrefHeight(400);
				Font font = new Font(20);
				Label label = new Label("customer id: "+ customer.getId());
				label.setFont(font);
				label.setTextFill(Color.BLACK);
				Label label1 = new Label("customer name: "+ customer.getName());
				label1.setFont(font);
				label1.setTextFill(Color.BLACK);
				Label label2 = new Label("customer nationality: "+ customer.getNationality());
				label2.setFont(font);
				label2.setTextFill(Color.BLACK);
				Label label3 = new Label("citizen identification number: "+ customer.getCitizenIdentificationNumber());
				label3.setFont(font);
				label3.setTextFill(Color.BLACK);
				infor.getChildren().add(label);
				infor.getChildren().add(label1);
				infor.getChildren().add(label2);
				infor.getChildren().add(label3);
				pop.setContentNode(infor);
				pop.show(node, -15);
			});
			
			checkout.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
				PayController.roomId = idRoom;
				popOver.hide();
				new SwitchToScene().switchToAddRoom(ev, SwitchToScene.pay);
			});
			
			addService.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
				AddServiceInvoice.roomId = idRoom;
				popOver.hide();
				new SwitchToScene().switchToAddRoom(ev, SwitchToScene.addService);
			});

		});
	}

	public void setNote() {
		File file = new File("image/room_not.png");
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Image img = new Image(is, 150, 80, false, true);
		not_empty_room.setImage(img);

		file = new File("image/room-active.png");
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		img = new Image(is, 150, 80, false, true);
		empty_room.setImage(img);
	}

	public void setInitListRoom() {
		if (list.size() == 0) {
			list = roomRepo.findAll();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		setInitListRoom();
		loadListRoom();
		setNote();
		listNumber.add("single room");
		listNumber.add("double room");
		listNumber.add("more than");
		numberOfPeople.setItems(listNumber);
	}

}
