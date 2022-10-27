package entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private String citizenIdentificationNumber;
	
	private Timestamp createdDate;
	
	private String nationality;
	
	@OneToMany(mappedBy = "customer")
	private List<Invoice> invoices = new ArrayList<Invoice>();

	public Customer() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCitizenIdentificationNumber() {
		return citizenIdentificationNumber;
	}

	public void setCitizenIdentificationNumber(String citizenIdentificationNumber) {
		this.citizenIdentificationNumber = citizenIdentificationNumber;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", citizenIdentificationNumber=" + citizenIdentificationNumber
				+ ", createdDate=" + createdDate + ", nationality=" + nationality + "]";
	}

	
	
}
