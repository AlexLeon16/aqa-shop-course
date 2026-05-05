package ru.netology.helpers;

import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

public class DbHelper {
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                System.getProperty("db.url"),
                System.getProperty("db.user"),
                System.getProperty("db.password")
        );
    }

    public static void cleanDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM credit_request_entity");
            stmt.executeUpdate("DELETE FROM payment_entity");
            stmt.executeUpdate("DELETE FROM order_entity");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLatestPaymentStatus() {
        return queryForString("SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1");
    }

    public static String getLatestCreditStatus() {
        return queryForString("SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1");
    }

    public static int getPaymentCount() {
        return queryForInt("SELECT COUNT(*) FROM payment_entity");
    }

    public static int getCreditCount() {
        return queryForInt("SELECT COUNT(*) FROM credit_request_entity");
    }

    public static int getOrderCount() {
        return queryForInt("SELECT COUNT(*) FROM order_entity");
    }

    public static void assertNoCardDataSaved() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet tables = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String table = tables.getString("TABLE_NAME");
                try (ResultSet rows = stmt.executeQuery("SELECT * FROM " + table + " LIMIT 20")) {
                    ResultSetMetaData meta = rows.getMetaData();
                    while (rows.next()) {
                        for (int i = 1; i <= meta.getColumnCount(); i++) {
                            Object value = rows.getObject(i);
                            if (value != null) {
                                assertThat(String.valueOf(value))
                                        .doesNotContain("1111222233334444")
                                        .doesNotContain("1111 2222 3333 4444")
                                        .doesNotContain("5555666677778888")
                                        .doesNotContain("5555 6666 7777 8888");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String queryForString(String sql) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getString(1) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int queryForInt(String sql) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
