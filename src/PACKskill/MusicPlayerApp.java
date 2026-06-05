package PACKskill;

//i will use the previous code which you give me                                                                                   import javafx.application.Application; 
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerApp extends Application 
{

    private MediaPlayer mediaPlayer;
    private List<File> songList = new ArrayList<>();
    private ListView<String> listView = new ListView<>();
    private int currentIndex = -1;

    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("Music Player");

        // Buttons
        Button importBtn = new Button("Import Songs");
        Button playBtn = new Button("Play");
        Button pauseBtn = new Button("Pause");
        Button stopBtn = new Button("Stop");
        Button nextBtn = new Button("Next");
        Button prevBtn = new Button("Previous");

        Slider volumeSlider = new Slider(0, 1, 0.5);

        // Import songs
        importBtn.setOnAction(e -> importSongs(stage));

        // Play
        playBtn.setOnAction(e -> playSong());

        // Pause
        pauseBtn.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.pause();
        });

        // Stop
        stopBtn.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.stop();
        });

        // Next
        nextBtn.setOnAction(e -> {
            if (songList.size() > 0) {
                currentIndex = (currentIndex + 1) % songList.size();
                playSong();
            }
        });

        // Previous
        prevBtn.setOnAction(e -> {
            if (songList.size() > 0) {
                currentIndex = (currentIndex - 1 + songList.size()) % songList.size();
                playSong();
            }
        });

        // Volume control
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue());
            }
        });

        // List click
        listView.setOnMouseClicked(e -> {
            currentIndex = listView.getSelectionModel().getSelectedIndex();
            playSong();
        });

        HBox controls = new HBox(10, playBtn, pauseBtn, stopBtn, prevBtn, nextBtn);
        VBox layout = new VBox(10, importBtn, listView, controls, new Label("Volume"), volumeSlider);

        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void importSongs(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Music Files");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if (files != null) {
            songList.addAll(files);
            for (File file : files) {
                listView.getItems().add(file.getName());
            }
        }
    }

    private void playSong() {
        try {
            if (currentIndex < 0 && songList.size() > 0) {
                currentIndex = 0;
            }

            if (currentIndex >= 0) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }

                File file = songList.get(currentIndex);
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

