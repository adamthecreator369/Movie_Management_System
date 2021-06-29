# Movie_Management_System

<b>Objective</b><br>
The objective of the movie management system group project was to develop a menu-based Java program that maintains two lists of movies, “received” and “released”, and allows the user to enter commands to perform operations to the lists and the movies contained within those lists.

<b>Our team:</b><br> 
Adam Jost<br>
Neha Metlapalli

<b>System Functionality Overview</b><br>
Our team developed a system to read in and store data from an input file containing a list of movies and all of their data. A Movie object is created for each individual movie and is then stored into one of two lists, received movies or released movies. When populating the movie lists, the system automatically updates the status of all movies with a release date of today’s date or prior to “released”. The system then prints a simple console interface consisting of a heading and a command key which provides the user with all available commands at their disposal. The operations that the user can perform includes displaying the command key, displaying the movies from both of the movie lists, editing a movie’s release date, editing a movie’s description, changing a movie’s status from “received” to “released”, counting the number of movie’s prior to a given date whose movie status is “received”, canceling the current command in progress, saving all changes made, and exiting the program. The system gracefully handles invalid input from the user and responds with success or error messages after the completion of all operations that are performed.

<b>File Input</b><br>
At the start of the program, a one-time operation occurs where the system reads in a source document (.txt file) which contains a list of movies and their data in the following format:
<br><br>
<i>name, release date, description, receive date, status</i>
<br>
<br>All of the above-mentioned data is initially accepted line-by-line as String values and then parsed accordingly when instantiating Movie objects.
<br><br>
<b>Interface for Accepting User Input</b><br>
The user interface is a simple command line interface and is constructed using a textual-based design utilizing “=” and “-“ symbols as separators which is printed to the standard console. This text-based, menu-driven system provides the user with a pleasing appearance and precise instructions of how to proceed and accomplish the operations they may wish to perform.
User Input
The user input design consists of precise input instructions, which are easy, logical, and easy to follow. The system then evaluates the user input data and depending on whether it’s in the expected format or not, the process either goes to the next step or rejects the input by outputting an error response and then once again requests the required information. If an operation is completed
successfully using only valid input the user will receive a success message
signifying that the operation has been completed successfully.
<br><br><b>Valid User Input</b><br>
• Commands: Can be any combination of capital and non-capital letters of any of the acknowledged commands.
<br>
• Movie name: Must be a non-empty string.
<br>
• Date: Must be entered in the format 01/01/2021.
<br>
• Description: Must be a non-empty string.
<br>
• Status: Can be any combination of capital and non-capitol letters of the
words “received” or “released”.
<br><br>
<b>Major Classes</b>
<br>
There is a total of three major classes:
<br>• SystemManager <br>• MovieList
<br>• Movie
<br><br>
<b>Relationship Between the Major Classes</b>
<br>
• SystemManager creates a MovieList object.<br>
• SystemManager creates many Movie objects.<br>
• SystemManager can modify a MovieList.<br>
• MovieList stores 0...* Movie objects in one of two ArrayLists data structures.<br>
• A MovieList can modify a Movie object’s description or releaseDate.<br>
