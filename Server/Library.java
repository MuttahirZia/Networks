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
			String out = "";
			switch (n) {
				case 0: out = this.isbn; break;
				case 1: out = this.title; break;
				case 2: out = this.author; break;
				case 3:	out = this.publisher; break;
				case 4: out = this.year; break;
			}
			return out;
		}
	}



	public void submitBook(String isbn, String title, String author, 
		String publisher, String year) {

		Book book = new Book();

		book.isbn = isbn;
		book.title = title;
		book.author = author;
		book.publisher = publisher;
		book.year = year;

		synchronized(library) {
			library.add(book);
		}
	}



	public void updateBook(String isbn, String[] inputValues) {

		String[] inputTypes = {isbn,"","","",""};

		List<Book> booksToUpdate = new ArrayList<Book>(searchLibrary(inputTypes,inputValues));

		Book currentBook = booksToUpdate.get(0);
		currentBook.title = "lol";
	}



	public void getBook(String[] inputTypes, String[] inputValues) {
		try {
			synchronized(library) {

				List<Book> foundBooks = new ArrayList<Book>(searchLibrary(inputTypes,inputValues));

				for (int i = 0; i < foundBooks.size(); i++){

					Book currentBook = foundBooks.get(i);
					currentBook.printBook();
				}
			}
			
		} catch (Exception e) {
			System.err.println(e);
		}
	}



	public void removeBook(int index) {
		try {
			synchronized(library) {
				library.remove(index);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}



	public void displayLibrary() {

		try{
			synchronized(library) { //needed?
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
		for (int k = 0; k < 5; k++) {
			if (inputTypes[k] != "") {
				checkArray[k] = true; 
			}
		}

		System.out.println(Arrays.toString(inputTypes));
		System.out.println(Arrays.toString(checkArray));
		System.out.println("Test against: " + Arrays.toString(inputValues));

		try{
			synchronized(library) {
				for (int i = 0; i < library.size(); i++) {

					Book currentBook = library.get(i);
					boolean match = true;

					for (int j = 0; j < 5; j++) {

						if (checkArray[j] && inputValues[j] != currentBook.index(j)) {
							match = false;
						}
					}

					if (match) {
						matchedBooks.add(currentBook);
					}

				}
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		return matchedBooks;
	}
}