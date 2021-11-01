package org.sj.tools.pdfjuice.gui;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.sj.tools.graphics.tablemkr.util.PDFTableExporter;
import org.sj.tools.pdfjuice.ExampleGenerator;


public class PDFJuiceWindow extends JFrame {
		
	private static Logger log = Logger.getLogger("org.sj.tools.pdfjuice.gui.PDFJuiceWindow");
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public PDFJuiceWindow() {
			super("SWING test");
		    createUI();
		    this.setSize(560, 300);//TODO:      
		    this.setLocationRelativeTo(null);
		    this.addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Anything else?
					System.exit(0);
				}

				@Override
				public void windowClosed(WindowEvent e) {
				}

				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
		    	
		    	
		    });
		}
		
	   public static void main(String[] args) {
	      createWindow();
	   }	

	   private static void createWindow() {    
	      PDFJuiceWindow win = new PDFJuiceWindow();
	      win.setVisible(true);
	   }

	   private void createUI(){  
		  final JFrame frame = this;
	      JPanel panel = new JPanel();
	      LayoutManager layout = new BoxLayout(panel, BoxLayout.Y_AXIS);  
	      panel.setLayout(layout);
	      
	     //TODO:
	      // create input filename field + open button.
	      // create output filename field + save as button.
	      // - defaultfilename
	      // create "execute" button.
	      
	  


	      JLabel lblIn = new JLabel("Input filename:");
	      final JTextField fldFilenameIn = new JTextField();
	      JButton btnBrowseIn = new JButton("(in)...");
	      
	      JLabel lblOut = new JLabel("Output filename:");
	      final JTextField fldFilenameOut = new JTextField();
	      JButton btnBrowseOut = new JButton("(out)...");
	      
	      btnBrowseIn.addActionListener(new ActionListener() {
	    	  @Override
	    	  public void actionPerformed(ActionEvent e) {
	    		  final JFileChooser fc = new JFileChooser();

	    		  fc.setMultiSelectionEnabled(true);
	    		  int returnVal = fc.showOpenDialog(frame);

	    	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	        	
	    	            File file = fc.getSelectedFile();
	    	            String filenameIn = file.getAbsolutePath();
	    	            fldFilenameIn.setText(filenameIn);
	    	            String defFilenameOut = ExampleGenerator.changeExtension(filenameIn, ".html");
	    	            fldFilenameOut.setText(defFilenameOut);

	    	        } else {
	    	            // log.append("Open command cancelled by user." + newline);

	    	        }
	    	  }
	      });
	      
	      btnBrowseOut.addActionListener(new ActionListener() {
	    	  @Override
	    	  public void actionPerformed(ActionEvent e) {
	    		  final JFileChooser fc = new JFileChooser();
	    		  int returnVal = fc.showSaveDialog(frame);

	    	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	            File file = fc.getSelectedFile();
	    	            fldFilenameOut.setText(file.getAbsolutePath());
	    	        } else {
	    	            // log.append("Open command cancelled by user." + newline);
	    	        }
	    	  }
	      });


	      JButton btnProcess = new JButton("Process");
	      btnProcess.addActionListener(new ActionListener() {
	    	  @Override
	    	  public void actionPerformed(ActionEvent e) {

	    	  		//JOptionPane.showMessageDialog(frame,"No input files selected.");
	    		  extractTables(fldFilenameIn.getText(), fldFilenameOut.getText());
	    		  
	    	  }
	      });

	      panel.add(btnBrowseIn);
	      panel.add(lblIn);
	      panel.add(fldFilenameIn);
	      panel.add(lblOut);
	      panel.add(fldFilenameOut);
	      panel.add(btnBrowseOut);
	      panel.add(btnProcess);
	      frame.getContentPane().add(panel, BorderLayout.CENTER);    
	   }
	   
	   public void extractTables(String filenameIn, String filenameOut) { 
		   log.info("extactTables in:"+filenameIn+ " out:"+filenameOut);
		   PDFTableExporter proc = new PDFTableExporter(); 
		   proc.setDestination(filenameOut);
   			proc.run(filenameIn);
	   }
	   
}

