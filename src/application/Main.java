package application;
	
//import javafx.application.Application;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
//import javafx.fxml.FXMLLoader;
import javax.swing.*;

public class Main /*extends Application*/ {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            JFrame window = null;
            try {
                window = new Window();
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
	}

//	@Override
//	public void start(Stage primaryStage) {
//		try {
//			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("GraphProc.fxml"));
//			Scene scene = new Scene(root);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void main(String[] args) {
//		launch(args);
//	}
}
