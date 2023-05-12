/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.SQL;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julian
 */
public class Controller {

private final SQL query;

/**
 * Controller class accessible from other classes.
 */
public Controller() {
	query = new SQL(this);

}

/**
 * Method responsible of linking SQL.dollars() with the rest of the program.
 *
 * @param id Call id to be updated.
 */
public void dollars(String id) {

	try {
		query.dollars(id);
	} catch (IOException ex) {
		Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
	}

}

/**
 * Method responsible of linking SQL.pesos() with the rest of the program.
 *
 * @param id Call id to be updated.
 */
public void pesos(String id) {
	try {
		query.pesos(id);
	} catch (IOException ex) {
		Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
	}
}

/**
 * Method responsible of linking SQL.readPesos() with the rest of the program.
 *
 * @param id Call id to be queried.
 */
public void readPesos(String id) {
	try {
		query.readPesos(id);
	} catch (IOException ex) {
		Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
	}
}

/**
 * Method responsible of linking SQL.uploadID() with the rest of the program.
 *
 * @param id Call id to be uploaded.
 */
public void uploadID(String id) {
	query.uploadId(id);
}

/**
 * Method responsible of linking SQL.uploadLength() with the rest of the
 * program.
 *
 * @param id Call id to be updated.
 * @param length Call length to be associated with call id.
 */
public void uploadLength(String id, double length) {
	query.uploadLength(id, length);
}

/**
 * Method responsible of linking SQL.conexion() with the rest of the program.
 *
 * @return SQL server connection.
 */
public Connection conexion() {
	return query.conexion();
}

/**
 * Method responsible of linking SQL.cerrar() with the rest of the program.
 *
 * @throws SQLException If connection can not be closed.
 */
public void cerrar() throws SQLException {
	query.cerrar();
}

/**
 * Method responsible of linking SQL.uploadTimestamp() with the rest of the
 * program.
 *
 * @param id Call id to be updated.
 * @param time Timestamp to be associated with Call id.
 */
public void timestamp(String id, String time) {
	query.uploadTimestamp(time, id);
}

/**
 *
 * @param date
 * @return
 * @throws java.io.IOException
 */
public String queryDay(String date) throws IOException {
	return query.queryDay(date);
}

/**
 *
 * @param id
 * @return
 * @throws java.io.IOException
 */
public String queryCall(String id) throws IOException {
	return query.queryCall(id);
}

/**
 * Gets timestamp for a particular call.
 *
 * @param id Call to be queried
 * @return Timestamp in the database
 * @throws IOException IF retrieval process fails.
 */
public String getTimestamp(String id) throws IOException {
	return query.getTimestamp(id);
}

/**
 * Overrides all call information, length, pesos and dollars
 *
 * @param id Call to be overridden.
 * @param length length parameter to be added.
 * @param pesos pesos parameter to be added.
 * @param dollars dollars parameter to be added.
 * @throws IOException If process fails.
 */
public void override(String id, double length, String pesos, String dollars) throws IOException {
	query.override(id, length, pesos, dollars);
}

/**
 * Queries main API method and converts the value that is passed into pesos.
 *
 * @param amount The dollar amount to be converted.
 * @return Converted value in pesos.
 * @throws IOException If process fails.
 */
public double API(double amount) throws IOException {
	return query.API() * amount;
}

/**
 * Links SQL.queryMonth with the rest of the program.
 *
 * @param month Month to be queried
 * @param year
 * @return String of processed data.
 */
public String queryMonth(int month, int year) {
	return query.queryMonth(month, year);

}

}
