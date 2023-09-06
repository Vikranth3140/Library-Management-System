//C:\Users\vikra\IdeaProjects\Assignment1\src\main\java\org\example

package org.example;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.HashSet;
import java.util.Set;

class Book {
    private static int nextBookID = 1;
    private final String bookID;
    private final String title;
    private final String author;
    private final int totalCopies;
    private int availableCopies;

    public Book(String title, String author, int totalCopies) {
        this.bookID = String.valueOf(nextBookID++);
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public String getAuthor() {
        return author;
    }

    public void decreaseAvailableCopies() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void increaseAvailableCopies() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

}

class Member {
    private static int nextMemberID = 1;
    private final String memberID;
    private final String name;
    private String phoneNumber;
    private final double penaltyAmount;
    private static Set<String> uniquePhoneNumbers = new HashSet<>();
    public Member(String name, String phoneNumber) {
        this.memberID = String.valueOf(nextMemberID++);
        this.name = name;
        setPhoneNumber(phoneNumber);
        this.penaltyAmount = 0.0;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        while (!isPhoneNumberValid(phoneNumber) || !isPhoneNumberUnique(phoneNumber)) {
            System.out.println("Invalid or non-unique phone number. Please enter a valid phone number.");
            Scanner scanner = new Scanner(System.in);
            phoneNumber = scanner.nextLine();
        }
        uniquePhoneNumbers.add(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("^\\d+$");
    }

    private static boolean isPhoneNumberUnique(String phoneNumber) {
        return !uniquePhoneNumbers.contains(phoneNumber);
    }
}

class MemberBooksFines {
    private final Member member;
    private final List<BorrowedBook> borrowedBooks;
    private double fines;
    private long firstBookDueDateInSeconds = -1;

    public MemberBooksFines(Member member, double fines) {
        this.member = member;
        this.borrowedBooks = new ArrayList<>();
        this.fines = fines;
    }

    public Member getMember() {
        return member;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public double getFines() {
        return fines;
    }

    public BorrowedBook getBorrowedBook(Book book) {
        for (BorrowedBook borrowedBook : borrowedBooks) {
            if (borrowedBook.getBook().equals(book)) {
                return borrowedBook;
            }
        }
        return null;
    }

    public long getFirstBookDueDateInSeconds() {
        return firstBookDueDateInSeconds;
    }

    public void setFirstBookDueDateInSeconds(long dueDateInSeconds) {
        this.firstBookDueDateInSeconds = dueDateInSeconds;
    }

    public void increasePenaltyAmount(double amount) {
        fines += amount;
    }

}

class Librarian {
    private final List<Book> books;
    private final List<Member> members;
    private final List<MemberBooksFines> memberBooksFinesList;

    public Librarian() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        memberBooksFinesList = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(String bookID) {
        Book bookToRemove = null;
        for (Book book : books) {
            if (book.getBookID().equals(bookID)) {
                bookToRemove = book;
                break;
            }
        }
        if (bookToRemove != null) {
            books.remove(bookToRemove);
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book with ID " + bookID + " not found.");
        }
    }

    public String registerMember(Member member) {
        members.add(member);
        memberBooksFinesList.add(new MemberBooksFines(member, 0.0));
        return member.getMemberID();
    }

