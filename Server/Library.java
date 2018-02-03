import java.io.*;
import java.util.*;

public class Library {

	private List<Book> library;

	public Library() {
		library = new ArrayList<Book>();
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
		book.author = bookInfo[2];
		book.publisher = bookInfo[3];
		book.year = bookInfo[4];

		library.add(book);
	}

	public void updateBook(String updatedBook[]) {
		//library[index] = newBook;

	}

	public void getBook() {
		//currentBook = library.indexOf(BookString)
	}

	public void removeBook(Book book) {
		library.remove(library.indexOf(book));
	}
}