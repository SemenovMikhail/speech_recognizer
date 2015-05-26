package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;
import java.io.File;

import java.io.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*; 
import java.util.*;
import play.data.Form;
import play.data.DynamicForm;
import org.apache.commons.io.*;


public class Application extends Controller {

    public static Result index() {

    	return ok(index.render());
    }

    public static Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();
        String id = MD5(request().remoteAddress());
        String filename = "";
        FilePart pic = body.getFile("file");
        boolean blob = false;
        if (pic != null) {
        //Logger.info("NOT null");
        filename = id+"_"+pic.getFilename();
        Logger.info("FILENAME: "+filename);
  
        if (filename.equals("blob")) {
            filename = id+"_recorded.wav";
            blob = true;
        }
        String contentType = pic.getContentType();
        File file = pic.getFile();
        uploadFile(file, filename);
        if (blob)
            return ok();
        return redirect(controllers.routes.Application.sendRequest(filename));
        } else {
        return badRequest();
        }
    }

    public static void uploadFile(File file, String filename) {
        try {
            InputStream f = new FileInputStream(file);

            File newFile = new File("/Users/cortez/Учеба/3 курс/Курсовая/project/play-java/database/"+filename);


            byte[] byteFile = IOUtils.toByteArray(f);
            FileUtils.writeByteArrayToFile(newFile,byteFile);
        } catch (FileNotFoundException ex) {
            Logger.info("Exception while uploading! File not found! "+ex.getMessage());
        } catch (IOException ex) {
            Logger.info("Exception while uploading! "+ex.getMessage());
        }
    }

    public static Result sendRequest(String filename) {
        File file = new File("/Users/cortez/Учеба/3 курс/Курсовая/project/play-java/database/"+filename);
        String id = MD5(request().remoteAddress());
        Logger.info("Send request file: ", filename);
        Promise<WSResponse> response = WS.url("https://dictation.nuancemobility.net/NMDPAsrCmdServlet/dictation")
        .setQueryParameter("appId","NMDPTRIAL_fknisit_gmail_com20150506152637")
        .setQueryParameter("appKey","e2513983fd5ee1e2b048fb87bd6eed3bb9c91ff4b8ed07f801fdd13880112f626269e560c7c642770e3dfeccabec0764b526586d135f65155d364ddd487c3faa")
        .setQueryParameter("id", id)
        .setHeader("Transfer-Encoding", "chunked")
        .setHeader("Accept", "text/plain")
        .setHeader("Accept-Language", "en_US")
        .setContentType("audio/x-wav;codec=pcm;bit=16;rate=8000")
        .post(file);

        return ok(recognize.render(response.get(20000).getBody()));
    }

    public static Result recognizeRecorded() {
        String id = MD5(request().remoteAddress());
        String filename = id+"_recorded.wav";
        return redirect(controllers.routes.Application.sendRequest(filename));
    }


    public static Result file(String file) {
        try {
            String f = file;
            f = java.net.URLDecoder.decode(f, "ISO8859-1");
            f = new String(f.getBytes("ISO8859-1"), "utf-8").trim();
            response().setContentType("");

            return ok(new java.io.File("/Users/cortez/Учеба/3 курс/Курсовая/project/play-java/database/"+ f));


        } catch (Exception e) {
            Logger.info(e.getMessage());
            return badRequest();
        }
    }

    public static Result jsRoutes() {
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        routes.javascript.Application.upload()))
                .as("text/javascript");
    }

    public static String MD5(String md5) {
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
    }
    return null;
}

}
