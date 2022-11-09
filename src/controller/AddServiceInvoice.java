package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import entity.DetailInvoice;
import entity.Invoice;
import entity.Room;
import entity.Services;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import repository.CustomerRepository;
import repository.InvoiceRepository;
import repository.RoomRepository;
import repository.ServiceRepository;
import service.Message;

public class AddServiceInvoice extends FrameController {

	@FXML
	private TableView<Services> table;

	@FXML
	private TableColumn<Services, Integer> col_id;

	@FXML
	private TableColumn<Services, String> col_name;

	@FXML
	private TableColumn<Services, Double> col_price;

	@FXML
	private TableColumn<Services, String> col_type;

	@FXML
	private Label roomName;

	@FXML
	private Label customer;

	@FXML
	private VBox vbox_listService;

	private ObservableList<Services> listService = FXCollections.observableArrayList();

	private ServiceRepository serviceRepos = new ServiceRepository();

	private InvoiceRepository invoiceRe = new InvoiceRepository();

	private CustomerRepository cusRepo = new CustomerRepository();

	private RoomRepository roomRe = new RoomRepository();

	private Services services = null;

	public static Integer roomId = null;

	public Room room = null;

	private Invoice invoice = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		setInitTable();
		clickMainTable();
		Room room = roomRe.findById(roomId);
		invoice = invoiceRe.findByRoomIdAndCardCustomer(room.getIdCardCustomer());
		System.out.println(invoice);
		roomName.setText(room.getRoomName());
		customer.setText(invoice.getCustomer().getName());
		setInitList();
	}
	// add service to used room 
	public void setInitList() {
		vbox_listService.getChildren().clear();
		Room room = roomRe.findById(roomId);
		Invoice i = invoiceRe.findByRoomIdAndCardCustomer(room.getIdCardCustomer());
		for (DetailInvoice d : invoiceRe.findByInvoiceId(invoice.getId())) {
			String ser = "서비스: " + d.getServices().getName() + ", 가격: " + d.getServices().getPrice()
					+ ", 시간: " + d.getCreatedDate();
			Label label = new Label(ser);
			vbox_listService.getChildren().add(label);
		}
	}

	public void setInitTable() {
		for (Services s : serviceRepos.findAll()) {
			listService.add(s);
		}
		table.setItems(listService);
		col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
		col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
		col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
	}

	public void clickMainTable() {
		table.setRowFactory(tv -> {
			TableRow<Services> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.SECONDARY) {
					PopOver popOver = new PopOver();
					popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
					VBox vBox = new VBox();
					vBox.setPrefHeight(100);
					vBox.setPrefWidth(200);
					Button update = new Button("서비스 추가");
					update.setPrefWidth(200);
					update.setPrefHeight(50);
					vBox.getChildren().add(update);
					popOver.setContentNode(vBox);
					popOver.show((Node) event.getSource(), -15);
					update.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
						services = row.getItem();
						System.out.println(services);
						DetailInvoice detailInvoice = new DetailInvoice();
						detailInvoice.setInvoice(invoice);
						detailInvoice.setServices(services);
						detailInvoice.setCreatedDate(new Timestamp(System.currentTimeMillis()));

						invoiceRe.saveDetail(detailInvoice);
						if (detailInvoice.getId() != null) {
							Message.getMess("서비스 추가 성공");
							setInitList();
						}
					});
				}
			});
			return row;
		});
	}

}
