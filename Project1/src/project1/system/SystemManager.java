/* Created by Adam Jost on 06/25/2021 */
/* Update by Adam Jost on 06/29/2021 */

package project1.system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import project1.system.movies.Movie;
import project1.system.movies.MovieList;
import project1.system.movies.MovieStatus;

public class SystemManager {

	public static void main(String[] args) throws IOException {

		// Open input streams.
		FileInputStream fin = new FileInputStream("movies.txt");
		Scanner scanner = new Scanner(fin);

		// MovieList object used for storing Movie objects within two lists: "Received"
		// and "Released"
		MovieList movieList = new MovieList();

		// Parse input file data.
		while (scanner.hasNext()) {
			// Read in next line from file and place in a String array.
			String[] data = scanner.nextLine().split(", ");
			// Instantiate a Movie object using data retrieved from input file.
			Movie movie = new Movie(data[0], convertDate(data[1]), data[2], convertDate(data[3]),
					convertStatus(data[4]));
			// Update the movies status if it has been released as of today's date.
			movie.updateMovieStatus();
			// If currently "Released" add the Movie to the released MovieList.
			if (movie.getStatus() == MovieStatus.RELEASED) {
				movieList.addToReleasedList(movie);
			} else {
				// If currently "Received" then add the Movie to the received MovieList.
				movieList.addToReceivedList(movie);
			}
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

			// Objects needed throughout switch block.
			Date date = null, releaseDate = null, receiveDate = null;
			String input = "", movieName = "", movieDescription = "";
			MovieStatus status = null;
			boolean isCanceled = false;

			// Get the next command from the user.
			String command = userInput.nextLine().toLowerCase().trim();

			// Perform actions depending on the entered command.
			switch (command) {
			case ("key"): // If the user entered the command "KEY"
				printHeadingAndKey();
				break;
			case ("display"): // If the user entered the command "DISPLAY", execute the following.
				// Display the command key containing all display command options.
				printDisplayCommandKey();
				while (input.length() < 1) {
					System.out.println("Which movies list would you like to display?");
					input = userInput.nextLine().toLowerCase().trim();
					// If the user canceled the command in-progress.
					if (input.equals("cancel")) {
						break;
					}
					// If a valid command was entered then exit the while loop. Otherwise, display
					// an
					// error message and reset the input variable to continue the loop.
					if (input.equals("all") || input.equals("received") || input.equals("released")) {
						break;
					} else {
						System.out.println("\"" + input + "\" is not a valid command.\n");
						input = "";
					}
				}
				// The user has canceled so inform them that the operation has in fact canceled.
				if (input.equals("cancel")) {
					System.out.println("Displaying movies has been canceled.");
					break;
				}
				// Display the chosen movie list.
				if (input.equals("received")) {
					movieList.displayReceivedMovies();
				} else if (input.equals("released")) {
					movieList.displayReleasedMovies();
				} else {
					movieList.displayAll();
				}
				break;
			case ("add"): // If the user entered the command "ADD", execute the following.
				// Ask the user for a movie name until they enter a non-empty string or cancels.
				movieName = getMovieName(userInput, "add");
				// Adding a movie has been canceled.
				if (movieName.equalsIgnoreCase("cancel")) {
					System.out.println("Adding movie has been canceled.");
					break;
				}
				// Prompt user for a release date until the user enters a valid date or cancels.
				releaseDate = getDate(userInput, "release date", null);
				// Adding a movie has been canceled?
				if (releaseDate == null) {
					System.out.println("Adding movie has been canceled.");
					break;
				}
				// Print notice listing all valid movie descriptions (genres).
				printValidDescriptionsList();
				// Continue to ask the user to enter a movie description until a non-empty
				// string is entered or cancel.
				movieDescription = getDescription(userInput);
				// Adding a movie has been canceled if it is still an empty string.
				if (movieDescription.equals("")) {
					System.out.println("Adding movie has been canceled.");
					break;
				}
				// Prompt user for a receive date until the user enters a valid date or cancels.
				receiveDate = getDate(userInput, "receive date", releaseDate);
				// If date = null then the operation has been canceled and we inform the user it
				// has.
				if (receiveDate == null) {
					System.out.println("Adding movie has been canceled.");
					break;
				}
				// Prompt user to enter the movie status until a valid status is entered.
				while (status == null) {
					System.out.println("Enter the movie's status (e.g. \"Received\" or \"Released\"): ");
					// Retrieve user input
					input = userInput.nextLine().trim();
					// Check to see if the user has canceled.
					if (input.toLowerCase().equals("cancel")) {
						isCanceled = true;
						break;
					}
					// Convert status plus ensure capitalization of only the first letter of input
					// String to avoid unnecessary errors.
					status = convertStatus(input.toLowerCase());
				}
				// User has canceled?
				if (isCanceled) {
					System.out.println("Adding movie has been canceled.");
					break;
				}
				// Only allow adding movies to the "Received" MovieList
				if (status == MovieStatus.RELEASED) {
					System.out.println("Error: Cannot add a new movie to the \"Released\" list.");
					// Leave "add" a new movie which will prompt user for a new command.
					break;
				}
				// Create/Instantiate movie object using user entered data.
				Movie newMovie = new Movie(movieName, releaseDate, movieDescription, receiveDate, status);
				// Add the new movie to the "Received" MovieList.
				if (movieList.addToReceivedList(newMovie)) {
					System.out.println("Movie has been successfully added.");
				}
				break;
			case ("edit"): // If the user enters the command "EDIT", execute the following.
				// Print notice to user that only "Received" movies can be edited.
				printEditNotice();
				// Request user for a movie name until they enter a non-empty string or they
				// cancel.
				movieName = getMovieName(userInput, "edit");
				// Editing has been canceled?
				if (movieName.equalsIgnoreCase("cancel")) {
					System.out.println("Editing movie has been canceled.");
					break;
				}
				// Stores new command.
				String editCommand = "";
				// Prompt user to enter an editing option until a valid option is entered.
				while (editCommand.length() < 1) {
					// Prompt user asking if they want to edit description or release date;
					System.out.println("Edit \"DESCRIPTION\" or \"RELEASE DATE\"?");
					// Retrieve user desired editing option.
					editCommand = userInput.nextLine().trim().toLowerCase();
					// Check to see if the user has canceled.
					if (editCommand.equals("cancel")) {
						isCanceled = true;
						break;
					}
					// Check if user did not enter a valid editing option.
					if (editCommand.equals("description") || editCommand.equals("release date")) {
						break;
					} else {
						System.out.println("Error: Editing option entered is not valid.");
						// Reset the following and prompt user again to enter a valid option.
						editCommand = "";
					}
				}
				// Has editing been canceled?
				if (isCanceled) {
					System.out.println("Editing movie has been canceled.");
					break;
				}
				// If the user chose to edit the movie's description.
				if (editCommand.equals("description")) {
					// Print notice listing all valid movie descriptions (genres).
					printValidDescriptionsList();
					// Prompt the user to enter a movie description until a non-empty string is
					// entered.
					movieDescription = getDescription(userInput);
					// Adding a movie has been canceled.
					if (movieDescription.equals("")) {
						System.out.println("Editing movie has been canceled.");
						break;
					}
					// Edit the specified movie's description.
					movieList.editDescription(movieName, movieDescription);
				}
				// If the user chose to edit the movie's release date.
				if (editCommand.equals("release date")) {
					// Prompt user for a release date until the user enters a valid date.
					releaseDate = getDate(userInput, "edit", null);
					// Has editing been canceled?
					if (releaseDate == null) {
						System.out.println("Editing movie has been canceled.");
						break;
					}
					// Edit the specified movie's release date information.
					movieList.editReleaseDate(movieName, releaseDate);
				}
				break;
			case "delete":
				// Prompt user for a movie name until they enter a non-empty string
				movieName = getMovieName(userInput, "delete");
				// Editing has been canceled?
				if (movieName.equalsIgnoreCase("cancel")) {
					System.out.println("Canceling movie has been canceled.");
					break;
				}
				// Attempt to delete the movie and print a response depending on if it was
				// successful or not.
				if (movieList.deleteMovie(movieName)) {
					System.out.println(" has been deleted successfully.");
				} else {
					System.out.println("Movie cannot be deleted because it does not exist.");
				}
				break;
			case "start showing": // If the user enters the command "START SHOWING", execute the following.
				// Prompt user for a release date until the user enters a valid date.
				date = getDate(userInput, "start showing", null);
				// Has operation been canceled?
				if (date == null) {
					System.out.println("The start showing operation has been canceled.");
					break;
				}
				movieList.startShowing(date);
				break;
			case ("count"): // If the user enters the command "COUNT", execute the following.
				// Prompt user to enter a Date until a valid Date is entered.
				date = getDate(userInput, "count", null);
				// If date is null then count is canceled so notify user it has been
				// successfully canceled.
				if (date == null) {
					System.out.println("The count operation has been canceled.");
					break;
				}
				// Display the count to the user.
				System.out.printf("Count of movie's received before the given date is %d.\n",
						movieList.countReceivedMovies(date));
				break;
			case ("cancel"): // The user entered the command "CANCEL" when an operation was not in-progress.
				System.out.println("No operation currently in-progress to cancel.");
				break;
			case ("save"): // If the user enters the command "SAVE", execute the following.
				saveChanges(movieList);
				break;
			case ("exit"): // If the user enters the command "EXIT", execute the following.
				// Setting this to false causes the program to exit the while loop.
				running = false;
				// Inform the user the program has ended.
				System.out.println("Application has been successfully exited.");
				break;
			default: // The user has failed to enter a valid command..
				// Notify the user their command is invalid.
				System.out.println("Error: \"" + command + "\" is not a valid command.");
			}
			// If user has not entered the exit command, prompt user to enter another
			// command.
			if (running) {
				System.out.println("\nEnter another command or \"EXIT\" to exit:");
			}
		}
		// Close open Scanner
		userInput.close();
	}

