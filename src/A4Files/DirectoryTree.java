package A4Files;

/*
CS 1027B – Assignment 4
Name: Parth Joshi
Student Number: 251443404
Email: pjoshi62@uwo.ca
Created: Mar 27, 2025
*/

import java.util.Iterator;

public class DirectoryTree {
	FileSystemObject root; //private instance variable
	
	public DirectoryTree(FileSystemObject rt) { //constructor initializing instance variables
		this.root = rt;
	}
	
	public FileSystemObject getRoot () { //return the root
		return this.root;
	}
	
	public int level(FileSystemObject fso) { //recursively get the level of the root
		if (isRoot(fso)) //base case
			return 0;
		return level(fso.getParent()) + 1; //recursively go up the tree
	}
	
	public FileSystemObject lca (FileSystemObject a, FileSystemObject b) { //method to get lowest common ancestor
		if (level(a) < level(b)) { //loop through until a and b are or the same level by going up the parent chain
			while (!(level(a) == level(b)))
				b = b.getParent();
		}
		else if (level(a) > level(b)) {
			while (!(level(a) == level(b)))
				a = a.getParent();
		}
		
		if (a == b) //if they are the same return it
			return a;
		
		return lcaRecursive(a, b); //recursively go up both till lca found using recursive method
	}
	
	private FileSystemObject lcaRecursive (FileSystemObject a, FileSystemObject b) {
		if (a.getParent() == (b.getParent())) { //base case if both have same parents
			return a.getParent();
		}
		return lcaRecursive(a.getParent(), b.getParent()); //recursively call method to go up tree
	}
	
	public String buildPath (FileSystemObject a, FileSystemObject b) {
		if (a.getParent() == b.getParent()) //if both in the same folder
			return b.getName();
		
		String path = ""; //Initialize path variable
		FileSystemObject commonParent = lca(a, b); //find the lowest common ancestor 
		
		while (level(commonParent) != level(a)) { //go up a until at the common ancestor
			path += "../"; //add for as long as you have to go up
			a = a.getParent(); //go up the chain
		}
		
		FileSystemObject x = b; //Initialize helper variable
		while (commonParent != b.getParent()) { //loop until common parent is b's parent
			while (x.getParent() != commonParent) //loop until helper variable's parent is common parent
				x = x.getParent();
			path += x.getName() + "/"; //add x's name to the path string
			commonParent = x; //set common parent to x
			x = b; //set x to b again
		}
		path += b; //add b to the path string
		
		return path;
	}
	
	public String toString() { //method to get string representation of the tree using private recursion helper method
		return toStringRecursion(this.getRoot());
	}
	
	private String toStringRecursion(FileSystemObject object) { //private recursion helper method for string
		if (object.isFile()) //base case if object is file (ie. no children)
			return indentationHelper(object) + "\n"; //return name, with help of indentation helper private method to assign proper level indentation
		
		String stringRep = indentationHelper(object) + "\n"; //add the object to the string representation with the proper indentation

		
		if (object.getChildren() != null) { //check if folder has a child
			Iterator<FileSystemObject> iterator = object.getChildren().iterator(); //create iterator
		
			while (iterator.hasNext()) //while the folder has more children
				stringRep += toStringRecursion(iterator.next()); //recursively call this method on the child adding the result to string helper
		}
		
		return stringRep; //return string
		
	}
	
	private String indentationHelper(FileSystemObject object) { //private helper method to indent each name based on level
		if (isRoot(object)) //if is root, do not indent, return name directly
			return object.getName();
		
		String stringRep = ""; //initialize variable
		
		for (int i = 1; i < level(object); i++)
			stringRep += "  "; //add 2 spaces per level down
		
		stringRep += " - " + object.getName(); //add spaces hyphen space in front of the name, and the name
		
		return stringRep; //return string
	}
	
	public void cutPaste(FileSystemObject f, FileSystemObject dest) { //method to cut and paste some parts of tree to a different part
		if (dest.isFile()) //error check if destination file is a file
			throw new DirectoryTreeException("cannot store files/folders inside a file.");
		if (isRoot(f)) //error check if we are cutting the root
			throw new DirectoryTreeException("cannot remove/cut/move the root of the whole tree.");
		f.getParent().getChildren().remove(f); //removes f from, its parent
		f.setParent(dest); //sets new parent of cutting fso
		dest.addChild(f); //adds the file system object to the destination
	}
	
	public void copyPaste(FileSystemObject f, FileSystemObject dest) { //method to copy and paste a part of tree from one spot to another recursively
		if (dest.isFile()) //error check if destination is a file
			throw new DirectoryTreeException("cannot store files/folders inside a file.");
		
		dest.addChild(clone(f)); //add child of recursive method
	}
	
	private FileSystemObject clone(FileSystemObject f) { //private recursive method to copy and paste
		FileSystemObject clone; //create variable
		
		if (f.isFile()) //base case if f is a file, copy it with id + 100
			clone = new ComputerFile(f.getName(), f.getID() + 100, f.size());
		else {
			clone = new FileSystemObject(f.getName(), f.getID() + 100); //if folder, copy it with id + 100
			if (f.getChildren() != null) { //if f has children loop through them using iterator
				Iterator<FileSystemObject> iterator = f.getChildren().iterator();
				while (iterator.hasNext()) { //checks if there is another child
					FileSystemObject child = iterator.next(); //gets the child
					clone.addChild(clone(child)); //clones the children recursively adding the children to our variable
				}
			}
		}
		
		return clone; //return clone

	}
	
	private boolean isRoot(FileSystemObject object) { //private helper method to check if something is a root
		return object == this.getRoot();
	}
	

}