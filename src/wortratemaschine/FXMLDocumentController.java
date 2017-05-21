
package wortratemaschine;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Andreas N.
 */
public class FXMLDocumentController implements Initializable {

	@FXML
	private Label guessedWordLabel;
	@FXML
	private Label anzahlVersucheLabel;
	@FXML
	private TextField wordToGuessTextField;
	@FXML
	private Label statusLabel;
	@FXML
	private Label probabilityLabel;
	@FXML
	private Label timeLeftLabel;
	static boolean shouldRun;
	static boolean verified = false;
	NumberFormat format = new DecimalFormat("# E0");
	long startTime;
	int t = 0;

	@FXML
	private void start(ActionEvent event) {

		if (!verified) {
			return;
		}

		shouldRun = true;
		Task<Void> wordtask = new Task<Void>() {
			public Void call() {
				startTime = System.currentTimeMillis();
				String myWord = wordToGuessTextField.getText();
				String guessedWord;
				int length = myWord.length();
				t = 0;
				while (shouldRun) {
					t++;
					guessedWord = WordUtil.randomString(length);
					System.out.println(t + ": " + guessedWord);
					updateMessage(guessedWord);
					updateTitle("Versuche: " + t);
					if (guessedWord.equals(myWord)) {
						break;
					}

				}
				return null;
			}
		};
		anzahlVersucheLabel.textProperty().bind(wordtask.titleProperty());
		guessedWordLabel.textProperty().bind(wordtask.messageProperty());
		Thread t1 = new Thread(wordtask);
		t1.start();
		/**
		 * Task<Void> statisticstask = new Task<Void>() { public Void call() {
		 * 
		 * while (shouldRun) {
		 * 
		 * updateMessage("Vorr. Dauer: " + 1 / (Math.pow((1 /
		 * WordUtil.AB.length()), wordToGuessTextField.getText().length()) * t
		 * /( (System.currentTimeMillis() - startTime) / 1000))); try {
		 * Thread.sleep(200); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * } return null; } };
		 * timeLeftLabel.textProperty().bind(statisticstask.messageProperty());
		 * Thread t2 = new Thread(statisticstask); t2.start();
		 **/
	}

	@FXML
	private void stop(ActionEvent event) {
		shouldRun = false;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		wordToGuessTextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
					final String newValue) {
				validateText(newValue);
			}
		});
	}

	public void validateText(String text) {
		System.out.println(text);
		if (text.equals("")) {
			verified = false;
			statusLabel.setText("Bitte nur Kleinbuchstaben von a-z verwenden:");
			probabilityLabel.setText("Wahrscheinlichkeit:");
		} else if (text.matches("[a-z]+")) {
			verified = true;
			statusLabel.setText("Bitte nur Kleinbuchstaben von a-z verwenden: Ok");
			probabilityLabel.setText("Wahrscheinlichkeit: 1 : " + (long) Math.pow(WordUtil.AB.length(), text.length()));
		} else {
			verified = false;
			statusLabel.setText("Bitte nur Kleinbuchstaben von a-z verwenden: Fehler");
			probabilityLabel.setText("Wahrscheinlichkeit:");
		}

	}

}
