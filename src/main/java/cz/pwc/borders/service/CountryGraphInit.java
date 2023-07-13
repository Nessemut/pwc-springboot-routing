package cz.pwc.borders.service;

import cz.pwc.borders.model.Country;
import org.apache.http.HttpException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import static org.springframework.http.HttpMethod.GET;

@Component
public class CountryGraphInit implements ApplicationRunner {

    static Logger logger = Logger.getLogger(CountryGraphInit.class.getName());

    private final CountryService service;

    public CountryGraphInit(CountryService service) {
        this.service = service;
    }

    private JSONArray httpGetJsonResponse() throws Exception {
        String dataUrl = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";

        URL apiUrl = new URL(dataUrl);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod(String.valueOf(GET));
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpStatus.OK.value()) {
            throw new HttpException("HTTP GET Request failed: Error code " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONArray(response.toString());
    }

    private void populateGraph(JSONArray json) {
        json.forEach(c -> {
            JSONObject obj = (JSONObject) c;
            String cca3 = obj.getString("cca3");
            service.save(new Country(cca3));
        });

        json.forEach(c -> {
            JSONObject obj = (JSONObject) c;
            Country country = service.getByCca3(obj.getString("cca3"));
            JSONArray bordersJson = obj.getJSONArray("borders");
            bordersJson.forEach(b -> service.addBorders(country, service.getByCca3(b.toString())));
        });

        logger.info("Graph initialization finished");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JSONArray countryArray = httpGetJsonResponse();
        populateGraph(countryArray);
    }

}
