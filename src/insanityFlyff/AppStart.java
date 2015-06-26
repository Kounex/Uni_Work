package insanityFlyff;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
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
    ListView<IngameItem> itemListView = new ListView<>();
    Label imageNameLabel = new Label();
    String imagePathSelected;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Insanity Flyff - Offer Collection");
        LoadSaveItems.loadObject(this.allIngameItems,this);
        primaryStage.setOnCloseRequest(c -> {
            LoadSaveItems.saveObject(this.allIngameItems);
        });

        itemListView.setOnMouseClicked(c -> {
            if(c.getButton() == MouseButton.PRIMARY) {
                if(c.getClickCount() == 2) {
                    showItemStage();
                }
            }
        });

        BorderPane borderPane = new BorderPane();

        HBox topImage = new HBox();
        topImage.setPadding(new Insets(240, 0, 0, 0));
        topImage.setStyle("-fx-background-image: url('insanityFlyff/images/insanity_logo.PNG');-fx-background-size: auto");


        this.refreshItemList();

        Button addItemButton = new Button();
        addItemButton.setText("Add new Item");
        addItemButton.setOnAction(e -> addItemStage());

        Button deleteItemButton = new Button();
        deleteItemButton.setText("Delete Item");
        deleteItemButton.setOnAction(e -> {
            this.allIngameItems.remove(itemListView.getSelectionModel().getSelectedItem());
            this.refreshItemList();
        });

        VBox vboxBottom = new VBox();
        //vboxBottom.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%)");
        //vboxBottom.setStyle("-fx-background-color: linear-gradient(#C8DCA6 0%, #DBFFC0 25%, #E0FFCF 75%, #FFFFFF 100%)");
        vboxBottom.setStyle("-fx-background-image: url('insanityFlyff/images/insanity_sidebar.png');-fx-background-size: cover");
        vboxBottom.setSpacing(15);
        vboxBottom.setPadding(new Insets(50, 10, 10, 10));
        vboxBottom.getChildren().add(addItemButton);
        vboxBottom.getChildren().add(deleteItemButton);

        borderPane.setTop(topImage);
        borderPane.setLeft(vboxBottom);
        borderPane.setCenter(itemListView);

        Scene scene = new Scene(borderPane,680,800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showItemStage() {
        Stage showItemStage = new Stage();

        BorderPane borderPaneShowItem = new BorderPane();

        IngameItem currentItem = itemListView.getSelectionModel().getSelectedItem();

        ImageView itemImage = new ImageView(new Image(currentItem.getImageURL()));

        VBox vboxItemImageLeft = new VBox();
        vboxItemImageLeft.getChildren().add(itemImage);

        borderPaneShowItem.setLeft(vboxItemImageLeft);

        Scene sceneShowItem = new Scene(borderPaneShowItem,500,500);
        showItemStage.setScene(sceneShowItem);
        showItemStage.show();
    }

    /**
     * New stage for adding a new item to the list ~ called via addItem Button
     */
    private void addItemStage() {
        Stage addItemStage = new Stage();
        addItemStage.setTitle("Add new item");

        BorderPane borderPaneAddItem = new BorderPane();

        Label itemNameLabel = new Label();
        itemNameLabel.setText("Item Name");
        itemNameLabel.setStyle("-fx-font-weight: bold");

        TextField itemNameTextField = new TextField();
        itemNameTextField.setPrefWidth(250);

        Button selectItemImage = new Button();
        selectItemImage.setText("Select Image");
        selectItemImage.setOnAction(e -> {
            String imageName = addImageViaFileChooser();
            if(imageName!=null) {
                imageNameLabel.setText("Image name:\n" + imageName);
            }else{
                imageNameLabel.setText("Image name:\n None selected");
            }
            imageNameLabel.setStyle("-fx-font-weight: bold; -fx-font-style: oblique");
        });

        HBox hboxForImageButton = new HBox();
        hboxForImageButton.setPadding(new Insets(47, 0, 0, 0));
        hboxForImageButton.getChildren().add(selectItemImage);

        VBox vboxForImageButton = new VBox();
        vboxForImageButton.getChildren().addAll(hboxForImageButton, imageNameLabel);

        Button sendItemAdd = new Button();
        sendItemAdd.setText("Send");
        sendItemAdd.setOnAction(a -> {
            if (!itemNameTextField.getText().isEmpty()) {
                this.allIngameItems.add(new IngameItem(itemNameTextField.getText(), imagePathSelected));
                this.refreshItemList();
                addItemStage.close();
            }
        });

        VBox vboxInsideHboxItemAddCenter = new VBox();
        vboxInsideHboxItemAddCenter.setSpacing(10);
        vboxInsideHboxItemAddCenter.setPadding(new Insets(20, 0, 0, 20));
        vboxInsideHboxItemAddCenter.getChildren().addAll(itemNameLabel, itemNameTextField);

        HBox hboxItemAddCenter = new HBox();
        hboxItemAddCenter.setSpacing(30);
        hboxItemAddCenter.getChildren().addAll(vboxInsideHboxItemAddCenter, vboxForImageButton);

        HBox hboxItemAddBottom = new HBox();
        hboxItemAddBottom.getChildren().add(sendItemAdd);
        hboxItemAddBottom.setPadding(new Insets(0, 0, 20, 0));
        hboxItemAddBottom.setAlignment(Pos.CENTER);

        borderPaneAddItem.setCenter(hboxItemAddCenter);
        borderPaneAddItem.setBottom(hboxItemAddBottom);

        Scene sceneItemAdd = new Scene(borderPaneAddItem,500,140);

        addItemStage.setScene(sceneItemAdd);
        addItemStage.setResizable(false);
        addItemStage.show();
    }

    public String addImageViaFileChooser() {
        FileChooser fileChooser = new FileChooser();
        File chosenFile = fileChooser.showOpenDialog(new Stage());
        if(chosenFile!=null) {
            try {
                imagePathSelected = "file:///"+chosenFile.getCanonicalPath();
                return chosenFile.getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Everytime something is added/deleted from the ListView, call this method to see the change~
     */
    public void refreshItemList() {
        itemListView.setItems(FXCollections.observableList(this.allIngameItems));
    }

    public void transmitItemList(List<IngameItem> allIngameItems) {
        this.allIngameItems = allIngameItems;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
