package com.librarymanagementsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        Library library = new Library();

        List<LibraryItem> libraryItems = LibraryIO.loadItemsFromFile("itemlist.lms");
        for(LibraryItem item:libraryItems){
            library.addItem(item);
        }

        List<User> users = LibraryIO.loadUserListFromFile("userlist.lms");
        for(User user:users){
            library.addUser(user);
        }

        Map<String,String> borrowedItems = LibraryIO.loadBorrowedItemsFromFile("borroweditems.lms");
        for(Map.Entry<String,String> borrowedItem:borrowedItems.entrySet()){
            library.getBorrowedItems().put(borrowedItem.getKey(),borrowedItem.getValue());
        }

        System.out.println("Please find the list of all library items");
        library.getLibraryItems().forEach(item-> System.out.println(item.getTitle()+"\t"+item.getAuthor()+"\t"+item.getSerialNumber()));
        System.out.println("-----------------------------------------");


        System.out.println("Please find the list of all users");
        library.getUserList().forEach(user -> System.out.println(user.getName()));
        System.out.println("-----------------------------------------");

        System.out.println("Please find the list of all the borrowed items from the library");
        library.getBorrowedItems().forEach((item,user)-> System.out.println(item+" : "+user));
        System.out.println("-----------------------------------------");

        boolean exit = false;
        L1:while (!exit) {
            //Main menu options(L1)
            System.out.println("Enter the main option");
            System.out.println("1. Need to create a new item");
            System.out.println("2. Need to create a new User");
            System.out.println("3. User need to borrow an item");
            System.out.println("4. User need to return an item");
            System.out.println("5. Exit the application");

            BufferedReader mainOption = new BufferedReader(new InputStreamReader(System.in));
            int mainOptionStr;
            try {
                mainOptionStr = Integer.parseInt(mainOption.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println(e.getMessage());
                continue;
            }
            // Sub menu options for Main menu option 01(L2)
            if (mainOptionStr == 1) {
                System.out.println("Which item do you need to create ?");
                System.out.println("1. Book");
                System.out.println("2. Magazine");

                BufferedReader createItemType = new BufferedReader(new InputStreamReader(System.in));
                int createItemTypeStr;
                try {
                    createItemTypeStr = Integer.parseInt(createItemType.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //(L3) sub menu option
                if (createItemTypeStr == 1) {
                    System.out.println("Please enter the book name you want to create");
                    String bookNameStr = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    System.out.println("Please enter the book author you want to create");
                    String bookAuthorStr = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    System.out.println("Please enter the book serial number you want to create");
                    String bookSerialNumberStr = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    for (LibraryItem li : library.getLibraryItems()) {
                        if (Objects.equals(li.getSerialNumber(), bookSerialNumberStr)) {
                            System.out.println("This serial number is already entered !");
                            continue L1;
                        }
                    }
                    LibraryItem createBook = new Book(bookNameStr, bookAuthorStr, bookSerialNumberStr);
                    library.addItem(createBook);
                } else if (createItemTypeStr == 2) {
                    System.out.println("Please enter the magazine name you want to create");
                    String magazineNameStr = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    System.out.println("Please enter the magazine author you want to create");
                    String magazineAuthorStr = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    System.out.println("Please enter the magazine serial number you want to create");
                    String magazineSerialNumberStr = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    for (LibraryItem li : library.getLibraryItems()) {
                        if (Objects.equals(li.getSerialNumber(), magazineSerialNumberStr)) {
                            System.out.println("This serial number is already entered !");
                            continue L1;
                        }
                    }
                    LibraryItem createMagazine = new Magazine(magazineNameStr, magazineAuthorStr, magazineSerialNumberStr);
                    library.addItem(createMagazine);
                }

            } else if (mainOptionStr == 2) {
                System.out.println("Please enter the user name you want to create");
                String userNameStr = new BufferedReader(new InputStreamReader(System.in)).readLine();
                for (User usn : library.getUserList()) {
                    if (Objects.equals(usn.getName(), userNameStr)) {
                        System.out.println("This user name is already entered !");
                        continue L1;
                    }

                }
                User createUser = new User(userNameStr);
                library.addUser(createUser);


            }

            else if (mainOptionStr == 3) {
                // Display the list of users
                System.out.println("Available users:");
                if (library.getUserList().isEmpty()) {
                    System.out.println("No users found! Please create users first.");
                    continue;
                }

                for (int i = 0; i < library.getUserList().size(); i++) {
                    System.out.println((i + 1) + ". " + library.getUserList().get(i).getName());
                }

                System.out.println("Enter the number corresponding to the user who wants to borrow an item:");
                int userChoice;
                try {
                    userChoice = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    continue;
                }

                // Validate user selection
                if (userChoice < 1 || userChoice > library.getUserList().size()) {
                    System.out.println("Invalid choice! Please select a valid user.");
                    continue;
                }

                User borrower = library.getUserList().get(userChoice - 1); // Get the selected user

                // Display the list of available items
                System.out.println("Available items in the library:");
                Map<String, LibraryItem> availableItemsMap = new HashMap<>();

                for (LibraryItem item : library.getLibraryItems()) {
                    if (!library.getBorrowedItems().containsKey(item.getSerialNumber())) {
                        availableItemsMap.put(item.getSerialNumber(), item);
                        System.out.println(item.getSerialNumber() + " - " + item.getTitle() + " by " + item.getAuthor());
                    }
                }

                if (availableItemsMap.isEmpty()) {
                    System.out.println("No items are currently available for borrowing.");
                    continue;
                }

                System.out.println("Enter the serial number of the item you want to borrow:");
                String itemSerialNumber;
                try {
                    itemSerialNumber = new BufferedReader(new InputStreamReader(System.in)).readLine();
                } catch (IOException e) {
                    System.out.println("Invalid input! Please try again.");
                    continue;
                }

                // Validate item serial number
                if (!availableItemsMap.containsKey(itemSerialNumber)) {
                    System.out.println("Invalid serial number! Please select a valid item.");
                    continue;
                }

                LibraryItem itemToBorrow = availableItemsMap.get(itemSerialNumber);


                library.getBorrowedItems().put(itemToBorrow.getSerialNumber(), borrower.getName());
                System.out.println("Item '" + itemToBorrow.getTitle() + "' has been successfully borrowed by " + borrower.getName());


                LibraryIO.saveBorrowedItemsToFile(library.getBorrowedItems(), "borroweditems.lms");


                System.out.println("Currently borrowed items:");
                library.getBorrowedItems().forEach((serial, user) ->
                        System.out.println("Serial Number: " + serial + " | Borrowed By: " + user));
            }


            else if (mainOptionStr == 4) {
                // Display the list of users
                System.out.println("Available users:");
                if (library.getUserList().isEmpty()) {
                    System.out.println("No users found! Please create users first.");
                    continue;
                }

                for (int i = 0; i < library.getUserList().size(); i++) {
                    System.out.println((i + 1) + ". " + library.getUserList().get(i).getName());
                }

                System.out.println("Enter the number corresponding to the user who wants to return an item:");
                int userChoice;
                try {
                    userChoice = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    continue;
                }

                // Validate user selection
                if (userChoice < 1 || userChoice > library.getUserList().size()) {
                    System.out.println("Invalid choice! Please select a valid user.");
                    continue;
                }

                User user = library.getUserList().get(userChoice - 1); // Get the selected user

                // Display borrowed items for this user
                System.out.println("Items borrowed by " + user.getName() + ":");
                borrowedItems = library.getBorrowedItems();
                Map<String, LibraryItem> borrowedByUser = new HashMap<>();

                for (String serial : borrowedItems.keySet()) {
                    if (borrowedItems.get(serial).equals(user.getName())) {
                        for (LibraryItem item : library.getLibraryItems()) {
                            if (item.getSerialNumber().equals(serial)) {
                                borrowedByUser.put(serial, item);
                                System.out.println("Serial Number: " + serial + " | Title: " + item.getTitle() + " by " + item.getAuthor());
                            }
                        }
                    }
                }

                if (borrowedByUser.isEmpty()) {
                    System.out.println("No items borrowed by this user.");
                    continue;
                }

                // Ask for the serial number of the item to return
                System.out.println("Enter the serial number of the item to return:");
                String serialNumberToReturn;
                try {
                    serialNumberToReturn = new BufferedReader(new InputStreamReader(System.in)).readLine();
                } catch (IOException e) {
                    System.out.println("Invalid input! Please try again.");
                    continue;
                }

                // Validate serial number
                if (!borrowedByUser.containsKey(serialNumberToReturn)) {
                    System.out.println("Invalid serial number! Please select a valid item.");
                    continue;
                }

                // Return the item
                borrowedItems.remove(serialNumberToReturn);
                System.out.println("Item '" + borrowedByUser.get(serialNumberToReturn).getTitle() + "' has been successfully returned by " + user.getName());

                // Save the updated borrowed items to file
                LibraryIO.saveBorrowedItemsToFile(borrowedItems, "borroweditems.lms");

                // Display the updated borrowed items
                System.out.println("Updated list of borrowed items:");
                borrowedItems.forEach((serial, borrower) ->
                        System.out.println("Serial Number: " + serial + " | Borrowed By: " + borrower)
                );
            }










            else if (mainOptionStr == 5) {
                exit = true;
            }
        }
        LibraryIO.saveItemToFile(library.getLibraryItems(),"itemlist.lms");
        LibraryIO.saveUserListToFile(library.getUserList(),"userlist.lms");
    }
}