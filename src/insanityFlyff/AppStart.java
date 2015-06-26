package insanityFlyff;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kounex on 26.06.15.
 */
public class AppStart extends Application {

    List<IngameItem> allIngameItems = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: crimson");

        Button addItemButton = new Button();
        addItemButton.setText("Add new Item");

        Button deleteItemButton = new Button();
        deleteItemButton.setText("Delete Item");

        VBox vboxBottom = new VBox();
        vboxBottom.getChildren().add(addItemButton);
        vboxBottom.getChildren().add(deleteItemButton);

        ListView<IngameItem> itemListView = new ListView<>();

        borderPane.setLeft(vboxBottom);
        borderPane.setCenter(itemListView);

        Scene scene = new Scene(borderPane,800,600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
