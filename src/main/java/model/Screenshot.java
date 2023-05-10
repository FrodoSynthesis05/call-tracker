/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;

/**
 * This class is in charge of taking a screenshot of the main monitor and
 * storing it on a predefined project hosted folder for further OCR analysis.
 *
 * @author Julian
 */
public class Screenshot {

BufferedImage Image;
File file;
private String timeStamp;

/**
 * Takes a screenshot of the entire screen and stores it under /data/images. It
 * names the screenshot the complete timestamp of when it was taken.
 *
 * @return Created image file containing screenshot.
 */
public File Screenshot() {
	try {

		Thread.sleep(120);
		Robot r = new Robot();

		String path = "D://WorkCalculator//images//";
		timeStamp = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date());

		file = new File(path + timeStamp + ".jpg");
		Rectangle capture
			= new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		Image = r.createScreenCapture(capture);

		Graphics2D g2d = Image.createGraphics();
		g2d.setColor(Color.RED);
		g2d.drawRect(245, 328, 73, 20);
		g2d.dispose();

		ImageIO.write(Image, "jpg", file);

	} catch (AWTException | IOException | InterruptedException ex) {
		System.out.println(ex);
	}
	return file;
}

/**
 * Returns the timestamp assigned to the created image, AKA the name.
 *
 * @return The name of the file that was created.
 */
public String getTimeStamp() {
	return timeStamp;
}

}
