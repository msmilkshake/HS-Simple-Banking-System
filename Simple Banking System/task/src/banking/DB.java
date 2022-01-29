package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    
    private static DB instance;
    
    private final SQLiteDataSource SRC = new SQLiteDataSource();
    
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
        instance.SRC.setUrl("jdbc:sqlite:" + path);
        instance.create();
    }
    
    private void create() {
        String query = "CREATE TABLE IF NOT EXISTS card ( " +
                "    id INTEGER PRIMARY KEY," +
                "    number TEXT NOT NULL, " +
                "    pin TEXT NOT NULL, " +
                "    balance INTEGER DEFAULT 0 " +
                ");";
        try (Connection conn = SRC.getConnection();
             Statement s = conn.createStatement()) {
            s.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean matchCardInfo(String cardNum, String pin) {
        String cardQuery = "SELECT * FROM card WHERE number = ?";
        boolean success = false;
        try (Connection conn = SRC.getConnection();
             PreparedStatement s = conn.prepareStatement(cardQuery)) {
            s.setString(1, cardNum);
            try (ResultSet result = s.executeQuery()) {
                success = result.next() &&
                        result.getString(2).equals(cardNum) &&
                        result.getString(3).equals(pin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    
    public boolean cardExists(String card) {
        String query = "SELECT number " +
                "FROM card " +
                "WHERE number = ?";
        boolean exists = true;
        try (Connection conn = SRC.getConnection();
             PreparedStatement s = conn.prepareStatement(query)) {
            s.setString(1, card);
            try (ResultSet result = s.executeQuery()) {
                exists = result.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    
    public void insertCard(Card card) {
        String query = "INSERT INTO card (id, number, pin) " +
                "VALUES (NULL, ?, ?)";
        try (Connection conn = SRC.getConnection();
             PreparedStatement s = conn.prepareStatement(query)) {
            s.setString(1, card.toString());
            s.setString(2, card.getPin());
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getBalance(Card card) {
        String query = "SELECT balance " +
                "FROM card " +
                "WHERE number = ?";
        try (Connection conn = SRC.getConnection();
             PreparedStatement s = conn.prepareStatement(query)) {
            s.setString(1, card.toString());
            try (ResultSet result = s.executeQuery()) {
                if (result.next()) {
                    return result.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public int addMoney(Card card, int money) {
        String query = "UPDATE card " +
                "SET balance = balance + ? " +
                "WHERE number = ?";
        try (Connection conn = SRC.getConnection();
             PreparedStatement s = conn.prepareStatement(query)) {
            s.setInt(1, money);
            s.setString(2, card.toString());
            return s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean transfer(Card sender, String receiver, int amount) {
        String updateSender = "UPDATE card " +
                "SET balance = balance - ? " +
                "WHERE number = ?";
        String updateReceiver = "UPDATE card " +
                "SET balance = balance + ? " +
                "WHERE number = ?";
        try (Connection conn = SRC.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement sendQuery = conn.prepareStatement(updateSender);
                 PreparedStatement receiveQuery = conn.prepareStatement(updateReceiver)) {
                if (getBalance(sender) < amount) {
                    return false;
                }
                sendQuery.setInt(1, amount);
                sendQuery.setString(2, sender.toString());
                sendQuery.executeUpdate();
                receiveQuery.setInt(1, amount);
                receiveQuery.setString(2, receiver);
                receiveQuery.executeUpdate();
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void deleteCard(Card card) {
        String query = "DELETE FROM card WHERE number = ?";
        try (Connection conn = SRC.getConnection();
             PreparedStatement s = conn.prepareStatement(query)) {
            s.setString(1, card.toString());
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
