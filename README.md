# Library Management System
## Introduction:
The Library Management System is a Java-based application designed to manage books, members, and book borrowing in a library. It allows librarians to add and remove books, register and remove members, and view information about members, books, and fines. Members can use the system to view available books, borrow and return books, and pay fines.

### Welcome to the Library Management system:
When you run the application, you will be presented with a menu:
- Enter 1 for the Librarian section (to manage members and books).
- Enter 2 for the Member section (to borrow, return books, and pay fines).
- Enter 3 to exit the application.

### Librarian Section:
1. **Register Member:** Librarians can register a new library member by providing their name, age, and phone number. The system ensures phone numbers are unique and valid.
2. **Remove Member:** Librarians can remove a member by providing their member ID.
3. **Add Book:** Librarians can add books by entering the title, author, and the total number of copies.
4. **Remove Book:** Librarians can remove books by providing the book ID.
5. **View all members along with their books and fines to be paid:** Displays a list of all members, their borrowed books, and fines.
6. **View all books:** Displays a list of all available books.

### Member Section:
1. **View available books:** Members can see a list of available books in the library.
2. **View my books:** Members can view the books they have borrowed.
3. **Issue a book:** Members can borrow a book by entering the book ID and title. The system checks for fines and limits on the number of books a member can borrow.
4. **Return a book:** Members can return a book by entering the book ID. Fines are calculated for late returns.
5. **Pay fines:** Members can pay their fines.

<hr>

## Class Structures:
### Book Class
- The Book class represents a book in the library. It contains information about the book's title, author, unique book ID, total copies, and available copies. This class is responsible for tracking the book's availability and managing copies.

#### Attributes:

- bookID: A unique identifier for each book.
- title: The title of the book.
- author: The author of the book.
- totalCopies: The total number of copies of the book.
- availableCopies: The number of copies currently available in the library.

#### Methods:

- decreaseAvailableCopies(): Decreases the count of available copies when a book is borrowed.
- increaseAvailableCopies(): Increases the count of available copies when a book is returned.

### Member Class
- The Member class represents a library member. It stores information about the member, including their name, unique member ID, phone number, and penalty amount for fines. This class also ensures that phone numbers are unique among members.

#### Attributes:

- memberID: A unique identifier for each member.
- name: The name of the member.
- phoneNumber: The phone number of the member.
- penaltyAmount: The amount of fines that the member needs to pay.

#### Methods:

- setPhoneNumber(String phoneNumber): Sets the phone number for the member while ensuring it is unique and valid.

### MemberBooksFines Class
- The MemberBooksFines class is responsible for tracking a member's borrowed books and fines. It contains information about the member, a list of borrowed books, and the total fines incurred by the member.

#### Attributes:

- member: The member associated with this object.
- borrowedBooks: A list of BorrowedBook objects representing books borrowed by the member.
- fines: The total fines accrued by the member.
- firstBookDueDateInSeconds: The due date (in seconds since epoch) of the member's first borrowed book. Used to enforce the rule that a member cannot borrow a second book if the first is overdue by more than 10 days.

#### Methods:

- getBorrowedBook(Book book): Retrieves a BorrowedBook object for a specific book borrowed by the member.
- increasePenaltyAmount(double amount): Increases the member's fines by a specified amount.

### Librarian Class
- The Librarian class serves as the main controller for library operations. It manages books, members, and the borrowing/returning of books. It also provides methods for adding, removing, and retrieving information about books, members, and fines.

#### Attributes:

- books: A list of Book objects representing books in the library.
- members: A list of Member objects representing registered members.
- memberBooksFinesList: A list of MemberBooksFines objects to track fines and borrowed books for members.

#### Methods:

- Various methods for adding, removing, and retrieving books and members.
- Methods for borrowing and returning books, including enforcing borrowing limits and calculating fines.

### BorrowedBook Class
- The BorrowedBook class represents a book that has been borrowed by a member. It contains information about the book and the due date for its return.

#### Attributes:

- book: The book object representing the borrowed book.
- dueTimeInSeconds: The due date (in seconds since epoch) for returning the book.

#### Methods:

- None, as this class primarily serves as a data container.

<hr>

## Data Structures:
The Library Management System uses various data structures to manage data efficiently.

1. **Lists (ArrayLists):**
    - Used to store collections of members, books, and member-fine information.
    - Members, books, and their details are stored in separate ArrayLists for easy access and management.

2. **HashSet:**
    - Used to store unique phone numbers of members to ensure uniqueness during member registration.

## Exception Handling:
The Library Management System has implemented exception handling to ensure smooth operation and data integrity.
- **Input Validation:** The system validates phone numbers, ages, book availability, and fines calculation to prevent incorrect data entry.
- **Invalid Choices:** The system handles invalid menu choices by displaying a message and prompting the user to enter a valid option.

<hr>

## Functionality Summary:

- Librarians can manage members and books, including registration and removal.
- Librarians can view a list of members with their borrowed books and fines.
- Librarians can add and remove books from the library.
- Members can view available books and books they have borrowed.
- Members can borrow and return books while handling fines for late returns.
- Members can pay their fines.

## Important Notes:

- Librarians should ensure that they have unique phone numbers for each member.
- Librarians should add books to the system before members can borrow them.
- Members can't borrow more than two books at a time.
- Members can't borrow books if they have overdue books by more than 10 days.
- Fines are calculated based on late returns and charged at a rate of 3 Rupees per day.

This Library Management System is designed to efficiently manage library operations and provide a user-friendly interface for both librarians and members. Enjoy using the system for your library needs!