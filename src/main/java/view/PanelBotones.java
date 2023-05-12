/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package view;

import controller.Controller;
import model.Screenshot;
import model.Work;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang3.time.StopWatch;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import javax.swing.JFrame;
import com.toedter.calendar.JCalendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Initializes buttons to be added to the panel.
 *
 * @author Julian
 */
public class PanelBotones extends JPanel implements ActionListener, HotkeyListener, IntellitypeListener {

private final JButton start;
private final JButton stop;
Controller ctrl;
private String out;
private final StopWatch stopWatch;
private final Screenshot sc;
private static final int SEMICOLON = 94;
private static final int TILDE = 96;
private static final int ALT_S = 97;
private JTextArea textArea;
Font font = new Font("Cascadia Mono", Font.PLAIN, 15);
private JCalendar cal;
private SimpleDateFormat dateFormat;
private JTextField lengthField;
private JTextField pesosField;
private JTextField dollarsField;
private JTextField idField;
private int checker = 0;

/**
 * Class constructor.
 */
public PanelBotones() {

	this.stopWatch = new StopWatch();
	this.ctrl = new Controller();
	this.sc = new Screenshot();

	setLayout(null);

	start = new JButton("\u23F5");
	start.setFont(font);
	start.setBounds(0, 0, 40, 40);
	add(start);

	stop = new JButton("\u23F9");
	stop.setFont(font);
	stop.setBounds(0, 57, 40, 40);
	add(stop);

	btnRegisterHotKey_actionPerformed();

	start.addActionListener(e -> actionPerformed(e));
	stop.addActionListener(a -> {
		try {
			ActionPerformed(a);
		} catch (IOException ex) {
			Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
		}
	});
	ctrl.conexion();

}

/**
 * If the start button is clicked, invoke screenshot and calls OCR to analyze
 * the image.
 *
 * @param e Interaction with start button.
 */
@Override
public void actionPerformed(ActionEvent e) {
	CompletableFuture.supplyAsync(() -> {
		try {
			try {
				if (checker != 1) {
					out = Tesseract(sc.Screenshot());
				} else {
				}
			} catch (IOException ex) {
				Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
			}
			return out;
		} catch (TesseractException ex) {
			Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
			return "";
		}
	}).thenAcceptAsync((String result) -> {
		try {
			if (!"".equals(result) && containsOnlyNumbers(result) && checker != 1) {
				if (containsOnlyNumbers(result)) {
					checker = 1;
					execute();
					String path = "D://WorkCalculator//Work//src//main//java//resources//start.wav";
					reminder(path);
				}
			} else if (checker == 1) {
				System.out.println("A call is already being tracked. ");
			} else {
				try (BufferedWriter output = new BufferedWriter(new FileWriter("log.txt", true))) {
					System.out.println("Invalid call ID, please try again.");
					output.append("Invalid call ID, please try again.");
					output.newLine();
				} catch (IOException ex) {
					Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} catch (UnsupportedAudioFileException | InterruptedException | IOException ex) {
			Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
		}
	});
}

/**
 * If OCR output is empty, that usually means that the button was pressed too
 * soon, if that is the case, the program will throw a warning and tell the user
 * to try again.If OCR output is detected to be of value != null then this
 * method is executed starting a stopwatch and warning the user that a call has
 * been detected and is being tracked.
 *
 * @throws java.io.IOException
 */
public void execute() throws IOException {
	CompletableFuture.runAsync(() -> {
		try (BufferedWriter output = new BufferedWriter(new FileWriter("log.txt", true))) {
			try {
				stopWatch.start();
			} catch (IllegalStateException ec) {
				System.out.println("Stopwatch is most likely running already.");
				output.append("Stopwatch is most likely running already.");
				output.newLine();
				return;
			}
			Date time = new java.util.Date(System.currentTimeMillis());
			System.out.println("Started tracking call " + out + " on date " + new SimpleDateFormat("yyyy-MM-dd").format(time) + " at " + new SimpleDateFormat("HH:mm:ss").format(time));
			output.append("Started tracking call " + out + " on date " + new SimpleDateFormat("yyyy-MM-dd").format(time) + " at " + new SimpleDateFormat("HH:mm:ss").format(time));
			output.newLine();
		} catch (IOException ex) {
			Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
		}
	}).thenRunAsync(() -> {
		ctrl.uploadID(out);
	});
}

/**
 * If the stop button is pressed, this method will alert the user of the
 * duration of the call that just ended and call the controller to update the
 * monetary and tracking information on the database.After 5 minutes of being
 * clicked, it will alert the user that is time to go back on available via an
 * audible warning.
 *
 * @param a Interaction with stop button.
 * @throws java.io.IOException
 */
public void ActionPerformed(ActionEvent a) throws IOException {
	CompletableFuture.runAsync(() -> {
		try (BufferedWriter output = new BufferedWriter(new FileWriter("log.txt", true))) {
			if (checker != 0) {
				long time = Math.round(TimeUnit.MILLISECONDS.toMinutes(stopWatch.getTime()));
				try {
					stopWatch.reset();
				} catch (IllegalStateException ex) {
					System.out.println("StopWatch is not running.");
					output.append("StopWatch is not running.");
					output.newLine();
					return;
				}

				ctrl.uploadLength(out, time);
				ctrl.dollars(out);
				ctrl.readPesos(out);
				ctrl.timestamp(out, sc.getTimeStamp());

				try {
					String path = "D://WorkCalculator//Work//src//main//java//resources//sound.wav";
					reminder(path);
					checker = 0;
				} catch (UnsupportedAudioFileException | IOException | InterruptedException ex) {
					Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
				}
			} else {
				output.append("There are no calls being tracked right now.");
				output.newLine();
				System.out.println("There are no calls being tracked right now.");
			}
		} catch (IOException ex) {
			Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
		}
	});
}

/**
 * Calls audio stream to play sound when starting and stopping the timer.
 *
 * @param path File path to the audio clip to be played.
 * @throws UnsupportedAudioFileException If provided audio clip is unsupported
 * by Java.
 * @throws IOException If audio clip cannot be accessed by Java.
 * @throws java.lang.InterruptedException
 */
public void reminder(String path) throws UnsupportedAudioFileException, IOException, InterruptedException {

	String soundName = path;
	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
	Clip clip;
	try {
		clip = AudioSystem.getClip();
		clip.open(audioInputStream);
		FloatControl gainControl
			= (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-20.0f); // Reduce volume by 10 decibels.
		clip.start();
	} catch (LineUnavailableException ex) {
		Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
	}

}

/**
 * OCR method, takes an image file and based on coordinates describing a
 * rectangle analyses the provided image within those restraints to finally
 * detect a call id number that can be stored in the database.
 *
 * @param Image Image file to be analyzed.
 * @return Detected call id number as a String.
 * @throws TesseractException If OCR process is unsuccessful.
 * @throws java.io.IOException
 */
public static String Tesseract(File Image) throws TesseractException, IOException {
	Rectangle bounds = new Rectangle(245, 328, 73, 20);
	Tesseract tess = new Tesseract();
	String tessPath = "D://OpenCV//TesseractOCR//tesseract//Code//tesseract-main//tesseract-main//tessdata";
	tess.setDatapath(tessPath);
	String text = tess.doOCR(Image, bounds);

	return text.replaceAll("\n", "");
}

/**
 * Initialize the JInitellitype library making sure the DLL is located.
 */
public void initJIntellitype() {
	try {
		JIntellitype.getInstance().addHotKeyListener(this);
		JIntellitype.getInstance().addIntellitypeListener(this);
	} catch (RuntimeException ex) {
		System.out.println("Either you are not on Windows, or there is a problem with the JIntellitype library!");
	}
}

/**
 * Method to register a hotkey using the RegisterHotKey Windows API call.
 * <p>
 *
 * @param aEvent the ActionEvent fired.
 */
private void btnRegisterHotKey_actionPerformed() {

	JIntellitype.getInstance().registerHotKey(SEMICOLON, 0, 186);
	JIntellitype.getInstance().registerHotKey(TILDE, 0, 222);
	JIntellitype.getInstance().registerHotKey(ALT_S, JIntellitype.MOD_ALT, 83);

}

/**
 * Whenever the system detects hotkeys " ; ", " ' " and Alt + S the program will
 * perform different actions based on the hotkey received.
 *
 * @param i Hotkey system registered.
 */
@Override
public void onHotKey(int i) {

	switch (i) {
		case 94 -> {

			ActionEvent e = null;
			actionPerformed(e);
		}
		case 96 -> {
			try {
				ActionEvent a = null;
				ActionPerformed(a);
			} catch (IOException ex) {
				Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
		case 97 -> {

			textArea = new JTextArea();
			cal = new JCalendar();
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//textField.setBorder(BorderFactory.createLineBorder(Color.black));
			JFrame newFrame = new JFrame("Income");
			try {
				newFrame.setIconImage(ImageIO.read(new File("D://WorkCalculator//Work//src//main//java//resources//icon.png")));
			} catch (IOException ex) {
				Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
			}
			newFrame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					newFrame.dispose();
				}
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
					pesosField = new JTextField();
					dollarsField = new JTextField();
					lengthField = new JTextField();
					idField = new JTextField();
					JDialog dialog = new JDialog(newFrame, "Override", true);
					dialog.setLocationRelativeTo(null);
					dialog.setPreferredSize(new Dimension(200, 100));
					dialog.setLayout(new GridLayout(4, 2));
					dialog.add(new JLabel("ID:"));
					dialog.add(idField);
					dialog.add(new JLabel("Length:"));
					dialog.add(lengthField);
					dialog.add(new JLabel("Pesos:"));
					dialog.add(pesosField);
					dialog.add(new JLabel("Dollars:"));
					dialog.add(dollarsField);
					idField.setHorizontalAlignment(SwingConstants.CENTER);
					lengthField.setHorizontalAlignment(SwingConstants.CENTER);
					lengthField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							try {
								double callDollars = Math.round(Integer.parseInt(lengthField.getText()) * 0.14);
								System.out.println(callDollars);
								dollarsField.setText(String.valueOf(callDollars));
								double callPesos = Math.round(ctrl.API(callDollars));
								pesosField.setText(String.valueOf(callPesos));
							} catch (IOException ex) {
								Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}
					});
					pesosField.setHorizontalAlignment(SwingConstants.CENTER);
					dollarsField.setHorizontalAlignment(SwingConstants.CENTER);
					Color backgroundColor = idField.getBackground();
					idField.setCaretColor(backgroundColor);
					lengthField.setCaretColor(backgroundColor);
					pesosField.setCaretColor(backgroundColor);
					dollarsField.setCaretColor(backgroundColor);
					dollarsField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							CompletableFuture.supplyAsync(() -> {
								try {
									double length = Double.parseDouble(lengthField.getText());
									ctrl.override(idField.getText(), length, pesosField.getText(), dollarsField.getText());
									return "Call information successfully overridden.";
								} catch (IOException ex) {
									Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
									return "An error occurred while overriding call information.";
								}
							}).thenAccept(result -> {
								idField.setText("");
								lengthField.setText("");
								dollarsField.setText("");
								pesosField.setText("");
								updateText(result);
								dialog.dispose();
							});
						}
					}
					});
					dialog.setUndecorated(false);
					dialog.setResizable(false);
					dialog.pack();
					dialog.setVisible(true);

				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
			});
			newFrame.setFocusable(true);
			newFrame.requestFocus();
			newFrame.setLayout(new FlowLayout());
			JTextField query = new JTextField();
			query.setText("?");
			query.setFont(font);

			query.setAlignmentX(BOTTOM_ALIGNMENT);
