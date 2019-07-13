package de.dhbw.tinf18b4.chess.backend.utility;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility functions for the mysql connectivity. <br>
 * This class provides the SQL connection to a remote server and allows to
 * perform a prepared statement with parameters on that remote server
 */
public class MySQLUtility {
    /**
     * Create a new mysql {@link Connection} to a static specified server and database and return it
     *
     * @return the {@link Connection}
     * @throws SQLException on connection failures
     */
    private static Connection createConnection() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("chessgame");
        dataSource.setPassword("tTrY4SsPs7Kl76sN");
        dataSource.setServerName("gahr.dev");
        dataSource.setDatabaseName("chessgame");
        dataSource.setServerTimezone("UTC");

        return dataSource.getConnection();
    }

    /**
     * Execute a query as prepared statement.
     * The statement can already include variable parameters
     * but it is highly recommended to use the parametrization of the prepared statement class <br>
     * Example of a query: "SELECT * FROM user WHERE username=? AND password=?" <br>
     * The questionmarks indicate that these values will be replaced by the parameters passed to this function.
     * They may be of any type, but it is recommended to use only "traditional" types like
     * int, String, bool, float, double, array (of any of these types)...<br>
     * Also note that the number of questionmarks in the query string must match the
     * number of the parameters passed, otherwise there will be a {@link SQLException}
     *
     * @param query      the sql query to execute
     * @param parameters the parameters to insert to the query
     * @return the {@link ResultSet} of the executed statement or null, if it wasn't a select operation
     * @throws SQLException on syntax error or parameter errors
     */
    static ResultSet executeQuery(String query, Object... parameters) throws SQLException {
        Connection con = createConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        // set the parameters, if there are some
        for (int i = 0; i < parameters.length; i++) {
            stmt.setObject(i + 1, parameters[i]);
        }

        if (stmt.execute()) {
            return stmt.getResultSet();
        }
        return null;
    }
}
