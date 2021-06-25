package movies.lists;

import java.util.ArrayList;
import java.util.Date;

import movies.movie.Movie;

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
	public void display() {
		for (int i = 0; i < comingMovies.size(); i++) {
			System.out.print(comingMovies.get(i)); // Should I use iterator for this?
			
			/*
			 * Dear Neha, 
			   Yes you should be using iterators to iterate through the lists.
			   At any point we are traversing the ArrayLists the requirements
			   state that we should be using iterators.
			   Also, the display method is supposed to display both the Coming
			   and the Showing movies. I would print a heading "Showing"
			   followed by all the movies then print heading "Coming" followed by
			   all of the coming movies.
			*/
			
			// Doesn't hurt being here but the empty quotes are not necessary here. 
			System.out.println("");
		}
	}
	
	public void addToComingList(Movie m) {
		/* 
		*  TODO:: Iterators when iterating through lists
		*  TODO:: Keep from adding duplicate movies to the list. 
		*/ 
		for (int i = 0; i < comingMovies.size(); i++) {
			if (comingMovies.get(i).getReleaseDate().after(m.getReleaseDate())) { // Change "getReleaseDate()" later
				comingMovies.add(i, m);
				return;
			}
		}
		
		// Add the movie to the coming movies list.
		comingMovies.add(m);
	}
	
	public void addToShowingList(Movie m) {
		showingMovies.add(m);
	}
	
	public void editDescription(String name, String description) {
		/*
		*   TODO:: Must use iterators when iterating through lists.
		*/
		for (int i = 0; i < comingMovies.size(); i++) {
			if (comingMovies.get(i).getName().equals(name)) {
				comingMovies.get(i).setDescription(description);
				System.out.println("Edit was successful.");
				return;
			}
		}
		System.out.println("Error: Edit Unsuccessful.\nThat movie does not exist in our \"coming\" movies list.");
	}
	
	public int countComingMovies(Date d) {
		
		int totalMoviesBeforeDate = 0;
		/*
		*	Since the List is supposed to be Ordered by release date then
		*   binary would be faster.
		* 	
		*   
		*/
		for (int i = 0; i < comingMovies.size(); i++) { // Should I use binary or linear search?
			if (comingMovies.get(i).getReleaseDate().compareTo(d) < 0) {
				totalMoviesBeforeDate++;
			} 
		}
		return totalMoviesBeforeDate;
	}
	
	public void editReleaseDate(String name, Date d) {
	
		/*
		*  TODO:: Iterators when iterating through lists.
		*  TODO:: Don't allow changing release date to a date prior to receive date
		*/ 
		for (int i = 0; i < comingMovies.size(); i++) {
			if (comingMovies.get(i).getName().equals(name)) {
				comingMovies.get(i).setReleaseDate(d);
				System.out.println("Edit was successful.");
				return;
			}
		}
		System.out.println("Error: Edit Unsuccessful.\nThat movie does not exist in our \"coming\" movies list.");

	}
	
	// Should I create editReceiveDate method?
	
	// No, Neha,  this is not an option for the user. Receive dates are set. 
	// My only thinking about this was if we have the start showing method for a certain date
	// How can we Make a Movie showing when receive date hasn't passed yet. Logically, that
	// doesn't make sense. 
	
	
	/*
	*  TODO:: We still need a Start Showing method
	*/
	
	/*
	 * TODO:: Create a printAll function that prints all the movies from both list to the movies.txt file
	 * This function will be use to "SAVE" all changes made by the user by overwriting the data file 
	 * movies.txt. 
	 */

}
