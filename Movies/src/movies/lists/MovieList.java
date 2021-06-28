package movies.lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

import movies.movie.Movie;
import movies.movie.MovieStatus;

public class MovieList {
	
	// Data fields
	ArrayList<Movie> releasedMovies;
	ArrayList<Movie> receivedMovies;
	
	// Constructor
	public MovieList() {
		releasedMovies = new ArrayList<>();
		receivedMovies = new ArrayList<>();
	}
	
	// Class-member methods
	
	/** Displays all of the movies from both movie lists by status "Received" or "Released" */
	public void display() {
		
		// Print a heading 
		System.out.println("\n================\nReceived Movies:\n================");
		
		// Iterator for receivedMovies list
		Iterator<Movie> itReceived = receivedMovies.iterator();
		
		// Until we have reached the end of the received list.
		while (itReceived.hasNext()) {
			System.out.print(itReceived.next());
			System.out.println();
		}
		
		// Print a divider between the displayed movie lists
		for (int i = 0; i <= 53; i++) System.out.print("-");
		
		// Print heading to the standard console.
		System.out.println("\n\n================\nReleased Movies:\n================");
		
		// Iterator for releasedMovies list
		Iterator<Movie> itReleased = releasedMovies.iterator();
		
		// Until we have reached the end of the released list.
		while (itReleased.hasNext()) {
			System.out.print(itReleased.next());
			System.out.println();
		}
	}
	
	/**
	 * Adds a movie to the coming movie list.
	 * @param m: the movie to be added
	 * @return: {true} if the movie was add successfully; {false} otherwise.
	 */
	public boolean addToReceivedList(Movie m) {
		ListIterator<Movie> it = receivedMovies.listIterator(); // Create iterator to iterate over the comingMovies list
		int moviePos = -1; 
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = (Movie) it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getName().toLowerCase().equals(m.getName().toLowerCase())) { // If the input movie already exists in the list
				System.out.println("Error: Cannot add duplicate movies to the system.");
				return false; // Then end the method here
			}
			if (currMovie.getReleaseDate().after(m.getReleaseDate()) && moviePos == -1) { // If the input movie should be inserted here because the element after it is greater than it
				moviePos = receivedMovies.indexOf(currMovie);
			}
		}
		// If an greater element was found place movie before that element.
		if (moviePos != -1) {
			receivedMovies.add(moviePos, m);
		} else { // Otherwise,
			// Add the movie to the end of the received movies list in case the movie has the latest release date
			receivedMovies.add(m);
		}
		return true;
	}
	
	/**
	 * Adds a movie to the released movies list.
	 * @param m: the movie to be added.
	 */
	public void addToReleasedList(Movie m) {
		releasedMovies.add(m); // Add the movie to the end of the releasedMovies list
	}
	
	/**
	 * Edits the description of a given movie.
	 * @param name: The name of the movie to be edited.
	 * @param description: The new description.
	 */
	public void editDescription(String name, String description) {
		Iterator<Movie> it = receivedMovies.iterator(); // Create iterator to iterate over the receivedMovies list
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = (Movie) it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getName().toLowerCase().equals(name.toLowerCase())) { // If the current movie's name is the same as the movie we're trying to change the description of
				currMovie.setDescription(description); // Change the description
				System.out.println("Edit was successful."); 
				return;
			}
		}
		System.out.println("Error: Edit Unsuccessful.\nThat movie does not exist in our \"received\" movies list.");
	}
	
	/**
	 * Counts and returns the number of received movies before a given date.
	 * @param d: the date to count up to.
	 * @return: the number of received movies before the date.
	 */
	public int countReceivedMovies(Date d) {
		int totalMoviesBeforeDate = 0; // This will store the number of movies before the input date
		Iterator<Movie> it = receivedMovies.iterator(); // Create iterator to iterate over the receivedMovies list
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getReleaseDate().compareTo(d) < 0) { // If the current movie's release date is before the input date
				totalMoviesBeforeDate++; // Then add one to the count of the total number of movies before the input date
			} 
		}
		return totalMoviesBeforeDate;
	}
	
	/**
	 * Edits the release date of a movie.
	 * @param name: The name of the movie to be edited.
	 * @param d: The date to be changed to.
	 */
	public void editReleaseDate(String name, Date d) {
		Iterator<Movie> it = receivedMovies.iterator(); // Create iterator to iterate over the comingMovies list
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getName().toLowerCase().equals(name.toLowerCase())) {  // If the current movie's name is the same as the movie we're trying to change the releaseDate of
				// That means that this is the element we are trying to change
				if (currMovie.getReceiveDate().compareTo(d) > 0) { // If the target movie's receive date is after the release date
					// Notify the user that this action cannot be performed. 
					System.out.print("Error: Cannot change a release date to before the receive date.");
					return; // End the method here
				}
				currMovie.setReleaseDate(d); // Otherwise, set the release date as the input date
				System.out.println("Edit was successful.");
				return;
			}
		}
		System.out.println("Error: Edit Unsuccessful.\nThat movie does not exist in our \"received\" movies list.");

	}
	
	/**
	 * Starts showing movies by changing all movies on a specific date to RELEASED, removing them from the received list and adding them to the released list.
	 * @param d: the date of the movie's to start showing. 
	 */
	public void startShowing(Date d) {
		Iterator<Movie> it = receivedMovies.iterator(); // Create iterator to iterate over the receivedMovies list
		ArrayList<Integer> removePos = new ArrayList<>();
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getReleaseDate().compareTo(d) == 0) { // If the current movie has the same target release date
				addToReleasedList(currMovie); // Add this movie to the releasedMovies list
				currMovie.setStatus(MovieStatus.RELEASED); // Change the status from received to released
				// Add the index position of the movie with the matching date to be changed to the list. 
				removePos.add(receivedMovies.indexOf(currMovie));
			} 
		}
		// Remove all elements to be transfered from the list if any exist. Otherwise notify user that they do not exist. 
		if (removePos.size() == 0) { System.out.println("No movies with that release date  the status \"Received\"."); }
		while (removePos.size() != 0)  { 
			// Notify the user which movie's have been changed. 
			System.out.println(receivedMovies.get(removePos.get(0)).getName() + " started showing successfully.");	
			// Now remove this movie from the receivedMovies list because it's been transferred to the releasedMovies list
			receivedMovies.remove(receivedMovies.get(removePos.get(0))); 
			// Now remove the current index position from the list of positions to be removed. 
			removePos.remove(0);
		} 
		
	}
	
	@Override
	public String toString() {
		
		// Iterator used to iterate through both movie lists.
		Iterator<Movie> iter = receivedMovies.iterator();
		
		// Used to build a String to print to the output file "movies.txt".
		StringBuilder sb = new StringBuilder();
		
		// Until the end of the receivedMovies list...
		while (iter.hasNext()) {
			// Append String representation of each movie followed by a new line. 
			sb.append(iter.next().toFileString() + "\n");
		}
		
		// Set the iterator to the releasedMovies list.
		iter = releasedMovies.iterator();
		
		// Until the end of the releasedMovies list...
		while (iter.hasNext()) {
			// Append String representation of each movie followed by a new line. 
			sb.append(iter.next().toFileString() + "\n");
		}
		
		return sb.toString();
	}

}
