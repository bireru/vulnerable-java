package lab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe minimale du lab. Sert surtout a donner du code source au scanner :
 * - le pom.xml genere les findings SCA (dependances),
 * - la ligne ci-dessous ajoute aussi un finding SAST (log d'entree non maitrisee).
 * NE PAS utiliser en production.
 */
public class App {

    private static final Logger log = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        String userInput = args.length > 0 ? args[0] : "test";

        // SAST : log d'une donnee controlee par l'utilisateur (Log Injection / Log4Shell)
        log.info("Entree utilisateur : " + userInput);

        System.out.println("Lab SCA demarre.");
    }
}
