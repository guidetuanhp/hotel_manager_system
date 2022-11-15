package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import entity.Account;
import entity.Role;
import entity.Services;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import repository.AccountRepository;
import service.SwitchToScene;

public class AccountController extends FrameController{
	
	private ObservableList<Account> listAccount = FXCollections.observableArrayList();
	
	private ObservableList<String> listRole = FXCollections.observableArrayList();
	
	private AccountRepository accountRepository = new AccountRepository();

    @FXML
    private TableView<Account> table;

    @FXML
    private TableColumn<Account, String> col_username;

    @FXML
    private TableColumn<Account, String> col_fullname;

    @FXML
    private TableColumn<Account, String> col_phone;

    @FXML
    private TableColumn<Account, String> col_address;

    @FXML
    private TableColumn<Account, String> col_password;

    @FXML
    private TableColumn<Account, Integer> col_active;
    
    @FXML
    private TableColumn<Account, String> col_role;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_user;

    @FXML
    private TextField txt_pass;

    @FXML
    private TextField txt_addredd;

    @FXML
    private TextField txt_phone;
    
    @FXML
    private TextField txt_search;

    @FXML
    private CheckBox active;
    
    @FXML 
    private Label lb_editaccount;
    
    @FXML 
    private Button btn_editaccount;
    
    
    @FXML
    private ChoiceBox<String> choice_role;
    @FXML
	void refresh(ActionEvent event) {
    	SwitchToScene sw = new SwitchToScene();
		sw.switchToAddRoom(event, sw.account);
	}
    @FXML
    void addOrUpdate(ActionEvent event) {
    	Account account = new Account();
    	
    	if(!txt_id.getText().equals("")) {
    		account = accountRepository.findById(Integer.valueOf(txt_id.getText()));
    	}
    	account.setAddress(txt_addredd.getText());
    	account.setFullname(txt_name.getText());
    	account.setPassword(txt_pass.getText());
    	account.setPhone(txt_phone.getText());
    	account.setUsername(txt_user.getText());
    	if(active.isSelected()) {
    		account.setActived(1);
    	}else {
    		account.setActived(0);
    	}
    	if(!txt_id.getText().equals("")) {
    		Role role = account.getRoles().get(0);
    		role.setRoleName(choice_role.getValue());
    		accountRepository.update(account);
    		accountRepository.updateRole(role);
    		listAccount.clear();
    		accountRepository.findAll().forEach(p->{
    			listAccount.add(p);
    		});
    		setInitTable();
    	}
    	else {
    		accountRepository.save(account);
    		Role role = new Role();
    		role.setAccount(account);
    		role.setRoleName(choice_role.getValue());
    		accountRepository.saveRole(role);
    		listAccount.clear();
    		accountRepository.findAll().forEach(p->{
    			listAccount.add(p);
    		});
    		setInitTable();
    	}
    }

    @FXML
    void search(KeyEvent event) {
    	listAccount.clear();
    	accountRepository.search(txt_search.getText()).forEach(p->{
    		listAccount.add(p);
    	});
    	setInitTable();
    }

    public void setColum() {
		col_active.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_address.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		col_fullname.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_password.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_phone.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		col_username.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_role.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		
    }
    
    public void setInitTable() {
    	table.setItems(listAccount);
    	System.out.println(listAccount);
    	col_active.setCellValueFactory(new PropertyValueFactory<>("actived"));
    	col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
    	col_fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
    	col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
    	col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    	col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
    	col_role.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoles().get(0).getRoleName()));
    	
    }
    
    public void clickTable() {
    	table.setRowFactory(tv -> {
			TableRow<Account> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(event.getButton() == MouseButton.PRIMARY) {
					
					Account a = row.getItem();
					txt_id.setText(a.getId().toString());
					txt_addredd.setText(a.getAddress());
					txt_name.setText(a.getFullname());
					txt_pass.setText(a.getPassword());
					txt_phone.setText(a.getPhone());
					txt_user.setText(a.getUsername());
					choice_role.setValue(a.getRoles().get(0).getRoleName());
					if(a.getActived()==1) {
						active.setSelected(true);
					}
					else {
						active.setSelected(false);
					}
					lb_editaccount.setText("계정 수정");
					btn_editaccount.setText("수정");
					
				}
				else if(event.getButton() == MouseButton.SECONDARY) {
					PopOver popOver = new PopOver();
					popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
					VBox vBox = new VBox();
					vBox.setPrefHeight(100);
					vBox.setPrefWidth(200);
					String str = "";
					if(row.getItem().getActived()==1) {
						str = "계정 block";
					}
					else if(row.getItem().getActived()==0) {
						str = "계정 unblock";
					}
					Button update = new Button(str);
					update.setPrefWidth(200);
					update.setPrefHeight(50);
					
					Button delete = new Button("계정 삭제");
					delete.setPrefWidth(200);
					delete.setPrefHeight(50);
					
					vBox.getChildren().add(update);
					vBox.getChildren().add(delete);
					
					popOver.setContentNode(vBox);
					popOver.show((Node) event.getSource(), -15);
					
					delete.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
						accountRepository.delete(row.getItem().getId());
						listAccount.clear();
						accountRepository.findAll().forEach(p->{
							listAccount.add(p);
						});
						setInitTable();
					});
					update.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
						Account a = row.getItem();
						if(update.getText().equals("계정 block")) {
							a.setActived(0);
						}
						else {
							a.setActived(1);
						}
						accountRepository.update(a);
						listAccount.clear();
						accountRepository.findAll().forEach(p->{
							listAccount.add(p);
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
		setColum();
		accountRepository.findAll().forEach(p->{
			listAccount.add(p);
		});
		System.out.println(listAccount);
		setInitTable();
		clickTable();
		listRole.add("ADMIN");
		listRole.add("EMPLOYEE");
		choice_role.setItems(listRole);
		//btn_editaccount.setText("skjafsa");
	}

    
}
