import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class rubiksDB {


    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/vet";     //Connection string – where's the database?
    static final String USER = "sczapuchlak";   //TODO replace with your username
    static final String PASSWORD = "stardust123";   //TODO replace with your password

    public static void main(String[] args) {
        Scanner newScanner = new Scanner(System.in);
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drivers and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }

        //Try with resources to open the connection and create a statement. Make sure your language level is 1.7+
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {
            //You should have already created a database via terminal/command prompt OR MySQL Workbench

            //Create a table in the database, if it does not exist already
            String createTableSQL = "CREATE TABLE IF NOT EXISTS rubiks_cube (Things_that_can_solve_cube varchar(100), Time_taken_in_seconds double)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Created Rubiks Cube table");

            //Add some data
            String preparedStatementInsert = "INSERT INTO rubiks_cube VALUES(?,?)";
            PreparedStatement psInsert = conn.prepareStatement(preparedStatementInsert);
            psInsert.setString(1, "PoppCubestormer II robot");
            psInsert.setDouble(2, 5.270);
            psInsert.executeUpdate();
            //next insert
            psInsert.setString(1, "Fakhri Raihaan (using his feet)");
            psInsert.setDouble(2, 27.93);
            psInsert.executeUpdate();
            //next insert
            psInsert.setString(1, "Ruxin Liu (age 3)");
            psInsert.setDouble(2, 99.33);
            psInsert.executeUpdate();
            //next insert
            psInsert.setString(1, "Mats Valk (human record holder)");
            psInsert.setDouble(2, 6.27);
            psInsert.executeUpdate();
            //ask user for the thing and then the time
            System.out.println("Please enter the thing that can solve the rubik's cube: ");
            String thing_that_solves = newScanner.nextLine();

            System.out.println("Please enter the time it was completed (in seconds): ");
            Double time_solved = newScanner.nextDouble();

            //create a statement from the user input
            String sql_userinput = "INSERT INTO rubiks_cube VALUES(?,?)";
            psInsert.setString(1, thing_that_solves);
            psInsert.setDouble(2, time_solved);
            psInsert.executeUpdate();

            String fetchAllDataSQL = "SELECT * FROM rubiks_cube";
            ResultSet rs = statement.executeQuery(fetchAllDataSQL);

            while (rs.next()) {
                String Things_that_can_solve_cube = rs.getString("Things_that_can_solve_cube");
                double Time_taken_in_seconds = rs.getInt("Time_taken_in_seconds");
                System.out.println("Things That can Solve The Rubik's Cube = " + Things_that_can_solve_cube + " Time Taken = " + Time_taken_in_seconds);
            }

            rs.close();

            statement.close();
            conn.close();

        } catch(SQLException se){
            se.printStackTrace();
            System.exit(-1);
        }

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {

            Scanner scan = new Scanner(System.in);

            while (true) {
                System.out.println("Enter thing/name to find in database, or enter to continue program");
                String thing_or_name = scan.nextLine();
                if (thing_or_name.equals("")) {
                    break;
                }

                // Have to convert all data to uppercase and compare (or convert to lower and compare)
                PreparedStatement findThing = conn.prepareStatement("SELECT * FROM rubiks_cube where UPPER(Things_that_can_solve_cube) = UPPER(?)");

                findThing.setString(1, thing_or_name);

                ResultSet rs = findThing.executeQuery();

                boolean Thingfound = false;
                while (rs.next()) {
                    Thingfound = true;
                    String nameOrThing = rs.getString("Things_that_can_solve_cube");
                    Double timeFound = rs.getDouble("Time_taken_in_seconds");
                    System.out.println("Thing or Name That Completed the Rubix Cube: " + nameOrThing +"In this amount of time:"+timeFound);
                    System.out.println("Update the time for this record? Press Enter to continue or enter the time");
                    Double newTime = newScanner.nextDouble();
                    System.out.println("Updating database. Hold on one second!");
                    String updateRecord = "UPDATE rubix_cube SET Time_taken_in_seconds= newTime WHERE name = nameOrThing";
                    System.out.println(nameOrThing+ "Updated!");
                }
                if (!Thingfound) {
                    System.out.println("Sorry, that person or thing was not found in the system!");
                }

                rs.close();
            }

            conn.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(-1);
        }

    }
}


