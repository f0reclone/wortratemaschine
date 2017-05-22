package wortratemaschine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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
	private Label wordsPerSecond;
	@FXML
	private Label timeLeftLabel;
	
	@FXML
	private Button startButton;
	@FXML
	private Button stopButton;
	
	static boolean shouldRun;
	static boolean verified = false;
	NumberFormat format = new DecimalFormat("# E0");
	long startTime;
	int t = 0;
	private BigDecimal mediumNeededTries;

	@FXML
	private void start(ActionEvent event) {

		startButton.setDisable(true);
		stopButton.setDisable(false);

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
					guessedWord = Wortratemaschine.zufallszeichenketteErzeugen(length);
					System.out.println(t + ": " + guessedWord);
					updateMessage("Aktuell geratenes Wort: "+ guessedWord);
					updateTitle("Versuche: " + t);
					if (guessedWord.equals(myWord)) {
						break;
					}

				}
				shouldRun = false;
				startButton.setDisable(false);
				stopButton.setDisable(true);
				return null;
			}
		};
		anzahlVersucheLabel.textProperty().bind(wordtask.titleProperty());
		guessedWordLabel.textProperty().bind(wordtask.messageProperty());
		Thread t1 = new Thread(wordtask);
		t1.start();

		Task<Void> statisticstask = new Task<Void>() {
			public Void call() {

				while (shouldRun) {
						
					long timepassed = (System.currentTimeMillis() - startTime) / 1000;
					if (timepassed > 0)
					{
						long triesPerSecondRate = t / timepassed;
						updateTitle("Geratene Wörter pro Sekunde: "+triesPerSecondRate);
						
						BigDecimal stillNeededTries = mediumNeededTries.subtract(new BigDecimal(t));
						
						BigDecimal stillNeededSeconds = stillNeededTries.divide(new BigDecimal(triesPerSecondRate), 0, BigDecimal.ROUND_HALF_UP);
						
						// Wenn es mehr als 10000 Jahre sind, nur die Jahreszahl ausgeben. Die andere Methode schafft es darüber nicht mehr
						if (stillNeededSeconds.toBigInteger().multiply(new BigInteger("1000")).compareTo(new BigInteger("315360000000000"))> 0) {
							BigInteger years = stillNeededSeconds.toBigInteger().multiply(new BigInteger("1000")).divide(new BigInteger("31536000000"));
							updateMessage("Vorr. Dauer: " + years.toString()+ " JAHRE");                                                 
						} else
						{
						DateTime now = new DateTime();
						DateTime finished = (new DateTime()).plus(stillNeededSeconds.toBigInteger().multiply(new BigInteger("1000")).longValue());
						Period period = new Period(now, finished);

						PeriodFormatter formatter = new PeriodFormatterBuilder()
						    .appendYears().appendSuffix(" Jahr, ", " Jahre, ")
						    .appendMonths().appendSuffix(" Monat, ", " Monate, ")
						    .appendWeeks().appendSuffix(" Woche, ", " Wochen, ")
						    .appendDays().appendSuffix(" Tag, ", " Tage, ")
						    .appendHours().appendSuffix(" Stunde, ", " Stunden, ")
						    .appendMinutes().appendSuffix(" Minute, ", " Minuten, ")
						    .appendSeconds().appendSuffix(" Sekunde", " Sekunden")
						    .printZeroNever()
						    .toFormatter();

						String elapsed = formatter.print(period);
						updateMessage("Vorr. Dauer: " + elapsed); 
						}
						
						
						
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		};
		timeLeftLabel.textProperty().bind(statisticstask.messageProperty());
		wordsPerSecond.textProperty().bind(statisticstask.titleProperty());
		Thread t2 = new Thread(statisticstask);
		t2.start();

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
		if (text.equals("")) {
			verified = false;
			statusLabel.setTextFill(Color.BLACK);
			startButton.setDisable(true);
			statusLabel.setText("Bitte nur Kleinbuchstaben von a-z verwenden");
			probabilityLabel.setText("Wahrscheinlichkeit:");
		} else if (text.matches("[a-z]+")) {
			verified = true;
			statusLabel.setTextFill(Color.BLACK);
			startButton.setDisable(false);
			statusLabel.setText("Bitte nur Kleinbuchstaben von a-z verwenden: Ok");
			mediumNeededTries = new BigDecimal(Wortratemaschine.ERLAUBTEZEICHEN.length()).pow(text.length());
			probabilityLabel.setText("Wahrscheinlichkeit: 1 : " + mediumNeededTries);
		} else {
			verified = false;
			statusLabel.setTextFill(Color.RED);
			startButton.setDisable(true);
			statusLabel.setText("Bitte nur Kleinbuchstaben von a-z verwenden: Fehler");
			probabilityLabel.setText("Wahrscheinlichkeit:");
		}

	}

}
