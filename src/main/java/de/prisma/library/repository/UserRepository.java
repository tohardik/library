package de.prisma.library.repository;

import de.prisma.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.fullName = :name")
    User findByName(@Param("name") String name);

    @Query(
            value = "SELECT DISTINCT U.id as id, U.FIRST_NAME as firstName, U.LAST_NAME as lastName, U.GENDER as gender, U.MEMBERSHIP_START as membershipStart, U.MEMBERSHIP_END as membershipEnd " +
                    "FROM BORROWED_BOOKS b JOIN USERS U on U.ID = b.USER_ID",
            nativeQuery = true
    )
    List<Object[]> getBorrowingUsers();

    @Query(
            value = "SELECT DISTINCT U.id as id, U.FIRST_NAME as firstName, U.LAST_NAME as lastName, U.GENDER as gender, U.MEMBERSHIP_START as membershipStart, U.MEMBERSHIP_END as membershipEnd " +
                    "FROM USERS u LEFT JOIN BORROWED_BOOKS b on u.ID = b.USER_ID " +
                    "WHERE (b.RETURN_DATE < CURRENT_TIMESTAMP() OR b.ID IS NULL) AND u.MEMBERSHIP_END IS NULL",
            nativeQuery = true
    )
    List<Object[]> getActiveUsersWithoutBorrowedBooks();
}
