import java.util.ArrayList;


/***
 * The GroupChat class is used to store the messages just like it is in reality.
 * Here, we have the usernames included in the groupchat, and the messages that are sent by them.
 * 
 * @author Angelica Charvensym
 */
public class GroupChat {
	private ArrayList<String> usernames;
	private ArrayList<Message> messages;
	
	
	/***
	 * Constructs a new GroupChat object
	 */
	public GroupChat(){
		usernames = new ArrayList<String>();
		messages = new ArrayList<Message>();
	}
	
	
	/***
	 * Returns the list of usernames joined in the groupchat.
	 * @return List of usernames
	 */
	public ArrayList<String> getUsernames(){
		ArrayList<String> copyUsernames = new ArrayList<String>(usernames);
		return copyUsernames;
	}
	
	
	/***
	 * To add a new user to the groupchat. However, there must be no duplicate usernames 
	 * and it needs to be 1-20 lengthwise. If it pass the requirements, it will be added to the list of usernames
	 * @param username New username to be added
	 */
	public void addUser(String username) {
		if(username == null)
			throw new NullPointerException("Please enter the right username!");
		
		if(usernames.contains(username)) {
			return;
		}else if(username.length() < 1 && username.length() > 20) {
			username = username.substring(0, 20);
		}
		
		usernames.add(username);
	}
	
	
	
	/****
	 * To send a message to the groupchat by stating the person who sent it and the text message.
	 * The person who sends the message must already be part of the groupchat to send the text successfully.
	 * It will then be added to the list of messages of the groupchat.
	 * @param creator A member of the groupchat
	 * @param text A text message
	 */
	public void postMessage(String creator, String text){
		if(creator ==  null || text == null) {
			throw new NullPointerException("Creator and text must be filled!");
		}
		if(usernames.contains(creator) == false) {
			throw new IllegalArgumentException("Creator is not in the group!");
		}else {
			Message m1 = new Message(creator, text);
			messages.add(m1);
		}
	}
	
	
	/***
	 * To give a reaction to the text message
	 * @param id
	 * @param newReaction
	 */
	public void reactToMessage(int id, Message.Reaction newReaction) {
		if(id < 0 || newReaction == null) {
			throw new IllegalArgumentException("ID can't be less than 0 or only available reactions!");
		}
		boolean idFound = false;
		int i = 0;
		for(; i < messages.size(); i++) {
			if(messages.get(i).getID() == id) {
				idFound = true;
				messages.get(i).addReaction(newReaction);
				return;
			}
		}
		if(idFound == false) {
			throw new IllegalArgumentException("Message with that ID not found!");
		}	
	}
	
	/***
	 * Returns an arraylist of messages sent in the groupchat
	 * @return Array of messages
	 */
	public ArrayList<String> getMessages(){
		ArrayList<String> allMessages = new ArrayList<String>();
		for(Message i : messages) {
			allMessages.add(i.toString() + '\n');
		}
		return allMessages;
	}
	
	/***
	 * Returns an arraylist of shorten messages sent in the groupchat
	 * @return Array of summarized messages
	 */
	public ArrayList<String> getShortMessage(){
		ArrayList<String> allMessages = new ArrayList<String>();
		for(Message i : messages) {
			allMessages.add(i.shortString() + '\n');
		}
		return allMessages;
	}
	
	
	/***
	 * Returns an arraylist of relevent messages to the inputed word in the groupchat
	 * 
	 * @param word The word to be searched
	 * @return An array of relevent messages
	 */
	public ArrayList<String> getRelevantMessages(String word){
		if(word == null) {
			throw new NullPointerException("Please enter a reference word!");
		}
		ArrayList<String> allMessages = new ArrayList<String>();
		for(Message i : messages) {
			if(i.relevantTo(word)) {
				allMessages.add(i.toString() + '\n');
			}
		}
		if(allMessages.size() == 0) {
			allMessages.add("There's no relevant messages with " + word);
		}
			
		return allMessages;
	}
	
	
	/***
	 * Posts a reply message to one of the message in the groupchat.
	 * We need to know the id of the message to be able to reply to it.
	 * After making sure the id of the message is valid, the method creates a reply object
	 * and add it to the messages arraylist as a reply.
	 * @param creator User that sends the reply
	 * @param text Reply message
	 * @param id ID of the replying to message
	 */
	public void postReply(String creator, String text, int id) {
		if(id < 0) {
			throw new IllegalArgumentException("ID does not match with any messages.");
		}
		if(creator == null || text == null) {
			throw new NullPointerException("User or text can't be null!");
		}
		boolean idFound = false;
		int i = 0;
		for(; i < messages.size(); i++) {
			if(messages.get(i).getID() == id) {
				idFound = true;
				Reply m = new Reply(creator, text, messages.get(id));
				messages.add(m);
				return;
			}
		}
		
		if(idFound == false) {
			throw new IllegalArgumentException("Message with that ID not found!");
		}
		
	}
	
	/***
	 * Deletes a message known by its user and id.
	 * The inputed user and ID must be found and same with the found user and ID. 
	 * If there's no exception thrown, it will delete the message itself and also deleted from the message arraylist
	 * @param user The user of the message that wants to be deleted
	 * @param id The ID of the message that wants to be deleted
	 */
	public void deleteMessage(String user, int id) {
		if(id < 0) {
			throw new IllegalArgumentException("ID does not match with any messages.");
		}
		if(user == null) {
			throw new NullPointerException("User can't be null!");
		}
		boolean idFound = false;
		int i = 0;
		for(; i < messages.size(); i++) {
			if(messages.get(i).getID() == id) {
				idFound = true;
				Message toBeDeleted = messages.get(i);
				
				if(toBeDeleted.getUsername().equalsIgnoreCase(user) == true) {
					toBeDeleted.deleteMessage();
					messages.remove(i);
					return;
				}else {
					throw new IllegalArgumentException("Username and ID does not match, can't delete message!");
				}
			}
		}
		
		if(idFound == false) {
			throw new IllegalArgumentException("Message with that ID not found!");
		}
	}
	
	public void addUser() {
		addUser("Angela");
		postMessage("Angela", "This is Angela, the worst sister.");
		addUser("Angelica");
		postMessage("Angelica", "Roger, this is Angelica!!");
		addUser("Angelo");
		postMessage("Angelo", "This is Angelo, the cool one.");
	}
	
}
