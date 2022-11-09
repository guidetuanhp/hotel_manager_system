package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;



import entity.Customer;
import entity.DetailInvoice;
import entity.Invoice;
import entity.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import repository.CustomerRepository;
import repository.InvoiceRepository;
import repository.RoomRepository;
import service.NotificationWindow;
import service.Report;
import service.SwitchToScene;

public class PayController extends FrameController {

	public static Integer roomId = null;

	public Room room = null;
	private Customer customer = null;
	private RoomRepository roomRe = new RoomRepository();

	private InvoiceRepository invoiceRe = new InvoiceRepository();

	private CustomerRepository cusRepo = new CustomerRepository();

	private RoomRepository roomRepo = new RoomRepository();

	private Invoice invoiceG = null;

	@FXML
	private Label roomName;

	@FXML
	private Label roomStatus;

	@FXML
	private Label price;

	@FXML
	private Label capacity;

	@FXML
	private Label cus_name;

	@FXML
	private Label card;

	@FXML
	private Label bookingdate;

	@FXML
	private Label createdDate;

	@FXML
	private Label returndate;

	@FXML
	private Label actualreturndate;

	@FXML
	private Label overDay;

	@FXML
	private Label fine;

	@FXML
	private Label serviceFee;

	@FXML
	private Label totalAmount;

	@FXML
	private TextArea txtNote;

	@FXML
	void payment(ActionEvent event) {
		invoiceG.setActualReturnDate(new Timestamp(System.currentTimeMillis()));
		invoiceG.setFine(Double.valueOf(fine.getText()));
		invoiceG.setNote(txtNote.getText());
		invoiceG.setTotalAmount(Double.valueOf(totalAmount.getText()));
		Report.createBill(room, customer, invoiceRe.findByInvoiceId(invoiceG.getId()), invoiceG,
				Integer.valueOf(overDay.getText()), Double.valueOf(fine.getText()),
				Double.valueOf(totalAmount.getText()),
				"biil_" + customer.getName() + "_" + new Timestamp(System.currentTimeMillis()).toString().split(" ")[0] + ".pdf");
		NotificationWindow.showNotification("성공", "영수증 발생 " + customer.getName() + " 성공");
    	invoiceRe.update(invoiceG);
    	room.setIdCardCustomer(null);
    	roomRe.update(room);
    	RoomController.list = roomRepo.findAll();
    	new SwitchToScene().switchToAddRoom(event, SwitchToScene.listRoom);
	}

	public void setInit() {
		roomName.setText(room.getRoomName());
		roomStatus.setText("일반");
		price.setText(room.getPrice().toString());
		capacity.setText(room.getCapacity().toString());
		String now = new Timestamp(System.currentTimeMillis()).toString().split(" ")[0];
		actualreturndate.setText(now);
		cus_name.setText(customer.getName());
		card.setText(customer.getCitizenIdentificationNumber());
		System.out.println("=====> " + room.getIdCardCustomer());
		Invoice invoice = invoiceRe.findByRoomIdAndCardCustomer(room.getIdCardCustomer());
		invoiceG = invoice;
		bookingdate.setText(invoice.getBookingDate().toString());
		createdDate.setText(invoice.getCreatedDate().toString());
		returndate.setText(invoice.getReturnDate().toString());
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date parsedDate = dateFormat.parse(now);
			Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
			Long snq = timestamp.getTime() - invoice.getReturnDate().getTime();
			if (snq <= 0) {
				overDay.setText("0");
				fine.setText("0");
			} else {
				Long numDay = snq / 1000 / 60 / 60 / 24;
				overDay.setText(numDay.toString());
				fine.setText(String.valueOf(room.getPrice() * numDay));

			}
		} catch (Exception e) {
		}
		List<DetailInvoice> dt = invoiceRe.findByInvoiceId(invoice.getId());
		Double sum = 0D;
		for (DetailInvoice d : dt) {
			sum += d.getServices().getPrice();
		}
		serviceFee.setText(sum.toString());
		Long sts = invoice.getReturnDate().getTime() - invoice.getBookingDate().getTime();
		Long totalDay = sts / 1000 / 60 / 60 / 24;
		Double total = invoice.getPrice() * totalDay + sum + Double.valueOf(fine.getText());
		totalAmount.setText(total.toString());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		room = roomRe.findById(roomId);
		customer = cusRepo.findById(Integer.valueOf(room.getIdCardCustomer()));
		setInit();
	}

}
