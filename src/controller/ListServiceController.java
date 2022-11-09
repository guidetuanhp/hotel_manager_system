package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import entity.Services;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import repository.ServiceRepository;
import service.Message;

public class ListServiceController extends FrameController {
	
	private ObservableList<String> allType = FXCollections.observableArrayList();
	
	private ObservableList<Services> listService = FXCollections.observableArrayList();
	
	private ServiceRepository serviceRepos = new ServiceRepository();
	
	private Integer id = null;

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
    private VBox form_addService;

    @FXML
    private TextField txt_serviceName;

    @FXML
    private TextField txt_price;

    @FXML
    private ChoiceBox<String> txt_type;
    
    @FXML
    private TextField txt_search;
    
    @FXML
    private Label cancel;
    
    @FXML
    void addOrUpdate(ActionEvent event) {
    	Services services = new Services();
    	if(id != null) {
    		services.setId(id);
    	}
    	services.setName(txt_serviceName.getText());
    	services.setPrice(Double.valueOf(txt_price.getText()));
    	services.setType(txt_type.getValue());
    	if(id != null) {
    		services.setId(id);
    		try {
				serviceRepos.update(services);
    			Message.getMess("수정 성공");
			} catch (Exception e) {
				Message.getMess("수정 실패");
			}
    	}
    	else {
    		try {
				serviceRepos.save(services);
    			Message.getMess("추가 성공");
			} catch (Exception e) {
				Message.getMess("추가 실패");
			}
    	}
    	listService.clear();
    	for(Services s : serviceRepos.findAll()) {
			listService.add(s);
		}
		setInitTable();
    	id = null;
    	txt_price.setText("");
    	txt_serviceName.setText("");
    	
    }
    
    @FXML
    void search(KeyEvent event) {
    	if(!txt_search.getText().equals("")) {
    		listService.clear();
    		serviceRepos.search(txt_search.getText()).forEach(p->{
    			listService.add(p);
    		});;
    		setInitTable();
    	}
    	else {
    		listService.clear();
    		serviceRepos.findAll().forEach(p->{
    			listService.add(p);
    		});;
    		setInitTable();
    	}
    }

    public void setInitTable() {
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
				if(event.getButton() == MouseButton.SECONDARY) {
					PopOver popOver = new PopOver();
					popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
					VBox vBox = new VBox();
					vBox.setPrefHeight(100);
					vBox.setPrefWidth(200);
					Button update = new Button("수정하기");
					update.setPrefWidth(200);
					update.setPrefHeight(50);
					Button delete = new Button("삭제하기");
					delete.setPrefWidth(200);
					delete.setPrefHeight(50);
					vBox.getChildren().add(update);
					vBox.getChildren().add(delete);
					popOver.setContentNode(vBox);
					popOver.show((Node) event.getSource(), -15);
					
					update.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
						Services s = row.getItem();
						txt_price.setText(s.getPrice().toString());
						txt_serviceName.setText(s.getName());
						txt_type.setValue(s.getType());
						id = s.getId();
						cancel.setVisible(true);
					});
					delete.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
						Services s = row.getItem();
						serviceRepos.delete(s.getId());
						listService.clear();
						serviceRepos.findAll().forEach(p->{
							listService.add(p);
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
		col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		col_name.prefWidthProperty().bind(table.widthProperty().multiply(0.5));
		col_price.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		col_type.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		allType.add("dish");
		allType.add("drink");
		allType.add("others");
		txt_type.setItems(allType);
		for(Services s : serviceRepos.findAll()) {
			listService.add(s);
		}
		setInitTable();
		clickMainTable();
		cancel.setVisible(false);
		
		cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, even -> {
			cancel.setVisible(false);
			id = null;
		});
	}

}
