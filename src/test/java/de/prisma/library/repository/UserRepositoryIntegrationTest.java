package de.prisma.library.repository;

import de.prisma.library.model.Book;
import de.prisma.library.model.BorrowedBook;
import de.prisma.library.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryIntegrationTest {

    @Autowired
    private QueryUtil queryUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @BeforeEach
    public void init() throws ParseException {
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

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
        bookRepository.deleteAll();
        borrowedBookRepository.deleteAll();
    }

    @Test
    public void testGetBorrowingUsers() throws ParseException {
        List<User> borrowingUsers = queryUtil.getBorrowingUsers();

        // Assert John and Jane are returned as borrowers
        Assertions.assertEquals(2, borrowingUsers.size());
        Set<String> borrowerNames = borrowingUsers.stream().map(User::getFirstName).collect(Collectors.toSet());
        Assertions.assertTrue(borrowerNames.containsAll(Arrays.asList("John", "Jane")));
    }

    @Test
    public void testGetActiveUsersWithoutBorrowedBooks() throws ParseException {
        List<User> activeNonBorrowingUsers = queryUtil.getActiveUsersWithoutBorrowedBooks();

        // Assert only the active member Jack is returned
        Assertions.assertEquals(1, activeNonBorrowingUsers.size());
        Assertions.assertEquals("Jack", activeNonBorrowingUsers.get(0).getFirstName());
    }
}
