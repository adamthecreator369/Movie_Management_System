package movies.movie;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Movie implements ComparableType<Movie> {
			
	// Data fields
	
	private String name; 
	private Date releaseDate; 
	private String description;
	private Date receiveDate;
	private MovieStatus status; 
	
	// Constructor 
	
	public Movie(String name, Date releaseDate, String description, Date receiveDate, MovieStatus status) {
		this.releaseDate = releaseDate;
		this.name = name;
		this.description = description;
		this.receiveDate = receiveDate;
		this.status = status;
		updateMovieStatus();
	}
	
	// Getters
	
	public Date getReleaseDate() { return releaseDate; }
	public String getName() { return name; }
	public String getDescription() { return description; }
	public Date getReceiveDate() { return receiveDate; }
	public MovieStatus getStatus() { return status; }   
	
	// Setters 
	
	public void setReleaseDate(Date date) { this.releaseDate = date; } 
	public void setName(String name) { this.name = name; }
	public void setDescription(String description) { this.description = description; } 
	public void setReceiveDate(Date date) { this.receiveDate = date; }
	public void setStatus(MovieStatus status) { this.status = status; }  
	
	// Member methods
	
	/**
	 * Checks and updates the MovieStatus to RELEASED if the Movie's release date is today's date or before.
	 */
	public void updateMovieStatus() {
		
		Calendar c = Calendar.getInstance();

		// Set the calendar to the beginning of today
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		// Create a date object using the above.
		Date today = c.getTime();
		
		// If the movie's release date is today or before set MovieStatus to RELEASED.
		if (getReleaseDate().compareTo(today) <= 0) this.status = MovieStatus.RELEASED;
	}
	
	@Override
	public int compareTo(Movie other) { return getReleaseDate().compareTo(other.getReleaseDate()); } 
	
	/**
	 * Formats then returns the Date in desired String format.
	 * @param date: The Date to be formatted.
	 * @returns: The formatted date.
	 */
	private String dateToString(Date date) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return dateFormat.format(date);
	}
	
	/**
	 * Constructs a String representation of the Movie object for writing to an output file.
	 * @returns: the String representation of the Movie object.
	 */
	public String toFileString() {
		return String.format("%s, %s, %s, %s, %s", getName(), dateToString(getReleaseDate()), getDescription(), dateToString(getReceiveDate()), getStatus());
	}
	
	@Override 
	public String toString() {
		return String.format("\n%s\nGenre: %s\nStatus: %s\nReceive Date: %s\nRelease Date: %s\n", getName(), getDescription(), getStatus(), dateToString(getReceiveDate()), dateToString(getReleaseDate()));
	}
}
