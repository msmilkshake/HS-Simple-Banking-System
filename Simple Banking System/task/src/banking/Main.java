package banking;

import java.io.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            new UI(args).start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}