/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.Controller;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julian
 */
public class SQL {

private final ConexionDB con;

/**
 * Class constructor.
 *
 * @param ctrl Program controller for linkage.
 */
public SQL(Controller ctrl) {
    this.con = new ConexionDB();
}

/**
 * Establishes a connection with SQL server.
 *
 * @return Established connection.
 */
public Connection conexion() {
    return con.conectar();
}

/**
 * Closes an existing connection with SQL server.
 *
 * @throws SQLException If connection cannot be successfully closed.
 */
public void cerrar() throws SQLException {
    con.closeConnection();
}

/**
 * Uploads call id detected by PanelBotones.OCR() to the calls table in the work
 * database.
 *
 * @param id Call id to be uploaded.
 */
public void uploadId(String id) {
    String idDown = "";
    try {
        ResultSet rst = con.leer("SELECT id FROM calls WHERE id = '" + id + "'");
        while (rst.next()) {
            idDown = rst.getString("id");
        }
        if ("".equals(idDown)) {
            con.insertar("INSERT INTO calls VALUES('" + id + "'" + "," + null + "," + null + "," + null + "," + null + ")");
        }

    } catch (SQLException ex) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
    }
}

/**
 * Updates length for a specific call to be identified by its id.
 *
 * @param id Call to be updated.
 * @param length Numerical value that denotes the duration of the call. Always
 * expressed in minutes for ease of calculation.
 */
public void uploadLength(String id, double length) {
    String idDown = "";
    try {
        ResultSet rst = con.leer("SELECT id FROM calls WHERE id = '" + id + "'");
        while (rst.next()) {
            idDown = rst.getString("id");
        }
        if ("".equals(idDown)) {
// con.insertar("INSERT INTO calls VALUES('" + id + "'," + null + "," + null + ",'" + length + "'," + null + "," + null + ")");
        } else {
            con.insertar("UPDATE calls SET length = '" + length + "' WHERE id = '" + id + "'");
        }

    } catch (SQLException ex) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
    }
}

/**
 * Converts the output of dollars() to pesos through an exchange rate API.
 *
 * @param id Call id to be updated.
 * @return The amount of money in pesos a particular call generated.
 * @throws IOException If API call is unsuccessful.
 */
public String pesos(String id) throws IOException {
    String pesos = "";
    double apiResult = API();
    double returnValue = apiResult == 0.0 ? API_Persistence() : apiResult;
    try {
        ResultSet rest = con.leer("SELECT " + id + ", dollars * " + returnValue + " AS pesos FROM calls WHERE id = '" + id + "'");
        while (rest.next()) {
            pesos = rest.getString("pesos");
        }
        if ("".equals(pesos)) {

        } else {
            double pesosDouble = Math.round(Double.parseDouble(pesos));
            con.insertar("UPDATE calls SET pesos = " + pesosDouble + "WHERE id = '" + id + "'");
            Date time = new java.util.Date(System.currentTimeMillis());
            try (BufferedWriter output = new BufferedWriter(new FileWriter("log.txt", true))) {
                output.append("Updated call " + id + " at " + new SimpleDateFormat("HH:mm:ss").format(time));
                System.out.println("Updated call " + id + " at " + new SimpleDateFormat("HH:mm:ss").format(time));
                output.newLine();
            }

        }
    } catch (SQLException ex) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
    }
    return pesos;

}

/**
 * Calculates the generated monetary value (expressed in US Dollars) generated
 * by a particular call.
 *
 * @param id Call id to be updated.
 * @return The amount of money in dollars a particular call generated.
 * @throws IOException If call to conversion to pesos is unsuccessful due to the
 * API
 */
public String dollars(String id) throws IOException {
    String dollars = "";
    try {
        ResultSet rst = con.leer("SELECT id, length * 0.14" + "AS dollars FROM calls WHERE id = '" + id + "'");
        while (rst.next()) {
            dollars = rst.getString("dollars");
        }
        if ("".equals(dollars)) {

        } else {
            con.insertar("UPDATE calls SET dollars = " + dollars + "WHERE id = '" + id + "'");
        }
    } catch (SQLException ex) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
    }
    pesos(id);
    return dollars;
}

