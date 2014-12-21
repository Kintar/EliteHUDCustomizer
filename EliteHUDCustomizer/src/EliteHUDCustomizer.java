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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import javax.swing.JOptionPane;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
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
        if(new File("profiles.cfg").exists()) {
            ProfileSorter sorter = new ProfileSorter();
            sorter.sortProfiles();
        }
        
        // initialize drop down menu
        ComboBox comboBox = new ComboBox(comboBoxData);
        comboBoxData = populateComboBox();
        comboBox.setItems(comboBoxData);
        comboBox.setPromptText("Choose a theme...");
        
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
        Button createBtn = new Button();
        
        createBtn.setText("Create default GraphicsConfiguration.xml");
        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    JOptionPane.showMessageDialog(null, "Browse to \"$Install Folder\\Frontier\\EDLaunch\\Products\\FORC-FDEV-D-1002\"", "Message", JOptionPane.INFORMATION_MESSAGE);
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Browse to \"$Install Folder\\Frontier\\EDLaunch\\Products\\FORC-FDEV-D-1002\"");
                    File file = directoryChooser.showDialog(primaryStage);
                    if (!file.isDirectory()) throw new java.io.FileNotFoundException();
                    if (!file.exists()) throw new java.io.FileNotFoundException();
                    
                    filePath.delete(0, filePath.length());
                    filePath.append(file.getAbsolutePath()).append("/GraphicsConfiguration.xml");
                     
                    file = new File(filePath.toString());
                    file.createNewFile();
                    
                    FileWriter printer = new FileWriter(file);
                    printer.write(XmlFormatter.defaults);
                    printer.close();
                    
                    resetProfile(filePath.toString());
                    cleanXml(filePath.toString());
                    JOptionPane.showMessageDialog(null, "Default GraphicsConfiguration.xml created in chosen directory", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (Exception e) {  
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error Occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        defaultBtn.setText("Defaults");
        defaultBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (filePath.length() == 0)
                        throw new FileNotFoundException();
                    resetProfile(filePath.toString());
                    cleanXml(filePath.toString());
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
                    if (filePath.length() == 0)
                        throw new FileNotFoundException();
                    resetProfile(filePath.toString());
                    Profile selectedProfile = (Profile) comboBox.getSelectionModel().getSelectedItem();
                    setProfile(filePath.toString(), selectedProfile.getXml());
                    cleanXml(filePath.toString());
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
        
        
        // initialize GridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        grid.add(fileChooserLabel, 0, 0, 1, 1);
        grid.add(openBtn, 1, 0);
        
        grid.add(createBtn, 0, 1);
        grid.add(defaultBtn, 1, 1);
        
        
        grid.add(comboBox, 0, 2, 1, 1);
        grid.add(activateBtn, 1, 2);
        
        
        // center all elements       
        GridPane.setHalignment(fileChooserLabel, HPos.CENTER);
        GridPane.setHalignment(createBtn, HPos.CENTER);        
        GridPane.setHalignment(openBtn, HPos.CENTER);
        GridPane.setHalignment(defaultBtn, HPos.CENTER);
        GridPane.setHalignment(activateBtn, HPos.CENTER);
        GridPane.setHalignment(comboBox, HPos.CENTER);
        createBtn.setTextAlignment(TextAlignment.CENTER);
        
        
        grid.setGridLinesVisible(false);
        
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
            try{
            FileWriter printer = new FileWriter("profiles.cfg");
            String profiles = "80's Revival Purple\n" +
            "<MatrixRed> 0.3, 0.3, 1 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 1, 0, 0 </MatrixBlue>\n" +
            "Baron's Blue\n" +
            "<MatrixRed> 0, 0.33, 0.75 </MatrixRed> <MatrixGreen> 0, 1, 0 </MatrixGreen> <MatrixBlue> 1, 0, 0 </MatrixBlue>\n" +
            "Beige\n" +
            "<MatrixRed> .33, .11, 0 </MatrixRed> <MatrixGreen> 1, 1, 1 </MatrixGreen> <MatrixBlue> 1, 1, 1 </MatrixBlue>\n" +
            "Black\n" +
            "<MatrixRed> 0, 0, 0 </MatrixRed> <MatrixGreen> 0, 0, 0 </MatrixGreen> <MatrixBlue> 1, 1, 1 </MatrixBlue>\n" +
            "Blue with Purple Accents\n" +
            "<MatrixRed> 0.03, 0.1, 1 </MatrixRed> <MatrixGreen> 0.4, 1.25, 0.2 </MatrixGreen> <MatrixBlue> 1, -1.25, 0.3 </MatrixBlue>\n" +
            "Blue with Red Accents\n" +
            "<MatrixRed> 0, 0.33, 0.75 </MatrixRed> <MatrixGreen> 0.6, 0, 0 </MatrixGreen> <MatrixBlue> 1, 0, 0 </MatrixBlue>\n" +
            "Blue with Red and Yellow Accents\n" +
            "<MatrixRed> -1, 0.51, 0.99 </MatrixRed> <MatrixGreen> 1, -1, -0.26 </MatrixGreen> <MatrixBlue> 1, 0.29, -1 </MatrixBlue>\n" +
            "Blue with White and Yellow Accents\n" +
            "<MatrixRed> 0, 0, 1 </MatrixRed> <MatrixGreen> 0, 1, 0 </MatrixGreen> <MatrixBlue> 1, 0, 0 </MatrixBlue>\n" +
            "Bright Blue with White and Yellow Accents\n" +
            "<MatrixRed> 0, 0.5, 1 </MatrixRed> <MatrixGreen> 0, 0.5, 0 </MatrixGreen> <MatrixBlue> 1, 0, 0 </MatrixBlue>\n" +
            "Bright Green with Red and Yellow Accents\n" +
            "<MatrixRed> 0, 1, 0 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 0, 0, 1 </MatrixBlue>\n" +
            "Cool Blue\n" +
            "<MatrixRed> 0.21, 0.29, 0.93 </MatrixRed> <MatrixGreen> 0, 1, 0 </MatrixGreen> <MatrixBlue> 1, 1, 1 </MatrixBlue>\n" +
            "Fade to Grey\n" +
            "<MatrixRed> 0.2, 0.2, 0.5 </MatrixRed> <MatrixGreen> 0.4, 0.2, 0.3 </MatrixGreen> <MatrixBlue> 0.4, 0.2, 0.3 </MatrixBlue>\n" +
            "Ghost\n" +
            "<MatrixRed> 0.0, 0.3, 0.5 </MatrixRed> <MatrixGreen> 0, 0, 0.2 </MatrixGreen> <MatrixBlue> 0.2, 0, 0 </MatrixBlue>\n" +
            "Green Venom\n" +
            "<MatrixRed> 0, 0.3, 0 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 0, 0.3, 0 </MatrixBlue>\n" +
            "Green with Pink\n" +
            "<MatrixRed> 0, 1, 0 </MatrixRed> <MatrixGreen> 0.5, 0, 0 </MatrixGreen> <MatrixBlue> 0.5, 0, 1 </MatrixBlue>\n" +
            "Green with Yellow Accents\n" +
            "<MatrixRed> 0, 3, 0 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 0, 3, 0 </MatrixBlue>\n" +
            "Hot Pink\n" +
            "<MatrixRed> 1, 0, 0.33 </MatrixRed> <MatrixGreen> 1, 1, 1 </MatrixGreen> <MatrixBlue> 0.33, 0, 1 </MatrixBlue>\n" +
            "Jaded\n" +
            "<MatrixRed> 0.2, 0.5, 0.2 </MatrixRed> <MatrixGreen> 0.3, 0.2, 0.4 </MatrixGreen> <MatrixBlue> 0.3, 0.2, 0.4 </MatrixBlue>\n" +
            "Light Blue with White\n" +
            "<MatrixRed> 0, 0.4, 0.7 </MatrixRed> <MatrixGreen> -1, -1, -1 </MatrixGreen> <MatrixBlue> 1, 1, 1 </MatrixBlue>\n" +
            "Light Blue with White and Purple Accents\n" +
            "<MatrixRed> 0, 1, 1 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 0, 0, 1 </MatrixBlue>\n" +
            "Light Green\n" +
            "<MatrixRed> 0.69, 1, 0.15 </MatrixRed> <MatrixGreen> 0, 1, 0 </MatrixGreen> <MatrixBlue> 0, 0, 0.15 </MatrixBlue>\n" +
            "Medium Blue with White Accents\n" +
            "<MatrixRed> 0, 4, 7 </MatrixRed> <MatrixGreen> -1.0, -1.0, -1.0 </MatrixGreen> <MatrixBlue> 0, 0, 1 </MatrixBlue>\n" +
            "Medium Green with Blue Accents\n" +
            "<MatrixRed> 0, 0.5, 0 </MatrixRed> <MatrixGreen> 0, 0, 1 </MatrixGreen> <MatrixBlue> 0.1, 0.1, 0 </MatrixBlue>\n" +
            "Medium Green with Pink and Purple Accents \n" +
            "<MatrixRed> 0, 0.5, 0 </MatrixRed> <MatrixGreen> 0, 0, 1 </MatrixGreen> <MatrixBlue> 0.5, 0, 0 </MatrixBlue>\n" +
            "Nightvision Green\n" +
            "<MatrixRed> 0.31, 0.75, 0.1 </MatrixRed> <MatrixGreen> 0.5, 0.5, 1 </MatrixGreen> <MatrixBlue> 0, 0, 1 </MatrixBlue>\n" +
            "Pink with White Accents\n" +
            "<MatrixRed> 1, 0, 0.42 </MatrixRed> <MatrixGreen> 1, 0.17, 1 </MatrixGreen> <MatrixBlue> 0.63, 1, 1 </MatrixBlue>\n" +
            "Pure White with Blue Accents\n" +
            "<MatrixRed> 1, 1, 1 </MatrixRed> <MatrixGreen> -1, -1, 1 </MatrixGreen> <MatrixBlue> 0, 0, 1 </MatrixBlue>\n" +
            "Purple Haze\n" +
            "<MatrixRed> 0, 0, 0.5 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 0, 0.5, 0 </MatrixBlue>\n" +
            "Purple with White and Yellow Accents\n" +
            "<MatrixRed> 0, 0, 5 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 0, 5, 0 </MatrixBlue>\n" +
            "Red\n" +
            "<MatrixRed> 1, 0, 0 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 0, 0, 0 </MatrixBlue>\n" +
            "Retro Gamer\n" +
            "<MatrixRed> 0.2, 1, 0 </MatrixRed> <MatrixGreen> 1, 0, 0 </MatrixGreen> <MatrixBlue> 1, 0, 0 </MatrixBlue>\n" +
            "Sunset Blast\n" +
            "<MatrixRed> 1, 0, 0 </MatrixRed> <MatrixGreen> 0.5, 0.7, 0 </MatrixGreen> <MatrixBlue> 0, 0, 0.1 </MatrixBlue>\n" +
            "White\n" +
            "<MatrixRed> 0.42, 0.42, 0.42 </MatrixRed> <MatrixGreen> 0.84, 0.84, 0.84 </MatrixGreen> <MatrixBlue> 0.21, 0.21, 0.21 </MatrixBlue>\n" +
            "";
            printer.write(profiles);
            printer.close();
            ObservableList<Profile> list = FXCollections.observableArrayList();
            File file = new File("profiles.cfg");
            if(!file.exists()) throw new java.io.IOException();
            
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                list.add(new Profile(scanner.nextLine(), scanner.nextLine()));
            }
            return list;
            }catch(Exception e2){return null;}
        }
    }
    
    private void resetProfile(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        else {            
            cleanXml(path);
            Scanner scanner = new Scanner(file);
            StringBuilder buffer = new StringBuilder();
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.contains("<GUIColour>")) {
                    buffer.append("<GUIColour>\n    <Default>\n        <LocalisationName>Standard</LocalisationName>\n");
                    buffer.append("            <MatrixRed> 1, 0, 0 </MatrixRed>\n");
                    buffer.append("            <MatrixGreen> 0, 1, 0 </MatrixGreen>\n");
                    buffer.append("            <MatrixBlue> 0, 0, 1 </MatrixBlue>\n");
                    buffer.append("    </Default>\n");
                    buffer.append("</GUIColour>");
                    while(!line.contains("</GUIColour>")) {
                        line = scanner.nextLine();
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
    
    private void setProfile(String path, String xml) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "No config file found, creating default in current directory..."
                , "Warning:", JOptionPane.INFORMATION_MESSAGE);
            file = new File("GraphicsConfiguration.xml");
            FileWriter printer = new FileWriter(file);
            printer.write(XmlFormatter.defaults);
            printer.close();
        }
        
        
        else {
            cleanXml(path);
            Scanner scanner = new Scanner(file);
            StringBuilder buffer = new StringBuilder();

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.contains("<GUIColour>")) {
                    buffer.append("<GUIColour>\n    <Default>\n        <LocalisationName>Standard</LocalisationName>\n");
                    buffer.append("            " + xml + "\n");
                    buffer.append("    </Default>\n");
                    buffer.append("</GUIColour>");
                    while(!line.contains("</GUIColour>")) {
                        line = scanner.nextLine();
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
    
    public void cleanXml(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        
        else {
            Scanner scanner = new Scanner(file);
            StringBuilder buffer = new StringBuilder();

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                buffer.append(line);
            }
                
            scanner.close();
            
            FileWriter printer = new FileWriter(path);
            printer.write(new XmlFormatter().format(buffer.toString()));
            printer.close();
        }
    }
    
}