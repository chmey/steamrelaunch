/**
 * 
 */
package de.chrisyou.steamrelaunch;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;






import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author chrisyou
 * File: SteamRelaunch.java
 * Date: 01.04.2015
 * Definition: 
 */
public class SteamRelaunch extends JFrame implements ActionListener{

	/**
	 * @param args
	 */
	private JButton btnLogin1, btnLogin2, btnShutdown, btnBrowse;
	private JTextField txtAccinfo1, txtAccinfo2, txtSteamPath;
	private JLabel lblAcc1, lblAcc2, lblSteamPath;
	private File fileSteam, fileConfig;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SteamRelaunch();
	}
	public SteamRelaunch(){
		setTitle("Steam Relaunch Tool");		
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		lblSteamPath = new JLabel("Path to Steam.exe: ");
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;				
		gbc.anchor= GridBagConstraints.LINE_START;		
		getContentPane().add(lblSteamPath, gbc);

		txtSteamPath = new JTextField(20);
		txtSteamPath.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 0;
		getContentPane().add(txtSteamPath, gbc);

		btnBrowse = new JButton("Browse..");
		btnBrowse.addActionListener(this);
		btnBrowse.setActionCommand("browse");
		gbc.gridx = 3;
		gbc.gridy = 0;
		getContentPane().add(btnBrowse,gbc);

		lblAcc1 = new JLabel("Account 1:");	
		gbc.gridx = 0;
		gbc.gridy = 1;
		getContentPane().add(lblAcc1, gbc);

		txtAccinfo1 = new JTextField(20);		
		gbc.gridx = 1;
		gbc.gridy = 1;
		txtAccinfo1.setToolTipText("username:password");		
		getContentPane().add(txtAccinfo1, gbc);

		btnLogin1 = new JButton("Login..");
		btnLogin1.addActionListener(this);
		btnLogin1.setActionCommand("login1");
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;		
		getContentPane().add(btnLogin1, gbc);	

		lblAcc2 = new JLabel("Account 2:");	
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		getContentPane().add(lblAcc2, gbc);

		txtAccinfo2 = new JTextField(20);		
		gbc.gridx = 1;
		gbc.gridy = 2;
		txtAccinfo2.setToolTipText("username:password");		
		getContentPane().add(txtAccinfo2, gbc);

		btnLogin2 = new JButton("Login..");
		btnLogin2.addActionListener(this);
		btnLogin2.setActionCommand("login2");
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(btnLogin2, gbc);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		fileConfig = new File("config.properties");
		readProps();
		txtAccinfo1.requestFocus();
	}
	private void readProps() {
		// TODO Auto-generated method stub		
		try {			
			if(fileConfig.exists()){
				Properties props = new Properties();
				FileReader readConfig = new FileReader(fileConfig);
				props.load(readConfig);
				txtSteamPath.setText(props.getProperty("path"));
				readConfig.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void saveProps() {
		// TODO Auto-generated method stub
		OutputStream output = null;
		try {
			Properties props = new Properties();			
			output = new FileOutputStream("config.properties");
			props.setProperty("path", txtSteamPath.getText());
			props.store(output, null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getActionCommand().equalsIgnoreCase(btnBrowse.getActionCommand())){
			final JFileChooser fc = new JFileChooser();
			FileFilter filt = new FileNameExtensionFilter("Steam.exe",new String[] {"exe"});
			fc.setFileFilter(filt);
			fc.setDialogTitle("Browse to Steam.exe..");			
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				fileSteam = fc.getSelectedFile();				
				if(fileSteam.getName().equals("Steam.exe")){					
					txtSteamPath.setText(fileSteam.getAbsolutePath());
					saveProps();
				}
			}


		}else if(evt.getActionCommand().equalsIgnoreCase(btnLogin1.getActionCommand())){
			login(txtAccinfo1.getText());
		}else if(evt.getActionCommand().equalsIgnoreCase(btnLogin2.getActionCommand())){
			login(txtAccinfo2.getText());
		}
	}
	
	private void login(String info) {
		// TODO Auto-generated method stub
		String[] data = info.split(":");
		try {
			if(boolSteamRunning()){
				Runtime.getRuntime().exec("cmd /c start "+ fileSteam.getAbsolutePath() + " -shutdown");
				Thread.sleep(1500);
			}

			Runtime.getRuntime().exec("cmd /c start "+ fileSteam.getAbsolutePath() + " -login "+data[0] + " " + data[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	private boolean boolSteamRunning() {
		// TODO Auto-generated method stub
		try {
			String line;
			Process proc = Runtime.getRuntime().exec("wmic.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			OutputStreamWriter oStream = new OutputStreamWriter(proc.getOutputStream());
			oStream .write("process where name='Steam.exe'");
			oStream .flush();
			oStream .close();
			while ((line = input.readLine()) != null) {
				if(line.contains("Win32_Process")){
					return true;
				}
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

}
