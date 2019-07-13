package de.dhbw.tinf18b4.chess.backend.utility;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Leonhard Gahr
 */
public class MySQLUtility {
    private static Connection createConnection() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("chessgame");
        dataSource.setPassword("tTrY4SsPs7Kl76sN");
        dataSource.setServerName("gahr.dev");
        dataSource.setDatabaseName("chessgame");
        dataSource.setServerTimezone("UTC");

        return dataSource.getConnection();
    }

    static @Nullable ResultSet executeQuery(String query, @NotNull Object... parameters) throws SQLException {
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
