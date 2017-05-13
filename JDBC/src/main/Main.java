package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class Main {
	
	public static Connection con;
	private static SecureRandom random = new SecureRandom();
	private static JLSecurity sec = new JLSecurity();
	private static String user, pass;
	private static Scanner scanner = new Scanner(System.in);
	private static String userType = "type";
	private static String userID, password;
	
	
	public static void main(String[] args) {
		
		try{

			con = getConnection();
			
			//Carriers
			String cID = "CA7";
			//Carrier.getCarrierRouteAndTruck(cID);
			Carrier.getMailToDeliver(cID);
			//Carrier.getMailToDeliverByPostalCode(cID, "H7T 3R4");
			//Carrier.getMailToDeliverByAddress(cID, "79 Trailsway Circle");
			//Carrier.markMailStatus(cID, "M12", "delivered");
			//GetStatusOfMail("M12");
			
			// PostMasters
			//PostMaster.GetNonEmptyRoutes();
			//PostMaster.ViewAverageRouteTime();
			
		}
		catch (SQLException s){
			print(s.getMessage());
		}
		
	}
		
		public static Connection getConnection() throws SQLException {
			Connection conn = null;
			//String sqlUrl = "jdbc:oracle:thin:@198.168.52.73:1521:orad11g";
			String sqlUrl = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(sqlUrl,"system", "#Oraclepassword92");
			print("Connected to database");
			return conn;
		}
	
	
		public static void GetEmployeeInfo(String eID){
			
			String select = "SELECT * from employees where employeeid= + \'" + eID+ "\'";
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(select);
				while (rs.next()){
					
					print("");
					
					
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
		
		
	public static void GetStatusOfRoute(String rID){
			
			String select = "SELECT * from routes where routeid = + \'" + rID+ "\'";
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(select);
				while (rs.next()){
					print("Route ID: " + rs.getString("mailid"));
					print("Routename:\t" + rs.getString("registration"));
					print("LastDelivered:\t" + rs.getString("postalcode"));
					print("RouteStatus:\t" + rs.getString("returnaddress"));
					print("============================");
					print("");
					
					
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
		
		public static void GetStatusOfMail(String mID){
			
			String select = "SELECT * from mail where mailid = + \'" + mID+ "\'";
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(select);
				while (rs.next()){
					print("Mail ID: " + rs.getString("mailid"));
					print("Registration:\t" + rs.getString("registration"));
					print("PostalCode:\t" + rs.getString("postalcode"));
					print("ReturnAddress:\t" + rs.getString("returnaddress"));
					print("DeliveryAddress: " + rs.getString("deliveryaddress"));
					print("Weight:\t" + rs.getInt("weight"));
					print("TotalPostage:\t" + rs.getString("totalpostage"));
					print("DeliveryStatus:\t" + rs.getString("deliverystatus"));
					print("SenderName:\t" + rs.getString("sendername"));
					print("Country:\t" + rs.getString("country"));
					print("SentTo:\t" + rs.getString("sentto"));
					print("============================");
					print("");
					
					
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
		
		public static void showPrompt(){
			int choice = 0;
			print("\tWelcome to PostOfficeDB!");
			print("1. Login");
			print("2. Create new user");
			System.out.print("Please enter your choice: ");
			choice = scanner.nextInt();
			switch (choice){
				case 1: login();
						 break;
				
				case 2: createNewUser();
						 break;
				
				default:
						showPrompt();
						break;
			}
			
		}
		public static void login() {
			print("\t=====Login=====");
			System.out.print("Enter your userID: ");
			user = scanner.next();
			System.out.print("\nEnter your password: ");
			pass = scanner.next();
			
			if (user.substring(0,1).equals("E") || user.substring(0,1).equals("M"))
				userType = "employee";
			else if (user.substring(0,1).equals("C"))
				userType = "customer";
			/* Validate user input */
			if (user.isEmpty() || pass.isEmpty())
				throw new IllegalArgumentException("The username or password cannot be empty");
			
			/*Validate user credentials */
			String salt = "",
				   userID = "";
			byte[] hash = null,
				   tempHash = null;
			boolean result = false;
			String select = "SELECT USERID, HASH, SALT FROM CREDENTIALS WHERE USERID = \'" + user + "\'";
			
			try{
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery(select);
				rs.next();
				userID = rs.getString("USERID");
				tempHash = rs.getBytes("HASH");
				salt = rs.getString("SALT");
				rs.close();
			}
			catch (SQLException s){
				print(s.getMessage());
			}
			/* If salt == "" there is no user */
			if (salt.isEmpty())
				result = false;
			/* If the salt exists, hash the password and compare with parameter */
		
			else{
				hash = sec.hash(pass, salt);
				if (Arrays.equals(hash, tempHash))
					showScreen();
				
			}
			
			//print("Valid user?: " + result);
		}
		
		public static void print(String str){
			
			System.out.println(str);
		}
		
		public static void createNewUser (){
			print("\t=====Create new user=====");
			System.out.print("Enter your userID: ");
			user = scanner.next();
			System.out.print("\nEnter your password: ");
			pass = scanner.next();
			
			/* Validate user input */
			if (user.isEmpty() || pass.isEmpty())
				throw new IllegalArgumentException("The username or password cannot be empty");
			
			/* Create the user */
			String insert = "INSERT INTO CREDENTIALS (USERID, HASH, SALT) VALUES (?, ?, ?)";
			String salt = sec.getSalt();
			byte[] hash = sec.hash(pass, salt);
			try{
				PreparedStatement statement = con.prepareStatement(insert);
				
				/* Set parameters */
				statement.setString(1, user);
				statement.setBytes(2, hash);
				statement.setString(3, salt);
				
				/*Execute statement */
				statement.execute();
			}
			catch(SQLException s){
				print(s.getMessage());
			}
			
			print("Created new user, disconnecting.");
			
		}
		
		public static void showScreen(){
			int choice = 0;
			print("\tWelcome to Bankruptster!");
			print("1. Search for movie by genre");
			print("2. Search for movie by title");
			print("3. Search for movie by actor/actress");
			print("4. List all currently available movies");
			print("5. View movies you currently have rented");
			print("6. View list of recommended movies");
			if (userType.equals("employee")){
				print("\t=======EMPLOYEE VIEW=======");
				print("7. View late overdue movies");
				print("8. Create new rental");
				print("9. Add return");
				print("10. Add new movie");
			}
			System.out.print("Please enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine();
			switch (choice){
				case 1: System.out.print("Please enter the genre: ");
						 searchMovie("genre", scanner.next());
						 break;
				
				case 2: System.out.print("Please enter the title: ");
						 
				 		 searchMovie("title", scanner.nextLine());
						 break;
				
				case 3:
						System.out.print("Please enter the name (E.X. First,Last): ");
						searchMovie("person", scanner.next());
						break;
				
				case 4:
						listAllAvailableMovies();
						break;
				
				case 5: 
						viewRentedMovies();
						break;
						
						
				case 6:
						System.out.print("Please choose the algorithm\n1. Show only movies with above a 90 rating\n"
								+ "2. Select every movie that has same genre as previous rentals with rating > 90\n"
								+ "3. See what other customers have watched\nChoose: ");
						viewRecommendedMovies(scanner.nextInt());
						break;
				case 7: 
					if (userType.equals("employee"))
						viewOverDueMovies();
						break;
						
				case 8: 
					if (userType.equals("employee"))
							addToDatabase("rental");
					break;
				case 9:
					if (userType.equals("employee"))
						addToDatabase("return");
					break;
				case 10:
					if (userType.equals("employee"))
							addToDatabase("movie");
					break;
				
				default: 
					showScreen();
					break;
						
			}
			showScreen();
		}
		
		public static void searchMovie(String type, String name){
			/* Validate input */
			if (type.isEmpty() || name.isEmpty())
				throw new IllegalArgumentException("The parameters cannot be empty");
			
			String statement = "",
				   movieIDStatement = "",
				   actorIDStatement = "",
				   title = "",
				   price = "",
				   genre = "",
				   rating = "",
				   movieID = "",
				   actorID = "",
				   firstName = "",
				   lastName = "";
			
			
			if (type.equals("genre")){
				statement = "select distinct title, price, genre, movieid, rating from movies where genre = ?";
				try{
					PreparedStatement stmt = con.prepareStatement(statement);
					stmt.setString(1, name);
					ResultSet rs = stmt.executeQuery();
					print("=======Movies found with genre: " + name +"=======");
					while (rs.next()){
						print("=======movie ID: " + rs.getString("movieid") + "=======");
						print("Title:\t" + rs.getString("title"));
						print("Rating:\t" + rs.getInt("rating"));
						print("Price:\t" + rs.getInt("price"));
						print("Genre:\t" + rs.getString("Genre"));
						print("============================");
						print("");
					}
					rs.close();
				}
				catch (SQLException s){
					print(s.getMessage());
				}
			}
			else if (type.equals("title")){
				statement = "select distinct title, price, genre, rating, movieid from movies where title = \'" + name + "\'";
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(statement);
					print("=======Movies found with title: " + name +"=======");
					while (rs.next()){
						print("=======movie ID: " + rs.getString("movieid") + "=======");
						print("Title:\t" + rs.getString("title"));
						print("Rating:\t" + rs.getInt("rating"));
						print("Price:\t" + rs.getInt("price"));
						print("Genre:\t" + rs.getString("Genre"));
						print("============================");
						print("");
					}
					rs.close();
					
				}
				catch (SQLException s){
					print(s.getMessage());
				}
			
			
			}
			else if (type.equals("person")){
				/* Isolate first and last name of actor */
				firstName = name.substring(0,name.indexOf(",")).trim();
				lastName = name.substring(name.indexOf(",") + 1, name.length()).trim();
				
				statement = "select distinct title, rating, price, genre, movieid from movies where movieID in (select movieid from movieactors where actorid = (select actorID from actors where first_Name = \'" + firstName + "\' and last_Name = \'" + lastName + "\'))";
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(statement);
					print("=======Movies found for actor: " + firstName + " " + lastName + "=======");
					while (rs.next()){
						print("=======movie ID: " + rs.getString("movieid") + "=======");
						print("Title:\t" + rs.getString("title"));
						print("Rating:\t" + rs.getInt("rating"));
						print("Price:\t" + rs.getInt("price"));
						print("Genre:\t" + rs.getString("Genre"));
						print("============================");
						print("");
					}
					rs.close();
					
				}
				catch (SQLException s){
					print(s.getMessage());
				}
				
			}
			
		}
		
		public static void listAllAvailableMovies() {
			String statement = "select title, movieid, rating, price, genre from movies where totalinstock >= 1";
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(statement);
				print("=======Listing all movies=======");
				rs = stmt.executeQuery(statement);
				while (rs.next()){
					print("=======movie ID: " + rs.getString("movieid") + "=======");
					print("Title:\t" + rs.getString("title"));
					print("Rating:\t" + rs.getInt("rating"));
					print("Price:\t" + rs.getInt("price"));
					print("Genre:\t" + rs.getString("Genre"));
					print("============================");
					print("");
				}
				rs.close();
				
			}
			catch (SQLException s){
				print(s.getMessage());
			}
			
		}
		
		
		public static void viewRentedMovies() {
			String custID, inventoryID = "";
			System.out.print("\nEnter your customerID: ");
			custID = scanner.next();
			String statement = "select title, return_date from Movies join inventory using (movieid) join rentals using(inventoryid) where customerid = \'" + custID +"\'";
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(statement);
				print("Listing all rentals");
				while (rs.next()){
					print("title: " + rs.getString("title"));
					print("return date: " + rs.getString("return_date"));
				}
				rs.close();
			}
			catch (SQLException s){
				print(s.getMessage());
			}
		}
		
		public static void addToDatabase(String type){
			/* Validate parameter */
			if (type.isEmpty() || type == null)
				throw new IllegalArgumentException("The type cannot be empty");
			int price,
			     rating,
			     totalInStock,
			     totalAvailable,
				   isOnSpecial;
			String statement = "",
				   rentalID,
				   returnID,
				   movieID,
				   customerID,
				   employeeID,
				   serialNumber,
				   returnDate,
				   title,
				   genre,
				   inventoryID = null,
				   releaseDate,
				   specialID;
				   
			if (type.equals("rental"))  {
				System.out.print("Enter rental id: ");
				rentalID = scanner.next();
				System.out.print("Enter customer id: ");
				customerID = scanner.next();
				System.out.print("Enter employee id: ");
				employeeID = scanner.next();
				System.out.print("Enter inventory id: ");
				inventoryID = scanner.next();
				System.out.print("Enter serial number: ");
				serialNumber = scanner.next();
				System.out.print("Enter price: ");
				price = scanner.nextInt();
				statement = "insert into rentals values (\'" + rentalID + "\', \'" + customerID + "\', \'" 
						   + employeeID + "\', \'" + inventoryID + "\', CURRENT_DATE,  CURRENT_DATE + 2, "
						   + price + ", null, " + price + ", 0)";
				try{
					Statement stmt = con.createStatement();
					stmt.executeQuery(statement);
				}		
				catch (SQLException s){
					print(s.getMessage());
				}
				
			}
			
			else if (type.equals("movie")){
				System.out.print("Enter movie id: ");
				movieID = scanner.next();
				scanner.nextLine();
				System.out.print("Enter title: ");
				title = scanner.nextLine();
				System.out.print("Enter price: ");
				price = scanner.nextInt();
				System.out.print("Enter rating: ");
				rating = scanner.nextInt();
				System.out.print("Enter total available: ");
				totalAvailable = scanner.nextInt();
				System.out.print("Enter total in stock: ");
				totalInStock = scanner.nextInt();
				System.out.print("Enter Release Date (YYYY-MM-DD): ");
				releaseDate = scanner.next();
				System.out.print("Enter genre: ");
				genre = scanner.next();
				System.out.print("Enter IsOnSpecial?(0/1): ");
				isOnSpecial = scanner.nextInt();
				System.out.print("Enter special ID: ");
				specialID = scanner.next();
				
				
				statement = "insert into movies values (\'" + movieID + "\', \'" + title + "\', " + price + ", " + rating + ", " + totalAvailable + ", " + totalInStock +  ", TO_DATE(\'" + releaseDate + "\', \'YYYY-MM-DD\'), \'" + genre + "\', " + 
				isOnSpecial + ", " + specialID + ")";
				
				try{
					Statement stmt = con.createStatement();
					stmt.executeQuery(statement);
				}		
				catch (SQLException s){
					print(s.getMessage());
				}
			}
			
			else if (type.equals("return")){
				System.out.print("Enter rental id: ");
				rentalID = scanner.next();
				
				
				statement = "UPDATE RENTALS SET RETURNED = 1 WHERE RENTALID = \'" + rentalID + "\'";
				
				try{
					Statement stmt = con.createStatement();
					stmt.executeQuery(statement);
				}		
				catch (SQLException s){
					print(s.getMessage());
				}
			}
			
		}
		
		public static void viewOverDueMovies(){
			String statement = "select first_name, last_name, phone_number, inventoryID, return_date, title from storecustomers join rentals using (customerID) join inventory using (inventoryid) join movies using (movieid) where customerid in (select customerID from rentals where return_date < CURRENT_DATE and returned = 0)";
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(statement);
				print("Printing all overdue movies");
				while (rs.next()){
						print(rs.getString("first_name") + " " + rs.getString("last_name"));
						print("Phone number: " + rs.getString("phone_number"));
						print("Title: " + rs.getString("title"));
						print("Return date: " + rs.getString("return_date"));
						print("");
					}
					rs.close();
				}
			catch (SQLException s){
				print(s.getMessage());
			}
		}
		
		public static void viewRecommendedMovies(int choice) {
			String genre, statement = "", custID;
			if (choice <1 || choice > 3)
				showPrompt();
			
			switch (choice){
				case 1: 
					statement = "select movieid, title, avg(rating) from movies where rating >= 90 group by movieid, title, rating";
					break; 
				case 2:
					System.out.print("Enter your customerID: ");
					custID = scanner.next();
					statement = "select movieid, title, genre, rating from movies where genre IN (select genre from rentals join inventory using(inventoryid) join movies using (movieid) where customerid = \'" +
					custID + "\' group by genre) group by movieid, title,genre, rating having rating > 90";
					break;
				
				case 3:
					System.out.print("Enter your customerID: ");
					custID = scanner.next();
					statement = "select movieid, genre, rating, title from rentals join (select customerid from rentals join inventory using(inventoryid) join movies using (movieid) where genre in (select genre from rentals join inventory using (inventoryid) join movies using(movieid) where customerid = \'" +
							custID + "\' group by genre)) using (customerid) join inventory using (inventoryid) join movies using(movieid) where rating > 90";
					break;
				
				default:
						break;
			}
			
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(statement);
				print("Listing all recommended movies");
				while (rs.next()){
					print("title: " + rs.getString("title"));
					print("movieid: " + rs.getString("movieid"));
					if (choice == 2 || choice == 3){
						print("genre: " + rs.getString("genre"));
						print("rating: " + rs.getString("rating"));
					}
				
				}
				rs.close();
			}
			catch (SQLException s){
				print(s.getMessage());
			}
		}
		
		public static void getCredentials(String filename) throws IOException{
			File file = new File(filename);
			int counter = 0;
			String str;
			try {
				BufferedReader in = new BufferedReader(new FileReader(file));
				while ((str = in.readLine()) != null) {
					if (str.isEmpty())
						continue;
					if (counter == 0)
						userID = str;
					else if (counter == 1)
						password = str;
					counter++;
				}
			} catch (FileNotFoundException e) {
				print(e.getMessage());
			}

		}

	

	
	
}


