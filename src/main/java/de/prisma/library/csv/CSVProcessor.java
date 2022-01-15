package de.prisma.library.csv;

import de.prisma.library.model.Book;
import de.prisma.library.model.BorrowedBook;
import de.prisma.library.model.User;
import de.prisma.library.repository.BookRepository;
import de.prisma.library.repository.BorrowedBookRepository;
import de.prisma.library.repository.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class CSVProcessor {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    public long loadUserCSV() throws IOException, ParseException {
        Reader in = new FileReader("src/main/resources/user.csv");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            if (record.size() > 1) {
                String lastName = record.get("Name");
                String firstName = record.get("First name");
                Date membershipStart = new SimpleDateFormat("MM/dd/yyyy").parse(record.get("Member since"));
                String membershipEndStr = record.get("Member till");
                Date membershipEnd = membershipEndStr == null || membershipEndStr.trim().length() == 0 ? null : new SimpleDateFormat("MM/dd/yyyy").parse(membershipEndStr);
                String gender = record.get("Gender");

                User user = User.builder()
                        .firstName(firstName).lastName(lastName).gender(gender)
                        .membershipStart(membershipStart).membershipEnd(membershipEnd)
                        .build();
                boolean alreadyExists = userRepository.findByName(user.getFullName()) != null;

                if (!alreadyExists)
                    userRepository.save(user);
            }
        }

        return userRepository.count();
    }

    public long loadBookCSV() throws IOException, ParseException {
        Reader in = new FileReader("src/main/resources/books.csv");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            if (record.size() > 1) {
                String title = record.get("Title");
                String author = record.get("Author");
                String genre = record.get("Genre");
                String publisher = record.get("Publisher");

                if (!isEmpty(title) && !isEmpty(genre) && !isEmpty(author) && !isEmpty(publisher)) {
                    Book book = Book.builder()
                            .title(title).author(author).genre(genre).publisher(publisher)
                            .build();
                    boolean alreadyExists = bookRepository.findByTitle(book.getTitle()) != null;
                    System.out.println(title + " " + author + " " + genre + " " + publisher);
                    if (!alreadyExists)
                        bookRepository.save(book);
                }
            }
        }
        return bookRepository.count();
    }

    public long loadBorrowedBookCSV() throws IOException, ParseException {
        Reader in = new FileReader("src/main/resources/borrowed.csv");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            if (record.size() > 1) {
                String borrowerName = record.get("Borrower");
                String bookTitle = record.get("Book");
                Date issuedDate = new SimpleDateFormat("MM/dd/yyyy").parse(record.get("borrowed from"));
                Date returnDate = new SimpleDateFormat("MM/dd/yyyy").parse(record.get("borrowed to"));

                User borrower = userRepository.findByName(borrowerName);
                Book book = bookRepository.findByTitle(bookTitle);

                BorrowedBook borrowedBook = BorrowedBook.builder()
                        .user(borrower).book(book).issuedDate(issuedDate).returnDate(returnDate)
                        .build();
                borrowedBookRepository.save(borrowedBook);
            }
        }
        return borrowedBookRepository.count();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public void sampleData() throws ParseException {
        // Users
        // Active user John
        User john = User.builder().firstName("John").lastName("Doe").gender("m")
                .membershipStart(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2021")).membershipEnd(null).build();
        userRepository.save(john);
        // Inactive user Jane
        User jane = User.builder().firstName("Jane").lastName("Doe").gender("f")
                .membershipStart(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2021"))
                .membershipEnd(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2021")).build();
        userRepository.save(jane);
        // Active user Jack
        User jack = User.builder().firstName("Jack").lastName("Doe").gender("m")
                .membershipStart(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2021")).membershipEnd(null).build();
        userRepository.save(jack);


        // Books
        // book1 - Art of Loving
        Book book1 = Book.builder()
                .title("The Art of Loving").author("Erich Fromm").genre("genre").publisher("publisher")
                .build();
        bookRepository.save(book1);
        // book2 - Sapiens
        Book book2 = Book.builder()
                .title("Sapiens").author("Yuval Noah Harrari").genre("genre").publisher("publisher")
                .build();
        bookRepository.save(book2);


        // Borrower information
        // John borrowed book1 - 5 days ago to 5 days later, i.e., currently borrowed
        Date issuedDate = Date.from(Instant.now().minus(Duration.ofDays(5)));
        Date returnDate = Date.from(Instant.now().plus(Duration.ofDays(5)));
        BorrowedBook borrowedByJohn = BorrowedBook.builder()
                .user(john).book(book1)
                .issuedDate(issuedDate)
                .returnDate(returnDate)
                .build();
        borrowedBookRepository.save(borrowedByJohn);
        // Jane borrowed book2 - 12 Dec to 15 Dec, i.e., borrowed in past
        BorrowedBook borrowedByJane = BorrowedBook.builder()
                .user(jane).book(book2)
                .issuedDate(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2021"))
                .returnDate(new SimpleDateFormat("dd/MM/yyyy").parse("15/12/2021"))
                .build();
        borrowedBookRepository.save(borrowedByJane);
        // Jack is active but doesn't borrow any books
    }
//
//    public static void main(String[] args) throws IOException, ParseException {
//        CSVProcessor p = new CSVProcessor();
//        p.loadUserCSV();
//    }
}