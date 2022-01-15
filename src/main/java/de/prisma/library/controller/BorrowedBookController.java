package de.prisma.library.controller;

import de.prisma.library.csv.CSVProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/borrowed-books")
public class BorrowedBookController {
    @Autowired
    private CSVProcessor csvProcessor;

    @PostMapping("load-csv")
    public long loadBorrowedBookCSV() throws IOException, ParseException {
        return csvProcessor.loadBorrowedBookCSV();
    }
}
