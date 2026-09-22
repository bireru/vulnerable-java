package com.bpce.lab.integ;

import com.stripe.Stripe;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Integration d'un service de paiement externe (SaaS).
 * USED : SDK Stripe + Apache HttpClient.
 */
public class PaymentClient {

    public void init(String apiKey) {
        // SDK SaaS USED
        Stripe.apiKey = apiKey;
    }

    public int ping(String url) throws Exception {
        // HttpClient USED
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(new HttpGet(url)).getStatusLine().getStatusCode();
        }
    }
}
