/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parksys;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ridhw
 */
public class Api {
    public static BufferedReader reader;
    public static String end_point, responseServer, main_url = "https://api.tamankopoindah.com/api/", api_key = "a72d26ab-bf7a-4597-8c29-48ee2faf9920";
    public static boolean rs;

    public Api(){

    }

    public String MasterGate(){
    	  try {
              end_point = "master/gate";
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

    public String KonfigurasiParkir(){
        try {
            end_point = "parkir";
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
            }else{
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;) sb.append((char)c);
                String response = sb.toString();
                responseServer = response;
            }


        }catch(MalformedInputException e){
            System.out.println(e);
        }catch (IOException e) {
            System.out.println(e);
        }

        return responseServer;
    }

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

    public String generate(){
        try {
            end_point = "generate/ticket";
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
            }else{
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;) sb.append((char)c);
                String response = sb.toString();
                responseServer = response;
            }


        }catch(MalformedInputException e){
            System.out.println(e);
        }catch (IOException e) {
            System.out.println(e);
        }

        return responseServer;
    }

    public String parkirIn(String kategori, String no_ticket, String barcode_id, String check_in){
        end_point = "member/in";
        String charset = "UTF-8";
        File uploadFile = new File("capture/"+kategori +"/"+kategori+"_"+ barcode_id + ".jpg");
        String requestURL = main_url + end_point;
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addFormField("kategori", kategori);
            multipart.addFormField("no_ticket", no_ticket);
            multipart.addFormField("barcode_id", barcode_id);
            multipart.addFormField("check_in", check_in);
            multipart.addFormField("api_key", api_key);


            multipart.addFilePart("image", uploadFile);

            List<String> response = multipart.finish();

//            System.out.println("SERVER REPLIED:");

            StringBuilder sb = new StringBuilder();
            for (String line : response) {
                sb.append(line);
            }
            String responses = sb.toString();
            responseServer = responses;
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return responseServer;
    }


    public String parkirInWithoutPicture(String kategori, String no_ticket, String barcode_id, String check_in){
        try {
            end_point = "parkir/in";
            URL url = new URL(main_url + end_point);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("kategori", kategori);
            params.put("no_ticket", no_ticket);
            params.put("barcode_id", barcode_id);
            params.put("check_in", check_in);
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


        }catch(MalformedInputException e){
            System.out.println(e);
        }catch (IOException e) {
            System.out.println(e);
        }
        return responseServer;
    }

    public String MemberIn(String rfid,String kategori, String no_ticket, String barcode_id, String check_in){
        end_point = "member/in";
        String charset = "UTF-8";
        File uploadFile = new File("capture/"+kategori +"/"+kategori+"_"+ barcode_id + ".jpg");
        String requestURL = main_url + end_point;
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addFormField("rfid", rfid);
            multipart.addFormField("kategori", kategori);
            multipart.addFormField("no_ticket", no_ticket);
            multipart.addFormField("barcode_id", barcode_id);
            multipart.addFormField("check_in", check_in);
            multipart.addFormField("api_key", api_key);


            multipart.addFilePart("image", uploadFile);

            List<String> response = multipart.finish();

//            System.out.println("SERVER REPLIED:");

            StringBuilder sb = new StringBuilder();
            for (String line : response) {
                sb.append(line);
            }
            String responses = sb.toString();
            responseServer = responses;
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return responseServer;
    }

    public String MemberInWithoutPicture(String rfid,String kategori, String no_ticket, String barcode_id, String check_in){
         try {
            end_point = "member/in";
            URL url = new URL(main_url + end_point);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("rfid", rfid);
            params.put("kategori", kategori);
            params.put("no_ticket", no_ticket);
            params.put("barcode_id", barcode_id);
            params.put("check_in", check_in);
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


        }catch(MalformedInputException e){
            System.out.println(e);
        }catch (IOException e) {
            System.out.println(e);
        }
        return responseServer;
    }

    /**
     *
     * @param rfid
     * @return
     */
    public String readMember(String rfid){
        try {
            end_point = "member/read";
            URL url = new URL(main_url + end_point);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("rfid", rfid);


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


        }catch(MalformedInputException e){
            System.out.println(e);
        }catch (IOException e) {
            System.out.println(e);
        }
        return responseServer;
    }
    
    public String mainUrl() {
    	return main_url;
    }
}


