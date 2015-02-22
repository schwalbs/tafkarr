package edu.rose_hulman.tafkarr;

import java.util.GregorianCalendar;


/**
 * Created by gartzkds on 2/20/2015.
 */
public class Util {
    public static final String TERM_ONE_SUFFIX = "10";
    public static final String TERM_TWO_SUFFIX = "20";
    public static final String TERM_THREE_SUFFIX = "30";
    public static final String TERM_FOUR_SUFFIX = "40";

    public static String getCurrentTerm() {
        String term = "";
        GregorianCalendar now = new GregorianCalendar();
        int month = now.get(GregorianCalendar.MONTH);
        int year = now.get(GregorianCalendar.YEAR);
        if (month >= GregorianCalendar.SEPTEMBER && month <= GregorianCalendar.NOVEMBER) {
            //sept-november -> 201510
            term = (year + 1) + TERM_ONE_SUFFIX;
        } else if (month == GregorianCalendar.DECEMBER || month <= GregorianCalendar.FEBRUARY) {
            //december-february -> 201520
            if (month == GregorianCalendar.DECEMBER) {
                year = year + 1;
            }
            term = year + TERM_TWO_SUFFIX;
        } else if (month >= GregorianCalendar.MARCH && month <= GregorianCalendar.MAY) {
            //march-May -> 201530
            term = year + TERM_THREE_SUFFIX;
        } else {
            //june-august ->201540
            term = year + TERM_FOUR_SUFFIX;
        }
        return term;
    }

}
