package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import pf.bb.Main;
import pf.bb.model.Configuration;
import pf.bb.model.User;
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
        loginTask.setOnSucceeded((WorkerStateEvent e2) -> {
            activeUser = loginTask.getValue();
            if (activeUser == null) {
                // login failed
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


    public static void testConfigurationApi() {

        // USER SUCCESSFULLY LOGGED IN -> ACCESS TO GET, POST, PUT, DELETE

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
        configurationsTask1.setOnRunning((successEvent) -> {
            System.out.println("trying to save configuration...");
        });
        configurationsTask1.setOnSucceeded((WorkerStateEvent e1) -> {
            System.out.println("configurations saved. id=" + configurationsTask1.getValue());
            Configuration createdConfiguration = configurationsTask1.getValue();
            System.out.println("Created Configuration:" + createdConfiguration.toString());


            // TEST GET
            // query all configurations from REST API with Task Thread
            GetConfigurationsTask configurationsTask2 = new GetConfigurationsTask(activeUser);
            //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
            configurationsTask2.setOnRunning((successEvent) -> {
                System.out.println("loading  configurations...");
            });
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

                    // UPDATES setzen
                    updatedConfig.status = Configuration.stats[1]; // ABGESCHLOSSEN

                    // Test PUT - Update Configuration
                    PutConfigurationTask configurationsTask3 = new PutConfigurationTask(activeUser, updatedConfig, oldConfigId);
                    //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                    configurationsTask3.setOnRunning((successEvent) -> {
                        System.out.println("trying to update configuration...");
                    });
                    configurationsTask3.setOnSucceeded((WorkerStateEvent e3) -> {
                        System.out.println("configuration updated: " + configurationsTask3.getValue());


                        // Test DELETE
                        if (Main.CONFIGURATIONS.size() > 10) {
                            int configId = Main.CONFIGURATIONS.get(0).id; // DELETE OLDEST ELEMENT
                            // query all configurations from REST API with Task Thread
                            DeleteConfigurationTask configurationsTask4 = new DeleteConfigurationTask(activeUser, configId);
                            //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                            configurationsTask4.setOnRunning((successEvent) -> {
                                System.out.println("trying to delete configuration...");
                            });
                            configurationsTask4.setOnSucceeded((WorkerStateEvent e4) -> {
                                System.out.println("configurations deleted:" + configurationsTask4.getValue());
                            });
                            configurationsTask4.setOnFailed((WorkerStateEvent e41) -> {
                                System.out.println("deleting failed" + configurationsTask4.getException());
                            });
                            //Tasks in eigenem Thread ausführen
                            new Thread(configurationsTask4).start();
                        }


                    });
                    configurationsTask3.setOnFailed((WorkerStateEvent e31) -> {
                        System.out.println("saving failed" + configurationsTask3.getException());
                    });
                    //Tasks in eigenem Thread ausführen
                    new Thread(configurationsTask3).start();
                }



            });
            //Tasks in eigenem Thread ausführen
            new Thread(configurationsTask2).start();


        });
        configurationsTask1.setOnFailed((WorkerStateEvent e11) -> {
            System.out.println("saving failed" + configurationsTask1.getException());
        });
        new Thread(configurationsTask1).start();


    }
}