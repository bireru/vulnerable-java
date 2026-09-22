package com.bpce.lab.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * Chargement de configuration.
 * USED : log4j-core (Log4Shell) + snakeyaml (desertialisation YAML).
 * commons-collections et guava sont declarees dans le pom mais NON utilisees
 * => elles ressortiront "Unused" dans Checkmarx.
 */
public class ConfigLoader {

    private static final Logger log = LogManager.getLogger(ConfigLoader.class);

    public Object load(String yamlContent, String user) {
        // entree utilisateur loggee => Log4Shell exploitable
        log.info("Chargement de la configuration pour l'utilisateur " + user);

        // parsing YAML non securise => desertialisation exploitable
        Yaml yaml = new Yaml();
        return yaml.load(yamlContent);
    }
}
