package com.babyartsoft.bussinestasks1.Message;

import com.babyartsoft.bussinestasks1.Public;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FCM {
    public FCM(){}

    final private static String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    protected static String server_key =	// Проект Bussines Tasks
            "AAAA6jTAjh4:APA91bGtwnuYB-whkirc_lMKtNcyXSttmzrtRc0MHxT-XzUHVv9ccsrZZINvrbPB9HOaks2Z8r40tTD3P1OLbXErhtVlBm55qU_NTr5DkSFcd7FeH-7SlvV75Ca4a-xLVzvlSvb7Tt2h";

    public static void send_FCM_Notification(String tokenId, String message){

        try {

            URL url = new URL(FCM_URL);

            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key="+server_key);
            conn.setRequestProperty("Content-Type","application/json");

            JSONObject infoJson = new JSONObject();
            infoJson.put("title","Buss_Task");
            infoJson.put("body", message);

            JSONObject json = new JSONObject();
            json.put("to",tokenId.trim());
			json.put("notification", infoJson);		// Если убрать строку, то сообщение приходят пустыми и не отображаются

//            Public.toLog("Сформировано .... ");

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//            Public.toLog("Сформировано ....1 ");
            wr.write(json.toString());
//            Public.toLog("Сформировано ....2 ");
            wr.flush();

//            Public.toLog("Отправлено .... ");

            int status = 0;
            if( null != conn ){
                status = conn.getResponseCode();
            }
//            Public.toLog("Status= " + status);
            if( status != 0){

                if( status == 200 ){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Android Notification Response : YES status=200");
                }	else if(status == 401){
                    System.out.println("Notification Response NO status 401 : TokenId : " + tokenId + "Error occurred :");
                }	else if(status == 501){
                    System.out.println("Notification Response NO status 501 : [ errorCode=ServerError ] TokenId : " + tokenId);
                }	else if( status == 503){
                    System.out.println("Notification Response NO status 503 : FCM Service is Unavailable  TokenId : " + tokenId);
                }
            }

        }	catch(MalformedURLException mlfexception){
            // Prototcal Error
            System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
        }	catch(IOException mlfexception){
            //URL problem
            System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
        }	catch (Exception exception) {
            //	General Error or exception.
            System.out.println("Error occurred while sending push Notification!.." +  exception.getMessage());
        }
    }

}
