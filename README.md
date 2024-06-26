# Inventory Management System
### A system designed to manage the inventory of sole proprietorships and small businesses.

This inventory system was devised out of a need for a cost-effective solution to manage the inventory of small 
businesses.
The inspiration for this project came from my father, who runs his own small home automation business, and presently 
manages his inventory of thousands of items using a rudimentary and often unreliable Microsoft Excel-based solution. 
The items he has in his workshop ranges in sizes and scope from large TVs and speakers, to small, visually identical, 
yet unique pieces.
<br><br>
Many off-the-shelf solutions already exist, however they often have _too much_ functionality.  This leads to not only a 
higher purchasing cost for the small business, but also confusing layers of additional and unnecessary features. 
<br><br>
As a result, it is imperative that this inventory management system maintains the following core competencies:
- Simplistic in its accessibility for the user
- Easily digestible for new users to understand with little training and manual-referencing, and
- Able to scale up or down with however many inventory items a user may desire

## User Stories
1. As a user, I want to be able to maintain a list of unique items (each with a title, quantity, description, and unique identifier) in my inventory collection
2. As a user, I want to be able to add new unique items to, and remove existing items from, my inventory collection  
3. As a user, I want to be able to edit titles and descriptions on each unique item in my inventory collection
4. As a user, I want to be able to adjust the quantity of existing items in my inventory collection upwards or downwards
5. As a user, I want to be able to view and search for items in my inventory collection by id or by title
6. As a user, I want to be able to save my inventory list to file (if I so choose)
7. As a user, I want to be able to load my inventory list from file (if I so choose)
8. As a user, I want to be able to provide a name to my inventory list to outline which specific inventory list it is
9. As a user, I want to be able to save and load numerous inventory lists based upon the names of the respective lists

## Instructions for Grader
- An example list has been created for you, simply click "load" on the splash screen and enter in **The Best List**.  
Case and space/punctuation-insensitive.
  - Alternatively, you may create your own list and name it anything you would like. 
    Case and space/punctuation-insensitive.
- You can generate the first required action related to the user story, "I want to be able to add new unique items to, 
and remove existing items from, my inventory collection" by the following:
  - After you have created or loaded a list, there will be a section to add an item.  Provide the 1) title, 2) quantity,
    and 3) description, then click add, to add a new item to the list.
  - To remove an item, click the "Remove" button in the top-left, and specify the item ID # you wish to remove.
- You can generate the second required action related to the user story, "I want to be able to view and search for items
  in my inventory collection by id or by title" by the following:
  - After you have created or loaded a list, there will be two buttons in the top left: VIEW and SEARCH.
  - **VIEW:** displays the current list of all inventory items
  - **SEARCH:** input a specific Item ID (int) to search for that individual item, or specify part of a title (string) to 
    display any item which contains part of that provided title.
  - In either case, a new table will be generated.
- You can locate the visual component (of an adorable, yet chonky tabby cat) on the splash screen, when you initially run the program.
- You can save the state of my program by the following:
  - After you have created or loaded a list, there will be a button in the top left: SAVE.  Clicking it will prompt you 
    to save and overwrite the existing file.
  - Or, click the X button on the Windows program.  It will prompt you if you want to save before quitting.
- You can load the state of my program by the following:
  - On the splash screen, there will be a button to load an existing list.  A test list has already been made for you, simply
    enter in **The Best List**.  Case and space/punctuation-insensitive.
  - After you have created or loaded a list, you could alternatively load a different list. There will be a button in the top left: LOAD. 
  Clicking it will prompt you to enter in the name of a different list to load it.
- Bonus Feature!
    - On any of the generated and visible tables, you can click on any of the cells (except for ID) to edit the contents!

## Phase 4: Task 2
- Sat Mar 30 17:37:06 PDT 2024
    - Created a new list named Event List
- Sat Mar 30 17:37:06 PDT 2024
    - Successfully saved Event List.
- Sat Mar 30 17:37:06 PDT 2024
    - Viewing list Event List.
- Sat Mar 30 17:37:39 PDT 2024
    - Added item with ID: 1, Title: First Item, Quantity: 111, and Description: This is the first item to be added.
- Sat Mar 30 17:37:39 PDT 2024
    - Viewing list Event List.
- Sat Mar 30 17:39:36 PDT 2024
    - Added item with ID: 2, Title: Second one, Quantity: 222, and Description: Another item added!
- Sat Mar 30 17:39:36 PDT 2024
    - Viewing list Event List.
- Sat Mar 30 17:39:50 PDT 2024
    - Added item with ID: 3, Title: To be Deleted, Quantity: 42069, and Description: This item will not last long!
- Sat Mar 30 17:39:50 PDT 2024
    - Viewing list Event List.
- Sat Mar 30 17:40:56 PDT 2024
    - Added item with ID: 4, Title: This is OK, Quantity: 444, and Description: This item will be kept, though!
- Sat Mar 30 17:40:56 PDT 2024
    - Viewing list Event List.
- Sat Mar 30 17:41:13 PDT 2024
    - Found 1 item(s) containing "Item".
- Sat Mar 30 17:41:19 PDT 2024
    - Found Item with ID: 3, with Title: To be Deleted, Quantity: 42069, and Description: This item will not last long!
- Sat Mar 30 17:41:27 PDT 2024
    - Removed item with ID: 3, Title: To be Deleted, Quantity: 42069, and Description: This item will not last long!
- Sat Mar 30 17:41:28 PDT 2024
    - Viewing list Event List.
- Sat Mar 30 17:42:05 PDT 2024
    - Successfully saved Event List.

## Phase 4: Task 3
Looking back, there are a number of ways I could refactor to improve the readability and design of the code.  For instance, in
the InventoryAppUI, there are numerous instances where I call inventoryList.getName() or inventoryList.getList().  I could instead
declare them at the top of the class.  The reason is, calling methods of inventoryList happens in many methods in the InventoryAppUI.
They could have been named something like listName or itemList, respectively.  In the odd chance I change one of the method names
(perhaps from .getList() to .getItems()), the adjustment would only need to be made to the variable itemList at the top, instead of
looking around through each method for instances where .getItems() is used.  This would reduce coupling.

Another area I could improve upon is improving Single Responsibility among numerous methods.  Looking at welcomeScreen() in the 
 InventoryAppUI, it is quite a mess.  It has: 1) a try-catch for loading the splash image, 2) a JInternalFrame for holding all the contents, 3) 
a JPanel for containing the buttons, 4) adding the JPanel to the JInternalFrame, and 5) adding the JInternalFrame to the program window.  
These could instead be split apart into 5 separate methods.  Thinking about it more, 
creating a separate method to add JInternalFrame to the program window can be used in many other areas (the button panel, addItemArea panel, etc.).
Same applies for the JPanel - I could create a separate method which adds JPanels to a JInternalFrame.  I sort of approached this concept
for itemInputFields(), but it was only designed as a one-time-use for only the item input fields.