/**
 * Uploads timestamp for book keeping and auditing purposes.
 *
 * @param time Timestamp to be uploaded.
 * @param id Call id to be associated with timestamp.
 */
public void uploadTimestamp(String time, String id) {
    try {
        con.insertar("UPDATE calls SET timestamp = '" + time + "' WHERE id = '" + id + "'");
    } catch (SQLException ex) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
    }
}

/**
 * Queries database to print out how much any particular call made in COP
 *
 * @param id Call id to be queried
 * @return Amount in COP call made.
 * @throws java.io.IOException
 */
public String readPesos(String id) throws IOException {
    String pesos = "";
    double length = 0;
    try {
        ResultSet rst = con.leer("SELECT pesos, length FROM calls WHERE id = '" + id + "'");
        while (rst.next()) {
            pesos = rst.getString("pesos");
            length = rst.getDouble("length");

        }
        try (BufferedWriter output = new BufferedWriter(new FileWriter("log.txt", true))) {
            if (length == 1) {
                System.out.println("Call " + id + " had a duration of " + length + " minute and made you $" + pesos + " COP");
                output.append("Call " + id + " had a duration of " + length + " minute and made you $" + pesos + " COP");
                output.newLine();
                System.out.println("-----------------------------");
                output.append("-----------------------------");
                output.newLine();
            } else {
                System.out.println("Call " + id + " had a duration of " + length + " minutes and made you $" + pesos + " COP");
                output.append("Call " + id + " had a duration of " + length + " minutes and made you $" + pesos + " COP");
                output.newLine();
                System.out.println("-----------------------------");
                output.append("-----------------------------");
                output.newLine();
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
    }

    return pesos;
}

/**
 * Fallback API method that uses a custom and demonstrably stable exchange rate
 * conversion system to be used in the event the API() method fails to return a
 * usable value
 *
 * @return The current exchange rate for 1 USD in Colombian Pesos.
 * @throws MalformedURLException If API URL resource cannot be accessed.
 * @throws IOException If connection to the API server cannot be established.
 */
public double API_Persistence() throws MalformedURLException, IOException {
    ConfigReader configReader = new ConfigReader("config.txt");
    String key = configReader.getAPIKey();

    URL url = new URL(key);
    HttpURLConnection request = (HttpURLConnection) url.openConnection();
    request.connect();

    JsonParser jp = new JsonParser();
    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
    JsonObject jsonobj = root.getAsJsonObject();

    String req_result = (String) jsonobj.get("rates").toString();
    req_result = req_result.replaceAll("\"COP\":", "").replaceAll("\\{", "").replaceAll("\\}", "");
    double exchangeRate = Double.parseDouble(req_result);
    System.out.println("API has been queried.");
    return (int) Math.round(exchangeRate / 10.0) * 10.0;

}

/**
 * Exchange rate API that returns conversion from 1 USD to COP.
 *
 * @return current exchange rate for 1 USD in COP
 * @throws MalformedURLException If the API access key is inaccessible
 * @throws IOException If BufferedWriter cannot write to log file or if
 * connection to the API server cannot be established.
 */
public Double API() throws MalformedURLException, IOException {
    String key = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/usd/cop.json";

    URL url = new URL(key);
    HttpURLConnection request = (HttpURLConnection) url.openConnection();
    request.connect();

    JsonParser jp = new JsonParser();
    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
    JsonObject jsonobj = root.getAsJsonObject();

    String req_result = (String) jsonobj.get("cop").toString();
    req_result = req_result.replaceAll("\"COP\":", "").replaceAll("\\{", "").replaceAll("\\}", "");
    double exchangeRate = Double.parseDouble(req_result);
    //System.out.println("API has been queried.");
    Double ret = Math.round(exchangeRate / 10.0) * 10.0;
    if (ret != null) {
        return ret;
    } else {
        try (BufferedWriter output = new BufferedWriter(new FileWriter("log.txt", true))) {
            output.append("Warning: API value is null. Fallback value used and persistance called.");
            output.newLine();
            System.out.println("Warning: API value is null. Fallback value used and persistance called.");
            return 0.0;
        }
    }

}

/**
 * Queries database to get a compendium of the values assigned to a particular
 * day.
 *
 * @param date Date to be queried, depends on user selection on UI calendar.
 * @return String comprised of message holding queried values
 * @throws IOException .
 */
public String queryDay(String date) throws IOException {
    double pesos;
    String dollars;
    String ret = "";
    double length;
//double ER = API();
    try {
        ResultSet rst = con.leer("SELECT timestamp, sum(dollars), sum(length), sum(pesos) FROM calls WHERE DATE(timestamp) = '" + date + "'");
        while (rst.next()) {
            length = rst.getDouble("sum(length)");
            dollars = rst.getString("sum(dollars)");
            pesos = rst.getDouble("sum(pesos)");
            double dollarsD = Double.parseDouble(dollars);
            ret = "On date " + date + " you made a total of $" + Math.round(dollarsD) + " dollars which translates to $" + Math.round(pesos) + " pesos and worked for a total of " + length + " minutes.";

        }

    } catch (SQLException e) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, e);
    }
    return ret;
}

