package de.prisma.library.controller;

import de.prisma.library.csv.CSVProcessor;
import de.prisma.library.model.User;
import de.prisma.library.repository.QueryUtil;
import de.prisma.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QueryUtil queryUtil;

    @Autowired
    private CSVProcessor csvProcessor;

    @PostMapping("load-csv")
    public long loadUserCSV() throws IOException, ParseException {
        return csvProcessor.loadUserCSV();
    }

    @GetMapping("borrowers")
    public List<User> getBorrowingUsers() throws ParseException {
        return queryUtil.getBorrowingUsers();
    }

    @GetMapping("active-non-borrowers")
    public List<User> getActiveUsersWithoutBorrowedBooks() throws ParseException {
        return queryUtil.getActiveUsersWithoutBorrowedBooks();
    }

}