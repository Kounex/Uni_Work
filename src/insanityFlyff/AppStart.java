package insanityFlyff;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
        /**
         * Filling the list by hand for testing purpose
         */
        IngameItem item1 = new IngameItem("Legendary Golden Bow","");
        item1.addOffer(2,0,"Kounex");
        this.allIngameItems.add(item1);

        BorderPane borderPane = new BorderPane();

        HBox topImage = new HBox();
        topImage.setPadding(new Insets(150,0,0,0));
        topImage.setStyle("-fx-background-image: url('insanity_logo.PNG');-fx-background-size: cover");

        ListView<IngameItem> itemListView = new ListView<>();
        itemListView.setItems(FXCollections.observableList(this.allIngameItems));

        Button addItemButton = new Button();
        addItemButton.setText("Add new Item");

        Button deleteItemButton = new Button();
        deleteItemButton.setText("Delete Item");
        deleteItemButton.setOnAction(e -> {
            this.allIngameItems.remove(itemListView.getSelectionModel().getSelectedItem());
            itemListView.setItems(FXCollections.observableList(this.allIngameItems));
        });

        VBox vboxBottom = new VBox();
        vboxBottom.getChildren().add(addItemButton);
        vboxBottom.getChildren().add(deleteItemButton);

        borderPane.setTop(topImage);
        borderPane.setLeft(vboxBottom);
        borderPane.setCenter(itemListView);

        Scene scene = new Scene(borderPane,800,600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void refreshItemList(List<IngameItem> allIngameItems) {
        this.allIngameItems = allIngameItems;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