//query.setBorder(BorderFactory.createLineBorder(Color.black));
			Dimension size = new Dimension(80, 30);
			query.setPreferredSize(size);
			query.setHorizontalAlignment(SwingConstants.CENTER);
			query.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				query.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
// do nothing
			}
			});
			query.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String input = query.getText();

					if (input.equals("month")) {
						Calendar calendar = cal.getCalendar();
						int currentYear = calendar.get(Calendar.YEAR);
						int currentMonth = calendar.get(Calendar.MONTH) + 1;
						CompletableFuture.runAsync(() -> {
							String result = ctrl.queryMonth(currentMonth, currentYear);
							updateText(result);
						});
					} else {
						CompletableFuture.supplyAsync(() -> {
							try {
								return ctrl.queryCall(input);
							} catch (IOException ex) {
								Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
								return null;
							}
						}).thenAccept(result -> {
							updateText(result);
							try {
								Date date = dateFormat.parse(ctrl.getTimestamp(input));
								Calendar c = Calendar.getInstance();
								c.setTime(date);
								cal.setCalendar(c);
							} catch (IOException | ParseException ex) {
								Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
							}
						});
					}
				}
			}

			});

			newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			newFrame.setSize(415, 465);
			newFrame.setLocationRelativeTo(null);
			newFrame.setVisible(true);
			cal.setPreferredSize(new Dimension(400, 300));
			textArea.setSize(350, 300);
			newFrame.add(cal);
			newFrame.add(textArea);
			newFrame.setResizable(false);
			textArea.setEditable(false);
			Color backgroundColor = textArea.getBackground();
			textArea.setCaretColor(backgroundColor);
			newFrame.add(query);
			cal.addPropertyChangeListener((PropertyChangeEvent e) -> {
				if (e.getPropertyName().equals("calendar")) {

					Date selectedDate = cal.getDate();

					String dateString = dateFormat.format(selectedDate);
					try {
						updateText(ctrl.queryDay(dateString));
					} catch (IOException ex) {
						Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
					}
					try {
						ctrl.queryDay(dateFormat.format(selectedDate));
					} catch (IOException ex) {
						Logger.getLogger(PanelBotones.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			});

		}

		default -> {
		}
	}
}

/**
 * Not used in current implementation.
 *
 * @param i null
 */
@Override
public void onIntellitype(int i) {
}

/**
 * Updates text on the JTextArea under the JCalendar on the Income window.
 *
 * @param text Text to be inserted into the text area.
 */
public void updateText(String text) {
	textArea.setText("");
	textArea.setText(text);
}

/**
 * Checks that the ID number returned by OCR contains numbers only, different
 * behaviors are triggered depending on the output of this method.
 *
 * @param str String to be assessed for numbers
 * @return true if string is only comprised of numbers, otherwise false.
 */
public static boolean containsOnlyNumbers(String str) {
	return str.matches("[0-9]+");
}

}
