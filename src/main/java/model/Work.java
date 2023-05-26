package model;

import java.awt.AWTException;
import view.PanelBotones;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialOceanicTheme;
import org.apache.commons.io.FileUtils;

/**
 * Main class that handles the creation of the window and initialization of all
 * panels and elements.
 *
 * @author Julian
 */
public class Work extends JFrame {

private final static ConfigReader configReader = new ConfigReader("config.txt");
private final static String executablePath = configReader.getExecutablePath();
private final static String destinationFolder = configReader.getDestinationFolder();
private final static String iconPackPath = configReader.getIconPackPath();
File path = new File(destinationFolder);

PanelBotones pnlBotones = new PanelBotones();

/**
 * Constructor for the main class that initializes all graphical components.
 *
 * @throws java.io.IOException
 */
public Work() throws IOException {

	try {
		String stonksPath = iconPackPath + "//stopwatch.png";
		setIconImage(ImageIO.read(new File(stonksPath)));
	} catch (IOException ex) {
		Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
	}

	pnlBotones.setBounds(0, 0, 310, 100);
	pnlBotones.setSize(370, 150);
	pnlBotones.initJIntellitype();
	getContentPane().add(pnlBotones);

	setSize(40, 97);
	setResizable(false);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setAlwaysOnTop(true);
	setLocation(18, 320);
	setUndecorated(true);

	addWindowListener(new WindowAdapter() {
	@Override
	public void windowClosing(WindowEvent we) {
		try {
			close();
		} catch (IOException ex) {
			Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {

			Runtime.getRuntime().exec("taskkill /f /im java.exe");

		} catch (IOException e4) {
			// TODO Auto-generated catch block

		}

	}
	});

}

/**
 * Initializes the program window and if the SQL server is not detected to be
 * running yet, it launches it. If the program is found to be running already,
 * throws warning that the software was started externally.Sleep method added to
 * halt run-time in order to allow the SQL server to launch appropriately.
 *
 * @param args Main class method.
 */
@SuppressWarnings("null")
public static void main(String[] args) {

	BufferedWriter output = null;
	BufferedWriter config = null;

	try {
		output = new BufferedWriter(new FileWriter("log.txt", true));
		config = new BufferedWriter(new FileWriter("config.txt", true));
		try {

			if (processCheck("httpd.exe") == false) {
				File file = new File(executablePath);
				Desktop.getDesktop().open(file);
				Thread.sleep(10000);
			} else {
				File f = new File("log.txt");
				File c = new File("config.txt");
				if (c.exists() && !c.isDirectory()) {

				} else {

				}
				if (f.exists() && !f.isDirectory()) {

				} else {

				}

			}

			try {
				UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialOceanicTheme()));

			} catch (UnsupportedLookAndFeelException e) {
			}

			if (SystemTray.isSupported()) {
				SwingUtilities.invokeLater(() -> {
					createTrayIcon();

				});
			} else {
				System.out.println("System tray is not supported.");
			}

		} catch (IOException | InterruptedException ex) {
			Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
		}

	} catch (IOException ex) {
		Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
		try {
			output.close();
		} catch (IOException ex) {
			Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}

private static void createTrayIcon() {
	try {

		SystemTray tray = SystemTray.getSystemTray();
		String stopwatchPath = iconPackPath + "//stopwatch.png";
		Image image = Toolkit.getDefaultToolkit().getImage(stopwatchPath);
		PopupMenu popupMenu = new PopupMenu();

		MenuItem websiteMenuItem = new MenuItem("Open Database");
		websiteMenuItem.addActionListener((ActionEvent e) -> {
			try {
				Desktop.getDesktop().browse(new URI("http://localhost/phpmyadmin/index.php?route=/sql&db=work&table=calls&pos=0"));
			} catch (URISyntaxException | IOException ex) {
				Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		popupMenu.add(websiteMenuItem);

		MenuItem deleteScreenshotsMenuItem = new MenuItem("Delete Screenshots");
		deleteScreenshotsMenuItem.addActionListener(e -> {
			try {
				Work work = new Work();
				work.close();
			} catch (IOException ex) {
				Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		popupMenu.add(deleteScreenshotsMenuItem);

		MenuItem openFolderItem = new MenuItem("Open Screenshot Folder");

		openFolderItem.addActionListener(e -> {

			try {

				Runtime.getRuntime().exec("explorer.exe /select," + destinationFolder);
			} catch (IOException ex) {
			}
		});

		popupMenu.add(openFolderItem);

		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.addActionListener(e -> System.exit(0));
		popupMenu.add(exitMenuItem);

		TrayIcon trayIcon = new TrayIcon(image, "Work", popupMenu);
		trayIcon.setImageAutoSize(true);

		AtomicInteger clickCount = new AtomicInteger(0);
		Work interfaz = new Work();
		interfaz.setVisible(false);
		trayIcon.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (clickCount.incrementAndGet() == 2) {

					if (interfaz.isVisible()) {
						interfaz.setVisible(false);
					} else {
						interfaz.setVisible(true);
					}
					clickCount.set(0);
				}

			}

		}
		});

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	} catch (IOException ex) {
		Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
	}
}

/**
 * Checks to see whether the SQL database host server software (WAMP server) is
 * running or not.
 *
 * @param findProcess Process name to find.
 * @return Whether or not the process was found, as a boolean value.
 * @throws IOException In the event the runtime tasks are inaccessible.
 */
public static boolean processCheck(String findProcess) throws IOException {

	String filenameFilter = "/nh /fi \"Imagename eq " + findProcess + "\"";
	String tasksCmd = System.getenv("windir") + "/system32/tasklist.exe " + filenameFilter;

	Process p = Runtime.getRuntime().exec(tasksCmd);
	ArrayList<String> procs;
	try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
		procs = new ArrayList<>();
		String line;
		while ((line = input.readLine()) != null) {
			procs.add(line);
		}
	}

	Boolean processFound = procs.stream().filter(row -> row.contains(findProcess)).count() > 0;

	return processFound;
}

/**
 * Adds closing behavior to the application for it to clear the screenshot
 * folder upon exit.
 *
 * @throws IOException If folder cannot be accessed.
 */
public void close() throws IOException {
	try (BufferedWriter output = new BufferedWriter(new FileWriter("log.txt", true))) {
		FileUtils.cleanDirectory(path);
		output.append("Cleared screenshot folder.");
		output.newLine();
	}
	System.out.println("Cleared screenshot folder.");

}

}
