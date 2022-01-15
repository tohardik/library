package de.prisma.library.repository;

import de.prisma.library.model.Book;
import de.prisma.library.model.BorrowedBook;
import de.prisma.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    @Query("SELECT b FROM BorrowedBook b WHERE b.user = :user AND b.book = :book")
    BorrowedBook findForUserAndBook(@Param("user") User user, @Param("book") Book book);
}
