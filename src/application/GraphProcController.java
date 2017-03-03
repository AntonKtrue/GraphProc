package application;
/*
 * Controller for GraphProc FXML view
 */
import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GraphProcController {
	
	@FXML
	private BorderPane rootNode;	
	@FXML
	private Window root;		
	@FXML
	private Button openFileButton;	
	@FXML
	private Button prepareMpsaButton;	
	@FXML
	private Button uploadModbus;
	@FXML
	private Label chosenFileLabel;

	private KpnGraph kpnGraph;
	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	private void onClickOpenFileButton() throws Exception {
		//chosenFileLabel.setText("File chosen");
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Flat ODG files (*.fodg)", "*.fodg");
		fileChooser.setInitialDirectory(new File("E:/SHARED/"));
		fileChooser.setInitialFileName("starolikeevo-KNP_MG3.fodg");
		fileChooser.getExtensionFilters().add(extFilter);
		File fodgGraphFile = fileChooser.showOpenDialog(new Stage());
		//System.out.println(fodgGraphFile);	
		//chosenFileLabel.setText(fodgGraphFile.toString());
		FodgGraphParse fgp = new FodgGraphParse(fodgGraphFile);
		kpnGraph = fgp.getGraph();
		kpnGraph.prepareMpsa();
	}
	
	@FXML
	private void onClickPrepareButton() throws Exception {
		//kpnGraph;
		
	}
	
	@FXML
	private void modbusUpload() throws Exception {
	//	KpnModbusUpload kmu = new KpnModbusUpload();
	}

	
}