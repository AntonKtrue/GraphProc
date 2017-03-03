package application;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

//import javafx.stage.FileChooser;
//import javafx.stage.Stage;

public class Window extends JFrame {
	JButton openFile;
	private KpnGraph kpnGraph;
	public Window() {
		openFile = new JButton("Открыть файл");
		openFile.addActionListener((ActionEvent e) -> {
			
			JFileChooser fileChooser = new JFileChooser();
			int ret = fileChooser.showDialog(null, "Выбрать граф объекта");
			if(ret == JFileChooser.APPROVE_OPTION) {
				File fodgGraphFile = fileChooser.getSelectedFile();
				
				try{
					FodgGraphParse fgp = new FodgGraphParse(fodgGraphFile);
					kpnGraph = fgp.getGraph();
					kpnGraph.prepareMpsa();	
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}	
		});
		add(openFile);
		setSize(400, 230);
        
	}
	
	
}
