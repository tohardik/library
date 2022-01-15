package de.prisma.library.repository;

import de.prisma.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryUtil {

    private final UserRepository userRepository;

    @Autowired
    public QueryUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getBorrowingUsers() throws ParseException {
        List<Object[]> resultSet = userRepository.getBorrowingUsers();
        return mapUserRows(resultSet);
    }

    public List<User> getActiveUsersWithoutBorrowedBooks() throws ParseException {
        List<Object[]> resultSet = userRepository.getActiveUsersWithoutBorrowedBooks();
        return mapUserRows(resultSet);
    }

    private static List<User> mapUserRows(List<Object[]> rows) throws ParseException {
        List<User> users = new ArrayList<>();
        for (Object[] row : rows) {
            User user = User.builder()
                    .id(Long.parseLong(row[0].toString()))
                    .firstName(row[1].toString())
                    .lastName(row[2].toString())
                    .gender(row[3].toString())
                    .membershipStart(new SimpleDateFormat("yyyy-MM-dd").parse(row[4].toString()))
                    .membershipEnd(row[5] == null ? null : new SimpleDateFormat("yyyy-MM-dd").parse(row[5].toString()))
                    .build();
            users.add(user);
        }
        return users;
    }
}
