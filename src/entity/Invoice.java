package entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Timestamp createdDate;
	
	private Timestamp returnDate;
	
	private Double price;
	
	private Double fine;
	
	private Double totalAmount;
	
	private String note;
	
	private Timestamp bookingDate;
	
	private Timestamp actualReturnDate;
	
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "invoice")
	private List<DetailInvoice> detailInvoices = new ArrayList<DetailInvoice>();

	public Invoice() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Timestamp returnDate) {
		this.returnDate = returnDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Double getFine() {
		return fine;
	}

	public void setFine(Double fine) {
		this.fine = fine;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Timestamp getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Timestamp bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Timestamp getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(Timestamp actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public List<DetailInvoice> getDetailInvoices() {
		return detailInvoices;
	}

	public void setDetailInvoices(List<DetailInvoice> detailInvoices) {
		this.detailInvoices = detailInvoices;
	}

	@Override
	public String toString() {
		return "Invoice [id=" + id + ", createdDate=" + createdDate + ", returnDate=" + returnDate + ", price=" + price
				+ ", fine=" + fine + ", totalAmount=" + totalAmount + ", note=" + note + ", bookingDate=" + bookingDate
				+ ", actualReturnDate=" + actualReturnDate + "]";
	}
	
	
}
