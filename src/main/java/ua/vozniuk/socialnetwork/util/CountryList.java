package ua.vozniuk.socialnetwork.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CountryList {
    private final static String[] locales = Locale.getISOCountries();
    public final static List<String> COUNTRIES =
            Arrays.stream(locales)
                    .map(countryCode -> new Locale("", countryCode))
                    .map(Locale::getDisplayCountry)
                    .toList();
}
