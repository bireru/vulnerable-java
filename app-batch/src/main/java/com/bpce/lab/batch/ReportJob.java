package com.bpce.lab.batch;

import java.sql.Connection;
import java.sql.DriverManager;

import com.thoughtworks.xstream.XStream;

/**
 * Job batch de reporting.
 * USED : mysql-connector (JDBC) + xstream (serialisation XML).
 * xstream est utilisee et vulnerable, mais sa montee de version est un
 * changement majeur cassant => bon candidat "risque accepte" a documenter.
 * La dependance interne legacy-crypto (pom) n'est PAS importee ici (Private/Unused).
 */
public class ReportJob {

    public void run(String host, String xmlConfig) throws Exception {
        // xstream USED : desertialisation XML
        XStream xstream = new XStream();
        Object cfg = xstream.fromXML(xmlConfig);

        // mysql-connector USED : host concatene => aussi un point SAST
        try (Connection c = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/bank", "user", "pass")) {
            System.out.println("Batch execute avec config " + cfg);
        }
    }
}