/**
 *
 * @param id
 * @return
 * @throws IOException
 */
public String queryCall(String id) throws IOException {
    double pesos;
    double length;
    double dollars;
    String ret = "";
    try {
        ResultSet rst = con.leer("SELECT pesos, length, dollars FROM calls WHERE id = '" + id + "'");
        while (rst.next()) {
            pesos = rst.getDouble("pesos");
            length = rst.getDouble("length");
            dollars = rst.getDouble("dollars");
            ret = "Call " + id + " made $" + dollars + " which translates to $" + Math.round(pesos) + " and lasted for " + length + " minutes.";

        }
    } catch (SQLException e) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, e);
    }
    return ret;

}

/**
 *
 * @param id
 * @return
 * @throws IOException
 */
public String getTimestamp(String id) throws IOException {

    String timeStamp = "";

    try {
        ResultSet rst = con.leer("SELECT timestamp FROM calls WHERE id = '" + id + "'");
        while (rst.next()) {
            timeStamp = rst.getString("timestamp");

        }
    } catch (SQLException e) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, e);
    }
    return timeStamp;
}

/**
 *
 * @param id
 * @param length
 * @param pesos
 * @param dollars
 * @throws IOException
 */
public void override(String id, double length, String pesos, String dollars) throws IOException {
    uploadLength(id, length);
    dollars(id);

}

/**
 * Queries database for the entire current month and returns sums for all
 * relevant values
 *
 * @param month
 * @param year
 * @return String containing processed data
 */
public String queryMonth(int month, int year) {
    String dollarsStr;
    String lengthStr;
    String pesosStr;
    String ret = "";
    try {
        ResultSet rst = con.leer("SELECT sum(length), SUM(pesos), sum(dollars) FROM calls WHERE timestamp >= '" + year + "-" + month + "-01' AND timestamp <= NOW()");
        while (rst.next()) {
            dollarsStr = rst.getString("sum(dollars)");
            lengthStr = rst.getString("sum(length)");
            double dollarsD = Double.parseDouble(dollarsStr);
            double pesosD = dollarsD * API();
            ret = "So far this month you have made $" + Math.round(dollarsD) + " USD which translates to $" + Math.round(pesosD) + "  COP and have been on the phone for a grand total of " + lengthStr + " minutes.";
        }
    } catch (SQLException | IOException e) {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, e);
    }
    return ret;
}
}
