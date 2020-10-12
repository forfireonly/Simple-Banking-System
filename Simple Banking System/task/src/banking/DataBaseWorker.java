package banking;

import java.sql.*;

public class DataBaseWorker {

    private int balance;
    private String cardNumberEntered;
    private String pinNumberEntered;

    public int getBalance() {
        return balance;
    }

    public String getCardNumberEntered() {
        return cardNumberEntered;
    }

    public String getPinNumberEntered() {
        return pinNumberEntered;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setCardNumberEntered(String cardNumberEntered) {
        this.cardNumberEntered = cardNumberEntered;
    }

    public void  setPinNumberEntered(String pinNumberEntered) {
        this.pinNumberEntered = pinNumberEntered;
    }

    public void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:" + fileName;


        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER,\n"
                + "	number TEXT,\n"
                + "	pin TEXT,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + Main.fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public int getBalanceFromDataBase() {
        int balance = 0;
        String sql = "SELECT balance FROM card WHERE number = " + cardNumberEntered +
                " AND pin = " + pinNumberEntered +";";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            balance  = rs.getInt("balance");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    public boolean selectAll(){
        String sql = "SELECT number, pin, balance FROM card";
        boolean isExist = false;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                if (rs.getString("number").equals(cardNumberEntered) &&
                        rs.getString("pin").equals(pinNumberEntered)) {
                    System.out.println();
                    System.out.println("You have successfully logged in!");
                    balance = rs.getInt("balance");
                    System.out.println();
                    isExist = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isExist;
    }

    public boolean checkExistance(String card){
        String sql = "SELECT number FROM card ";
        boolean isExist = false;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                if (rs.getString("number").equals(card)) {
                    isExist = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isExist;
    }


    public void insert(int id, String number, String pin, int balance) {
        String sql = "INSERT INTO card(id,number,pin, balance) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, number);
            pstmt.setString(3, pin);
            pstmt.setInt(4, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void update(int balance) {
        String sql = "UPDATE card SET balance = ?"  +" WHERE number = " + cardNumberEntered +
        " AND pin = " + pinNumberEntered +";";




        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
             pstmt.setInt(1, balance);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transfer(String number, Integer balance) {
        String sql = "UPDATE card SET balance = ?"  +" WHERE number = ?" + ";";




        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, balance);
            pstmt.setString(2, number);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //balance of the card to which transfer
    public int getBalanceCardTransfer(String card) {
        //int balance = 0;
        String sql = "SELECT balance FROM card WHERE number = "  + card + ";";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            balance  = rs.getInt("balance");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    public void delete() {
        String sql = "DELETE FROM card WHERE number = " + cardNumberEntered +
                " AND pin = " + pinNumberEntered +";";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}