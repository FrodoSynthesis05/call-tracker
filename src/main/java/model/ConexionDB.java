/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Julian
 */
public class ConexionDB {

private Connection conexion;
private ResultSet rst;
private Statement sentencia;

private static final String driver = "com.mysql.cj.jdbc.Driver";
private static final String user = "root";
private static final String password = "root";
private static final String url = "jdbc:mysql://localhost:3306/work?characterEncoding=latin1";

/**
 * Class constructor accessible from elsewhere.
 */
public ConexionDB() {

}

/**
 * Establishes a connection with the SQL server and internally authenticates
 * user.
 *
 * @return Established connection.
 */
public Connection conectar() {
	this.conexion = null;

	try {
		Class.forName(driver);
		this.conexion = DriverManager.getConnection(ConexionDB.url, ConexionDB.user, ConexionDB.password);
	} catch (ClassNotFoundException | SQLException e) {
		System.out.println("Mysql()  ERROR :: " + e.getMessage());
	}
	return this.conexion;
}

/**
 * Get current connection.
 *
 * @return Current connection.
 */
public Connection getConnection() {
	return this.conexion;
}

/**
 * Closes currently open connections through a call to Connection.close().
 *
 * @throws SQLException If connection cannot be closed.
 */
public void closeConnection() throws SQLException {
	conexion.close();
}

/**
 * Create a new record on database.
 *
 * @param sql SQL statement to be used.
 * @return Executed update return value.
 * @throws SQLException If SQL server encounters an error with provided
 * statement.
 */
public int crear(String sql) throws SQLException {
	this.sentencia = this.conexion.createStatement();
	return sentencia.executeUpdate(sql);
}

/**
 * Reads existing records off of database table.
 *
 * @param sql SQL statement to be used.
 * @return Output for the provided SQL statement after processing by the server.
 * @throws SQLException If SQL server encounters an error with provided
 * statement.
 */
public ResultSet leer(String sql) throws SQLException {
	this.sentencia = this.conexion.createStatement();
	this.rst = sentencia.executeQuery(sql);
	return rst;
}

/**
 * Inserts new information onto pre-existing records on database table.
 *
 * @param sql SQL Statement to be used.
 * @return Executed update return value.
 * @throws SQLException If SQL server encounters an error with provided
 * statement.
 */
public int insertar(String sql) throws SQLException {
	this.sentencia = this.conexion.createStatement();
	return sentencia.executeUpdate(sql);
}

/**
 * Deletes record off of database table.
 *
 * @param sql SQL statement to be used.
 * @return Executed update return value.
 * @throws SQLException If SQL server encounters an error with provided
 * statement.
 */
public int eliminar(String sql) throws SQLException {
	this.sentencia = this.conexion.createStatement();
	return sentencia.executeUpdate(sql);
}
}
