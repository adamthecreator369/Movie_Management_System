package movies.lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

import movies.movie.Movie;
import movies.movie.MovieStatus;

public class MovieList {
	
	// Data fields
	ArrayList<Movie> showingMovies;
	ArrayList<Movie> comingMovies;
	
	// Constructor
	public MovieList() {
		showingMovies = new ArrayList<>();
		comingMovies = new ArrayList<>();
	}
	
	// Class-member methods
	
	/** Displays all of the movies from both movie lists by status "Coming" or "Showing" */
	public void display() {
		
		// Print a heading 
		System.out.println("\n==============\nComing Movies:\n==============");
		
		// Iterator for comingMovies list
		Iterator<Movie> itComing = comingMovies.iterator();
		
		// Until we have reached the end of the MovieList
		while (itComing.hasNext()) {
			System.out.print(itComing.next());
			System.out.println();
		}
		
		// Print a divider between the displayed movie lists
		for (int i = 0; i <= 53; i++) System.out.print("-");
		
		// Print heading to the standard console.
		System.out.println("\n\n===============\nShowing Movies:\n===============");
		
		// Iterator for showingMovies list
		Iterator<Movie> itShowing = showingMovies.iterator();
		
		// Until we have reached the end of the showing list
		while (itShowing.hasNext()) {
			System.out.print(itShowing.next());
			System.out.println();
		}
	}
	
	/** Adds a movie to the coming movie list */
	public void addToComingList(Movie m) {
		ListIterator<Movie> it = comingMovies.listIterator(); // Create iterator to iterate over the comingMovies list
		int moviePos = -1; 
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = (Movie) it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getName().equals(m.getName())) { // If the input movie already exists in the list
				return; // Then end the method here
			}
			if (currMovie.getReleaseDate().after(m.getReleaseDate()) && moviePos == -1) { // If the input movie should be inserted here because the element after it is greater than it
				moviePos = comingMovies.indexOf(currMovie);
			}
		}
		// If an greater element was found place movie before that element.
		if (moviePos != -1) {
			comingMovies.add(moviePos, m);
		} else { // Otherwise,
			// Add the movie to the end of the coming movies list in case the movie has the latest release date
			comingMovies.add(m);
		}
	}
	
	/** Adds a movie to the showing movie list */
	public void addToShowingList(Movie m) {
		showingMovies.add(m); // Add the movie to the end of the showingMovies list
	}
	
	/**
	 * Edits the description of a given movie.
	 * @param name: The name of the movie to be edited.
	 * @param description: The new description.
	 */
	public void editDescription(String name, String description) {
		Iterator<Movie> it = comingMovies.iterator(); // Create iterator to iterate over the comingMovies list
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = (Movie) it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getName().equals(name)) { // If the current movie's name is the same as the movie we're trying to change the description of
				currMovie.setDescription(description); // Change the description
				System.out.println("Edit was successful."); 
				return;
			}
		}
		System.out.println("Error: Edit Unsuccessful.\nThat movie does not exist in our \"coming\" movies list.");
	}
	
	/**
	 * Counts and returns the number of coming movies before a given date.
	 * @param d: the date to count up to.
	 * @return: the number of coming movies before the date.
	 */
	public int countComingMovies(Date d) {
		int totalMoviesBeforeDate = 0; // This will store the number of movies before the input date
		Iterator<Movie> it = comingMovies.iterator(); // Create iterator to iterate over the comingMovies list
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
		Iterator<Movie> it = comingMovies.iterator(); // Create iterator to iterate over the comingMovies list
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getName().equals(name)) {  // If the current movie's name is the same as the movie we're trying to change the releaseDate of
				// That means that this is the element we are trying to change
				if (currMovie.getReceiveDate().compareTo(d) > 0) { // If the target movie's receive date is after the release date
					// Notify the user this action cannot be performed.
					System.out.print("Error: Cannot change a release date to before the receive date.");
					return; // End the method here
				}
				currMovie.setReleaseDate(d); // Otherwise, set the release date as the input date
				System.out.println("Edit was successful.");
				return;
			}
		}
		System.out.println("Error: Edit Unsuccessful.\nThat movie does not exist in our \"coming\" movies list.");

	}
	
	/**
	 * Changes all movies on a specific date to Showing, removing them from the coming list and adding them to the showing list.
	 * @param d: the date of the movie's to start showing. 
	 */
	public void startShowing(Date d) {
		Iterator<Movie> it = comingMovies.iterator(); // Create iterator to iterate over the comingMovies list
		while (it.hasNext()) { // While there are more elements to iterate over
			Movie currMovie = it.next(); // Move the iterator to the next element and store the element that was passed over
			if (currMovie.getReleaseDate().compareTo(d) == 0) { // If the current movie has the same target release date
				addToShowingList(currMovie); // Add this movie to the showingMovies list
				currMovie.setStatus(MovieStatus.Showing); // Change the status from received to released
				comingMovies.remove(currMovie); // Now remove this movie from the comingMovies list because it's been transferred to the showingMovies list
			} 
		}
	}
	
	@Override
	public String toString() {
		
		// Iterator used to iterate through both movie lists.
		Iterator<Movie> iter = comingMovies.iterator();
		
		// Used to build a String to print to the output file "movies.txt".
		StringBuilder sb = new StringBuilder();
		
		// Until the end of the comingMovies list...
		while (iter.hasNext()) {
			// Append String representation of each movie followed by a new line. 
			sb.append(iter.next().toFileString() + "\n");
		}
		
		// Set the iterator to the showingMovies list.
		iter = showingMovies.iterator();
		
		// Until the end of the showingMovies list...
		while (iter.hasNext()) {
			// Append String representation of each movie followed by a new line. 
			sb.append(iter.next().toFileString() + "\n");
		}
		
		return sb.toString();
	}

}
