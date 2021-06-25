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

	// My only thinking about this was if we have the start showing method for a certain date
	// How can we Make a Movie showing when receive date hasn't passed yet. Logically, that
	// doesn't make sense. 
	
	
	public void startShowing(Date d) {
		for (int i = 0; i < comingMovies.size(); i++) {
			if (comingMovies.get(i).getReleaseDate().compareTo(d) == 0) {
				addToShowingList(comingMovies.get(i));
				comingMovies.get(i).setStatus("RELEASED");
				comingMovies.remove(i);
			} 
		}
	}
	
	public String printAll() {
		
	}
	
	/*
	 * TODO:: Create a printAll function that prints all the movies from both list to the movies.txt file
	 * This function will be use to "SAVE" all changes made by the user by overwriting the data file 
	 * movies.txt. 
	 */

}
