package movies.movie;

public interface ComparableType<T> {
	
	/**
	 * Compares the current object with another object.
	 * @param other: the other object to compare with.
	 * @return positive integer if current object is greater the other object,
	 * 			0 if the current object is equal to the other object, and 
	 * 			a negative integer if the current object is less than the other object.
	 */
	int compareTo(T other);
	
}
