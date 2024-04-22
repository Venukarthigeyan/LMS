import java.awt.*;
import java.awt.event.*;
import java.util.*;
class Book {
    private String title;
    private int id;
    private String author;
    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Author: " + author;
    }
}
class Member {
    private int memberId;
    private String memberName;
    private ArrayList<Book> borrowedBooks;
    public Member(int memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.borrowedBooks = new ArrayList<>();
    }
    public int getMemberId() {
        return memberId;
    }
    public String getMemberName() {
        return memberName;
    }
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }
    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }
    @Override
    public String toString() {
        return "ID: " + memberId + ", Name: " + memberName;
    }
}
class Stack<T> {
    private Deque<T> stack = new LinkedList<>();
    public void push(T element) {
        stack.push(element);
    }
    public T pop() {
        return stack.pop();
    }
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    public ArrayList<T> getAllElements() {
        return new ArrayList<>(stack);
    }
}
class Queue<T> {
    private LinkedList<T> queue = new LinkedList<>();
    public void enqueue(T element) {
        queue.add(element);
    }
    public T dequeue() {
        return queue.poll();
    }
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    public ArrayList<T> getAllElements() {
        return new ArrayList<>(queue);
    }
}
class Library {
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private HashMap<Integer, Integer> borrowRecords;
    private Stack<Integer> addedBookIdsStack;
    private Stack<Integer> addedMemberIdsStack;
    private Queue<Integer> removedBookIdsQueue;
    private Queue<Integer> removedMemberIdsQueue;
    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        borrowRecords = new HashMap<>();
        addedBookIdsStack = new Stack<>();
        addedMemberIdsStack = new Stack<>();
        removedBookIdsQueue = new Queue<>();
        removedMemberIdsQueue = new Queue<>();
    }
    public void addBook(Book book) {
        for (Book b : books) {
            if (b.getId() == book.getId()) {
                throw new IllegalArgumentException("Book ID already exists.");
            }
        }
        books.add(book);
        addedBookIdsStack.push(book.getId());
    }
    public void addMember(Member member) {
        for (Member m : members) {
            if (m.getMemberId() == member.getMemberId()) {
                throw new IllegalArgumentException("Member ID already exists.");
            }
        }
        members.add(member);
        addedMemberIdsStack.push(member.getMemberId());
    }
    public boolean deleteBookById(int id) {
        Iterator<Book> it = books.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            if (book.getId() == id) {
                it.remove();
                removedBookIdsQueue.enqueue(id);
                return true;
            }
        }
        return false;
    }
    public boolean removeMemberById(int id) {
        Iterator<Member> it = members.iterator();
        while (it.hasNext()) {
            Member member = it.next();
            if (member.getMemberId() == id) {
                it.remove();
                removedMemberIdsQueue.enqueue(id);
                return true;
            }
        }
        return false;
    }
    public Book findBookById(int id) {
        return binarySearchBook(id);
    }
    public Member findMemberById(int id) {
        return binarySearchMember(id);
    }
    public boolean borrowBook(int memberId, int bookId) {
        Member member = findMemberById(memberId);
        Book book = findBookById(bookId);
        if (member != null && book != null && !borrowRecords.containsKey(bookId)) {
            member.borrowBook(book);
            borrowRecords.put(bookId, memberId);
            return true;
        }
        return false;
    }
    public boolean returnBook(int memberId, int bookId) {
        Member member = findMemberById(memberId);
        Book book = findBookById(bookId);
        if (member != null && book != null && borrowRecords.containsKey(bookId) && borrowRecords.get(bookId) == memberId) {
            member.returnBook(book);
            borrowRecords.remove(bookId);
            return true;
        }
        return false;
    }
    public void sortBooksById() {
        quickSortBooks(0, books.size() - 1);
    }
    public void sortMembersById() {
        quickSortMembers(0, members.size() - 1);
    }
    private void quickSortBooks(int low, int high) {
        if (low < high) {
            int pi = partitionBooks(low, high);
            quickSortBooks(low, pi - 1);
            quickSortBooks(pi + 1, high);
        }
    }
    private int partitionBooks(int low, int high) {
        Book pivot = books.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (books.get(j).getId() <= pivot.getId()) {
                i++;
                Collections.swap(books, i, j);
            }
        }
        Collections.swap(books, i + 1, high);
        return i + 1;
    }
    private void quickSortMembers(int low, int high) {
        if (low < high) {
            int pi = partitionMembers(low, high);
            quickSortMembers(low, pi - 1);
            quickSortMembers(pi + 1, high);
        }
    }
    private int partitionMembers(int low, int high) {
        Member pivot = members.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (members.get(j).getMemberId() <= pivot.getMemberId()) {
                i++;
                Collections.swap(members, i, j);
            }
        }
        Collections.swap(members, i + 1, high);
        return i + 1;
    }
    private Book binarySearchBook(int id) {
        int left = 0;
        int right = books.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (books.get(mid).getId() == id) {
                return books.get(mid);
            } else if (books.get(mid).getId() < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    private Member binarySearchMember(int id) {
        int left = 0;
        int right = members.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (members.get(mid).getMemberId() == id) {
                return members.get(mid);
            } else if (members.get(mid).getMemberId() < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    public ArrayList<Book> getBooks() {
        return books;
    }
    public ArrayList<Member> getMembers() {
        return members;
    }
    public String displayAddedBookIds() {
        return "Added Book IDs: " + addedBookIdsStack.getAllElements().toString() + "\n";
    }
    public String displayAddedMemberIds() {
        return "Added Member IDs: " + addedMemberIdsStack.getAllElements().toString() + "\n";
    }
    public String displayRemovedBookIds() {
        return "Removed Book IDs: " + removedBookIdsQueue.getAllElements().toString() + "\n";
    }
    public String displayRemovedMemberIds() {
        return "Removed Member IDs: " + removedMemberIdsQueue.getAllElements().toString() + "\n";
    }
   
    public String displaySortedBooks() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sorted Books List:\n");
        for (Book book : books) {
            sb.append(book.toString()).append("\n");
        }
        return sb.toString();
    }
    public String displaySortedMembers() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sorted Members List:\n");
        for (Member member : members) {
            sb.append(member.toString()).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<Map.Entry<Integer, Integer>> sortBorrowRecords() {
      
        ArrayList<Map.Entry<Integer, Integer>> sortedBorrowRecords = new ArrayList<>(borrowRecords.entrySet());
      
        sortedBorrowRecords.sort(Map.Entry.comparingByKey());
        return sortedBorrowRecords;
    }
    public String displaySortedBorrowRecords() {
        ArrayList<Map.Entry<Integer, Integer>> sortedBorrowRecords = sortBorrowRecords();
        StringBuilder sb = new StringBuilder();
        sb.append("Sorted Borrow Records (Book ID -> Member ID):\n");
        for (Map.Entry<Integer, Integer> entry : sortedBorrowRecords) {
            sb.append("Book ID: ").append(entry.getKey())
              .append(", Member ID: ").append(entry.getValue())
              .append("\n");
        }
        return sb.toString();
    }
    public ArrayList<Map.Entry<Integer, Integer>> sortReturnRecords() {
        ArrayList<Map.Entry<Integer, Integer>> returnRecords = new ArrayList<>(borrowRecords.entrySet());
 
        for (Map.Entry<Integer, Integer> entry : borrowRecords.entrySet()) {
            returnRecords.add(new AbstractMap.SimpleEntry<>(entry.getValue(), entry.getKey()));
        }
        returnRecords.sort(Map.Entry.comparingByKey());
        return returnRecords;
    }
    public String displaySortedReturnRecords() {
        ArrayList<Map.Entry<Integer, Integer>> sortedReturnRecords = sortReturnRecords();
        StringBuilder sb = new StringBuilder();
        sb.append("Sorted Return Records (Member ID ===-> Book ID):\n");
        for (Map.Entry<Integer, Integer> entry : sortedReturnRecords) {
            sb.append("Member ID: ").append(entry.getKey())
              .append(", Book ID: ").append(entry.getValue())
              .append("\n");
        }
        return sb.toString();
    }
}
public class LibraryManagementSystemAWT extends Frame implements ActionListener {
    private Library library;
    private TextField searchBookField;
    private TextField searchMemberField;
    private TextField idField;
    private TextField titleField;
    private TextField authorField;
    private TextField deleteField;
    private TextField memberIdField;
    private TextField memberNameField;
    private TextField borrowMemberIdField;
    private TextField borrowBookIdField;
    private TextField returnMemberIdField;
    private TextField returnBookIdField;
    private TextField removeMemberField;
    private TextArea displayArea;
    public LibraryManagementSystemAWT() {
        super("Library Management System");
        library = new Library();
       
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        setSize(1000, 700);       
       
        gbc.gridx = 0;
        gbc.gridy = 0;
       
        add(new Label("Search Book by ID:"), gbc);
        gbc.gridx++;
        searchBookField = new TextField(10);
        add(searchBookField, gbc);
        gbc.gridx++;
        Button searchBookButton = new Button("Search Book");
        add(searchBookButton, gbc);
     
        gbc.gridx = 0;
        gbc.gridy++;
      
        add(new Label("Search Member by ID:"), gbc);
        gbc.gridx++;
        searchMemberField = new TextField(10);
        add(searchMemberField, gbc);
        gbc.gridx++;
        Button searchMemberButton = new Button("Search Member");
        add(searchMemberButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
     
        add(new Label("Book ID:"), gbc);
        gbc.gridx++;
        idField = new TextField(10);
        add(idField, gbc);
        gbc.gridx++;
        add(new Label("Title:"), gbc);
        gbc.gridx++;
        titleField = new TextField(10);
        add(titleField, gbc);
        gbc.gridx++;
        add(new Label("Author:"), gbc);
        gbc.gridx++;
        authorField = new TextField(10);
        add(authorField, gbc);
        gbc.gridx++;
        Button addBookButton = new Button("Add Book");
        add(addBookButton, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
       
        add(new Label("Delete Book by ID:"), gbc);
        gbc.gridx++;
        deleteField = new TextField(10);
        add(deleteField, gbc);
        gbc.gridx++;
        Button deleteButton = new Button("Delete Book");
        add(deleteButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
       
        add(new Label("Member ID:"), gbc);
        gbc.gridx++;
        memberIdField = new TextField(10);
        add(memberIdField, gbc);
        gbc.gridx++;
        add(new Label("Member Name:"), gbc);
        gbc.gridx++;
        memberNameField = new TextField(10);
        add(memberNameField, gbc);
        gbc.gridx++;
        Button addMemberButton = new Button("Add Member");
        add(addMemberButton, gbc);
       
        gbc.gridx = 0;
        gbc.gridy++;
   
        add(new Label("Remove Member by ID:"), gbc);
        gbc.gridx++;
        removeMemberField = new TextField(10);
        add(removeMemberField, gbc);
        gbc.gridx++;
        Button removeMemberButton = new Button("Remove Member");
        add(removeMemberButton, gbc);
   
        gbc.gridx = 0;
        gbc.gridy++;
       
        add(new Label("Borrow Member ID:"), gbc);
        gbc.gridx++;
        borrowMemberIdField = new TextField(10);
        add(borrowMemberIdField, gbc);
        gbc.gridx++;
        add(new Label("Borrow Book ID:"), gbc);
        gbc.gridx++;
        borrowBookIdField = new TextField(10);
        add(borrowBookIdField, gbc);
        gbc.gridx++;
        Button borrowBookButton = new Button("Borrow Book");
        add(borrowBookButton, gbc);
       
        gbc.gridx = 0;
        gbc.gridy++;
     
        add(new Label("Return Member ID:"), gbc);
        gbc.gridx++;
        returnMemberIdField = new TextField(10);
        add(returnMemberIdField, gbc);
        gbc.gridx++;
        add(new Label("Return Book ID:"), gbc);
        gbc.gridx++;
        returnBookIdField = new TextField(10);
        add(returnBookIdField, gbc);
        gbc.gridx++;
        Button returnBookButton = new Button("Return Book");
        add(returnBookButton, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        
        Button displayAddedBookIdsButton = new Button("Display Added Book IDs");
        Button displayAddedMemberIdsButton = new Button("Display Added Member IDs");
        Button displayRemovedBookIdsButton = new Button("Display Removed Book IDs");
        Button displayRemovedMemberIdsButton = new Button("Display Removed Member IDs");
        add(displayAddedBookIdsButton, gbc);
        gbc.gridx++;
        add(displayAddedMemberIdsButton, gbc);
        gbc.gridx++;
        add(displayRemovedBookIdsButton, gbc);
        gbc.gridx++;
        add(displayRemovedMemberIdsButton, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
       
        Button sortBooksByIdButton = new Button("Sort Books by ID");
        Button sortMembersByIdButton = new Button("Sort Members by ID");
        Button sortBorrowRecordsButton = new Button("Sort Borrow Records");
        Button sortReturnRecordsButton = new Button("Sort Return Records");
        add(sortBooksByIdButton, gbc);
        gbc.gridx++;
        add(sortMembersByIdButton, gbc);
        gbc.gridx++;
        add(sortBorrowRecordsButton, gbc);
        gbc.gridx++;
        add(sortReturnRecordsButton, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        Button displaySortedBooksButton = new Button("Display Sorted Books");
        Button displaySortedMembersButton = new Button("Display Sorted Members");
        Button displayBorrowRecordsButton = new Button("Display Sorted Borrow Records");
        Button displayReturnRecordsButton = new Button("Display Sorted Return Records");
        add(displaySortedBooksButton, gbc);
        gbc.gridx++;
        add(displaySortedMembersButton, gbc);
        gbc.gridx++;
        add(displayBorrowRecordsButton, gbc);
        gbc.gridx++;
        add(displayReturnRecordsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
     
        displayArea = new TextArea(10, 50);
        gbc.gridwidth = 7;
        add(displayArea, gbc);
    
        searchBookButton.addActionListener(this);
        searchMemberButton.addActionListener(this);
        addBookButton.addActionListener(this);
        deleteButton.addActionListener(this);
        addMemberButton.addActionListener(this);
        borrowBookButton.addActionListener(this);
        returnBookButton.addActionListener(this);
        removeMemberButton.addActionListener(this);
        displayAddedBookIdsButton.addActionListener(this);
        displayAddedMemberIdsButton.addActionListener(this);
        displayRemovedBookIdsButton.addActionListener(this);
        displayRemovedMemberIdsButton.addActionListener(this);
        sortBooksByIdButton.addActionListener(this);
        sortMembersByIdButton.addActionListener(this);
        displaySortedBooksButton.addActionListener(this);
        displaySortedMembersButton.addActionListener(this);
        sortBorrowRecordsButton.addActionListener(this);
        sortReturnRecordsButton.addActionListener(this);
        displayBorrowRecordsButton.addActionListener(this);
        displayReturnRecordsButton.addActionListener(this);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Search Book")) {
            int id = Integer.parseInt(searchBookField.getText());
            Book book = library.findBookById(id);
            if (book != null) {
                displayArea.append("Book found: ID = " + book.getId() + ", Title = " + book.getTitle() + ", Author = " + book.getAuthor() + "\n");
            } else {
                displayArea.append("Book not found.\n");
            }
        } else if (command.equals("Search Member")) {
            int id = Integer.parseInt(searchMemberField.getText());
            Member member = library.findMemberById(id);
            if (member != null) {
                displayArea.append("Member found: ID = " + member.getMemberId() + ", Name = " + member.getMemberName() + "\n");
            } else {
                displayArea.append("Member not found.\n");
            }
        } else if (command.equals("Add Book")) {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String author = authorField.getText();
            Book book = new Book(id, title, author);
            library.addBook(book);
            displayArea.append("Book added: ID = " + id + ", Title = " + title + ", Author = " + author + "\n");
        } else if (command.equals("Delete Book")) {
            int id = Integer.parseInt(deleteField.getText());
            if (library.deleteBookById(id)) {
                displayArea.append("Book with ID " + id + " deleted.\n");
            } else {
                displayArea.append("Book with ID " + id + " not found.\n");
            }
        } else if (command.equals("Add Member")) {
            int memberId = Integer.parseInt(memberIdField.getText());
            String memberName = memberNameField.getText();
            Member member = new Member(memberId, memberName);
            library.addMember(member);
            displayArea.append("Member added: ID = " + memberId + ", Name = " + memberName + "\n");
        } else if (command.equals("Borrow Book")) {
            int memberId = Integer.parseInt(borrowMemberIdField.getText());
            int bookId = Integer.parseInt(borrowBookIdField.getText());
            if (library.borrowBook(memberId, bookId)) {
                displayArea.append("Book with ID " + bookId + " borrowed by Member with ID " + memberId + "\n");
            } else {
                displayArea.append("Borrow operation failed.\n");
            }
        } else if (command.equals("Return Book")) {
            int memberId = Integer.parseInt(returnMemberIdField.getText());
            int bookId = Integer.parseInt(returnBookIdField.getText());
            if (library.returnBook(memberId, bookId)) {
                displayArea.append("Book with ID " + bookId + " returned by Member with ID " + memberId + "\n");
            } else {
                displayArea.append("Return operation failed.\n");
            }
        } else if (command.equals("Remove Member")) {
            int memberId = Integer.parseInt(removeMemberField.getText());
            if (library.removeMemberById(memberId)) {
                displayArea.append("Member with ID " + memberId + " removed.\n");
            } else {
                displayArea.append("Member with ID " + memberId + " not found.\n");
            }
        } else if (command.equals("Display Added Book IDs")) {
            displayArea.append(library.displayAddedBookIds());
        } else if (command.equals("Display Added Member IDs")) {
            displayArea.append(library.displayAddedMemberIds());
        } else if (command.equals("Display Removed Book IDs")) {
            displayArea.append(library.displayRemovedBookIds());
        } else if (command.equals("Display Removed Member IDs")) {
            displayArea.append(library.displayRemovedMemberIds());
        } else if (command.equals("Sort Books by ID")) {
            library.sortBooksById();
            displayArea.append("Books sorted by ID.\n");
        } else if (command.equals("Sort Members by ID")) {
            library.sortMembersById();
            displayArea.append("Members sorted by ID.\n");
        } else if (command.equals("Display Sorted Books")) {
            displayArea.append(library.displaySortedBooks());
        } else if (command.equals("Display Sorted Members")) {
            displayArea.append(library.displaySortedMembers());
        }
    String ActionCommand = e.getActionCommand();
    if (command.equals("Sort Borrow Records")) {
        library.sortBorrowRecords();
        displayArea.append("Borrow records sorted by Book ID.\n");
    } else if (command.equals("Display Sorted Borrow Records")) {
        displayArea.append(library.displaySortedBorrowRecords());
    } else if (command.equals("Sort Return Records")) {
        library.sortReturnRecords();
        displayArea.append("Return records sorted by Member ID.\n");
    } else if (command.equals("Display Sorted Return Records")) {
        displayArea.append(library.displaySortedReturnRecords());
    }

    }
    public static void main(String[] args) {
        new LibraryManagementSystemAWT();
    }
}