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

	public void updateBook() {
		
		//find book to update 

		synchronized(library) {
			//update book inside library
		}

	}

	public void getBook(int index) {
		try {


			synchronized(library) {

				// for (int i = 0; i < library.size(); i++) {

				// 	if (cure)
				// }

				Book currentBook = library.get(index);
				

				System.out.println(currentBook.isbn + 
					" " + currentBook.title +
					" " + currentBook.author +
					" " + currentBook.publisher +
					" " + currentBook.year);
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
					getBook(i);
					// Thread.sleep(1000);
					// System.out.println(Thread.currentThread().getId());
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void searchLibrary(){//String[] inputTypes, String[] inputValues) {

		//create the search array to match with
		//using wildcards in fields that dont need to be checked

		String[] wildCardArray = new String[] {".*","test2",".*",".*",".*"};
		System.out.println("test" + Arrays.toString(wildCardArray));

		//inputValues = wildarray
		

		for (int i = 0; i < library.size(); i++) {

			Book currentBook = library.get(i);

			String[] currbookarrary = new String[] {currentBook.isbn, currentBook.title,
			 currentBook.author, currentBook.publisher, currentBook.year};


			if (Arrays.equals(wildCardArray,currbookarrary)) {
				
				// for (int j =0; j < 5; j++){

				// String print = currbookarrary[j].toString();
				System.out.println(Arrays.toString(currbookarrary));
			}

		
			//loop through library checking each book with the search array
			//add the matched books to the outgoing set



		}
	
	}
}