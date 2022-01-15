package de.prisma.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 3, max = 50)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 3, max = 50)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonIgnore
    @Formula(value = "CONCAT(last_name, ',', first_name) ")
    private String fullName;

    @NotBlank(message = "Gender cannot be empty")
    @Column(name = "gender", nullable = false, length = 1)
    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "membership_start", nullable = false, updatable = false)
    private Date membershipStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "membership_end")
    private Date membershipEnd;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BorrowedBook> borrowedBooks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getMembershipStart() {
        return membershipStart;
    }

    public void setMembershipStart(Date membershipStart) {
        this.membershipStart = membershipStart;
    }

    public Date getMembershipEnd() {
        return membershipEnd;
    }

    public void setMembershipEnd(Date membershipEnd) {
        this.membershipEnd = membershipEnd;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
