package ru.hse.regex;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckerImplementation implements Checker {

    @Override
    public Pattern getPLSQLNamesPattern() {
        String test = "[a-zA-Z][a-zA-Z0-9_$]{0,29}";
        Pattern pattern = Pattern.compile(test);
        return pattern;
    }

    @Override
    public Pattern getHrefURLPattern() {
        // I suppose, today nobody uses "www"
        // A или name?
        String urlQuotes = "<(\\s*)[a-zA-Z0-9]+" +
                "(\\s+)(?i)href(\\s*)=(\\s*)" +
                "\"(\\s*)(h(\\s*)t(\\s*)t(\\s*)p(\\s*)s?(\\s*)(\\s*):(\\s*)/(\\s*)/)?(\\s*)" +
                "([\\d\\sa-z.-]+)(\\s*)(\\.)(\\s*)([a-z.\\s]{2,6})(\\s*)" +
                "([/?=\\w\\s.-]*)*(\\s*)(/?)(\\s*)\"(\\s*)/?>";
        String urlNoQuotes = "<(\\s*)[a-zA-Z0-9]+(\\s+)(?i)href(\\s*)=(\\s*)(https?://)?" +
                "([\\da-z.-]+)\\.([a-z.]{2,6})([/?=\\w.-]*)*/?(\\s*)/?>";

        String test = urlNoQuotes + "|" + urlQuotes;
        Pattern pattern = Pattern.compile(test);
        return pattern;
    }

    @Override
    public Pattern getEMailPattern() {
        // Length = 2..22
        String acc = "[a-zA-Z0-9][a-zA-Z0-9_.-]{0,20}[a-zA-Z0-9]@";
        String dom = "([\\da-z][\\da-z-]*[\\da-z]\\.)+";
        String dom1 = "(ru|com|net|org)";
        String test = acc + dom + dom1;
        Pattern pattern = Pattern.compile(test);
        return pattern;
    }

    @Override
    public boolean checkAccordance(String inputString, Pattern pattern) throws IllegalArgumentException {
        if (inputString == null && pattern == null) {
            return true;
        } else if (inputString == null || pattern == null) {
            throw new IllegalArgumentException("inputString or pattern is null.");
        }

        Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }

    @Override
    public List<String> fetchAllTemplates(StringBuffer inputString, Pattern pattern) throws IllegalArgumentException {
        List<String> entries = new ArrayList<>();
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            entries.add(matcher.group());
        }
        return entries;
    }
}
