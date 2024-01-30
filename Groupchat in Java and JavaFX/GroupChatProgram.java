/***
 * GroupChatProgram is a class created to tests the groupchat class, reply class and message class.
 * Here, we created some methods that helps with the group chat usability. 
 * Most of them are only calling the method from other classes with extra prompt to make a better program.
 * 
 * @author Angelica Charvensym
 */

import java.util.Scanner;
public class GroupChatProgram {
	Scanner scan;
	GroupChat gc;
	String activeUser;
	
	
	/**
	 * The main method that creates the groupchat program and calls for execution.
	 * @param args
	 */
	public static void main(String[] args) {
		GroupChatProgram prog = new GroupChatProgram();
		prog.execute();
	}
	
	
	/***
	 * The constructor of the GroupChatProgram. 
	 * Creates a scanner to input from the user, creates a groupchat and 
	 */
	public GroupChatProgram() {
		scan = new Scanner(System.in);
		gc = new GroupChat();
		addUser();
		activeUser = "";
	}
	
	/***
	 * Adding some user and post messages to the groupchat
	 * 
	 */
	public void addUser() {
		gc.addUser("Angela");
		gc.postMessage("Angela", "This is Angela, the worst sister.");
		gc.addUser("Angelica");
		gc.postMessage("Angelica", "Roger, this is Angelica!!");
		gc.addUser("Angelo");
		gc.postMessage("Angelo", "This is Angelo, the cool one.");
	}
	
	
	/***
	 * Execution for the groupchat program. 
	 * Starts with making the user choose which user they are in the groupchat
	 * Then, chooses the options from the menu, representing what activity the user would like to do
	 * If the user is out of bound, it will keep asking for the right number
	 */
	public void execute() {
		
		chooseActiveUser();
		
		int choice = 1;
		
		while(choice != 8) {
			menu();
			choice = scan.nextInt();
			while(choice <= 0 || choice > 8) {
				System.out.println("Please choose between 1-8 (inclusive): ");
				choice = scan.nextInt();
			}
			process(choice);
		}
	}

	/***
	 * Makes the user chooses their user in the group chat by picking the number each user represents
	 * Then, changing the activeUser data member to the choosen user.
	 */
	public void chooseActiveUser() {
		System.out.println("Choose your user: ");
		for(int i = 0; i < gc.getUsernames().size(); i++) {
			System.out.println(i+1 + "" + gc.getUsernames().get(i));
		}
		int active = scan.nextInt();
		activeUser = gc.getUsernames().get(active-1);
		System.out.println("Welcome to the groupchat, " + activeUser);
	}
	
	/***
	 * Prints out the option the user needs to choose.
	 */
	public void menu() {
		String menu = "Choose between these choices:\n";
		menu += "1: See All Messages\n";
		menu += "2: See My Messages\n";
		menu += "3: Post Message\n";
		menu += "4: Reply To Message\n";
		menu += "5: React To Message\n";
		menu += "6: Delete Message\n";
		menu += "7: Switch Active User\n";
		menu += "8: Exit";
		System.out.println(menu);
	}

	
	/***
	 * Does different things according to choice from the menu chosen by user 
	 * Option 1: Prints out all of the message in the groupchat with its ID, username and reactions received
	 * Option 2: Prints out messages related to the activeUser
	 * Option 3: To post a message to the groupchat
	 * 			The user types in message they would like to post, 
	 * 			then it is posted by calling the postMessage method from groupchat class
	 * 			Prints a success prompt
	 * Option 4: To reply to a message in the groupchat
	 * 			The user types in the ID of the message they would like to reply to, 
	 * 			The user types in message they would like to reply, 
	 * 			then it is posted by calling the postMessage method from groupchat class
	 * 			Prints a success prompt
	 * Option 5: To react to a message using reactions available
	 * 			The user types in the ID of the message they would like to reply to, 
	 * 			Program prints out reactions available and user picks one from it
	 * 			Prints a success prompt
	 * Option 6: To delete a message 
	 * 			Program needs to make sure the activeUser is the one who sent the message to be deleted
	 * 			Prints a success prompt
	 * Option 7: To Change the active user
	 * 			User picks their user in the groupchat from the available user in the groupchat
	 * Option 8:  Exiting the program
	 * @param choice Options from the menu that the user picks
	 */
	public void process(int choice) {
		switch(choice) {
		case 1: //see all messages
			System.out.println("========Group Chat========\n");
			for(int i = 0; i < gc.getMessages().size(); i++) {
				System.out.println(gc.getMessages().get(i));
			}
			break;
		case 2: //see my messages
			System.out.println("Relevant Messages to " + activeUser + "\n");
			for(int i = 0; i < gc.getRelevantMessages(activeUser).size(); i++) {
				System.out.println(gc.getRelevantMessages(activeUser).get(i));
			}
			break;
		case 3: //post message
			System.out.println("Write your message: ");
			String m = scan.nextLine();
			String s = "sample";
			while(!s.equals("")) {
				s = scan.nextLine();
				m += s;
			}
			gc.postMessage(activeUser, m);
			System.out.println("Message successfully posted!");
			break;
		case 4: //reply message
			int chosenID = gettingID();
			System.out.println("Write your reply: ");
			String m1 = scan.next();
			String s1 = "sample";
			while(!s1.equals("")) {
				s1 = scan.nextLine();
				m1 += s1;
			}
			gc.postReply(activeUser,m1,chosenID);
			System.out.println("Reply successfully posted!");
			break;
		case 5: //react message
			chosenID = gettingID();
			System.out.println("Choose your reactions: ");
			listOfReactions();
			int chosenReaction = scan.nextInt();
			gc.reactToMessage(chosenID, Message.Reaction.values()[chosenReaction-1]);
			System.out.println("Reaction successfully posted!");
			break;
		case 6: //delete message
			chosenID = gettingID();
			gc.deleteMessage(activeUser, chosenID);
			System.out.println("Message successfully deleted!");
			break;
		
		case 7: //switch active user
			chooseActiveUser();
			break;
		
		case 8: //exit
			System.out.println("You left the group chat.");
			System.exit(0);
			break;
		}
	}
	
	
	/***
	 * Prints out the reactions available to react to a message
	 */
	public void listOfReactions() {
		int count = 1;
		for(Message.Reaction i : Message.Reaction.values()) {
			System.out.println(count++ + ": " + i);
		}
	}
	
	/***
	 * Used to get the ID of a message the user wants to reply or react to message
	 * @return ID of a message
	 */
	public int gettingID() {
		System.out.println("Enter the id of the message you would like to reply/react: ");
		int theID = scan.nextInt();
		scan.nextLine();
		return theID;
	}
}
