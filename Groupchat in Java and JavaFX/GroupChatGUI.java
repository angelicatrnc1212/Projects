
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;



/***
 * This GroupChatGUI creates an interface to imitate a groupchat.
 * It has the messages on the top, and some interfaces such as reacting, replying, delete or posting message.
 * First of all, the user has to pick their use inside the groupchat. 
 * Without doing this, they won't be able to use other interface
 * 
 * @author Angelica Charvensym
 */
public class GroupChatGUI extends Application{
	private Pane root;
	private ListView<String> LV;
	private ObservableList<String> LVItems;
	private GroupChat gc = new GroupChat();
	private Button select;
	private TextField tfUsername;
	private Text status;
	private String activeUser;
	private CheckBox showRelevant;
	private Tab tab1;
	private Tab tab2;
	private RadioButton rGood;
	private RadioButton rOkay;
	private RadioButton rBad;
	private ToggleGroup reactions;
	private TextArea taMessage;
	private Tab tab3;
	private Button postMessage;
	private Button replyMessage;
	
	
	
	public void start(Stage primaryStage) {
		root = new Pane();
		TabPane tp = new TabPane();
		gc.addUser();
		LV = new ListView<String>();
		LV.setPrefWidth(400);
		LV.setPrefHeight(150);
		LVItems = LV.getItems();
		for(int i = 0; i < gc.getMessages().size(); i++) {
			LVItems.add(gc.getMessages().get(i));
		}
		LV.scrollTo(gc.getMessages().size()-1);
		//Show Relevant Messages
		showRelevant = new CheckBox("Show my relevant messages only");
		ShowRelevantHandler srh = new ShowRelevantHandler();
		showRelevant.setOnAction(srh);
		showRelevant.setDisable(true);
	
		//Status Pane
		status = new Text("Select a user");
		
		//Choose username tabpane
		Text t1 = new Text("Enter username: ");
		tfUsername = new TextField();
		select = new Button("Select");
		SelectHandler sh = new SelectHandler();
		select.setOnAction(sh);
		HBox chooseUsername = new HBox(10, t1, tfUsername, select);
		chooseUsername.setAlignment(Pos.CENTER);
		tab1 = new Tab("Choose User", chooseUsername);
		tab1.setClosable(false);
		
		//Act on message tabpane
		rGood = new RadioButton("Good");
		rBad = new RadioButton("Bad");
		rOkay = new RadioButton("Okay");
		reactions = new ToggleGroup();
		rGood.setToggleGroup(reactions);
		rBad.setToggleGroup(reactions);
		rOkay.setToggleGroup(reactions);
		reactions.selectToggle(rGood);
		VBox reactionsChoices = new VBox(3, rGood, rBad, rOkay);
		reactionsChoices.setAlignment(Pos.CENTER);
		Button react = new Button("React");
		ReactHandler rh = new ReactHandler();
		react.setOnAction(rh);
		Button delete = new Button("Delete");
		DeleteHandler dh = new DeleteHandler();
		delete.setOnAction(dh);
		
		HBox actMessages = new HBox(20, reactionsChoices, react, delete);
		actMessages.setAlignment(Pos.CENTER);
		tab2 = new Tab("Act To Messages", actMessages);
		tab2.setClosable(false);
		tab2.setDisable(true);
		
		//Post message tabpane
		taMessage = new TextArea();
		taMessage.setPrefWidth(350);
		taMessage.setPrefHeight(100);
		postMessage = new Button("Post");
		MessageHandler mh =  new MessageHandler();
		postMessage.setOnAction(mh);
		replyMessage = new Button("Reply");
		replyMessage.setOnAction(mh);
		HBox postAndReply =  new HBox(5, postMessage, replyMessage);
		VBox postMessage = new VBox(10, taMessage, postAndReply);
		tab3 = new Tab("Post Message", postMessage);
		tab3.setClosable(false);
		tab3.setDisable(true);
		

		VBox outline = new VBox(10, LV, showRelevant, status, tp);
		tp.getTabs().addAll(tab1, tab2, tab3);

		
		root.getChildren().addAll(outline);
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("GroupChat GUI");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private void reloadLV() {
		if(showRelevant.isSelected()) {
			LVItems.clear();
			for(int i = 0; i < gc.getRelevantMessages(activeUser).size(); i++) {
				LVItems.add(gc.getRelevantMessages(activeUser).get(i));
			}
			LV.scrollTo(gc.getRelevantMessages(activeUser).size()-1);
		}else {
			LVItems.clear();
			for(int i = 0; i < gc.getMessages().size(); i++) {
				LVItems.add(gc.getMessages().get(i));
			}
			LV.scrollTo(gc.getMessages().size()-1);
		}
	}
	
	private class MessageHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			if(event.getSource() == postMessage) {
				gc.postMessage(activeUser, taMessage.getText());
				status.setText("Message successfully posted");
			}else {
				if(LV.getSelectionModel().getSelectedIndex() == -1) {
					status.setText("Please choose a message");
				}else {
					String messageToReply = LV.getSelectionModel().getSelectedItem();
					int idToReply = Message.extractID(messageToReply);
					gc.postReply(activeUser, taMessage.getText(), idToReply);
					status.setText("Reply successfully posted");
				}
			}
			
			reloadLV();
		}
		
	}
	
	private class DeleteHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			
			if(LV.getSelectionModel().getSelectedIndex() == -1) {
				status.setText("Please choose a message");
			}else {
				String messageToReact = LV.getSelectionModel().getSelectedItem();
				int idToReact = Message.extractID(messageToReact);
				try {
					gc.deleteMessage(activeUser, idToReact);
					status.setText("Delete successful");
				}catch(IllegalArgumentException e) {
					status.setText("Delete not successful, users may only delete their own message");
				}catch(NullPointerException e) {
					status.setText("Delete not successful");
				}
				
				reloadLV();
		}	
		}
		
	}
	
	private class ReactHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			if(LV.getSelectionModel().getSelectedIndex() == -1) {
				status.setText("Please choose a message");
			}else {
				String messageToReact = LV.getSelectionModel().getSelectedItem();
				int idToReact = Message.extractID(messageToReact);
				
				if(reactions.getSelectedToggle() == rGood) {
					gc.reactToMessage(idToReact, Message.Reaction.Good);
				} else if (reactions.getSelectedToggle() == rOkay) {
					gc.reactToMessage(idToReact, Message.Reaction.Okay);
				} else {
					gc.reactToMessage(idToReact, Message.Reaction.Bad);
				}
				status.setText("Reaction posted");
				reloadLV();

			}
				
		}
		
	}
	
	private class ShowRelevantHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			reloadLV();
		}
		
	}
	private class SelectHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent e) {
			// TODO Auto-generated method stub
			for(int i = 0; i < gc.getUsernames().size(); i++) {
				if(tfUsername.getText().equalsIgnoreCase(gc.getUsernames().get(i))) {
					status.setText("You are: " + tfUsername.getText());
					activeUser = gc.getUsernames().get(i);
					showRelevant.setDisable(false);
					tab2.setDisable(false);
					tab3.setDisable(false);
				}
			}
			if(activeUser == null)
				status.setText("Username you entered is not in the groupchat");
		}
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
