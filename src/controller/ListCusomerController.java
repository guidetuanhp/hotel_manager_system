package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import entity.Customer;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import repository.CustomerRepository;

public class ListCusomerController extends FrameController{
	
	private CustomerRepository customerRepository = new CustomerRepository();

	private ObservableList<Customer> listCustomers = FXCollections.observableArrayList();
	
    @FXML
    private TableView<Customer> table;

    @FXML
    private TableColumn<Customer, Integer> col_id;

    @FXML
    private TableColumn<Customer, String> col_name;

    @FXML
    private TableColumn<Customer, String> col_card;

    @FXML
    private TableColumn<Customer, Timestamp> col_date;

    @FXML
    private TableColumn<Customer, String> col_national;
    @FXML
    private TableColumn<Customer, String> col_room;
    
    @FXML
    private ChoiceBox<Integer> choice_month;

    @FXML
    private ChoiceBox<Integer> choice_year;

    @FXML
    private TextField txt_search;

    @FXML
    void btn_search(ActionEvent event) {
    	
    	listCustomers.clear();
    	customerRepository.search(choice_month.getValue(), choice_year.getValue()).forEach(p->{
    		listCustomers.add(p);
    	});
    	setInitTable();
    }
    
    @FXML
    void search(KeyEvent event) {
    	listCustomers.clear();
    	if(txt_search.getText().equals("")) {
    		customerRepository.findAll().forEach(p->{
    			listCustomers.add(p);
    		});
    	}
    	else {
    		customerRepository.search(txt_search.getText()).forEach(p->{
    			listCustomers.add(p);
    		});
    	}
    	setInitTable();
    }
    
  
    public void setInitTable() {
    	table.setItems(listCustomers);
    	System.out.println(listCustomers);
    	col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    	col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
    	col_card.setCellValueFactory(new PropertyValueFactory<>("citizenIdentificationNumber"));
    	col_date.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
    	col_national.setCellValueFactory(new PropertyValueFactory<>("nationality"));
    	

    	col_room.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInvoices().get(0).getRoom().getRoomName()));
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		customerRepository.findAll().forEach(p->{
			listCustomers.add(p);
		});
		System.out.println(listCustomers);
		
    	setInitTable();
    	
    	col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_name.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_card.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_national.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_date.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		col_room.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		setValueChoiceBox();
	}

	public void setValueChoiceBox() {
		ObservableList<Integer> month = FXCollections.observableArrayList();
		ObservableList<Integer> year = FXCollections.observableArrayList();
		
		for(int i=1; i<13; i++) {
			month.add(i);
		}
		for(int i=2022; i<2040; i++) {
			year.add(i);
		}
		choice_month.setItems(month);
		choice_year.setItems(year);
	}
    
}
