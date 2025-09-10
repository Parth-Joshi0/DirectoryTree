package A4Files;

/*
CS 1027B – Assignment 4
Name: Parth Joshi
Student Number: 251443404
Email: pjoshi62@uwo.ca
Created: Mar 27, 2025
*/

public class ComputerFile extends FileSystemObject {
	
	private int size; //private instance variable

	public ComputerFile(String name, int id, int size) { //constructor initializing instance variables
		super(name, id);
		this.size = size;
	}
	
	public int size() { //returns the size of the file
		return this.size;
	}

}
