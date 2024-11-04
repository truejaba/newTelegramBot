package edu .telegrambot;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;

@Component
public class SearhXolairService {

    public HashSet<Xolair> allDrugs = new HashSet<>();
    private final String url = "https://www.acmespb.ru/preparaty/ksolar";

    public SearhXolairService() {}

    public void getActualInfo()
    {   try {
        Thread.sleep(1000);
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        Elements elements = document.body().select("div").select("[class = trow]");
        for (Element e : elements) {
            String form = e.select("[class = cell name]").text();
            String adress = e.select("[class = cell address]").text();
            String price = e.select("[class = cell pricefull]").text();
            allDrugs.add(new Xolair(form, adress, price));
        }
    }catch (IOException | InterruptedException ex){
        ex.printStackTrace(System.out);
    }
    }

    public String getActualAdressAndPrice(){
        getActualInfo();
        StringBuilder builder = new StringBuilder();
        builder.append("К сожалению раствора нет в наличии, повторите запрос");
        for(Xolair x: allDrugs){
            if (x.isSolution()) {
                builder = new StringBuilder();
                builder.append("адрес: " + x.getAdress() + " цена: " + x.getPrice() + "\r\n");
            }
        }
        return builder.toString();
    }

}
