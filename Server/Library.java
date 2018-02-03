import java.io.*;
import java.util.*;

public class Library {

	private List<Book> library;

	public Library() {
		library = Collections.synchronizedList(new ArrayList<Book>());
	}

	private class Book {
		String isbn;
		String title;
		String author;
		String publisher;
		String year;
	}

	public void submitBook(String bookInfo[]) {

		Book book = new Book();

		book.isbn = bookInfo[0];
		book.title = bookInfo[1];
		// book.author = bookInfo[2];
		// book.publisher = bookInfo[3];
		// book.year = bookInfo[4];

		synchronized(library) {
			library.add(book);
		}
	}

	public void updateBook(String updatedBook[]) {
		//library[index] = newBook;

	}

	public void getBook(int index) {
		try {
			synchronized(library) {
				Book currentBook = library.get(index);
				System.out.println(currentBook.isbn + " " + currentBook.title);
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
			synchronized(library) {
				for (int i = 0; i < library.size(); i++) {
					getBook(i);
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}