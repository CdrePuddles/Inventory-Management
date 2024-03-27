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
