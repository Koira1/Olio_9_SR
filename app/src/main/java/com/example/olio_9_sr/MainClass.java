package com.example.olio_9_sr;

import android.text.Editable;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainClass {

    Theaters theaters = new Theaters();
    ArrayList<String> locations = new ArrayList<>();
    String result;
    String locationUrl = "https://www.finnkino.fi/xml/TheatreAreas/";
    Movies movies = new Movies();

    MainClass() throws ExecutionException, InterruptedException {
        result = makeRequest(locationUrl);
    }

    String makeRequest(String url) throws ExecutionException, InterruptedException {
        HttpGetRequest getRequest = new HttpGetRequest();
        result = getRequest.execute(url).get();
        Log.d("asd", result);
        return result;
    }


    ArrayList<String> getLocations(){
        locations = theaters.getLocations(theaters.Theaters, locations);
        return locations;
    }


    String parseUrl(String selectedLocation, String pvm) throws ParseException {
        String url = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        if(pvm.equals("PVM") || pvm.equals("")){
            Date date = new Date();
            String _date = formatter.format(date);
            Log.d("_date", _date);
            for(int i = 0; i < theaters.Theaters.size(); i++) {
                if(selectedLocation.equals(theaters.Theaters.get(i).Name)){
                    url = "https://www.finnkino.fi/xml/Schedule/?area=" + theaters.Theaters.get(i).id + "&dt=" + _date;
                }
            }
            Log.d("url", url);
            return url;

        } else {
            Date date = formatter.parse(pvm);
            pvm = formatter.format(date);

            for (int i = 0; i < theaters.Theaters.size(); i++){
                if(selectedLocation.equals(theaters.Theaters.get(i).Name)){
                    url = "https://www.finnkino.fi/xml/Schedule/?area=" + theaters.Theaters.get(i).id + "&dt=" + pvm;
                }
            }
            Log.d("url", url);
            return url;
        }

    }


    int parseMovies(String url, String time) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        String[] timeChecker;
        Document doc = builder.parse(new InputSource(new StringReader(url)));
        Element root = doc.getDocumentElement();
        Log.d("root", root.toString());
        NodeList nList = doc.getElementsByTagName("Show");
        if (nList.getLength() == 0){
            Log.d("nList", "NList on 0!");
            return 0;
        }
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Log.d("nList", String.valueOf(nList.getLength()));
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                timeChecker = eElement.getElementsByTagName("dttmShowStart").item(0).getTextContent().toString().split("T");
                Log.d("TimeChecker", timeChecker[1].substring(0, 5));
                if(time.equals("Klo") || time.equals("")){
                    Movie addNewMovie = new Movie(Integer.parseInt(eElement.getElementsByTagName("ID").item(0).getTextContent()), eElement.getElementsByTagName("Title").item(0).getTextContent(), eElement.getElementsByTagName("dttmShowStart").item(0).getTextContent(), eElement.getElementsByTagName("dttmShowEnd").item(0).getTextContent(), eElement.getElementsByTagName("Theatre").item(0).getTextContent());
                    movies.addMovie(addNewMovie);
                } else {
                    if(timeChecker[1].substring(0, 5).equals(time)){
                        Movie addNewMovie = new Movie(Integer.parseInt(eElement.getElementsByTagName("ID").item(0).getTextContent()), eElement.getElementsByTagName("Title").item(0).getTextContent(), eElement.getElementsByTagName("dttmShowStart").item(0).getTextContent(), eElement.getElementsByTagName("dttmShowEnd").item(0).getTextContent(), eElement.getElementsByTagName("Theatre").item(0).getTextContent());
                        movies.addMovie(addNewMovie);
                    }
                }
            }
        }
        return movies.movies.size();
    }



    void clearMovies(){
        Log.d("Movies length", String.valueOf(movies.movies.size()));
        for(int i = 0; i < movies.movies.size(); i++){
            movies.movies.remove(i);
        }
        if(movies.movies.size() > 0){
            movies.movies.remove(0);
        }

        Log.d("Movies length", String.valueOf(movies.movies.size()));
    }


    void parseXML(String result) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new InputSource(new StringReader(result)));
        Element root = doc.getDocumentElement();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        NodeList nlist = doc.getElementsByTagName("TheatreArea");

        for (int temp = 0; temp < nlist.getLength(); temp++) {
            Node nNode = nlist.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                Theater addTheater = new Theater(eElement.getElementsByTagName("Name").item(0).getTextContent(), Integer.parseInt(eElement.getElementsByTagName("ID").item(0).getTextContent()));
                Log.d("Theater: ", eElement.getElementsByTagName("Name").item(0).getTextContent());
                theaters.Theaters.add(addTheater);
            }
        }

    }



}
