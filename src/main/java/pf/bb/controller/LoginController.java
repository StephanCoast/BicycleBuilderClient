package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Label;
import pf.bb.Main;
import pf.bb.model.*;
import pf.bb.task.*;
import java.io.IOException;

public class LoginController {

    ViewManager vm = ViewManager.getInstance();
    public static User activeUser; // user who is currently logged in
    @FXML
    public JFXTextField username;
    @FXML
    public JFXPasswordField password;
    @FXML
    public Label loginFailure;

    ValidatorManager validatorManager = ValidatorManager.getInstance();
    public RequiredFieldValidator validatorName;
    public RequiredFieldValidator validatorPW;


    @FXML
    public void initialize() {
        validatorManager.initTextValidators(username, validatorName);
        validatorManager.initPasswordValidators(password, validatorPW);

    }

    public void authenticate(ActionEvent event) throws IOException {
        // todo: nach stephanLogin() verschieben beim erfolgreichen Login
        //vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
        // Stephan loginTask - zur Trennung
        stephanLogin(event);
    }


    //
    private void stephanLogin(ActionEvent event) {
        // alles was du in Login machen möchtest bitte hier rein
        // benenne die methode wie du willst

        PostLoginTask loginTask = new PostLoginTask(username.getText(), password.getText());

        loginTask.setOnFailed((WorkerStateEvent loginFailed) -> {
            System.out.println("Der Server ist nicht erreichbar!");
            loginFailure.setText("Der Server ist nicht erreichbar!");
            loginFailure.setVisible(true);
        });

        loginTask.setOnSucceeded((WorkerStateEvent e2) -> {
            activeUser = loginTask.getValue();
            if (activeUser == null) {
                // login failed -> Show somehow in FXML
                loginFailure.setVisible(true);
            } else {

                GetUserDetailsTask userDetailsTask = new GetUserDetailsTask(LoginController.activeUser);
                    userDetailsTask.setOnSucceeded((WorkerStateEvent e5) -> {
                        activeUser.id = userDetailsTask.getValue().id;


                        // Todo Remove before production - TEST Configuration Api
                        testConfigurationApi();


                        try {
                            vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });
                new Thread(userDetailsTask).start();
            }
        });
        new Thread(loginTask).start();
    }


    // todo: eventuell für fehlerhaften Login ebenfalls eine Alert-Box
    public void close() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Bicycle Builder");
        alert.setHeaderText("Möchten Sie die Anwendung schließen?");
        alert.setContentText(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }

    private void initValidators(JFXTextField textField, JFXPasswordField passwordField) {
        textField.getValidators().add(validatorName);
        passwordField.getValidators().add(validatorPW);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) { textField.validate(); }
            }
        });
        passwordField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) { passwordField.validate(); }
            }
        });
    }


    public static void testConfigurationApi() {

        // USER SUCCESSFULLY LOGGED IN -> ACCESS TO GET, POST, PUT, DELETE
        // For creating "unique" keys
        int randInt = (int)(Math.random()*10000);


        // Test CREATE USER - ONLY USER WITH ADMIN ROLL IS ALLOWED
        User newUser = new User("Zweiradler" + randInt, "osmi", "zweiradler" + randInt + "@bikeshop.de", "Armin", "Admin", "CONSULTANT");
        PostUserTask userTask1 = new PostUserTask(activeUser, newUser);
        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
        userTask1.setOnSucceeded((WorkerStateEvent userCreated) -> {
            if(userTask1.getValue() != null) {
                System.out.println("user id=" + userTask1.getValue().id + " created");


                // TEST UPDATE OF USER - ONLY USER WITH ADMIN ROLL IS ALLOWED
                User updatedUser = userTask1.getValue();
                updatedUser.id = userTask1.getValue().id;
                updatedUser.lastname = "Updated";
                PutUserTask userTask2 = new PutUserTask(activeUser, updatedUser, userTask1.getValue().id);
                //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                userTask2.setOnSucceeded((WorkerStateEvent userUpdated) -> {
                    if (userTask2.getValue() != null)
                        System.out.println("user id=" + userTask2.getValue().id + " updated");
                    else
                        System.out.println("user update failed for: " + updatedUser.id + " result: " + userTask2.getMessage());
                });
                userTask2.setOnFailed((WorkerStateEvent userUpdatedFailed) -> System.out.println("user update failed for: " + updatedUser.id + " result: " + userTask2.getMessage()));
                //Tasks in eigenem Thread ausführen
                new Thread(userTask2).start();

            }
            else {
                System.out.println("user creation failed for: " + newUser.id + " result: " + userTask1.getMessage());
            }
        });
        userTask1.setOnFailed((WorkerStateEvent userCreatedFailed) -> System.out.println("user creation failed for: " + newUser.id + " result: " + userTask1.getMessage()));
        //Tasks in eigenem Thread ausführen
        new Thread(userTask1).start();


        // Test CREATE

        int off = 1; // Local list index starts at 0, Db entity Ids with 1
        // Example data
        Configuration config1 = new Configuration(activeUser);
        config1.articles.add(Main.ARTICLES.get(2 - off));
        config1.articles.add(Main.ARTICLES.get(5 - off));
        config1.articles.add(Main.ARTICLES.get(7 - off));
        config1.articles.add(Main.ARTICLES.get(9 - off));

        // Test POST
        PostConfigurationTask configurationsTask1 = new PostConfigurationTask(activeUser, config1);
        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
        configurationsTask1.setOnRunning((successEvent) -> System.out.println("trying to save configuration..."));
        configurationsTask1.setOnSucceeded((WorkerStateEvent e1) -> {
            System.out.println("configurations saved. id=" + configurationsTask1.getValue());
            Configuration createdConfiguration = configurationsTask1.getValue();
            System.out.println("Created Configuration:" + createdConfiguration.toString());


            // TEST GET
            // query all configurations from REST API with Task Thread
            GetConfigurationsTask configurationsTask2 = new GetConfigurationsTask(activeUser);
            //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
            configurationsTask2.setOnRunning((successEvent) -> System.out.println("loading  configurations..."));
            configurationsTask2.setOnSucceeded((WorkerStateEvent e2) -> {
                System.out.println("configurations loaded.");
                Main.CONFIGURATIONS.addAll(configurationsTask2.getValue());

//                Main.CONFIGURATIONS.forEach(configuration -> {
//                    System.out.println(configuration.id + ": " + configuration.dateCreated + " - " + configuration.user.name);
//                });

                // Test PUT
                if (Main.CONFIGURATIONS.size() > 1) {

                    // Second to last set to "ABGESCHLOSSEN"
                    int localConfigID = Main.CONFIGURATIONS.size()-2;
                    int oldConfigId = Main.CONFIGURATIONS.get(localConfigID).id;
                    Configuration updatedConfig = Main.CONFIGURATIONS.get(localConfigID);
                    updatedConfig.status = Configuration.stats[1]; // ABGESCHLOSSEN



                    // Test PUT - Update Configuration
                    PutConfigurationTask configurationsTask3 = new PutConfigurationTask(activeUser, updatedConfig, oldConfigId);
                    //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                    configurationsTask3.setOnRunning((successEvent) -> System.out.println("trying to update configuration..."));
                    configurationsTask3.setOnSucceeded((WorkerStateEvent e3) -> {
                        System.out.println("configuration updated id: " + configurationsTask3.getValue().id);





                        // Test PUT - Update Configuration again
                        PutConfigurationWriteAccessTask writeAccessTask1 = new PutConfigurationWriteAccessTask(activeUser, oldConfigId);
                        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                        writeAccessTask1.setOnRunning((runningEvent) -> System.out.println("trying to get writeAccess for configuration..."));
                        writeAccessTask1.setOnSucceeded((WorkerStateEvent writeAccess) -> {
                            System.out.println("writeAccess for configuration: " + writeAccessTask1.getValue());



                            // Test PUT - Update Configuration
                            PutConfigurationTask configurationsTask4 = new PutConfigurationTask(activeUser, updatedConfig, oldConfigId);
                            //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                            configurationsTask4.setOnRunning((successEvent) -> System.out.println("trying to update configuration..."));
                            configurationsTask4.setOnSucceeded((WorkerStateEvent configTask4) -> {
                                System.out.println("configuration updated id: " + configurationsTask4.getValue().id);




                                // Order mit Kundendaten und Gesamtpreis zur Konfiguration hinzufügen
                                Customer newCustomer = new Customer("customer" + randInt + "@shopping.de", "Caro", "Customer", "Mallway", 17, "D-32423", "Mallland");
                                // Customer in DB erzeugen, um unique ID zu bekommen
                                PostCustomerTask customerTask1 = new PostCustomerTask(activeUser, newCustomer);
                                customerTask1.setOnRunning((runningEvent) -> System.out.println("trying to create customer..."));
                                customerTask1.setOnFailed((WorkerStateEvent createUserFailed) -> System.out.println("creating customer failed..."));
                                //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                                customerTask1.setOnSucceeded((WorkerStateEvent customerCreated) -> {
                                    System.out.println("customer created id:" + customerTask1.getValue().id);


                                    // Order in DB erzeugen
                                    // Calculate total price
                                    float priceTotal = 0;
                                    for (Article article : updatedConfig.articles) {
                                        priceTotal += article.price;
                                    }
                                    OrderClass newOrder = new OrderClass(configurationsTask3.getValue(), customerTask1.getValue(), priceTotal);

                                    PostOrderTask orderTask1 = new PostOrderTask(activeUser, newOrder);
                                    //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                                    orderTask1.setOnSucceeded((WorkerStateEvent orderCreated) -> {
                                        System.out.println("order created id:" + orderTask1.getValue().id);


                                        // CREATE BILL
                                        Bill newBill = new Bill(orderTask1.getValue());
                                        PostBillTask billTask1 = new PostBillTask(activeUser, newBill);
                                        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                                        billTask1.setOnSucceeded((WorkerStateEvent billCreated) -> {
                                            System.out.println("bill created id:" + billTask1.getValue().id);



                                            // DELETE A CONFIGURATION REQUIRES: DELETE BILL -> DELETE ORDER -> DELETE CONFIGURATION

                                            // Test DELETE BILL
                                            if (Main.CONFIGURATIONS.size() > 2) {
                                                int billId = Main.CONFIGURATIONS.get(0).order.bill.id; // DELETE OLDEST ELEMENT
                                                DeleteBillTask billTask2 = new DeleteBillTask(activeUser, billId);
                                                //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                                                billTask2.setOnSucceeded((WorkerStateEvent billDeleted) -> {
                                                    System.out.println("bill id=" + billId + " deleted=" +  billTask2.getValue());

                                                    // Test DELETE ORDER
                                                    int orderId = Main.CONFIGURATIONS.get(0).order.id; // DELETE OLDEST ELEMENT
                                                    DeleteOrderTask orderTask2 = new DeleteOrderTask(activeUser, orderId);
                                                    //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                                                    orderTask2.setOnSucceeded((WorkerStateEvent orderDeleted) -> {
                                                        System.out.println("order id=" + orderId + " deleted="  +  orderTask2.getValue());

                                                        // Test DELETE CONFIGURATION
                                                        int configId = Main.CONFIGURATIONS.get(0).id; // DELETE OLDEST ELEMENT
                                                        DeleteConfigurationTask configTask1 = new DeleteConfigurationTask(activeUser, configId);
                                                        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                                                        configTask1.setOnSucceeded((WorkerStateEvent configDeleted) -> {
                                                            System.out.println("configuration id=" + configId + " deleted="  + configTask1.getValue());

                                                            // FINISH TEST

                                                        });
                                                        //Tasks in eigenem Thread ausführen
                                                        new Thread(configTask1).start();

                                                    });
                                                    //Tasks in eigenem Thread ausführen
                                                    new Thread(orderTask2).start();

                                                });
                                                //Tasks in eigenem Thread ausführen
                                                new Thread(billTask2).start();
                                            }

                                        });
                                        //Tasks in eigenem Thread ausführen
                                        new Thread(billTask1).start();

                                    });
                                    //Tasks in eigenem Thread ausführen
                                    new Thread(orderTask1).start();
                                });
                                //Tasks in eigenem Thread ausführen
                                new Thread(customerTask1).start();

                            });
                            //Tasks in eigenem Thread ausführen
                            new Thread(configurationsTask4).start();


                        });
                        //Tasks in eigenem Thread ausführen
                        new Thread(writeAccessTask1).start();


                    });
                    configurationsTask3.setOnFailed((WorkerStateEvent e31) -> System.out.println("updating configuration failed" + configurationsTask3.getException()));
                    //Tasks in eigenem Thread ausführen
                    new Thread(configurationsTask3).start();
                }

            });
            //Tasks in eigenem Thread ausführen
            new Thread(configurationsTask2).start();


        });
        configurationsTask1.setOnFailed((WorkerStateEvent e11) -> System.out.println("saving failed" + configurationsTask1.getException()));
        new Thread(configurationsTask1).start();


    }




}