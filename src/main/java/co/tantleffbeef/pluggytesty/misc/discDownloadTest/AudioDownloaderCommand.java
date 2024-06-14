package co.tantleffbeef.pluggytesty.misc.discDownloadTest;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.YoutubeCallback;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.quality.AudioQuality;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.net.URL;

public class AudioDownloaderCommand implements CommandExecutor {

    final String audioPath;
    final String videoPath;

    Plugin plugin;

    YoutubeDownloader downloader = new YoutubeDownloader();

    public AudioDownloaderCommand(Plugin plugin) {
        this.plugin = plugin;
        videoPath = plugin.getDataFolder() + "/data/audio/temp/";
        audioPath = plugin.getDataFolder() + "/data/audio/";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        Bukkit.broadcastMessage("The directory " + plugin.getDataFolder() + ((new File(plugin.getDataFolder().getPath()).exists()) ? " exists" : " does not exist"));
        Bukkit.broadcastMessage("The directory " + videoPath + ((new File(videoPath).exists()) ? " exists" : " does not exist"));


        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "paths: " + videoPath + ", " + audioPath);

        for (String s : args) {
            Bukkit.broadcastMessage(ChatColor.GRAY + "arg: " + s);
        }

        try {

            Bukkit.broadcastMessage("downloading video");

            downloadVideo(args[0], videoPath, args[1]);

            Bukkit.broadcastMessage("download complete (dont know if it awaits?)");

//            downloadVideo(args[0], videoPath + args[1] + ".mp4");
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

    private void downloadVideo(String videoId, String outputDir, String name) {
        RequestVideoInfo infoRequest = new RequestVideoInfo(videoId);
        Response<VideoInfo> infoResponse = downloader.getVideoInfo(infoRequest);
        VideoInfo video = infoResponse.data();

        RequestVideoFileDownload downloadRequest = new RequestVideoFileDownload(video.bestVideoWithAudioFormat())
                .saveTo(new File(outputDir))
                .renameTo(name)
                .overwriteIfExists(true);

        Response<File> downloadResponse = downloader.downloadVideoFile(downloadRequest);
        File data = downloadResponse.data();

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
