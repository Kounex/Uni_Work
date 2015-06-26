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
 *
 * Class to realise the main app. GUI based on JavaFX
 *
 * List<IngameItem> allIngameItems: all items actively used for bidding
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
        topImage.setPadding(new Insets(240,0,0,0));
        topImage.setStyle("-fx-background-image: url('insanityFlyff/images/insanity_logo.PNG');-fx-background-size: auto");

        ListView<IngameItem> itemListView = new ListView<>();
        itemListView.setItems(FXCollections.observableList(this.allIngameItems));

        Button addItemButton = new Button();
        addItemButton.setText("Add new Item");
        addItemButton.setOnAction(e -> {
            Stage addItemStage = new Stage();

            BorderPane borderPaneAddItem = new BorderPane();
            GridPane gridPaneAddItem = new GridPane();
        });

        Button deleteItemButton = new Button();
        deleteItemButton.setText("Delete Item");
        deleteItemButton.setOnAction(e -> {
            this.allIngameItems.remove(itemListView.getSelectionModel().getSelectedItem());
            itemListView.setItems(FXCollections.observableList(this.allIngameItems));
        });

        VBox vboxBottom = new VBox();
        //vboxBottom.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%)");
        //vboxBottom.setStyle("-fx-background-color: linear-gradient(#C8DCA6 0%, #DBFFC0 25%, #E0FFCF 75%, #FFFFFF 100%)");
        vboxBottom.setStyle("-fx-background-image: url('insanityFlyff/images/insanity_sidebar.png');-fx-background-size: cover");
        vboxBottom.setSpacing(15);
        vboxBottom.setPadding(new Insets(50,10,10,10));
        vboxBottom.getChildren().add(addItemButton);
        vboxBottom.getChildren().add(deleteItemButton);

        borderPane.setTop(topImage);
        borderPane.setLeft(vboxBottom);
        borderPane.setCenter(itemListView);

        Scene scene = new Scene(borderPane,680,800);

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
