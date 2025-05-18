package github.PanheadGG.SuperMarioBros.player;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import github.PanheadGG.SuperMarioBros.assets.Asset;
import github.PanheadGG.SuperMarioBros.timer.MicroTimer;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import javazoom.spi.vorbis.sampled.convert.VorbisFormatConversionProvider;
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader;

import javax.sound.sampled.*;

public class MusicPlayer {

    public static final int INFINITE = -1;

    private int loopTime = 0;
    private URL url = null;

    private Thread thread;

    private SourceDataLine line = null;
    private AudioInputStream audio = null;
    private volatile boolean paused = false;
    private volatile boolean playing = false;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    private int interval = 100;

    public boolean isPlaying() {
        return playing;
    }

    public boolean isPaused() {
        return paused;
    }

    public MusicPlayer() {
    }

    public void load(URL url) {
        try {
            if (playing) {
                stop();
            }
            if (url == null) {
                System.err.println("File is not exist!");
                return;
            }

            String path = url.toString();
            AudioInputStream stream;
            if (path.toLowerCase().endsWith(".mp3")) {
                stream = new MpegAudioFileReader().getAudioInputStream(url);
            } else if (path.toLowerCase().endsWith(".ogg")) {
                AudioInputStream in = new VorbisAudioFileReader().getAudioInputStream(url);
                AudioFormat baseFormat = in.getFormat();
                AudioFormat format = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(),
                        false
                );
                stream = new VorbisFormatConversionProvider().getAudioInputStream(format, in);
            } else {
                stream = AudioSystem.getAudioInputStream(url);
            }
            if (stream == null) return;
            this.url = url;
            AudioFormat format = stream.getFormat();
            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(),
                    format.getChannels() * 2, format.getSampleRate(), false);
            stream = AudioSystem.getAudioInputStream(format, stream);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(), AudioSystem.NOT_SPECIFIED);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(stream.getFormat());
            audio = stream;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported Audio Format");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (playing) return;
        load(url);
    }


    public void load(File file) {
        if (!file.exists()) {
            System.err.println("File is not exist!");
            return;
        }
        try {
            load(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void load(String path) {
        load(new File(path));
    }

    public void load(Asset asset) {
        URL url = asset.getURL();
        if (url == null) {
            System.err.println("Asset does not exist");
            return;
        }
        load(url);
    }

    private void load(AudioInputStream stream) throws Exception {
        if (playing) {
            stop();
        }

        AudioFormat format = stream.getFormat();
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(),
                format.getChannels() * 2, format.getSampleRate(), false);
        stream = AudioSystem.getAudioInputStream(format, stream);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(), AudioSystem.NOT_SPECIFIED);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(stream.getFormat());
        audio = stream;
    }

    public void pause() {
        paused = true;
    }


    public void resume() {
        paused = false;
        synchronized (this) {
            notifyAll();
        }
    }
    public void play(){
        if (playing) {
            return;
        }
        if (audio == null || line == null) {
            return;
        }
        loopTime = 1;
        playMusic();
    }

    private void playMusic() {
        if (playing) {
            return;
        }
        if (audio == null || line == null) {
            return;
        }
        if (loopTime < INFINITE) {
            System.err.println("MusicPlayer: loop(int) -> Illegal loop times!");
            loopTime = 0;
            return;
        }

        thread = new Thread(() -> {
            while (loopTime != 0) {
                try {
                    playing = true;
                    line.start();

                    byte[] buff = new byte[4];
                    while (audio.read(buff) != -1 && playing) {
                        synchronized (this) {
                            while (paused) {
                                wait();
                            }
                        }
                        line.write(buff, 0, 4);
                    }
                    MicroTimer.sleep(1000L * interval);
//                    line.drain();
//                    line.flush();
//                    line.stop();
                    reload();
                    playing = false;
                    if (loopTime > 0) loopTime--;
                    reload();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            playing = false;
        });
        thread.start();
    }


    public void stop() {
        playing = false;
        if (null == audio || null == line) {
            return;
        }
        line.stop();
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
        reload();
    }

    public void release() {
        playing = false;
        paused = false;
        if (audio != null) {
            try {
                audio.close();
            } catch (Exception ignored) {
            }
        }
        if (line != null) {
            try {
                line.drain();
                line.flush();
                line.stop();
                line.close();
            } catch (Exception ignored) {
            }
        }


        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        thread = null;
    }

    public void loop(int times) {
        if (playing || audio == null || line == null) {
            return;
        }
        if(times<INFINITE){
            System.err.println("MusicPlayer: loop(int) -> Illegal loop times!");
            return;
        }
        if (loopTime != 0) return;
        loopTime = times;
        playMusic();
    }
}

