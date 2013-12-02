package org.jimmyray.mongo.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.jimmyray.mongo.framework.Strings;
import org.jimmyray.mongo.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Swing GUI to load file into MongoDB
 * 
 * @author jimmyray
 * @version 1.0
 */
public class FileOps extends JPanel implements ActionListener {
	private static final long serialVersionUID = -875291175944080765L;

	private static final int INSETS = 5;
	private static final int X_SIZE = 5;
	private static final int Y_SIZE = 20;

	private static Logger logger = LoggerFactory.getLogger(FileOps.class);

	private static final String NEW_LINE = "\n";

	private JButton saveButton;
	private JTextArea log;
	private JFileChooser fileChooser;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				FileOps.createAndShowGUI();
			}
		});
	}

	public FileOps() {
		super(new BorderLayout());

		log = new JTextArea(FileOps.X_SIZE, FileOps.Y_SIZE);
		log.setMargin(new Insets(FileOps.INSETS, FileOps.INSETS,
				FileOps.INSETS, FileOps.INSETS));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		this.fileChooser = new JFileChooser();
		this.fileChooser
				.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		// Create the save button. We use the image from the JLF
		// Graphics Repository (but we extracted it from the jar).
		saveButton = new JButton("Save a File...",
				createImageIcon("images/Save16.gif"));
		saveButton.addActionListener(this);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(saveButton);

		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Handle open button action.
		if (e.getSource() == saveButton) {
			int returnVal = fileChooser.showSaveDialog(FileOps.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					FileInputStream fis = new FileInputStream(file);

					SpringBeanFactory.initContext();
					FileStorageService fileStorageService = (FileStorageService) SpringBeanFactory
							.getBean("fileStorageService");

					Map<String, String> metadata = new HashMap<String, String>();
					metadata.put("SESSION", "Richmond JUG 2013-01-16");
					metadata.put("javaHome", System.getProperty("java.home"));
					metadata.put("userHome", System.getProperty("user.home"));
					metadata.put("userName", System.getProperty("user.name"));

					String fileId = fileStorageService.save(fis,
							"application/pdf", file.getName(), metadata, false);
					log.append(fileId + FileOps.NEW_LINE);
				} catch (FileNotFoundException fnfe) {
					logger.error(Strings.getStackTraceAsString(fnfe));
				}
				log.append("Saving: " + file.getName() + "." + FileOps.NEW_LINE);
			} else {
				log.append("Save command cancelled by user." + FileOps.NEW_LINE);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}

	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = FileOps.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			logger.error("Couldn't find file: " + path);
			return null;
		}
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("FileChooserDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new FileOps());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
}