	/** Prints the menu heading and command key to the standard console. */
	static void printHeadingAndKey() {
		// Spacing amounts for formatting printed user interface
		int interfaceWidth = 53;
		int headingSpace = 16;
		int keyHeadingSpace = 21;
		int typeCommandSpace = 10;

		// Print heading, command key, and instructions to the standard console.
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + headingSpace + "s%s\n", " ", "MOVIE MANAGEMENT SYSTEM");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + keyHeadingSpace + "s%s\n", " ", "Command Key");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("-");
		}
		System.out.print("\nKEY - Display the command key\n" + "DISPLAY - Display all movies\n"
				+ "ADD - Add a movie to the received movies list\n"
				+ "EDIT - Edit a movie's release date or description\n"
				+ "START SHOWING - Start showing movies on a given date\n"
				+ "COUNT - Number of received movies prior to a date\n"
				+ "DELETE - Delete a movie from either movie list\n" 
				+ "CANCEL - Cancels the current operation\n"
				+ "SAVE - Save all changes\n" + "EXIT - Exit the program\n");

		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + typeCommandSpace + "sType any above command to continue\n", " ");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.println();
	}

	/**
	 * Prints a key for the available commands of the display operation to the
	 * console
	 */
	static void printDisplayCommandKey() {
		// Spacing amounts for formatting printed user interface
		int interfaceWidth = 53;
		int headingSpace = 19;
		int keyHeadingSpace = 21;
		int typeCommandSpace = 10;

		// Print heading, command key, and instructions to the standard console.
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + headingSpace + "s%s\n", " ", "Display Options");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + keyHeadingSpace + "s%s\n", " ", "Command Key");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("-");
		}
		System.out.print("\nALL - Display all movies\n" 
				+ "RECEIVED - Display all received movies\n"
				+ "RELEASED - Display all released movies\n");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + typeCommandSpace + "sType any above command to continue\n", " ");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.println();
	}

	/**
	 * Prints a notice informing the user that only movie's with RECEIVED status can
	 * be edited.
	 */
	static void printEditNotice() {
		int interfaceWidth = 53, noticeSpace = 17;
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + noticeSpace + "s"
				+ "* Important Notice * \nA movie can only be edited if its status is \"Received\"\n", " ");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.println();
	}

	/**
	 * Creates and returns a Date from an input String.
	 * @param date: The date to be converted.
	 * @return: The parsed Date.
	 */
	static Date convertDate(String date) {
		// Create a null Date
		Date result = null;

		// Date cannot be expected format if argument length is not 10 (e.g.
		// 01/02/2022).
		if (date.length() != 10) {
			// Notify user input String entered was not in the valid/acceptable format.
			System.out.println("Error: Date entered was invalid.");
			return null;
		}
		try {
			// Specify format
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			// Set result to the parsed argument String
			result = dateFormat.parse(date);
		} catch (ParseException e) {
			// This catch will only ever apply to user input unless input file is manually
			// edited and invalid input is added.
			// Recommendation: Do not manually edit the input file. Use this system instead.
			// Notify user input String entered was not in the valid/acceptable format.
			System.out.println("Error: Date entered was invalid.");
		}
		// Return the Date
		return result;
	}

	/**
	 * Converts argument String to MovieStatus
	 * @param status: String to be converted to MovieStatus
	 * @return MovieStatus
	 */
	static MovieStatus convertStatus(String status) {
		// Set status depending on argument String, if valid
		if (status.equalsIgnoreCase("RECEIVED")) {
			return MovieStatus.RECEIVED;
		} else if (status.equalsIgnoreCase("RELEASED")) {
			return MovieStatus.RELEASED;
		} else {
			// If input String was not valid, notify user and leave status set to null.
			System.out.println("Error: Invalid status entered");
			return null;
		}
	}

	/**
	 * Requests, formats, and returns the name of the user entered movie.
	 * @param userInput: the scanner object
	 * @param command: the command being performed that is requesting the movie name.
	 * @return: the name of the movie.
	 */
	static String getMovieName(Scanner userInput, String command) {
		String movieName = "";
		while (movieName.length() < 1) {
			// Movie name request depend on which command is being performed.
			if (command.equals("delete")) {
				System.out.println("Enter the name of the movie you wish to delete: ");
			} else {
				System.out.println("Enter the movie name: ");
			}
			// Retrieve input
			String input = userInput.nextLine().trim();
			if (input.length() > 0) {
				// Capitalize the first letter of each word in the movie name and lower all subsequent letters.
				String[] inputArr = input.split(" ");
				for (int i = 0; i < inputArr.length; i++) {
					inputArr[i] = inputArr[i].substring(0, 1).toUpperCase() + inputArr[i].substring(1).toLowerCase();
				}
				movieName = String.join(" ", inputArr);
			} else {
				System.out.println("Error: You must enter a movie name to continue.");
			}
		}
		return movieName;
	}

	/**
	 * Requests, parses, and returns the Date entered by the user.
	 * @param userInput: Scanner used to read in the input.
	 * @param command: The command requesting the date.
	 * @param releaseDate: Another date used for comparison when requesting a receive date.
	 * @return: The date if the user entered one successfully and null if the user
	 *          cancels the operation.
	 */
	static Date getDate(Scanner userInput, String command, Date releaseDate) {
		Date date = null;
		String input = "";
		// Continue to request a date until a valid date is entered or the user enters
		// cancel.
		while (date == null) {
			// Print a relevant request message depending on the command being performed.
			if (command.equals("count")) {
				System.out.println("Enter the date to count up to (e.g. 01/22/2021): ");
			} else if (command.equals("start showing")) {
				System.out.println(
						"Enter the release date of the movies you would like\nto start showing (e.g. 01/22/2021): ");
			} else if (command.equals("edit") || command.equals("release date")) {
				System.out.println("Enter the movie's release date (e.g. 01/22/2021): ");
			} else if (command.equals("receive date")) {
				System.out.println("Enter the movie's receive date (e.g. 01/22/2021): ");
			}
			input = userInput.nextLine().trim();
			// Check to see if the user has canceled.
			if (input.equalsIgnoreCase("cancel")) {
				break;
			}
			// Convert to Date from user input String.
			date = convertDate(input);
			if (date != null && command.equals("receive date")) {
				if (date.compareTo(releaseDate) >= 0) {
					System.out.println("Error: Receive date must be before release date.");
					date = null;
				}
			}
		}
		return date;
	}

	/** Prints a list of all valid movie descriptions to the console. */
	static void printValidDescriptionsList() {
		int interfaceWidth = 53, noticeSpace = 17, titleSpace = 7;
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + noticeSpace + "s" + "* Important Notice * \n%" + titleSpace
				+ "sList of Valid Movie Descriptions Include\n", " ", " ");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.println("\nAction\nAdventure\nAnimation\nComedy\nCrime\nDrama\nExperimental\nFantasy\nHistorical\n"
				+ "Horror\nOther\nRomance\nSci-Fi\nThriller\nWestern");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
		System.out.printf("\n%" + titleSpace + "sList all that apply seperated by spaces\n", " ");
		for (int i = 0; i <= interfaceWidth; i++) {
			System.out.print("=");
		}
	}

	/**
	 * Requests, formats, and returns a movie's description from the user.
\	 * @param userInput: The Scanner to read in the user input.
	 * @return: The movie's description entered by the user.
	 */
	static String getDescription(Scanner userInput) {
		String movieDescription = "";
		while (movieDescription.length() < 1) {
			System.out.println("\nEnter the movie's description (e.g. Sci-Fi Adventure): ");
			// .replaceAll() accounts for user entering spaces between description genres.
			movieDescription = userInput.nextLine().trim();
			// Check to see if user has canceled adding a movie.
			if (movieDescription.equalsIgnoreCase("cancel")) {
				movieDescription = "";
				break;
			}
			// Check to make sure only valid genres have been entered.
			// And print all invalid entries, if any exist, to the console.
			if (!descriptionIsValid(movieDescription)) {
				movieDescription = "";
			}

			// If user failed to enter a movie description, display this error message.
			if (movieDescription.length() < 1) {
				System.out.println("Notice: Please enter a valid movie description.");
				continue;
			}
			movieDescription = ensureDescriptionFormat(movieDescription);
		}
		return movieDescription;
	}

	/**
	 * Checks if the entered movie description only consists of valid options.
	 * @param movieDescription: The description to be checked.
	 * @return: {true} if the description is valid; {false} otherwise.
	 */
	static boolean descriptionIsValid(String movieDescription) {
		boolean valid = true;
		for (String genre : movieDescription.split(" ")) {
			if (genre.equalsIgnoreCase("action") || genre.equalsIgnoreCase("animation")
					|| genre.trim().equalsIgnoreCase("comedy") || genre.equalsIgnoreCase("crime")
					|| genre.equalsIgnoreCase("drama") || genre.equalsIgnoreCase("experimental")
					|| genre.equalsIgnoreCase("fantasy") || genre.equalsIgnoreCase("historical")
					|| genre.equalsIgnoreCase("horror") || genre.equalsIgnoreCase("romance")
					|| genre.equalsIgnoreCase("sci-fi") || genre.equalsIgnoreCase("thriller")
					|| genre.equalsIgnoreCase("adventure") || genre.equalsIgnoreCase("western")
					|| genre.equalsIgnoreCase("other")) {
				continue;
			} else {
				System.out.println("Error: \"" + genre + "\" is not a valid description.");
				valid = false;
			}
		}
		return valid;
	}

	/**
	 * Ensure desired format of the movie description by making each words first
	 * letter is capitalized and all subsequent letters are lower case.
	 * @param description: the movie descriptions to be formatted.
	 * @return: the formatted description.
	 */
	static String ensureDescriptionFormat(String description) {
		String[] arr = description.split(" ");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			result.append(arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1).toLowerCase());
			if (i != arr.length - 1) {
				result.append("/");
			}
		}
		return result.toString();
	}

	/**
	 * Saves and prints all changes made by user to the movies.txt file.
	 * @throws IOException
	 */
	static void saveChanges(MovieList list) throws IOException {
		// Open output streams.
		FileOutputStream fout = new FileOutputStream("movies.txt");
		PrintWriter writer = new PrintWriter(fout);

		// Print the movie lists in their current state to the output file.
		writer.write(list.toString());

		// Notify the user the save has completed.
		System.out.println("Save completed successfully.");

		// Close open streams.
		writer.close();
		fout.close();
	}
}
