package movies.manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import movies.movie.MovieStatus;
import movies.lists.MovieList;
import movies.movie.Movie;

public class ManagementSystem {
	
	public static void main (String[] args) throws IOException {
		
		// Open input streams.
		FileInputStream fin = new FileInputStream("movies.txt");
		Scanner scanner = new Scanner(fin);
		
		// MovieList object used for storing Movie objects within two lists: "Coming" and "Showing"
		MovieList movieList = new MovieList();
				
		// Parse input file data.
		while (scanner.hasNext()) {
			// Read in next line from file and place in a String array.
			String[] data = scanner.nextLine().split(", ");
			// Instantiate Movie object using data retrieved from input file. 
			Movie movie = new Movie(data[0], convertDate(data[1]), data[2], convertDate(data[3]), convertStatus(data[4]));
			// If currently "Showing" add the Movie to the Showing MovieList.
			if (movie.getStatus() == MovieStatus.Showing) movieList.addToShowingList(movie);
			// If currently "Coming" then add the Movie to the Coming MovieList.
			else movieList.addToComingList(movie);
		}
		
		// Close open streams
		scanner.close();
		fin.close();
		
		// Print heading and command key to the standard console. 
		printHeadingAndKey(); 
		
		// Scanner for user input during program loop. 
		Scanner userInput = new Scanner(System.in);
		
		// Condition used for determining when to exit the program loop. 
		boolean running = true;
		
		// Program loop  
		while (running) {
			
			Date date = null, releaseDate = null, receiveDate = null; 
			String movieName = "", movieDescription = "";
			MovieStatus status = null; 
			
			// Get the next command from the user.
			String command = userInput.nextLine().trim().toLowerCase();
			
			// Perform actions depending on the entered command.
			switch(command) {
				case ("key"): // If the user entered the command "KEY"
					printHeadingAndKey();
					break;
				case ("display"): // If the user entered the command "DISPLAY", execute the following.
					// Display the MovieList.
					movieList.display();
					break;
				case ("add"): // If the user entered the command "ADD", execute the following.
					// Prompt user for a movie name until they enter a non-empty string
					while (movieName.length() < 1) {
						System.out.println("Enter the movie name: ");
						movieName = userInput.nextLine();
						// If user failed to enter a movie name, display this error message.
						if (movieName.length() < 1) System.out.println("Error: Please enter a non-empty String.");
					}
					// Prompt user for a release date until the user enters a valid date.
					while (releaseDate == null) {
						System.out.println("Enter the movie's release date (e.g. 01/22/2021): ");
						// Convert to Date from user input String.
						releaseDate = convertDate(userInput.nextLine().trim());
					}
					// Prompt the user to enter a movie description until a non-empty string is entered.
					while (movieDescription.length() < 1) {
						System.out.println("Enter the movie's description (e.g. Sci-Fi/Adventure): ");
						// .replaceAll() accounts for user entering spaces between description genres.
						movieDescription = userInput.nextLine().replaceAll(" ", "/");
						// If user failed to enter a movie description, display this error message.
						if (movieDescription.length() < 1) System.out.println("Error: Please enter a non-empty String.");
					}
					// Prompt user for a receive date until the user enters a valid date.
					while (receiveDate == null) {
						System.out.println("Enter the movie's receive date (e.g. 01/22/2021): ");
						// Convert to Date from user input String.
						receiveDate = convertDate(userInput.nextLine().trim());
						// Ensure receive date does not come after release date. 
						if (receiveDate != null) {
							if (receiveDate.after(releaseDate)) {
								System.out.println("Error: Receive date must be before release date.");
								receiveDate = null;
							}
						}
					}
					// Prompt user to enter the movie status until a valid status is entered.
					while (status == null) {
						System.out.println("Enter the movie's status (e.g. \"Coming\" or \"Showing\"): ");
						// Retrieve user input
						String str = userInput.nextLine().trim();
						// Convert status plus ensure capitalization of only the first letter of input String to avoid unnecessary errors.
						status = convertStatus(str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase());
					}
					// Only allow adding movies to the "Coming" MovieList
					if (status == MovieStatus.Showing) {
						System.out.println("Error: Cannot add a new movie to the \"Showing\" list.");
						// Leave "add" a new movie which will prompt user for a new command. 
						break;
					}
					// Create/Instantiate movie object using user entered data. 
					Movie newMovie = new Movie(movieName, releaseDate, movieDescription, receiveDate, status);	
					// Add the new movie to the "Coming" MovieList.
					movieList.addToComingList(newMovie);
					break;
				case ("edit"): // If the user enters the command "EDIT", execute the following.
					System.out.println("Enter the name of the movie to edit: ");
					movieName = userInput.nextLine().trim();
					// Stores new command.
					String editCommand = "";
					// Prompt user to enter an editing option until a valid option is entered.
					while (editCommand.length() < 1) {
						// Prompt user asking if they want to edit description or release date;
						System.out.println("Edit \"DESCRIPTION\" or \"RELEASE DATE\"?");
						// Retrieve user desired editing option.
						editCommand = userInput.nextLine().trim().toLowerCase();
						// Check if user did not enter a valid editing option.
						if (editCommand.equals("description") || editCommand.equals("release date")) {
							break;
						} else {
							System.out.println("Error: Editing option entered is not valid.");
							// Reset the following. 
							editCommand = "";
						}
					}
					// If the user chose to edit the movie's description.
					if (editCommand.equals("description")) {
						// Prompt the user to enter a movie description until a non-empty string is entered.
						while (movieDescription.length() < 1) {
							System.out.println("Enter the movie's description (e.g. Sci-Fi/Adventure): ");
							// .replaceAll() accounts for user entering spaces between description genres.
							movieDescription = userInput.nextLine().replaceAll(" ", "/");
							// If user failed to enter a movie description, display this error message.
							if (movieDescription.length() < 1) System.out.println("Error: Please enter a non-empty String.");
						}
						// Edit the specified movie's description.
						movieList.editDescription(movieName, movieDescription);
					}
					// If the user chose to edit the movie's release date. 
					if (editCommand.equals("release date")) {
						// Prompt user for a release date until the user enters a valid date.
						while (releaseDate == null) {
							System.out.println("Enter the movie's release date (e.g. 01/22/21): ");
							// Convert to Date from user input String.
							releaseDate = convertDate(userInput.nextLine().trim());
						}
						// Edit the specified movie's release date information.
						movieList.editReleaseDate(movieName, releaseDate);
					}
					break;
				case "start showing": // If the user enters the command "START SHOWING", execute the following.
					/*
					// TODO:: CODE GOES HERE
					// Start showing function call
				    */
					break;
				case ("count"): // If the user enters the command "COUNT", execute the following.
					// Prompt user to enter a Date until a valid Date is entered. 
					while (date == null) {
						System.out.println("Enter the date to count up to (e.g. 01/22/2021): ");
						// Convert to Date from user input String.
						date = convertDate(userInput.nextLine().trim());
					}
					// Display the count to the user. 
					System.out.printf("Count of movie's coming before the given date is %d.\n", movieList.countComingMovies(date));
					break;
				case ("save"): // If the user enters the command "SAVE", execute the following.
					saveChanges();
					break;
				case ("exit"): // If the user enters the command "EXIT", execute the following. 
					// Setting this to false causes the program to exit the while loop.
					running = false;
					break;
				default: // The user has failed to enter a valid command..
					// Notify the user their command is invalid.
					System.out.println("Error: "command + " is not a valid command.");
			}
			
			// If user has not entered the exit command, prompt user to enter another command.
			if (running) System.out.println("\nEnter another command or \"EXIT\" to exit:");
		}
		
		// Close open Scanner
		userInput.close();
		
	}
	
