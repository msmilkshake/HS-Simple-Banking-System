package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    private static DB instance;

    private SQLiteDataSource src = new SQLiteDataSource();

    private DB() {
    }

    public static DB getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DB not initialized. Call" +
                    "DB.init() before getting instance");
        }
        return instance;
    }

    public static void init(String path) {
        instance = new DB();
        if (path == null) {
            throw new IllegalArgumentException("-fileName command line argument" +
                    " not specified.");
        }
        instance.src.setUrl("jdbc:sqlite:" + path);
        instance.create();
    }

    private void create() {
        String query = "" +
                "CREATE TABLE IF NOT EXISTS card ( " +
                "    id INTEGER PRIMARY KEY," +
                "    number TEXT NOT NULL, " +
                "    pin TEXT NOT NULL, " +
                "    balance INTEGER DEFAULT 0 " +
                ");";
        try (Connection conn = src.getConnection();
             Statement s = conn.createStatement()) {
            s.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String query) {
        try (Connection conn = src.getConnection();
             Statement s = conn.createStatement();
             ResultSet result = s.executeQuery(query)) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean queryCard(String cardNum, String pin) {
        String cardQuery = "SELECT * FROM card WHERE number = %s;";
        try (Connection conn = src.getConnection();
             Statement s = conn.createStatement();
             ResultSet result = s.executeQuery(
                     String.format(cardQuery, cardNum))) {
            boolean success = result.next();
            System.err.println("Got Id: " + (success ?
                    result.getInt(1) : "NOT FOUND"));
            return  success &&
                    result.getString(2).equals(cardNum) &&
                    result.getString(3).equals(pin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cardExists(String cardNum) {
        String query = String.format("" +
                "SELECT number " +
                "FROM card " +
                "WHERE number = '%s';",
                cardNum);
        boolean exists = true;
        try (Connection conn = src.getConnection();
             Statement s = conn.createStatement();
             ResultSet result = s.executeQuery(query)) {
            exists = result.next();
            System.err.println("Card exists -- " + exists);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public void insertCard(Card card) {
        String query = String.format("" +
                "INSERT INTO card (id, number, pin) " +
                "VALUES (NULL, '%s', '%s');",
                card, card.getPin());
        try (Connection conn = src.getConnection();
             Statement s = conn.createStatement()) {
            int result = s.executeUpdate(query);
            if (result == 1) {
                System.err.println("Card insert -- SUCCESS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBalance(String cardNumber) {
        String query = String.format("" +
                "SELECT balance " +
                "FROM card " +
                "WHERE number = '%s';",
                cardNumber);
        try (Connection conn = src.getConnection();
             Statement s = conn.createStatement();
             ResultSet result = s.executeQuery(query)) {
            System.err.print("Get balance -- ");
            if (result.next()) {
                System.err.println("SUCCESS");
                return result.getInt(1);
            } else {
                System.err.println("NO RESULTS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void query(String query) {
        try (Connection conn = src.getConnection();
             Statement s = conn.createStatement();
             ResultSet result = s.executeQuery(query)) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
