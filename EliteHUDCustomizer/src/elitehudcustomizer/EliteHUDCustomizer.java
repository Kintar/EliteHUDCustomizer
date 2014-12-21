/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elitehudcustomizer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import javax.swing.JOptionPane;


/**
 *
 * @author ty
 */
public class EliteHUDCustomizer extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // initialize file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your GraphicsConfiguration.xml File"); 
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
                    JOptionPane.showMessageDialog(null, e.toString(), "Error:", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, e.toString(), "Error:", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        openBtn.setText("Open");
        openBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent event) {
                    try {
                        File xml = fileChooser.showOpenDialog(primaryStage);
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.toString(), "Error:", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        
        // initialize text fields
        
        // initialize drop down chooser
        
        
        // initialize GridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        // grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(openBtn, 1, 0);
        grid.add(defaultBtn, 0, 2);
        grid.add(activateBtn, 1, 2);
        
        Scene scene = new Scene(grid, 300, 100);
        
        primaryStage.setTitle("Elite HUD Customizer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}