package org.acme.aws;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InstanceMetadata {

    public static String getInstanceIp() {
        String EC2_METADATA_URL = "http://169.254.169.254/latest/meta-data/local-ipv4"; // For private IP
        // String EC2_METADATA_URL = "http://169.254.169.254/latest/meta-data/public-ipv4"; // For public IP, if needed

        try {
            URL url = new URL(EC2_METADATA_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get instance IP", e);
        }
    }
}

