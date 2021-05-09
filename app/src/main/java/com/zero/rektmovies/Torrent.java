package com.zero.rektmovies;

import java.net.URLEncoder;

public class  Torrent {
    String title;
    String hash;
    String type;
    String quality;

    public Torrent(String title, String hash,String quality,String type) {
        this.title = title;
        this.hash = hash;
        this.type = type;
        this.quality = quality;
    }

    public String magneticURLGenerator()
    {
        String encoded_Name;
        String magneticURL="error";
        try{
            encoded_Name = URLEncoder.encode(title,"UTF-8");
            magneticURL ="magnet:?xt=urn:btih:"+hash+"&dn="+encoded_Name+"&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969%2Fannounce&tr=udp%3A%2F%2F9.rarbg.to%3A2710%2Fannounce&tr=udp%3A%2F%2Fp4p.arenabg.ch%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.cyberia.is%3A6969%2Fannounce&tr=http%3A%2F%2Fp4p.arenabg.com%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.internetwarriors.net%3A1337%2Fannounce";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return magneticURL;
    }
}
