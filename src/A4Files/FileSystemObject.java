package A4Files;

/*
CS 1027B – Assignment 4
Name: Parth Joshi
Student Number: 251443404
Email: pjoshi62@uwo.ca
Created: Mar 27, 2025
*/

public class FileSystemObject implements Comparable<FileSystemObject>{
	
	private String name; 
	private OrderedListADT<FileSystemObject> children;
	private FileSystemObject parent;
	private int id; //creates private instance variables

	public FileSystemObject(String name, int id) { //constructor initializing private instance variables
		this.name = name;
		this.id = id;
		
		if (!isFile()) //checks if a folder, if folder it will have children
			children = new ArrayOrderedList<FileSystemObject>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public FileSystemObject getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(FileSystemObject parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	public OrderedListADT<FileSystemObject> getChildren() {
		return children;
	}

	/**
	 * @return the id
	 */
	public int getID() {
		return id;
	}
	
	public boolean isFile() { //checks if class is ComputerFile
		return this instanceof ComputerFile;
	}
	
	public void addChild(FileSystemObject node) { //adds a child to the folder, if a file throws error
		if (isFile()) //error check to see if is file
			throw new DirectoryTreeException("Cannot store a file/folder within a file");
		if (containsChildName(node.getName())) //error check to see if folder contains same name
			throw new DirectoryTreeException("Cannot have two files/folders with the same name stored in the same folder");
		children.add(node); //adds node to children
		node.setParent(this); //sets the nodes parent
		
	}
	
	private boolean containsChildName(String name) { //private helper method to check if a folder contains same name
		for (FileSystemObject child : children) { //loops through all the children
			if (child.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public String toString() { //returns the name
		return this.getName();
	}
	
	public int size() { //returns the size by using private recursion helper method
		if (isFile())
			return this.size();
		
		return sizeRecursion(this);
	}
	
	private int sizeRecursion(FileSystemObject object) { //private recursion method to count size
		if (object.isFile()) //base case
			return object.size(); //checks if object is a file if so returns the size of the file
		
		int total = 0; //Initializes total size
		
		if (object.getChildren() != null) { //checks if folder has children
			//gets the iterator
			ArrayIterator<FileSystemObject> iterator = (ArrayIterator<FileSystemObject>) object.getChildren().iterator();
			
			while (iterator.hasNext()) //loops through children adding the size as it goes
				total += sizeRecursion(iterator.next()); //recursively adds the size to the total
		}
		
		return total;
	}

	@Override
	public int compareTo(FileSystemObject o) { //compare to method from Comparable override
		if (!this.isFile() && o.isFile()) //if this is a folder and other a file
			return -1;
		else if (this.isFile() && !o.isFile()) //if this is a file and other a folder
			return 1;
		return this.getName().compareToIgnoreCase(o.getName()); //if both the same, compare them ignoring case
	}

}