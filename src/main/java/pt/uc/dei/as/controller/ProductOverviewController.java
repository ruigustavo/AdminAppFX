/*
 * Copyright 2017 (C) <University of Coimbra>
 * 
 * Created on : 15-02-2017
 * Author     : Bruno Cabral 
 */
package pt.uc.dei.as.controller;

import javax.persistence.TypedQuery;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import pt.uc.dei.as.AlertUtil;
import pt.uc.dei.as.MainApp;
import pt.uc.dei.as.entity.*;

import java.io.*;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductOverviewController.
 */
public class ProductOverviewController {
	
	/** The product table. */
	@FXML
	private TableView<Orders_Log> loggingTable;

	/** The code column. */
	@FXML
	private TableColumn<Orders_Log, String> idColumn;
	
	/** The description column. */
	@FXML
	private TableColumn<Orders_Log, String> nameColumn;
	
	/** The quantity column. */
	@FXML
	private TableColumn<Orders_Log, String> dateColumn;

	/** The trees radio button. */
	@FXML
	private RadioButton loginRadioButton;
	
	/** The shrubs radio button. */
	@FXML
	private RadioButton orderRadioButton;
	
	/** The seeds radio button. */
	@FXML
	private RadioButton shippingRadioButton;
	
	/** The toggle group. */
	@FXML
	private ToggleGroup toggleGroup = new ToggleGroup();

	/** The main app. */
	private MainApp mainApp;

	private DataOutputStream file = null;
	private DataOutputStream file2 = null;
	private DataOutputStream file3 = null;

	/**
	 * Instantiates a new product overview controller.
	 */
	public ProductOverviewController() {

	}

