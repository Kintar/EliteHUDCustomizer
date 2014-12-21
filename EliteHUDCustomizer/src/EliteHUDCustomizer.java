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
import java.io.FileWriter;
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
        // object representing the absolute file path of the GraphicsConfiguration.xml file
        StringBuffer filePath = new StringBuffer();
        
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
        
        // Handle ComboBox event.
        comboBox.setOnAction((event) -> {
            Profile selectedProfile = (Profile) comboBox.getSelectionModel().getSelectedItem();
            // System.out.println("ComboBox Action (selected: " + selectedProfile.getTitle() + ")");
            
            
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
                    resetProfile(filePath.toString());
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
                    resetProfile(filePath.toString());
                    Profile selectedProfile = (Profile) comboBox.getSelectionModel().getSelectedItem();
                    //System.out.println(selectedProfile.getXml());
                    setProfile(filePath.toString(), selectedProfile.getXml());
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
                        filePath.delete(0, filePath.length());
                        filePath.append(xml.getAbsolutePath());
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
    
    private void resetProfile(String path) throws java.io.IOException {
        //System.out.println(path);
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        StringBuilder buffer = new StringBuilder();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //System.out.println(line);
            if(line.equals("<GUIColour>")) {
                buffer.append("<GUIColour>\n    <Default>\n        <LocalisationName>Standard</LocalisationName>\n");
                buffer.append("            <MatrixRed> 1, 0, 0 </MatrixRed>\n");
                buffer.append("            <MatrixGreen> 0, 1, 0 </MatrixGreen>\n");
                buffer.append("            <MatrixBlue> 0, 0, 1 </MatrixBlue>\n");
                buffer.append("    </Default>\n");
                buffer.append("</GUIColour>");
                while(!line.contains("</GUIColour>")) {
                    line = scanner.nextLine();
                    //System.out.println(line);
                }
                   
            }
            else
                buffer.append(line);
        }
        scanner.close();
        FileWriter printer = new FileWriter(path);
        printer.write(buffer.toString());
        printer.close();
        
    }
    
    private void setProfile(String path, String xml) throws java.io.IOException {
        System.out.println(path);
        System.out.println(xml);
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        StringBuilder buffer = new StringBuilder();
        //System.out.println(path);
        
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //System.out.println(line);
            if(line.equals("<GUIColour>")) {
                buffer.append("<GUIColour>\n    <Default>\n        <LocalisationName>Standard</LocalisationName>\n");
                buffer.append("            " + xml + "\n");
                buffer.append("    </Default>\n");
                buffer.append("</GUIColour>");
                while(!line.contains("</GUIColour>")) {
                    line = scanner.nextLine();
                    //System.out.println(line);
                }
                   
            }
            else
                buffer.append(line);
        }
        scanner.close();
        FileWriter printer = new FileWriter(path);
        printer.write(buffer.toString());
        printer.close();
        
        
    }
    
}