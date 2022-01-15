package de.prisma.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "books")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100)
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Author cannot be empty")
    @Size(max = 40)
    @Column(name = "author", nullable = false)
    private String author;

    @NotBlank(message = "Genre cannot be empty")
    @Size(max = 20)
    @Column(name = "genre", nullable = false)
    private String genre;

    @NotBlank(message = "Publisher cannot be empty")
    @Size(max = 50)
    @Column(name = "publisher", nullable = false)
    private String publisher;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BorrowedBook> borrowedInformation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<BorrowedBook> getBorrowedInformation() {
        return borrowedInformation;
    }

    public void setBorrowedInformation(List<BorrowedBook> borrowedInformation) {
        this.borrowedInformation = borrowedInformation;
    }
}