    public void removeMember(String memberID) {
        Member memberToRemove = null;
        for (Member member : members) {
            if (member.getMemberID().equals(memberID)) {
                memberToRemove = member;
                break;
            }
        }
        if (memberToRemove != null) {
            members.remove(memberToRemove);
            System.out.println("Member removed successfully.");
        } else {
            System.out.println("Member with ID " + memberID + " not found.");
        }
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<MemberBooksFines> getMemberBooksFinesList() {
        return memberBooksFinesList;
    }

    public MemberBooksFines getMemberBooksFines(Member member) {
        for (MemberBooksFines memberBooksFines : memberBooksFinesList) {
            if (memberBooksFines.getMember().equals(member)) {
                return memberBooksFines;
            }
        }
        return null;
    }

    public void borrowBook(Member member, Book book) {
        MemberBooksFines memberBooksFines = getMemberBooksFines(member);
        if (memberBooksFines != null) {
            if (memberBooksFines.getBorrowedBooks().size() >= 2) {
                System.out.println("You can't borrow more than 2 books at a time.");
                return;
            }

            for (BorrowedBook borrowedBook : memberBooksFines.getBorrowedBooks()) {
                long currentTimeInSeconds = System.currentTimeMillis() / 1000;
                long dueTimeInSeconds = borrowedBook.getDueTimeInSeconds();
                long overdueTimeInSeconds = currentTimeInSeconds - dueTimeInSeconds;

                if (overdueTimeInSeconds > 10 ) {
                    System.out.println("You can't borrow a book if you have overdue books by more than 10 days.");
                    return;
                }
            }

            long dueTimeInSeconds = System.currentTimeMillis() / 1000 + 10;
            BorrowedBook borrowedBook = new BorrowedBook(book, dueTimeInSeconds);
            memberBooksFines.getBorrowedBooks().add(borrowedBook);
            book.decreaseAvailableCopies();
        }
    }

    public float returnBook(Member member, Book book) {
        MemberBooksFines memberBooksFines = getMemberBooksFines(member);
        if (memberBooksFines != null) {
            BorrowedBook borrowedBook = memberBooksFines.getBorrowedBook(book);
            if (borrowedBook != null) {
                long currentTimeInSeconds = System.currentTimeMillis() / 1000;
                long dueTimeInSeconds = borrowedBook.getDueTimeInSeconds();
                long timeLateInSeconds = currentTimeInSeconds - dueTimeInSeconds;
                double lateFineRate = 3.0;

                if (timeLateInSeconds > 0) {
                    double penaltyAmount = timeLateInSeconds * lateFineRate;
                    memberBooksFines.increasePenaltyAmount(penaltyAmount);
                    System.out.println("Fines of " + penaltyAmount + " rupees have been added to your account.");
                } else {
                    System.out.println("Book ID: " + book.getBookID() + " successfully returned.");
                }

                memberBooksFines.getBorrowedBooks().remove(borrowedBook);
                book.increaseAvailableCopies();

                books.add(book);

                return timeLateInSeconds;
            } else {
                System.out.println("You haven't borrowed this book.");
            }
        }
        return -1.0f;
    }
}

class BorrowedBook {
    private final Book book;
    private long dueTimeInSeconds;

    public BorrowedBook(Book book, long dueTimeInSeconds) {
        this.book = book;
        this.dueTimeInSeconds = dueTimeInSeconds;
    }

    public Book getBook() {
        return book;
    }

    public long getDueTimeInSeconds() {
        return dueTimeInSeconds;
    }
}

public class LibrarySystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Librarian librarian = new Librarian();

