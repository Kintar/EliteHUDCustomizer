/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import javax.swing.JOptionPane;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;


/**
 *
 * @author ty
 */
@SuppressWarnings("unchecked")
public class EliteHUDCustomizer extends Application {;
    private ObservableList<Profile> comboBoxData = FXCollections.observableArrayList();
    
    @Override
    public void start(Stage primaryStage) {
        // sort the profiles in profiles.cfg for use in the dropdown menu
        ProfileSorter sorter = new ProfileSorter();
        sorter.sortProfiles();
        // initialize drop down menu
        ComboBox comboBox = new ComboBox(comboBoxData);
        comboBoxData = populateComboBox(); //System.out.println(comboBoxData);\
        comboBox.setItems(comboBoxData);
        
        // Define rendering of the list of values in ComboBox drop down. 
        comboBox.setCellFactory((anotherComboBox) -> {
            return new ListCell<Profile> () {
                @Override
                protected void updateItem(Profile item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getTitle());
                    }
                }
            };
        });

        // Define rendering of selected value shown in ComboBox.
        comboBox.setConverter(new StringConverter <Profile> () {
                    @Override
                    public String toString(Profile item) {
                        if (item == null) {
                            return null;
                        } else {
                            return item.getTitle();
                        }
                    }
                    
                    @Override
                    public Profile fromString(String profileString) {
                        return null; // No conversion fromString needed.
                    }
        });
        
        
        // initialize file chooser
        Label fileChooserLabel = new Label("Select your GraphicsConfiguration.xml");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your GraphicsConfiguration.xml file"); 
                // i.e.  "C:\Users[username]\AppData\Local\Frontier_Developments\Products\FORC-FDEV-D-1002"
        
        // initialize buttons
        Button openBtn = new Button();
        Button defaultBtn = new Button();
        Button activateBtn = new Button();
        
        defaultBtn.setText("Defaults");
        defaultBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    JOptionPane.showMessageDialog(null, "Defaults Restored!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
                
                catch (Exception e) {
                        fileChooserLabel.setText("Choose your GraphicsConfiguration.xml");
                        String errorText = "Make sure you chose your GraphicsConfiguration.xml file!";
                        JOptionPane.showMessageDialog(null, errorText, "Error:", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
                
        activateBtn.setText("Activate");
        activateBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                try {   
                    JOptionPane.showMessageDialog(null, "Profile Activated!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
                
                catch (Exception e) {
                        fileChooserLabel.setText("Select your GraphicsConfiguration.xml");
                        String errorText = "Make sure you chose your GraphicsConfiguration.xml file!";
                        JOptionPane.showMessageDialog(null, errorText, "Error:", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        openBtn.setText("Browse");
        openBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent event) {
                    try {
                        File xml = fileChooser.showOpenDialog(primaryStage);
                        if(!xml.getName().equals("GraphicsConfiguration.xml")) 
                            throw new java.io.IOException();
                        else fileChooserLabel.setText("Configuration found!");
                    }
                    catch (Exception e) {
                        fileChooserLabel.setText("Select your GraphicsConfiguration.xml");
                        String errorText = "Make sure you chose your GraphicsConfiguration.xml file!";
                        JOptionPane.showMessageDialog(null, errorText, "Error:", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        
        // initialize text fields
        
        // initialize drop down chooser
        
        
        // initialize GridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(25);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        grid.add(fileChooserLabel, 0, 0, 3, 1);
        grid.add(openBtn, 4, 0);
        
        grid.add(defaultBtn, 0, 2);
        grid.add(comboBox, 2, 2);
        grid.add(activateBtn, 4, 2);
        
        Scene scene = new Scene(grid);
        
        primaryStage.setTitle("Elite HUD Customizer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private ObservableList<Profile> populateComboBox ()  {
        try {
            ObservableList<Profile> list = FXCollections.observableArrayList();
            File file = new File("profiles.cfg");
            if(!file.exists()) throw new java.io.IOException();
            
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                list.add(new Profile(scanner.nextLine(), scanner.nextLine()));
            }
            return list;
        }
        
        catch (Exception e) {
            String errorText = "profiles.cfg must be present!";
            JOptionPane.showMessageDialog(null, errorText, "Fatal Error:", JOptionPane.ERROR_MESSAGE);
                    try { 
            Thread.sleep(5000); }
                    catch (Exception e2) {}
            System.exit(1);
        }
        
        return null;
    }
    
}