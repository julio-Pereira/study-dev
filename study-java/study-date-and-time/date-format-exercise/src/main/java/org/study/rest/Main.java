package org.study.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/************************************************************
 *
 * Date format exercise
 * Change the date format from "1st Jan 2000" to "2000-01-01"
 *
 ************************************************************/
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        List<String> dates = new ArrayList<>();
        dates.add("20th Oct 2052");
        dates.add("6th Jun 1933");
        dates.add("26th May 1960");
        dates.add("20th Sep 1958");
        dates.add("16th Mar 2068");
        dates.add("25th May 1912");
        dates.add("16th Dec 2018");
        dates.add("26th Dec 2061");
        dates.add("4th Nov 2030");
        dates.add("28th Jul 1963");

        reformatDate(dates);
    }

    public static List<String> reformatDate(List<String> dates)  {
        List<String> formattedDates = new ArrayList<>();

        for (String date : dates) {
            String cleanTemplateDate = date.replaceAll("(st|nd|rd|th)", "");
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date parsedDate = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).parse(cleanTemplateDate);
                String formattedDate = formatter.format(parsedDate);
                logger.info("Parsing date to format (yyyy-MM-dd): {}", formattedDate);
                formattedDates.add(formattedDate);
            } catch (ParseException e) {
                logger.error("Error parsing date: {}", e.getMessage());
                e.printStackTrace();
            }
        }

        return formattedDates;
    }
}