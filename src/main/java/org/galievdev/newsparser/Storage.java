package org.galievdev.newsparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.io.IOException;
import java.util.ArrayList;

public class Storage {
    private final ArrayList<String> newsList;
    Storage()
    {
        newsList = new ArrayList<>();
        parser("https://www.gazeta.uz/ru/list/news/");
    }

    String getNews()
    {
        //Получаем последнюю новость
        return newsList.get(0);
    }

    String getOldNews(int i){
        return newsList.get(i);
    }

    int getSize(){
        return newsList.size();
    }

    ArrayList<String> getNewsList(){
        return newsList;
    }

    void parser(String strURL)
    {
        String className = "nblock ";
        Document doc = null;
        try {
            //Получаем документ нужной нам страницы
            doc = Jsoup.connect(strURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Получаем группу объектов, обращаясь методом из Jsoup к определенному блоку
        assert doc != null;
        Elements elQuote = doc.getElementsByClass(className);
        Elements img = elQuote.select("a");
        String url = img.attr("href");

        //Достаем текст из каждого объекта поочереди и добавляем в наше хранилище
        elQuote.forEach(el -> {
            newsList.add(el.text() + " \nhttps://www.gazeta.uz/" + url);
        });
    }
}
