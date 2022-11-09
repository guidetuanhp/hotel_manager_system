package controller;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import entity.Invoice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.InvoiceRepository;
import repository.RoomRepository;
import repository.ServiceRepository;

public class StatisticController extends FrameController {

	private RoomRepository roomRepo = new RoomRepository();

	private InvoiceRepository invoiceRepo = new InvoiceRepository();

	private CustomerRepository customerRepo = new CustomerRepository();

	private AccountRepository accountRepository = new AccountRepository();

	private ServiceRepository serviceRepos = new ServiceRepository();

	@FXML
	private HBox chart;

	@FXML
	private PieChart pieChart;

	@FXML
	private Label total_customer;

	@FXML
	private Label total_account;

	@FXML
	private Label total_services;

	@FXML
	private Label total_rentals;

	@FXML
	private Label total_amount;

	@FXML
	private Label total_over;

	public void setBarChart() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		int emptyRoom = roomRepo.findEmptyRoom().size();
		int en = roomRepo.findInhabitedRoom().size();
		int repair = roomRepo.findRepairRoom().size();
		ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
		pieData.add(new PieChart.Data("공방\n" + "(" + emptyRoom + ")", emptyRoom));
		pieData.add(new PieChart.Data("정리 객실\n" + "(" + repair + ")", repair));
		pieData.add(new PieChart.Data("사용 객실\n" + "(" + en + ")", en));
		pieChart.setData(pieData);
		pieChart.setPrefWidth(300);
		pieChart.setPrefHeight(300);
		setBarChart();
		total_account.setText(String.valueOf(accountRepository.findAll().size()));
		total_customer.setText(String.valueOf(customerRepo.findAll().size()));
		total_services.setText(String.valueOf(serviceRepos.findAll().size()));
		total_rentals.setText(String.valueOf(invoiceRepo.findThisMonth().size()));
		Double sum = 0D;
		for (Invoice i : invoiceRepo.findThisMonth()) {
			if (i.getTotalAmount() != null) {
				sum += i.getTotalAmount();
			}
		}

		total_amount.setText(sum.toString());
		total_over.setText(invoiceRepo.overdueThisMonth().toString());
		setLineChar();
	}

	public void setLineChar() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for (int i = 1; i <= month; i++) {
			Double amount = invoiceRepo.getAmountByMonth(i, year);
			System.out.println(i+"- "+amount);
			if(amount == null) {
				amount = 0D;
			}
			map.put(i, amount);
		}
		ObservableList<String> str = FXCollections.observableArrayList();
		XYChart.Series series = new XYChart.Series();
		for (Integer s : map.keySet()) {
			str.add(s.toString());
			series.getData().add(new XYChart.Data(s.toString(), map.get(s)));
		}
		CategoryAxis xAxis = new CategoryAxis(str);
		xAxis.setLabel("월");

		NumberAxis yAxis = new NumberAxis(0, getMax(map), getMax(map) / 10);
		yAxis.setLabel("수입");
		LineChart linechart = new LineChart(xAxis, yAxis);

		series.setName("월간 소득");
		linechart.getData().add(series);
		chart.getChildren().add(linechart);
	}
	
	public Double getMax(Map<Integer, Double> map) {
		Double max = 0D;
		for(Integer d : map.keySet()) {
			if(map.get(d) > max) {
				max = map.get(d);
			}
		}
		return max;
	}

}
