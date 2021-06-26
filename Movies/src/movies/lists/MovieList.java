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
		Iterator it = comingMovies.iterator();
		for (int i = 0; i < comingMovies.size(); i++) {
			Movie currMovie = it.next();
			if (currMovie.getName().equals(m.getName())) {
				return;
			}
			if (currMovie.getReleaseDate().after(m.getReleaseDate())) { // Change "getReleaseDate()" later
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
		Iterator it = comingMovies.iterator();
		for (int i = 0; i < comingMovies.size(); i++) {
			Movie currMovie = it.next();
			if (currMovie.getName().equals(name)) {
				currMovie.setDescription(description);
				System.out.println("Edit was successful.");
				return;
			}
		}
		System.out.println("Error: Edit Unsuccessful.\nThat movie does not exist in our \"coming\" movies list.");
	}
	
	public int countComingMovies(Date d) {
		
		int totalMoviesBeforeDate = 0;
		for (int i = 0; i < comingMovies.size(); i++) {
			if (comingMovies.get(i).getReleaseDate().compareTo(d) < 0) {
				totalMoviesBeforeDate++;
			} 
		}
		return totalMoviesBeforeDate;
	}
	
	public void editReleaseDate(String name, Date d) {
		Iterator it = comingMovies.iterator();
		for (int i = 0; i < comingMovies.size(); i++) {
			Movie currMovie = it.next();
			if (currMovie.getName().equals(name)) {
				if (currMovie.getReceiveDate.compareTo(d) > 0) {
					return;
				}
				currMovie.setReleaseDate(d);
				System.out.println("Edit was successful.");
				return;
			}
		}
		System.out.println("Error: Edit Unsuccessful.\nThat movie does not exist in our \"coming\" movies list.");

	}
	
	public void startShowing(Date d) {
		for (int i = 0; i < comingMovies.size(); i++) {
			if (comingMovies.get(i).getReleaseDate().compareTo(d) == 0) {
				addToShowingList(comingMovies.get(i));
				comingMovies.get(i).setStatus("RELEASED");
				comingMovies.remove(i);
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
