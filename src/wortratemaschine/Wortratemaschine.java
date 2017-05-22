package wortratemaschine;

import java.security.SecureRandom;

public class Wortratemaschine {
	
	/**
	 * Nur Kleinbuchstaben erlaubt
	 */
	static final String ERLAUBTEZEICHEN = "abcdefghijklmnopqrstuvwxyz";
	
	/**
	 * Zufallszahlengenerator
	 */
	static SecureRandom rnd = new SecureRandom();

	/**
	 * Erzeugt eine zufällige Zeichenkette der Länge laenge aus den Zeichen
	 * unter ERLAUBTEZEICHEN
	 * 
	 * @param laenge Länge
	 * @return Zufallszeichenkette
	 */
	static String zufallszeichenketteErzeugen(int laenge) {
		
		// Leere Zeichenkette vorbereiten
		StringBuilder sb = new StringBuilder(laenge);
		
		// Durchlauf genau laenge-Mal
		for (int i = 0; i < laenge; i++) {
			
			// Zufallszahl zwischen 0 (inklusive) und der Länge von ERLAUBTEZEICHEN (exklusive) ziehen, ...
			int zufallszahl = rnd.nextInt(ERLAUBTEZEICHEN.length());
			
			// ... den Buchstaben zur Zufallszahl holen, ...
			char zufallsbuchstabe = ERLAUBTEZEICHEN.charAt(zufallszahl);
			
			// ... und der Zeichenkette hinzufügen. 
			sb.append(zufallsbuchstabe);
		}
		
		// Gesamtzeichenkette zurückgeben
		return sb.toString();
	}
}
