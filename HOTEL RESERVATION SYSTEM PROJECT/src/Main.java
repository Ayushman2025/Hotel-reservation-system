import java.sql.*;
import java.util.Scanner;


public class Main {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/hotel_management";
    private static final String username = "root";
    private static final String password = "Aish@2004";

    public static void main(String[] args) {

        Scanner mm = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException f){
            System.out.println(f);
        }


        try {
            Connection connection = DriverManager.getConnection(url, username, password);

         while (true){
             System.out.println("=======Main menu======");
             System.out.println("1. New Reservation");
             System.out.println("2. View Reservation");
             System.out.println("3.Get Room Number");
             System.out.println("4. Update Reservation");
             System.out.println("5.Delete Reservation");
             System.out.println("6. Exit");
             System.out.println("=======================");
//             mm.nextLine();
             System.out.println("Enter your choice");
             int choice = mm.nextInt();
             mm.nextLine();
             switch (choice){
                 case 1: reservation(connection,mm);
                 break;
                 case 2: ViewReservation(connection);
                 break;
                 case 3: getRoomNo(connection,mm);
                 break;
                 case 4: updateReservation(connection,mm);
                 break;
                 case 5: deleteReservation(connection,mm);
                 break;
                 case 6: exit();
                 return;
                 default:
                     System.out.println("You Entered wrong choice Please try again letter....");
             }
         }

        } catch (SQLException s) {
            System.out.println(s);
            s.printStackTrace();
        }catch (InterruptedException i){
            i.printStackTrace();
        }

    }

    private static void reservation(Connection connection, Scanner mm) throws SQLException{

        System.out.println("\nEnter name for new reservation");
        String name = mm.nextLine();
        System.out.println("Enter contact no. ");
        String cnt_no = mm.nextLine();
        System.out.println("Enter available room no. ");
        int room_no = mm.nextInt();


        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservation(name, room_no, contact_no) VALUES(?,?,?)");
        preparedStatement.setString(1,name);
        preparedStatement.setInt(2,room_no);
        preparedStatement.setString(3,cnt_no);

        int rowAffected = preparedStatement.executeUpdate();

        if (rowAffected > 0){
            System.out.println("\nYour reservation Successful.....");
        }else {
            System.out.println("\nReservation Failed.....");
        }
    }


   private static void getRoomNo(Connection connection, Scanner mm) throws SQLException{

       System.out.println("\nPlease Enter the Name");
       String name = mm.nextLine();
       System.out.println("Please Enter the reservation Id");
       int r_id = mm.nextInt();


       PreparedStatement preparedStatement = connection.prepareStatement("SELECT room_no FROM reservation WHERE reservation_id = ? AND name = ?");
       preparedStatement.setInt(1,r_id);
       preparedStatement.setString(2,name);

       ResultSet resultSet = preparedStatement.executeQuery();

       if (resultSet.next()){
           int room_no = resultSet.getInt("room_no");
           System.out.println("\n" + name + " your room no for assigned reservation id : " + r_id + " is " + room_no);
       }else {
           System.out.println("\nNot reserve any room of this name and reservation id.......");
       }
   }


   private static void ViewReservation(Connection connection) throws SQLException{

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT reservation_id, name, room_no, contact_no, reservation_date FROM reservation");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            int r_id = resultSet.getInt("reservation_id");
            int room_no = resultSet.getInt("room_no");
            String time = resultSet.getTimestamp("reservation_date").toString();
            String name = resultSet.getString("name");
            String cnt_no = resultSet.getString("contact_no");
            System.out.println("|-----------------|--------------------|-------------------|----------------------|-------------------------|");
            System.out.println("| reservation_id  | name               | room_no           | contact_no           | reservation_date        |");
            System.out.println("|-----------------|--------------------|-------------------|----------------------|-------------------------|");
            System.out.println("|      " + r_id + "          | " + name + "     | " + room_no +  "               | " + cnt_no + "             | " + time + "   |");
            System.out.println("|-----------------|--------------------|-------------------|----------------------|-------------------------|");
            System.out.println("\n");
        }
   }


    private static void updateReservation(Connection connection, Scanner mm) throws SQLException{

        System.out.println("\nEnter which reservation id you updated");
        int id = mm.nextInt();
        mm.nextLine();

        if (ReservationExist(connection, id)){

            System.out.println("Enter the New name");
            String name = mm.nextLine();
            System.out.println("Enter contact no");
            String cnt_no = mm.nextLine();
            System.out.println("Enter New room no ");
            int r_no = mm.nextInt();

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE reservation SET name = ?, room_no = ?, contact_no = ? WHERE reservation_id = ?");
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,r_no);
            preparedStatement.setString(3,cnt_no);
            preparedStatement.setInt(4,id);

            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0){
                System.out.println("Update successful.....");
            }else {
                System.out.println("Update Unsuccessful.....");
            }
        }else {
            System.out.println("\nId not Exist Updation Failed.......");
        }
    }


    private static void deleteReservation(Connection connection, Scanner mm) throws SQLException{

        System.out.println("\nEnter which reservation id you deleted");
        int id = mm.nextInt();

        if (ReservationExist(connection, id)){

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reservation WHERE reservation_id = ?");
            preparedStatement.setInt(1,id);

            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0){
                System.out.println("Deleted successful.....");
            }else {
                System.out.println("Deleted Unsuccessful.....");
            }
        }else {
            System.out.println("\nId not Exist Deletion Failed.......");
        }
    }


    private static boolean ReservationExist(Connection connection, int id) throws SQLException{

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT reservation_id FROM reservation WHERE reservation_id = ?");
        preparedStatement.setInt(1,id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            return true;
        }
        return false;
    }


    private static void exit() throws InterruptedException{
        System.out.println();
        System.out.print("Exit Successfully");

        for (int i=1; i<= 10; i++){
            System.out.print(".");
            Thread.sleep(500);
        }
    }
}