package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
    	col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    	col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
    	col_card.setCellValueFactory(new PropertyValueFactory<>("citizenIdentificationNumber"));
    	col_date.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
    	col_national.setCellValueFactory(new PropertyValueFactory<>("nationality"));
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		customerRepository.findAll().forEach(p->{
			listCustomers.add(p);
		});
    	setInitTable();
    	col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_name.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		col_card.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
		col_national.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		col_date.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
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