        while (true) {
            System.out.println("Welcome to the Library Management System!");
            System.out.println("1. Librarian");
            System.out.println("2. Member");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1:
                    // Librarian section
                    librarianMenu(librarian, scanner);
                    break;
                case 2:
                    // Member section
                    memberMenu(librarian, scanner);
                    break;
                case 3:
                    System.out.println("Thanks for visiting!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void librarianMenu(Librarian librarian, Scanner scanner) {
        while (true) {
            System.out.println("Librarian Menu:");
            System.out.println("1. Register Member");
            System.out.println("2. Remove Member");
            System.out.println("3. Add Book");
            System.out.println("4. Remove Book");
            System.out.println("5. View all members along with their books and fines to be paid");
            System.out.println("6. View all books");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();
                    int age = -1;
                    while (age < 0) {
                        System.out.print("Enter member age: ");
                        String ageInput = scanner.nextLine();
                        if (Pattern.matches("\\d+", ageInput)) {
                            age = Integer.parseInt(ageInput);
                        } else {
                            System.out.println("Invalid age format. Please enter a valid age.");
                        }
                    }

                    System.out.print("Enter member phone number: ");
                    String phoneNumber = scanner.nextLine();
                    Member newMember = new Member(name, phoneNumber);
                    newMember.setAge(age);
                    String memberID = librarian.registerMember(newMember);
                    System.out.println("Member registered successfully with MemberID " + memberID + "!");
                    break;

                case 2:
                    System.out.print("Enter member ID to remove: ");
                    String memberIDToRemove = scanner.nextLine();
                    librarian.removeMember(memberIDToRemove);
                    break;
                case 3:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter total copies: ");
                    int totalCopies = scanner.nextInt();
                    scanner.nextLine();
                    for (int i = 0; i < totalCopies; i++) {
                        Book newBook = new Book(title, author, totalCopies);
                        librarian.addBook(newBook);
                    }
                    System.out.println("Book added successfully.");
                    break;
                case 4:
                    System.out.print("Enter book ID to remove: ");
                    String bookIDToRemove = scanner.nextLine();
                    librarian.removeBook(bookIDToRemove);
                    break;
                case 5:
                    System.out.println("List of Members with Books and Fines:");
                    List<Member> members = librarian.getMembers();
                    for (Member member : members) {
                        System.out.println("Member ID: " + member.getMemberID());
                        System.out.println("Name: " + member.getName());

                        MemberBooksFines memberBooksFines = librarian.getMemberBooksFines(member);
                        if (memberBooksFines != null) {
                            double fines = memberBooksFines.getFines();

                            System.out.println("Fines to be paid: " + fines);

                            System.out.println("Borrowed Books:");
                            for (BorrowedBook borrowedBook : memberBooksFines.getBorrowedBooks()) {
                                Book book = borrowedBook.getBook();
                                long dueTimeInSeconds = borrowedBook.getDueTimeInSeconds();
                                long currentTimeInSeconds = System.currentTimeMillis() / 1000;
                                long timeLateInSeconds = currentTimeInSeconds - dueTimeInSeconds;
                                double lateFines = 0.0;

                                if (timeLateInSeconds > 0) {
                                    lateFines = timeLateInSeconds * 3.0;
                                }
                                System.out.println("- Book ID: " + book.getBookID());
                                System.out.println("  Title: " + book.getTitle());
                                System.out.println("  Author: " + book.getAuthor());
                                System.out.println("  Due Date: " + dueTimeInSeconds + " seconds");
                                System.out.println("  Late Fines: " + lateFines);

                                fines += lateFines;
                            }

                            System.out.println("Total Fines: " + fines);
                        } else {
                            System.out.println("Fines to be paid: 0.0");
                            System.out.println("No borrowed books.");
                        }

                        System.out.println("---------------");
                    }
                    break;
                case 6:
                    List<Book> books = librarian.getBooks();
                    System.out.println("List of Available Books:");
                    for (Book book : books) {
                        if (book.getAvailableCopies() > 0) {
                            System.out.println("Book ID: " + book.getBookID());
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("---------------");
                        }
                    }
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void memberMenu(Librarian librarian, Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        Member currentMember = null;
        for (Member member : librarian.getMembers()) {
            if (member.getName().equals(name) && member.getPhoneNumber().equals(phoneNumber)) {
                currentMember = member;
                break;
            }
        }

        if (currentMember == null) {
            System.out.println("Member not found.");
            return;
        }

        MemberBooksFines currentMemberBooksFines = null;
        for (MemberBooksFines memberBooksFines : librarian.getMemberBooksFinesList()) {
            if (memberBooksFines.getMember().equals(currentMember)) {
                currentMemberBooksFines = memberBooksFines;
                break;
            }
        }

        System.out.println("Welcome " + currentMember.getName() + ", MemberID: " + currentMember.getMemberID());

        while (true) {
            System.out.println("Member Menu:");
            System.out.println("1. View available books");
            System.out.println("2. View my books");
            System.out.println("3. Issue a book");
            System.out.println("4. Return a book");
            System.out.println("5. Pay fines");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    List<Book> availableBooks = new ArrayList<>();
                    for (Book book : librarian.getBooks()) {
                        if (book.getAvailableCopies() > 0) {
                            availableBooks.add(book);
                        }
                    }
                    System.out.println("Available Books:");
                    for (Book book : availableBooks) {
                        System.out.println("Book ID: " + book.getBookID());
                        System.out.println("Title: " + book.getTitle());
                        System.out.println("Author: " + book.getAuthor());
                        System.out.println("---------------------");
                    }
                    break;
                case 2:
                    System.out.println("Your Borrowed Books:");
                    for (BorrowedBook borrowedBook : currentMemberBooksFines.getBorrowedBooks()) {
                        Book book = borrowedBook.getBook();
                        System.out.println("Book ID: " + book.getBookID());
                        System.out.println("Title: " + book.getTitle());
                        System.out.println("Author: " + book.getAuthor());
                        System.out.println("---------------------");
                    }
                    break;
                case 3:
                    if (currentMemberBooksFines.getFines() > 0) {
                        System.out.println("You have fines to be paid. Please pay your fines before issuing a book.");
                    } else if (currentMemberBooksFines.getBorrowedBooks().size() >= 2) {
                        System.out.println("You can't borrow more than 2 books at a time.");
                    } else {
                        System.out.print("Enter book ID to issue: ");
                        String issueBookID = scanner.nextLine();
                        System.out.print("Enter book title: ");
                        String issueBookTitle = scanner.nextLine();

                        Book issueBook = null;
                        for (Book book : librarian.getBooks()) {
                            if (book.getBookID().equals(issueBookID) && book.getTitle().equals(issueBookTitle) && book.getAvailableCopies() > 0) {
                                issueBook = book;
                                break;
                            }
                        }

                        if (issueBook != null) {
                            librarian.borrowBook(currentMember, issueBook);

                            for (int i = 0; i < librarian.getBooks().size(); i++) {
                                if (librarian.getBooks().get(i).getBookID().equals(issueBookID)) {
                                    librarian.getBooks().remove(i);
                                    break;
                                }
                            }

                            System.out.println("Book issued successfully.");
                        } else {
                            System.out.println("Book not available for issuing or the details provided are incorrect.");
                        }
                    }
                    break;
                case 4:
                    System.out.println("Your Borrowed Books:");
                    for (BorrowedBook borrowedBook : currentMemberBooksFines.getBorrowedBooks()) {
                        Book book = borrowedBook.getBook();
                        System.out.println("Book ID: " + book.getBookID());
                        System.out.println("Title: " + book.getTitle());
                        System.out.println("Author: " + book.getAuthor());
                        System.out.println("---------------------");
                    }
                    System.out.print("Enter book ID to return: ");
                    String returnBookID = scanner.nextLine();

                    Book returnBook = null;
                    BorrowedBook borrowedBookToRemove = null;
                    for (BorrowedBook borrowedBook : currentMemberBooksFines.getBorrowedBooks()) {
                        if (borrowedBook.getBook().getBookID().equals(returnBookID)) {
                            returnBook = borrowedBook.getBook();
                            borrowedBookToRemove = borrowedBook;
                            break;
                        }
                    }

                    if (returnBook != null) {
                        float fines = librarian.returnBook(currentMember, returnBook);

                        if (fines > 0.0f) {
                            System.out.println("Book ID: " + returnBookID + " successfully returned. " +
                                    (fines*3) + " Rupees has been charged for being " + (fines) + " days late.");
                        } else {
                            System.out.println("Book ID: " + returnBookID + " successfully returned.");
                        }

                        currentMemberBooksFines.getBorrowedBooks().remove(borrowedBookToRemove);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 5:
                    System.out.print("You had a total fine of Rs. " + currentMemberBooksFines.getFines());
                    currentMemberBooksFines.increasePenaltyAmount(-currentMemberBooksFines.getFines());
                    System.out.println(". It has been paid successfully!");
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}