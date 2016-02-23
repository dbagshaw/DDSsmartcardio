package org.mdpnp.smartcardio.db;

import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.rfid.ReadCard;

public class JavaFXNotificationPopUp extends Application {

	EmployeeManager eManager = new EmployeeManager();
	Text messageLabel = new Text("Hello, JavaFX!");

	// public static void main(String[] args) {
	// launch(JavaFXNotificationPopUp.class, args);
	// }

	public void start(final Stage primaryStage) {
		// launch(JavaFXNotificationPopUp.class);

		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();

		BorderPane border = new BorderPane(messageLabel);

		Scene scene = new Scene(border, 400, 200);
		primaryStage.setScene(scene);
		messageLabel.setId("message-text");
		
		primaryStage.setTitle("Notification");
		primaryStage.setY(0);
		scene.getStylesheets().add(
				JavaFXNotificationPopUp.class.getResource("notification.css")
						.toExternalForm());
		primaryStage.show();
		primaryStage.setX(scrSize.width - primaryStage.getWidth());

		// String UID = ReadCard.getUID();
		// CardDTO cardDto = eManager.findByUID(UID);
		// if (UID != null)
		// messageLabel.setText(notification + cardDto.getUserName());
		// else
		// messageLabel.setText(notification);

		displayFor(primaryStage);

		// final Timeline timeline = new Timeline(new KeyFrame(
		// Duration.seconds(2), new EventHandler<ActionEvent>() {
		// @Override
		// public void handle(ActionEvent actionEvent) {
		// primaryStage.close();
		// }
		// }));
		// timeline.play();
	}

	public void displayFor(Stage primaryStage) {
		displayFor(3000L, primaryStage);
	}

	public void displayFor(long milliseconds, Stage primaryStage) {
		// primaryStage.show();
		final Timeline timeline = new Timeline(new KeyFrame(
				Duration.seconds(2), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						primaryStage.close();
					}
				}));
		timeline.play();
	}

	public void accessNotification(String notification, String UID/*,
			Stage primaryStage */) {

		CardDTO cardDto = eManager.findByUID(UID);
		if (UID != null)
			messageLabel.setText(notification + cardDto.getUserName());
		else
			messageLabel.setText(notification);

//		group.getChildren().add(messageLabel);

		launch(JavaFXNotificationPopUp.class);
		// displayFor(primaryStage);

	}

	public void notificationSent(String notice /*, Stage primaryStage*/) {
		messageLabel.setText(notice);
//		group.getChildren().add(messageLabel);
		launch(JavaFXNotificationPopUp.class);
		// displayFor(primaryStage);
	}

	public void terminalNotification(String terminalNotice) {
		messageLabel.setText(terminalNotice);
//		group.getChildren().add(messageLabel);
		launch(JavaFXNotificationPopUp.class, terminalNotice);
		System.out.println(terminalNotice);

		// displayFor(primaryStage);
	}

	private static final JavaFXNotificationPopUp _instance = new JavaFXNotificationPopUp();

	public static JavaFXNotificationPopUp getInstance() {
		return _instance;
	}
}