package ru.hse.regex;

import java.util.List;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String names = "Noah1337 Juno_Beach374 eeEswws;wws3$3;  $555443asdw _What   w45_$s$$";
        String emails = "maxsurjudo@gmail.com_lolwoot@mail.rambler.ru" +
                "   35rofl.max@sse.wrt.er.org  _w.r.o.n.g@gm.ru  __ew_@ma.com  k.k.k.@mail.ru ";
        String hrefs = "< a href=https://chrome.google.com/webstore/detail/bicofajmgfmdjlcfdmkadheighomkhlc/>"
                + "<a HrEf=\"http://java.sun.c om/javase/6/d ocs/api/ja va/util/regex/package-summary.html\">"
                + "< a href =\" https://youtube.com/watch?v=rp1xeaG2R3w\" />" +
                "< a href = \"index.html\" >" + "<a href = \" %6s.com\">";

        CheckerImplementation ch = new CheckerImplementation();

        System.out.println("*****names:*****");
        printAll(names, ch.getPLSQLNamesPattern(), ch);
        System.out.println("*****hrefs*****");
        printAll(hrefs, ch.getHrefURLPattern(), ch);
        System.out.println("*****emails:*****");
        printAll(emails, ch.getEMailPattern(), ch);

        System.out.println("*****check accordance:*****");
        checkAccordance("<a href = \" %6s.com\">", ch.getHrefURLPattern(), ch);
        checkAccordance("$555443asdw", ch.getPLSQLNamesPattern(), ch);
        checkAccordance("35rofl.max@sse.wrt.er.org", ch.getEMailPattern(), ch);
    }

    static void printAll(String test, Pattern pattern, CheckerImplementation ch) {
        List<String> list;
        list = ch.fetchAllTemplates(new StringBuffer(test), pattern);

        for (String i : list) {
            System.out.println(i);
        }
    }

    static void checkAccordance(String test, Pattern pattern, CheckerImplementation ch) {
        System.out.println(test + " - " +
                ch.checkAccordance(test, pattern));
    }
}