package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import entity.DetailInvoice;
import entity.Invoice;
import entity.Room;
import entity.Services;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import repository.InvoiceRepository;
import service.Message;

public class ListInvoiceController extends FrameController {

	private InvoiceRepository invoiceRepo = new InvoiceRepository();
	
	
	
	private ObservableList<Invoice> listInvoices = FXCollections.observableArrayList();
	@FXML
	private TableView<Invoice> table;

	@FXML
	private TableColumn<Invoice, Integer> col_id;

	@FXML
	private TableColumn<Invoice, Timestamp> col_created_date;

	@FXML
	private TableColumn<Invoice, Double> col_fine;

	@FXML
	private TableColumn<Invoice, String> col_note;

	@FXML
	private TableColumn<Invoice, Timestamp> col_return;

	@FXML
	private TableColumn<Invoice, Double> col_total;

	@FXML
	private TableColumn<Invoice, String> col_created_by;

	@FXML
	private TableColumn<Invoice, String> col_customer;

	@FXML
	private TableColumn<Invoice, String> col_room;

	@FXML
	private TableColumn<Invoice, Timestamp> col_booking_date;

	@FXML
	private DatePicker startDate;

	@FXML
	private DatePicker endDate;

	@FXML
	
	//filter invoice
	void check(ActionEvent event) throws ParseException {
		String s = startDate.getValue().toString();
		String e = endDate.getValue().toString();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = dateFormat.parse(s);
		Timestamp start = new Timestamp(parsedDate.getTime());
		
		parsedDate = dateFormat.parse(e);
		Timestamp end = new Timestamp(parsedDate.getTime());
		if(start.getTime() == end.getTime() || start.after(end)) {
			Message.getMess("시작 날짜는 종료 날짜 이전이어야 합니다");
			return;
		}
		listInvoices.clear();
		invoiceRepo.sortByDate(start, end).forEach(p -> {
			listInvoices.add(p);
		});
		setInitTable();
	}

	@FXML
	void refresh(ActionEvent event) {
		listInvoices.clear();
		invoiceRepo.findAll().forEach(p -> {
			listInvoices.add(p);
		});
		setInitTable();
	}

	public void setInitTable() {
		table.setItems(listInvoices);
		col_customer
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getName()));

		col_room.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoom().getRoomName()));

		col_created_by.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getAccount().getUsername()));

		col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
		col_booking_date.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
		col_created_date.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
		col_fine.setCellValueFactory(new PropertyValueFactory<>("fine"));
		col_note.setCellValueFactory(new PropertyValueFactory<>("note"));
		col_return.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
		col_total.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
	}

	public void clickRow() {
		table.setRowFactory(tv -> {
			TableRow<Invoice> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(event.getButton() == MouseButton.SECONDARY) {
					List<DetailInvoice> list = invoiceRepo.findByInvoiceId(row.getItem().getId());
					PopOver popOver = new PopOver();
					popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
					VBox vBox = new VBox();
					vBox.setPrefHeight(50);
					vBox.setPrefWidth(200);
					Button delete = new Button("영수증 삭제");
					delete.setPrefWidth(200);
					delete.setPrefHeight(50);
					vBox.getChildren().add(delete);
					popOver.setContentNode(vBox);
					if(list.size()==0) {
						popOver.show((Node) event.getSource(), -15);
					}
					delete.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
						Room room = row.getItem().getRoom();
						System.out.println("===> room: "+room);
						room.setIdCardCustomer(null);
						invoiceRepo.delete(row.getItem().getId());
						listInvoices.clear();
						invoiceRepo.findAll().forEach(p->{
							listInvoices.add(p);
						});
						setInitTable();
					});
				}
			});
			return row;
		});
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		invoiceRepo.findAll().forEach(p -> {
			listInvoices.add(p);
		});
		setInitTable();
		clickRow();
	}
	

}
