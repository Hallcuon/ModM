package com.hallcuon.radiomod;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class AudioPlayer {
    private static final String YT_DLP_EXE = "yt_dpl_temp.exe";
    private static final String FFMPEG_EXE = "ffmpeg.exe";
    private static final String OUTPUT_DIR = "./config/radiomod/";
    private static final String CONVERTED_AUDIO_WAV = OUTPUT_DIR + "converted.wav";

    private Clip currentClip;
    private Process ytDlpProcess;

    public void playFromYouTube(String url) throws Exception {
        stopCurrentPlayback();
        prepareBinary(YT_DLP_EXE);
        prepareBinary(FFMPEG_EXE);
        downloadAudio(url);
        playDownloadedAudio();
    }

    private void prepareBinary(String filename) throws IOException {
        Path binPath = Paths.get(OUTPUT_DIR, filename);
        if (!Files.exists(binPath)) {
            Files.createDirectories(binPath.getParent());
            try (InputStream in = getClass().getResourceAsStream("/assets/radiomod/" + filename)) {
                if (in == null) throw new IOException(filename + " not found in resources");
                Files.copy(in, binPath, StandardCopyOption.REPLACE_EXISTING);
            }
            binPath.toFile().setExecutable(true);
        }
    }

    private void downloadAudio(String url) throws IOException, InterruptedException {
        System.out.println("Завантаження аудіо з URL: " + url);

        ytDlpProcess = new ProcessBuilder(
                OUTPUT_DIR + YT_DLP_EXE,
                "--ffmpeg-location", OUTPUT_DIR,
                "-f", "bestaudio",
                "-x",
                "--audio-format", "wav",
                "--audio-quality", "0",
                "-o", OUTPUT_DIR + "temp_audio.%(ext)s",
                "--no-warnings",
                url
        ).inheritIO().start();

        if (!ytDlpProcess.waitFor(2, TimeUnit.MINUTES)) {
            throw new IOException("Завантаження зайняло занадто багато часу");
        }

        File dir = new File(OUTPUT_DIR);
        File[] files = dir.listFiles((d, name) -> name.startsWith("temp_audio."));
        if (files == null || files.length == 0) {
            throw new IOException("Не знайдено завантажений аудіо файл");
        }
        File inputFile = files[0];
        System.out.println("Знайдено файл: " + inputFile.getName());

        Process ffmpegProcess = new ProcessBuilder(
                OUTPUT_DIR + FFMPEG_EXE,
                "-y",
                "-i", inputFile.getAbsolutePath(),
                "-ar", "44100",
                "-ac", "2",
                "-sample_fmt", "s16",
                CONVERTED_AUDIO_WAV
        ).inheritIO().start();

        if (!ffmpegProcess.waitFor(30, TimeUnit.SECONDS)) {
            throw new IOException("ffmpeg завис під час конвертації");
        }
        if (ffmpegProcess.exitValue() != 0) {
            throw new IOException("ffmpeg завершився з кодом " + ffmpegProcess.exitValue());
        }

        System.out.println("Конвертація завершена");

        if (!inputFile.delete()) {
            System.err.println("Не вдалося видалити тимчасовий файл " + inputFile.getName());
        }
    }

    private void playDownloadedAudio() throws Exception {
        File audioFile = new File(CONVERTED_AUDIO_WAV);
        if (!audioFile.exists()) {
            throw new IOException("Не вдалося знайти аудіо файл");
        }

        try (AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile)) {
            currentClip = AudioSystem.getClip();
            currentClip.open(stream);
            currentClip.start();
        }
    }

    public void stopCurrentPlayback() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
        if (ytDlpProcess != null && ytDlpProcess.isAlive()) {
            ytDlpProcess.destroy();
        }
    }

    public void cleanup() {
        stopCurrentPlayback();
        new File(CONVERTED_AUDIO_WAV).delete();

        File dir = new File(OUTPUT_DIR);
        File[] tempFiles = dir.listFiles((d, name) -> name.startsWith("temp_audio."));
        if (tempFiles != null) {
            for (File f : tempFiles) {
                f.delete();
            }
        }
    }
}