	/**
	 * Initialize.
	 */
	@FXML
	private void initialize() throws IOException {
		String fileName = "Orders_Log.txt";
		String fileName3 = "Login_log.txt";
		String fileName2 = "Shipping_Log.txt";

		try {
			file = new DataOutputStream(new FileOutputStream(fileName));
			file2 = new DataOutputStream(new FileOutputStream(fileName2));
			file3 = new DataOutputStream(new FileOutputStream(fileName3));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		loginRadioButton.setToggleGroup(toggleGroup);
		loginRadioButton.setUserData("Logins Log");
		orderRadioButton.setToggleGroup(toggleGroup);
		orderRadioButton.setUserData("Order Log");
		shippingRadioButton.setToggleGroup(toggleGroup);
		shippingRadioButton.setUserData("Shipping Log");
		toggleGroup.selectToggle(loginRadioButton);
		
		
		idColumn.setCellValueFactory(cellData -> cellData.getValue().id_OrdersProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().workers_NameProperty());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().orders_DateProperty());

/*
		loggingTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.DELETE)
					handleDelete();
				else if (event.getCode() == KeyCode.ENTER)
					handleEditProduct();
			}
		});

		loggingTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					handleEditProduct();
				}
			}
		});
		*/

		
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> obs, Toggle wasPreviouslySelected,
					Toggle isNowSelected) {
				handleRefresh();
			}
		});
		
		

	}

	/**
	 * Sets the main app.
	 *
	 * @param mainApp the new main app
	 */
	public void setMainApp(MainApp mainApp) throws IOException {
		this.mainApp = mainApp;
		writeToFiles();
		handleRefresh();
		loggingTable.setItems(mainApp.getOrdersData());
	}


	/**
	 * Handle new product.
	 */
	/*@FXML
	private void handleNewProduct() {
		Product newProduct = new Product();
		newProduct.setItems(new ArrayList<Item>());
		boolean saveClicked = mainApp.showProductEditorDialog(newProduct);
		if (saveClicked) {

			try {
				MainApp.em.getTransaction().begin();
				MainApp.em.persist(newProduct);
				MainApp.em.getTransaction().commit();
			} catch (Exception e) {
				AlertUtil.alert("Could not complete the operation", "Something is wrong!", "Try again or restart the application");
				handleRefresh();
				return;
			}
			switch(newProduct.getProductType().getProduct_Types_Code()) {
			case "Logins Log":
				toggleGroup.selectToggle(loginRadioButton);
				break;
			case "Order Log":
				toggleGroup.selectToggle(orderRadioButton);
				break;
			default:
				toggleGroup.selectToggle(shippingRadioButton);
			}
			handleRefresh();
		}
	}*/

	/**
	 * Handle edit product.
	 */
	/*@FXML
	private void handleEditProduct() {
		Product selectedProduct = loggingTable.getSelectionModel().getSelectedItem();
		if (selectedProduct != null) {
			boolean saveClicked = mainApp.showProductEditorDialog(selectedProduct);
			if (saveClicked) {
				try {
					MainApp.em.getTransaction().begin();
					selectedProduct = MainApp.em.merge(selectedProduct);
					MainApp.em.getTransaction().commit();
				} catch (Exception e) {
					AlertUtil.alert("Could not complete the operation", "Something is wrong!", "Try again or restart the application");
					handleRefresh();
					return;
				}
				switch(selectedProduct.getProductType().getProduct_Types_Code()) {
				case "Tree":
					toggleGroup.selectToggle(treesRadioButton);
					break;
				case "Shrub":
					toggleGroup.selectToggle(shrubsRadioButton);
					break;
				default:
					toggleGroup.selectToggle(seedsRadioButton);
				}
				handleRefresh();
			}

		} else {
			AlertUtil.alert("No selection", "Product not selected", "Please, select as product on the left table.");
		}
	}

*/
	/**
	 * Handle delete.
	 */
	/*@FXML
	private void handleDelete() {

		if (loggingTable.getItems() != null && loggingTable.getItems().size() > 0
				&& loggingTable.getSelectionModel().getSelectedItem() != null) {

			Product p = loggingTable.getSelectionModel().getSelectedItem();

			if (AlertUtil.askYesNoCancel("Delete Product?") == ButtonType.NO)
				return;
			mainApp.getProductsData().remove(loggingTable.getSelectionModel().getSelectedIndex());
			loggingTable.refresh();

			try {
				MainApp.em.getTransaction().begin();
				MainApp.em.remove(p);
				MainApp.em.getTransaction().commit();
			} catch (Exception e) {
				AlertUtil.alert("Could not complete the operation", "Something is wrong! Product might not be deletable at this time.", "Try again or check if the product has already be referenced in an order.");
				handleRefresh();			
			}
			return;
		}
		AlertUtil.alert("No selection", "Product not selected", "Please, select as product on the left table.");
	}
*/

	private void writeToFiles() throws IOException {

		TypedQuery<Orders_Log> queryP = MainApp.em.createNamedQuery("Order_Log.findAll",
				Orders_Log.class);
		mainApp.setOrdersData(queryP.getResultList());
		List <Orders_Log> ordersData = mainApp.getOrdersData();
		for(Orders_Log c :ordersData)
			file.writeChars( c.getIdLog() + "\t" +c.getIdOrders()+ "\t"+ c.getWorkers_Name() + "\t" + c.getOrders_Date() + "\n");
		loggingTable.setItems(mainApp.getOrdersData());

		TypedQuery<Shipping_Log> queryS = MainApp.em.createNamedQuery("Shipping_Log.findAll",
				Shipping_Log.class);
		List <Shipping_Log> shippingData = queryS.getResultList();
		for(Shipping_Log c :shippingData)
			file2.writeChars( c.getIdLog() + "\t" +c.getIdOrders()+ "\t"+ c.getWorkers_Name() + "\t" + c.getShipping_Date() + "\n");

		TypedQuery<Workers_Log> queryW = MainApp.em.createNamedQuery("Worker_log.findAll",
				Workers_Log.class);
		List <Workers_Log> workersData = queryW.getResultList();
		for(Workers_Log c : workersData){
			if(c.getCheck_point()==0)
				file3.writeChars( c.getIdLog() + "\t"+ c.getWorkers_Name() + "\t" + c.getIn_out_Date() + "\t"+ "Login"+ "\n");
			else
				file3.writeChars( c.getIdLog() + "\t"+ c.getWorkers_Name() + "\t" + c.getIn_out_Date() + "\t"+ "Logout" + "\n");
		}
	}

	/**
	 * Handle refresh.
	 */

	@FXML
	private void handleRefresh() {
		try {
			MainApp.refreshEm();
			String param = toggleGroup.getSelectedToggle().getUserData().toString();
			;
			if(param.compareTo("Order Log")==0){
				PrintWriter pw = new PrintWriter("Orders_Log.txt");
				pw.close();
				TypedQuery<Orders_Log> queryP = MainApp.em.createNamedQuery("Order_Log.findAll",
						Orders_Log.class);
				mainApp.setOrdersData(queryP.getResultList());
				List <Orders_Log> ordersData = mainApp.getOrdersData();
				for(Orders_Log c :ordersData)
				file.writeChars( c.getIdLog() + "\t" +c.getIdOrders()+ "\t"+ c.getWorkers_Name() + "\t" + c.getOrders_Date() + "\n");
				loggingTable.setItems(mainApp.getOrdersData());

			}
			else if(param.compareTo("Shipping Log")==0){
				PrintWriter pw = new PrintWriter("Shipping_Log.txt");
				pw.close();
				TypedQuery<Shipping_Log> queryP = MainApp.em.createNamedQuery("Shipping_Log.findAll",
						Shipping_Log.class);
				List <Shipping_Log> shippingData = queryP.getResultList();
				for(Shipping_Log c :shippingData)
					file2.writeChars( c.getIdLog() + "\t" +c.getIdOrders()+ "\t"+ c.getWorkers_Name() + "\t" + c.getShipping_Date() + "\n");
				loggingTable.setItems(mainApp.getOrdersData());
			}
			else if(param.compareTo("Logins Log")==0){
				PrintWriter pw = new PrintWriter("Login_Log.txt");
				pw.close();
				TypedQuery<Workers_Log> queryP = MainApp.em.createNamedQuery("Worker_log.findAll",
						Workers_Log.class);
				List <Workers_Log> workersData = queryP.getResultList();
				for(Workers_Log c : workersData){
					if(c.getCheck_point()==0)
					file3.writeChars( c.getIdLog() + "\t"+ c.getWorkers_Name() + "\t" + c.getIn_out_Date() + "\t"+ "Login"+ "\n");
					else
						file3.writeChars( c.getIdLog() + "\t"+ c.getWorkers_Name() + "\t" + c.getIn_out_Date() + "\t"+ "Logout" + "\n");
				}

				loggingTable.setItems(mainApp.getOrdersData());
			}

		} catch (Exception e) {
			AlertUtil.alert("Could not complete the operation", "Something is wrong!", "Try again or restart the application");
			MainApp.refreshEm();
		}
		loggingTable.refresh();
		return;
	}

}
