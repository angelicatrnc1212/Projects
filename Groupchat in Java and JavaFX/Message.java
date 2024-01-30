import java.util.ArrayList;

/***
 * The Message class is used to store information regarding each text. 
 * It includes the text itself, the username, the id and reactions following.
 * 
 * @author Angelica Charvensym
 */
public class Message {
	public enum Reaction{Good, Bad, Okay};
	
	private String text;
	private String username;
	private int id;
	private ArrayList<Reaction> reactions;
	private static int count = 0;
	
	
	/****
	 * The constructor of Message object.
	 * @param text Text sent 
	 * @param name The username whom send the text
	 * 
	 */
	Message(String name, String text){
		if(text == null || name == null) {
			throw new NullPointerException("Text and username must be filled!");
		}
		this.text = text;
		this.username = name;
		id = count;
		count++;
		reactions = new ArrayList<Reaction>();
	}
	
	/***
	 * Returns the text sent
	 * @return The text message
	 */
	public String getText() {
		return this.text;
	}
	
	
	/**
	 * Returns the username whom send the text message
	 * @return The username
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Returns the unique id of the message
	 * @return ID of the message
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Returns the reactions of the message
	 * @return Reactions received
	 */
	public ArrayList<Reaction> getReactions() {
		ArrayList<Reaction> copyReactions = new ArrayList<Reaction>(reactions);
		return copyReactions;
	}
	
	
	/**
	 * To add an inputed reaction to the message
	 * @param r1 Reaction to be added to the text message
	 */
	public void addReaction(Reaction r1){
		if(r1 == null ) {
			throw new IllegalArgumentException("Only enter reactions available!");
		}
		reactions.add(r1);
	}
	
	/**
	 * Returns all the reaction received from a text message
	 * @return Reactions of the message
	 */
	public String getReactionSummary() {
		if(reactions.size() == 0) {
			return "";
		}else {
			String summary="";
			for(Reaction e1:Message.Reaction.values()) {
				int i = 0;
				for(Reaction e2:reactions) {
					if(e1 == e2) {
						i++;
					}
				}
				if(i > 0)
					summary+=(e1+": "+i+", ");
			}
			return summary;
		}	
	}
	
	/**
	 * Returns true if the message has a relevent to the inputed user in some way.
	 * It can be the username, or the text contains the user name.
	 * @param user The word to be searched
	 * @return True if the inputed user has a relevance with the message object. False otherwise
	 */
	public boolean relevantTo(String user) {
		if(user == null) {
			throw new NullPointerException("Only enter a username!");
		}
		if(username.equals(user)) {
			return true;
		}else if(text.contains('@'+user+'@')) {
			return true;
		}else {
			return false;
		}
	}
	
	/****
	 * Summarizes the information of the object such as the ID, username, text message and reactions received.
	 * @return The summarization of the object
	 */
	public String toString() {
		String summary = "";
		summary+="(\0" + id + "\0) " + username;
		summary+="\n\t" + text;
		if(reactions.size()!= 0) {
			summary+="\nReactions: " + getReactionSummary();
		}
		
		return summary;
	}
	
	/***
	 * Summarizes shortly of the object, including the username and the first 50 characters of the text
	 * @return Short summarization of the message
	 */
	public String shortString() {
		String summary = "";
		summary+= username + "\t";
		text.replace("\n", " ");
		if(text.length() > 50) {
			summary+= text.substring(0, 50);
		}else {
			summary += text;
		}
		
		return summary;
	}
	
	
	/***
	 * Deletes a message by clearing the username, and change the text message
	 * 
	 */
	public void deleteMessage() {
		username = "";
		text = "This message is deleted.";
	}
	
	public static int extractID(String m) {
		String[]parts = m.split("\0", 3);
		int result = Integer.parseInt(parts[1]);
		return result;
	}
	
}

