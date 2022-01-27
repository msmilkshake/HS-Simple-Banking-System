package banking;

import org.sqlite.*;

import java.sql.*;

public class DB {
    private Connection conn;

    public DB(String path) throws SQLException {
        if (path == null) {
            throw new IllegalArgumentException("-fileName command line argument" +
                    " not specified.");
        }
        SQLiteDataSource src = new SQLiteDataSource();
        src.setUrl("jdbc:sqlite:" + path);
        conn = src.getConnection();
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }
}