	/**
	 * Prints the menu heading and command key to the standard console. 
	 */
	static void printHeadingAndKey() {
		
		// Spacing amounts for formatting printed user interface
		int interfaceWidth = 53;
		int headingSpace = 16;
		int keyHeadingSpace = 21;
		int typeCommandSpace = 10;
				
		// Print heading, command key, and instructions to the standard console.
		for (int i = 0; i <= interfaceWidth; i++) { System.out.print("="); }
		System.out.printf("\n%" + headingSpace + "s%s\n", " ", "MOVIE MANAGEMENT SYSTEM");
		for (int i = 0; i <= interfaceWidth; i++) { System.out.print("="); }
		System.out.printf("\n%" + keyHeadingSpace + "s%s\n", " ", "Command Key");
		for (int i = 0; i <= interfaceWidth; i++) { System.out.print("-"); }
		System.out.print("\nKEY - Display the command key\n"
				+ "DISPLAY - Display all movies\n"
				+ "ADD - Add a movie to the coming movies list\n"
				+ "EDIT - Edit a movies release date or description\n"
				+ "START SHOWING - Start showing movies on a given date\n"
				+ "COUNT - Number of coming movies prior to a given date\n"
				+ "SAVE - Save all changes\n"
				+ "EXIT - Exit the program\n");
		for (int i = 0; i <= interfaceWidth; i++) { System.out.print("="); }
		System.out.printf("\n%" + typeCommandSpace + "sType any above command to continue\n", " ");
		for (int i = 0; i <= interfaceWidth; i++) { System.out.print("="); }
		System.out.println();
	}
	
	/**
	 * Creates and returns a Date from an input String.
	 * @param date: The date to be converted.
	 * @return: The parsed Date.
	 */
	static Date convertDate(String date){
		
		// Create a null Date
	    Date result = null;
	    
	    // Date cannot be expected format if argument length < 10.
	    if (date.length() < 10) {
	    	// Notify user input String entered was not in the valid/acceptable format.
	    	System.out.println("Error: Date entered was invalid.");
	    	return null;
	    }
	        
	    
	    try{
	    	// Specify format
	        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	        // Set result to the parsed argument String
	        result  = dateFormat.parse(date);
	    }
	
	    catch(ParseException e){
	    	// Notify user input String entered was not in the valid/acceptable format.
	        System.out.println("Error: Date entered was invalid.");
	    }
	    
	    // Return the Date
	    return result ;
	}
	
	/**
	 * Converts argument String to MovieStatus
	 * @param status: String to be converted to MovieStatus
	 * @return MovieStatus
	 */
	static MovieStatus convertStatus(String status) {
		// Set status depending on argument String, if valid
		if (status.equals("Coming")) return MovieStatus.Coming;
		else if (status.equals("Showing")) return MovieStatus.Showing;
		// If input String was not valid, notify user and leave status set to null.
		System.out.println("Error: Invalid status entered");
		return null;
	}
	
	/**
	 * Saves and prints all changes made by user to the movies.txt file. 
	 * @throws IOException
	 */
	static void saveChanges() throws IOException {
		// Open output streams.
		FileOutputStream fout = new FileOutputStream("movies.txt");
		PrintWriter writer = new PrintWriter(fout);
		
		/* ******  Add code here  *****
		 * Iterate through MovieLists printing each movie to the output file.
		 */
		
		// Close open streams.
		writer.close();
		fout.close();
	}
}
