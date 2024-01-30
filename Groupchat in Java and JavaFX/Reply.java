

/***
 * 
 * The Reply class is an extension of Message class that inherits the data members, 
 * and also adding a new data member of the Message we are replying to.
 * It is used to reply to a message which is in form a message.
 * 
 * @author Angelica Charvensym
 */
public class Reply extends Message{
	private Message replyingTo;
	
	
	/***
	 * Constructs a reply object
	 * @param username Username of the user
	 * @param text The reply text
	 * @param replyingTo The text user is replying to
	 */
	public Reply(String username, String text, Message replyingTo) {
		super(username, text);
		
		if(replyingTo == null) {
			throw new NullPointerException("Message you are replying must not be null!");
		}
		this.replyingTo = replyingTo;
	}
	
	
	/***
	 * Overrides the relevantTo method in Message class
	 * It includes the reply message written by the user as part of relevant messages
	 * 
	 * @param user The word to be searched
	 * @return True if the inputed user has a relevance with any of the message including the reply message
	 */
	@Override
	public boolean relevantTo(String user) {
		boolean isRelevant = super.relevantTo(user);
		if(isRelevant == true) {
			return true;
		}else {
			if(replyingTo.getUsername().equalsIgnoreCase(user)==true) {
				isRelevant = true;
			}
			return true;
		}
	}
	
	
	/**
	 * 
	 * Summarizes the information of the object such as the ID, username, text message and reactions received,
	 * also adds a prompt to indicate which message are we replying to by giving its snippet
	 * @return The summarization of the object
	 */
	@Override
	public String toString() {
		String str = "replying to..";
		str+= replyingTo.shortString();
		str+='\n';
		str+= super.toString();

		return str;
	}
	
}
