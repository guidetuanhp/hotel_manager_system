package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import entity.Account;
import entity.Customer;
import entity.Invoice;
import entity.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import repository.CustomerRepository;
import repository.InvoiceRepository;
import repository.RoomRepository;
import service.Contains;
import service.Message;
import service.SwitchToScene;

public class BookingController extends FrameController {

	public static Integer idRoom = null;

	private RoomRepository roomRepo = new RoomRepository();

	private InvoiceRepository invoiceRepo = new InvoiceRepository();

	private CustomerRepository customerRepo = new CustomerRepository();

	private List<Invoice> listInvoice = new ArrayList<Invoice>();

	private Room roomP = null;

	@FXML
	private Label roomName;

	@FXML
	private Label roomStatus;

	@FXML
	private Label price;

	@FXML
	private Label capacity;

	@FXML
	private TextField txt_name;

	@FXML
	private TextField txt_iden_number;

	@FXML
	private TextField txt_national;

	@FXML
	private HBox hbox_date_start;

	@FXML
	private DatePicker date_start;

	@FXML
	private HBox hBox_date_end;

	@FXML
	private DatePicker date_end;

	@FXML
	private Button btn_checkTime;
	


	@FXML
	private ImageView room_image;

	@FXML
	private VBox vBox_List;

	@FXML
	
	void btn_search(ActionEvent event) {
		RoomController.list = roomRepo.findAll();
		SwitchToScene sw = new SwitchToScene();
		sw.switchToAddRoom(event, sw.listRoom);
	    	
	    }
	// check in
	@FXML
	void CheckTime(ActionEvent event) throws ParseException {
		String starts = date_start.getValue().toString();
		String ends = date_end.getValue().toString();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = dateFormat.parse(starts);
		Timestamp start = new java.sql.Timestamp(parsedDate.getTime());
		parsedDate = dateFormat.parse(ends);
		Timestamp end = new java.sql.Timestamp(parsedDate.getTime());
		
		
        String currents = dateFormat.format(new Date());
        parsedDate = dateFormat.parse(currents);
		Timestamp current = new java.sql.Timestamp(parsedDate.getTime());
		
		if(start.before(current)) {
			Message.getMess("시작 날짜 오류!");
		}
		else if (start.after(end) || start.equals(end)) {
			Message.getMess("시작 날짜는 종료 날짜 이후일 수 없습니다.");
		} 
		else {
			for (Invoice i : listInvoice) {
				if (((start.after(i.getBookingDate()) || start.equals(i.getBookingDate()))
						&& (start.before(i.getReturnDate()) || start.equals(i.getReturnDate())))
						|| (start.before(i.getBookingDate())
								&& (end.after(i.getBookingDate()) || end.equals(i.getBookingDate())))) {
					Message.getMess(
							"일정 기간 동안 예약되었습니다: " + i.getBookingDate() + " - " + i.getReturnDate());
					return;
				}
			}
			Customer customer = new Customer();
			customer.setCitizenIdentificationNumber(txt_iden_number.getText());
			customer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			customer.setName(txt_name.getText());
			customer.setNationality(txt_national.getText());
			customerRepo.save(customer);
			Invoice invoice = new Invoice();
			invoice.setAccount(Contains.account);
			invoice.setBookingDate(start);
			invoice.setReturnDate(end);
			invoice.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			invoice.setCustomer(customer);
			invoice.setPrice(roomP.getPrice());
			invoice.setRoom(roomP);
			invoiceRepo.save(invoice);
			if (invoice.getId() != null) {
				Message.getMess("체크인/예약 성공!");
				setInitListInvoice();
				if(checkNow(invoice)) {
					roomP.setIdCardCustomer(customer.getId().toString());
					roomRepo.update(roomP);
				}
			}

		}
	}

	public boolean checkNow(Invoice i) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (i.getBookingDate().getDate() <= now.getDate() && i.getBookingDate().getMonth() == now.getMonth()
				&& i.getBookingDate().getYear() == now.getYear() && i.getReturnDate().getDate() >= now.getDate()) {
			return true;
		}
		return false;
	}
	//update room info
	public void setInitRoom() {
		Room room = roomRepo.findById(idRoom);
		roomP = room;
		roomName.setText(room.getRoomName());
		price.setText(room.getPrice().toString());
		room_image.setImage(new Image(room.getLinkImage()));
		if (room.getIdCardCustomer() == null) {
			roomStatus.setText("공실");
			roomStatus.setTextFill(Color.GREEN);
		} else {
			roomStatus.setText("사용중");
			roomStatus.setTextFill(Color.RED);
		}
		capacity.setText(room.getCapacity().toString() + " 인");
	}
	//update list check in

	public void setInitListInvoice() {
		for (int i = 1; i < vBox_List.getChildren().size(); i++) {
			vBox_List.getChildren().remove(i);
			--i;
		}
		listInvoice = invoiceRepo.findReserveInvoice(idRoom);
		System.out.println("list invoice: ");
		System.out.println(listInvoice);

		for (Invoice i : listInvoice) {
			// if time is future =>> wait customer coming
			if (checkReserverTime(i)) {
				String info =  "접수 번호: " + i.getCustomer().getId()+ ", 고객: " + i.getCustomer().getName() + ", 체크인 일: " + i.getBookingDate()
				+ ", 체크아웃 일: " + i.getReturnDate();
				Label label = new Label(info);
				vBox_List.getChildren().add(label);
				vBox_List.setMargin(label, new Insets(10, 0, 0, 10));
				
			}
			//if time is past =>> don't show customer in list
			else {
				System.out.println("객실 안 온다.");
				
			}
			
		}
	}
	// check invoice customer not coming
	@SuppressWarnings("deprecation")
	public boolean checkReserverTime(Invoice i) {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		if (i.getReturnDate().getDate() > now.getDate() && i.getReturnDate().getMonth() == now.getMonth() || i.getReturnDate().getDate() < now.getDate() && i.getReturnDate().getMonth() >= now.getMonth()) {
			
			return true;
		}
		return false;
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		setInitRoom();
		setInitListInvoice();
	}

}
