import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;


public class PageInfo {

    public static void exit(int status){
        if (status == 0) System.out.println("Bye!");
        else System.out.println("Wrong city number!");
        System.exit(status);
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        int cityNr;
        StringBuilder url = new StringBuilder("https://pogoda.interia.pl");

        //connection
        Connection connect = Jsoup.connect(url.toString());
        Document mainPage = connect.get();

        Elements el = mainPage.select("#content > div.main-content.col-xs-12.col-rs-12.col-sm-12.col-md-8.col-lg-8" +
                " > section.weather-places-group.cities-weather.is-legend > ul");

        String[] weatherInfo = el.text().split("C ");

        // print cities
        for(int i = 0; i < weatherInfo.length; i++) {
            System.out.println(i + 1 + ". " + weatherInfo[i]);
        }

        //user enter the city number
        System.out.print("Choose nr of city to check details or 0 for exit: ");
        cityNr = in.nextInt();

        if(cityNr == 0) exit(0);
        if(cityNr > weatherInfo.length) exit(1);

        //looking for href of href city
        Elements cityElement = mainPage.select("#content > div.main-content.col-xs-12.col-rs-12.col-sm-12.col-md-8.col-lg-8" +
                " > section.weather-places-group.cities-weather.is-legend > ul > li:nth-child(" + cityNr + ") > a");
        String link = cityElement.attr("href");
        String cityName = cityElement.attr("title");
        url.append(link);

        //connecting with next link
        Connection newConnect = Jsoup.connect(url.toString());
        Document cityPage = newConnect.get();

        //extracting details about weather
        Elements time = cityPage.select("#weather-currently > div.weather-currently-middle >" +
                " div.weather-currently-middle-today-wrapper > div > div.weather-currently-today-legend >" +
                " p.weather-currently-info-item.date > span.weather-currently-info-item-time");

        Elements temp = cityPage.select("#weather-currently > div.weather-currently-middle >" +
                " div.weather-currently-middle-today-wrapper > div > div.weather-currently-temp");

        Elements details = cityPage.select("#weather-currently > div.weather-currently-middle >" +
                " div.weather-currently-middle-today-wrapper > div > ul.weather-currently-details");

        System.out.println("\n" + cityName + "\nnow: " + time.text() + " " + temp.text() + "\n" + details.text());

    }
}
