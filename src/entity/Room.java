package entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "room")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String roomName;
	
	private String roomStatus;
	
	private Integer actived;
	
	private Integer capacity;
	
	private String linkImage;
	
	private Double price;
	
	private String idCardCustomer;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
	private List<Invoice> invoices = new ArrayList<Invoice>();

	public Room() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	public Integer getActived() {
		return actived;
	}

	public void setActived(Integer actived) {
		this.actived = actived;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getLinkImage() {
		return linkImage;
	}

	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public String getIdCardCustomer() {
		return idCardCustomer;
	}

	public void setIdCardCustomer(String idCardCustomer) {
		this.idCardCustomer = idCardCustomer;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", roomName=" + roomName + ", roomStatus=" + roomStatus + ", actived=" + actived
				+ ", capacity=" + capacity + ", linkImage=" + linkImage + ", price=" + price + "]";
	}
	
	
}
