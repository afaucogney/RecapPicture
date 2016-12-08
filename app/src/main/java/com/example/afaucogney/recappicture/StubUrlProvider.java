package com.example.afaucogney.recappicture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by afaucogney on 10/11/2016.
 */

public class StubUrlProvider {

    public static final String MAIN_URL = "https://api.adorable.io/avatars/200/";
    public static final String TERMINAISON = ".io.png";

    public static String getMainPhotoUrl() {
        return "http://winefolly.com/wp-content/uploads/2016/01/French-Wine-Label-Terms-1.jpg";
//        return "https://www.google.fr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png";
    }

    public static List<String> getUserPhotoListUrls(int size) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(MAIN_URL + getRandomString(20) + TERMINAISON);
        }
        return result;
    }


    private static String getRandomString(int size) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static String getLogoUrl() {
        return "http://vignette2.wikia.nocookie.net/logopedia/images/d/d2/Google_icon_2015.png";
    }

    public static List<String> getTags() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            result.add(getRandomString(8));
        }
        return result;
    }
}
