package gatesys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class Api {
    public static String end_point, responseServer, main_url = "http://api-tki.localhost/api/", api_key = "049150ae-45e8-47b7-9386-9e6836166c34";
    private static boolean rs;

    public boolean CheckConnection(){
        end_point = "ping";
        try {
            URL url = new URL(main_url + end_point);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("api_key", api_key);
            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));



            int status = conn.getResponseCode();
            if(status > 299){
                conn.disconnect();
                rs = false;
            }else{
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;) sb.append((char)c);
                rs = true;
            }
        }catch (IOException e) {
        }
        return rs;
    }

    public String CardIn(String rfid, String PosID, String computerName){
        try {
            end_point = "card/read";
            URL url = new URL(main_url + end_point);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("rfid", rfid);
            params.put("pos_id", PosID);
            params.put("computer_name", computerName);
            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            int status = conn.getResponseCode();
            if(status > 299){
                conn.disconnect();
            }else{
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;) sb.append((char)c);
                String response = sb.toString();
                responseServer = response;
            }
        }catch (IOException e) {
            System.out.println(e);
        }
        return responseServer;
    }
    
    public String VisitorIn(String visitor_id, String generate_code){
        try {
            end_point = "visitor/in";
            URL url = new URL(main_url + end_point);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("visitor_id", visitor_id);
            params.put("generate_code", generate_code);
            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            int status = conn.getResponseCode();
            if(status > 299){
                conn.disconnect();
            }else{
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;) sb.append((char)c);
                String response = sb.toString();
                responseServer = response;
            }
        }catch (IOException e) {
            System.out.println(e);
        }
        return responseServer;
    }

    public String MasterListening(String trigger_button){
        try {
            end_point = "master/visitor";
            URL url = new URL(main_url + end_point);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("trigger_button", trigger_button);
            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            int status = conn.getResponseCode();
            if(status > 299){
                conn.disconnect();
            }else{
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;) sb.append((char)c);
                String response = sb.toString();
                responseServer = response;
            }
        }catch (IOException e) {
            System.out.println(e);
        }
        return responseServer;
    }

    public String PosListening(String pos_id){
        try {
            end_point = "master/pos";
            URL url = new URL(main_url + end_point);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("id", pos_id);
            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            int status = conn.getResponseCode();
            if(status > 299){
                conn.disconnect();
            }else{
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;) sb.append((char)c);
                String response = sb.toString();
                responseServer = response;
            }
        }catch (IOException e) {
            System.out.println(e);
        }
        return responseServer;
    }

}