import java.io.*;
import java.util.*;

public class Library {

	private List<Book> library;

	public Library() {
		library = Collections.synchronizedList(new ArrayList<Book>());
	}

	public class Book {
		String isbn;
		String title;
		String author;
		String publisher;
		String year;

		private void printBook() {
			System.out.println(
				this.isbn + " " + this.title + " " + 
				this.author + " " + this.publisher + " " + this.year);
		}

		private String index(int n) {
			String output = "";
			switch (n) {
				case 0: output = this.isbn; break;
				case 1: output = this.title; break;
				case 2: output = this.author; break;
				case 3:	output = this.publisher; break;
				case 4: output = this.year; break;
			}
			return output;
		}
	}





	public String submitBook(String[] inputValues) {

		String submitStatus;
		String[] inputTypes = {inputValues[0],"","","",""};
		List<Book> matchedBooks = new ArrayList<Book>(searchLibrary(inputTypes, inputValues));

		try {
			if (matchedBooks.size() == 0) {

				Book book = new Book();
				book.isbn = inputValues[0];
				book.title = inputValues[1];
				book.author = inputValues[2];
				book.publisher = inputValues[3];
				book.year = inputValues[4];

				synchronized(library) {
					library.add(book);
				}
				submitStatus = "Book successfully submitted";
			} 
			else {
				submitStatus = "Book with submitted ISBN already exists";
			} 

		} catch (Exception e) { 
			submitStatus = "Error occured with adding Book";
		}

		return submitStatus;
	}





	public String updateBook(String[] inputValues) {

		String updateStatus;
		String[] inputTypes = {inputValues[0],"","","",""};

		try {
			synchronized(library) {
				List<Book> booksToUpdate = new ArrayList<Book>(searchLibrary(inputTypes, inputValues));
		
				if (booksToUpdate.size() > 1) {
					updateStatus = "Error occured as multiple books have same ISBN";
				}
				else if (booksToUpdate.size() == 0) {
					updateStatus = "No matching books found";
				}
				else if (booksToUpdate.size() == 1) {

					Book currentBook = booksToUpdate.get(0);
					for (int j = 0; j < 5; j++) {
						if (!inputValues[j].equals("")) {	
							switch(j) {
								case 1: currentBook.title = inputValues[1]; break;
								case 2: currentBook.author = inputValues[2]; break;
								case 3:	currentBook.publisher = inputValues[3]; break;
								case 4: currentBook.year = inputValues[4]; break;
							}
						}
					}
					updateStatus = "Book match found and updated";
				}
				else {
					updateStatus = "Error occured on update";
				}
			}
		} catch (Exception e) {
			updateStatus = "Error occured on update";
		}

		return updateStatus;
	}


	public String[][] getBook(String[] inputValues) {
		List<Book> foundBooks = new ArrayList<Book>();
		String[][] returnVal;
		try {
			synchronized(library) {
				foundBooks = searchLibrary(inputValues,inputValues);

				if (foundBooks.size() < 1) {
					returnVal = new String[0][];
				} else {
					returnVal = new String[foundBooks.size()][5];
					
					for (int i = 0; i < foundBooks.size(); i++) {
						Book currentBook = foundBooks.get(i);

						returnVal[i][0] = currentBook.isbn;
						returnVal[i][1] = currentBook.title;
						returnVal[i][2] = currentBook.author;
						returnVal[i][3] = currentBook.publisher;
						returnVal[i][4] = currentBook.year;
					}
					// for (int j = 0; j < foundBooks.size(); j++) {
					// 	System.out.println(Arrays.toString(returnVal[j]));
					// }
				}
			}

		} catch (Exception e) {
			System.err.println(e);
			returnVal = new String[0][];
		}

		return returnVal;
	}





	public String removeBook(String[] inputValues) {

		String removeStatus = "Trying remove";
		int numberRemoved = 0;

		try {
			synchronized(library) {
				List<Book> foundBooks = new ArrayList<Book>(searchLibrary(inputValues, inputValues));

				if (foundBooks.size() > 0) {

					for (int i = 0; i < foundBooks.size(); i++) {
						Book currentBook = foundBooks.get(i);
						currentBook.printBook();

						for (int j = 0; j < library.size(); j++) {
							if (library.get(j).equals(currentBook)) {
								library.remove(j);
								numberRemoved++;
							}
						}
						removeStatus = numberRemoved + " books successfully removed";
					}
				}
				else if (foundBooks.size() <= 0){
					removeStatus = "No matching books found";
				}
			}
		} catch (Exception e) {
			removeStatus = "Error occured on remove";
		}

		return removeStatus;
	}





	public void displayLibrary() {

		System.out.println("Display:");
		try{
			synchronized(library) { 
				for (int i = 0; i < library.size(); i++) {
					Book book = library.get(i);
					book.printBook();
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}





	public List searchLibrary(String[] inputTypes, String[] inputValues) {

		List<Book> matchedBooks = new ArrayList<Book>();

		boolean[] checkArray = new boolean[] {false,false,false,false,false};
		boolean getAll = true;

		for (int k = 0; k < 5; k++) {
			if (inputTypes[k].trim().length() != 0) {
				checkArray[k] = true;
				getAll = false; 
			}
		}

		// System.out.println(getAll);
		// System.out.println("InputTypes:" + Arrays.toString(inputTypes));
		// System.out.println("Columns check:" + Arrays.toString(checkArray));
		// System.out.println("Test against: " + Arrays.toString(inputValues));

		for (int i = 0; i < library.size(); i++) {

			Book currentBook = library.get(i);
			boolean match = true;

			for (int j = 0; j < 5; j++) {
				if (checkArray[j] && !inputValues[j].equals(currentBook.index(j)))  {
					match = false;
				}
			}

			if (match || getAll) {
				matchedBooks.add(currentBook);
			}
		}

		//System.out.println("Size of matchbook: "+ matchedBooks.size());
		return matchedBooks;
	}
}