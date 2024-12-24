package rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static io.restassured.RestAssured.given;

public class api {

    public static void downloadImage(String url, String fileName) {
        try {
            Response response = given()
                    .get(url);

            if (response.getStatusCode() == 200) {
                InputStream inputStream = response.asInputStream();

                try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Image downloaded successfully!");
                }
            } else {
                System.out.println("Failed to download image. HTTP Status: " + response.getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occurred while downloading the image.");
        }
    }

    public static List<String> translateApi(List<String> toTranslate) {
        RestAssured.baseURI = "https://rapid-translate-multi-traduction.p.rapidapi.com/t";

        Response response = given().header("x-rapidapi-key", "bc7f416c41mshe88a549deedd436p11f31ejsn8e18afe24acc")
                .header("x-rapidapi-host", "rapid-translate-multi-traduction.p.rapidapi.com")
                .header("Content-Type", "application/json")
                .body(getJsonBody(toTranslate))
                .log().all()
                .when().post()
                .then().log().all().
                assertThat().statusCode(200).extract().response();

        return response.jsonPath().getList("");
    }

    private static String getJsonBody(List<String> input) {
        return "{\n" +
                "  \"from\": \"es\",\n" +
                "  \"to\": \"en\",\n" +
                "  \"e\": \"\",\n" +
                "  \"q\": [\n" +
                "    \"" + input.get(0) + "\",\n" +
                "    \"" + input.get(1) + "\",\n" +
                "    \"" + input.get(2) + "\",\n" +
                "    \"" + input.get(3) + "\",\n" +
                "    \"" + input.get(4) + "\"\n" +
                "  ]\n" +
                "}";
    }
}
