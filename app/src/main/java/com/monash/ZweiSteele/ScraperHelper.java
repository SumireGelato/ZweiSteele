package com.monash.ZweiSteele;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kin To Pang on 15/06/14.
 * HTML Scraping done by JSoup - http://jsoup.org/
 */
public class ScraperHelper {

    private static final String host = "http://www.heartofthecards.com";
    private static Document doc;

    public static TreeMap<String, String> getSeriesList(Character type)
            throws IOException {//list format: map<Series name, Series link>>

        Map<String, String> allSeries = new HashMap<String, String>();

        Elements seriesRaw;
        //connect and parse webpage
        doc = Jsoup.connect(host + "/code/cardlist.html?pagetype=ws").get();

        //selects the needed elements from the parsed webpage by CSS selectors
        switch (type) {
            case 't'://TDs
                seriesRaw = doc.select("table[width=100%][style=font-size:14px]>tbody>tr:first-of-type>td:first-of-type>a");
                break;
            case 'b'://Boosters
                seriesRaw = doc.select("table[width=100%][style=font-size:14px]>tbody>tr:first-of-type>td:nth-of-type(3)>a");
                break;
            case 'e'://Extra Boosters
                seriesRaw = doc.select("table[width=100%][style=font-size:14px]>tbody>tr:nth-of-type(3)>td:first-of-type>a");
                break;
            default:
                return null;
        }

        for (Element seriesLinks : seriesRaw) {
            //Put found elements into map
            allSeries.put(seriesLinks.text(), seriesLinks.attr("href"));
        }

        TreeMap<String, String> allSeriesSorted = new TreeMap<String, String>(allSeries);
        return allSeriesSorted;
    }

    //List format map<Card ID,  Array[carddata string,link]>
    //card data string format: english card name/japanese card name/card type/card color in hex
    public static TreeMap<String, String[]> getCardList(String link) throws IOException
    {
        Map<String, String[]> cardList = new HashMap<String, String[]>();

        Elements cardListRaw;

        doc = Jsoup.connect(host + link).get();

        cardListRaw = doc.select(".cardlist>tbody>tr>td:first-of-type:not([bgcolor])" +
                ", .cardlist>tbody>tr>td>a" +
                ", .cardlist>tbody>tr>td:nth-of-type(3):not([bgcolor])" +
                ", .cardlist>tbody>tr>td[style]:last-of-type");

        //Since the data is in the selected elements repeats every 5th element the loop increments by 5
        for (int i = 0; i < cardListRaw.size(); i += 5) {
            //check if the current card is a repeat
            String spCheckerString = cardListRaw.get(i).text().substring(cardListRaw.get(i).text().length() - 3);
            if (spCheckerString.contains("SP") || spCheckerString.contains("R") || spCheckerString.contains("S")) {
                //Skip
            } else {
                StringBuilder str = new StringBuilder();
                str.append(StringEscapeUtils.unescapeXml(cardListRaw.get(i + 1).text() + "/"));//ID
                str.append(StringEscapeUtils.unescapeXml(cardListRaw.get(i + 2).childNode(0).toString() + "/"));//Eng Name
                str.append(StringEscapeUtils.unescapeXml(cardListRaw.get(i + 2).childNode(2).toString() + "/"));//Jp Name
                str.append(cardListRaw.get(i + 3).text() + "/");//Type
                str.append(cardListRaw.get(i + 4).attr("bgcolor"));//Color
                String[] cardDetails = {str.toString(), cardListRaw.get(i + 1).attr("href")};
                cardList.put(cardListRaw.get(i).text(), cardDetails);
            }
        }
        TreeMap<String, String[]> cardListSorted = new TreeMap<String, String[]>(cardList);
        return cardListSorted;
    }

    public static HashMap<String, String> getSingleCard(String link)
            throws IOException {
        HashMap<String, String> cardData = new HashMap<String, String>();
        Elements singleCardHeadings;
        Elements singleCardData;

        doc = Jsoup.connect(host + link).get();


        singleCardData = doc.select(".cards3:not([align]), td[colspan=2]>b, .cards2, table[width=95%]>tbody>tr:nth-of-type(3)>td>img");
        singleCardHeadings = doc.select("td.cards");

        for (int i = 0; i < singleCardData.size(); i++) {
            if (i == 0) {
                cardData.put("Eng Name", singleCardData.get(i).text());
            } else if (i == 1) {
                cardData.put("Image Source", singleCardData.get(i).attr("src"));
            } else if (i == 2) {
                cardData.put("Jp Name", singleCardData.get(i).text());
            } else {
                cardData.put(singleCardHeadings.get(i - 2).text().split(":")[0], singleCardData.get(i).text());
            }
        }
        return cardData;
    }
}
