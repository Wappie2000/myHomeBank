# myHomeBank

An offline tool to get a better insight in your finances. You download a transactionsfile from your bank (ie CSV or text) and import it in the tool. Then you can categorize, edit and make graphs.

Goal of the project is mainly practice and have fun for myself, and secondly offcourse to add the functionalties that I think professional applications lack :) or at least model it to my preferences.

This is version 2 of the project, it is now more or less in a MVC structure, and grahics have improved and the main functionality is there.  

Of you like to try to use it, just downlaod the zip file.


/* BASIC OUTLINE OF THE MODEL
 * 
 * The 2 data sets that determine the state of the model are
 * ArrayList<Transaction> transactions
 * ArrayList<Category> categories
 * Observers (view, controller), get notifications if one of these changes
 * 
 * The functionalities the controller has with regard to the model are
 * 	-load a transactionfile
 * 	-add a category to a transaction
 * 	-load a textfile with categories (as a "startingpoint"), and add them to the list
 * 	-add a new category to the list provided by the view
 * 	-add new keywords or rules to a category, input provided by the view
 * 
 * Functionalities the controller has with regard to the view:
 * 	- Get input to make changes to the model, as the last 2 bullets above
 * 	- Perform a "sql query function" for the view to show specific information 
 * as requested by the human user, i.e. overview of monthly costs from date 1 to 
 * date 2, summed per subcategory.
 *  
 * For handling different kinds of input format from different banks, 
 * the strategy pattern is used: the model is composed with and delegates 
 * the task of input handling to classes that implement the InputHandler interface
 */
