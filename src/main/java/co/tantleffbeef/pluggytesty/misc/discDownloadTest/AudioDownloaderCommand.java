package co.tantleffbeef.pluggytesty.misc.discDownloadTest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
//import ws.schild.jave.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AudioDownloaderCommand implements CommandExecutor {

    final String audioPath = "data/audio/";
    final String videoPath = "data/audio/temp/";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        for (String s : args) {
            Bukkit.broadcastMessage(ChatColor.GRAY + "arg: " + s);
        }

        try {
            downloadVideo(args[0], videoPath + args[1] + ".mp4");
        } catch (Exception e) {
            Bukkit.broadcastMessage(ChatColor.RED + "Error Downloading Video");
            e.printStackTrace();
        }

//        try {
//            convertToMp3(videoPath + args[1], audioPath + args[1] + ".mp3");
//        } catch (EncoderException e) {
//            Bukkit.broadcastMessage(ChatColor.RED + "Error Encoding Audio");
//            e.printStackTrace();
//        }


        return false;
    }

    private void downloadVideo(String url, String outputPath) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream inputStream = entity.getContent();
                 FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }

//    private void convertToMp3(String videoPath, String mp3Path) throws EncoderException {
//        File source = new File(videoPath);
//        File target = new File(mp3Path);
//
//        AudioAttributes audio = new AudioAttributes();
//        audio.setCodec("libmp3lame");
//        audio.setBitRate(128000);
//        audio.setChannels(2);
//        audio.setSamplingRate(44100);
//
//        EncodingAttributes attrs = new EncodingAttributes();
//        attrs.setFormat("mp3");
//        attrs.setAudioAttributes(audio);
//
//        Encoder encoder = new Encoder();
//        encoder.encode(new MultimediaObject(source), target, attrs);
//
//        Bukkit.broadcastMessage("video file was " + (source.delete() ? "" : "not") + "deleted");
//    }
}
