package insanityFlyff;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
    ListView<Offer> itemOffersListView = new ListView<>();
    ListView<Offer> itemShopHistoryView = new ListView<>();
    Label imageNameLabel = new Label();
    ImageView itemImage;
    String defaultImagePath = "insanityFlyff/images/404-not-found.jpg";
    String imagePathSelected = defaultImagePath;
    boolean conditionMet;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Insanity Flyff - Offer Control");
        LoadSaveItems.loadObject(this.allIngameItems, this);
        primaryStage.setOnCloseRequest(c -> {
            LoadSaveItems.saveObject(this.allIngameItems);
        });

        /**
         * ListView settings
         */

        itemListView.setStyle("-fx-font-weight: bold; -fx-font-size: 16");

        itemOffersListView.setStyle("-fx-font-weight: bold;-fx-font-size: 16");

        itemListView.setOnMouseClicked(c -> {
            if (c.getButton() == MouseButton.PRIMARY) {
                if (c.getClickCount() == 2) {
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
        addItemButton.setPrefWidth(150);
        addItemButton.setOnAction(e -> addItemStage());

        Button deleteItemButton = new Button();
        deleteItemButton.setText("Delete Item");
        deleteItemButton.setPrefWidth(150);
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
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * New stage for adding a new auction item to the list ~ called via addItem Button
     */
    private void addItemStage() {
        Stage addItemStage = new Stage();
        addItemStage.setTitle("Add new item");

        BorderPane borderPaneAddItem = new BorderPane();

        Label itemNameLabel = new Label();
        itemNameLabel.setText("Item Name");
        itemNameLabel.setStyle("-fx-font-weight: bold");

        Label itemAmountLabel = new Label();
        itemAmountLabel.setText("Amount");
        itemAmountLabel.setStyle("-fx-font-weight: bold");

        TextField itemNameTextField = new TextField();
        itemNameTextField.setPrefWidth(250);

        TextField itemAmountTextField = new TextField();
        itemAmountTextField.setMaxWidth(50);

        CheckBox checkBoxAuction = new CheckBox();
        checkBoxAuction.setText("Auction?");

        Button selectItemImage = new Button();
        selectItemImage.setText("Select Image");
        selectItemImage.setOnAction(e -> {
            String imageName = this.addImageViaFileChooser();
            if (!imageName.equals("None selected")) {
                imageNameLabel.setText("Image name:\n" + imageName);
            } else {
                imageNameLabel.setText("Image name:\n None selected");
            }
            imageNameLabel.setStyle("-fx-font-weight: bold; -fx-font-style: oblique");
        });

        HBox hboxForImageButton = new HBox();
        hboxForImageButton.setPadding(new Insets(47, 0, 0, 0));
        hboxForImageButton.getChildren().add(selectItemImage);

        VBox vboxForImageButton = new VBox();
        vboxForImageButton.getChildren().addAll(hboxForImageButton, imageNameLabel);

        Button createItemAdd = new Button();
        createItemAdd.setText("Create");
        createItemAdd.setOnAction(a -> {
            conditionMet = true;
            this.itemListView.getItems().forEach(c -> {
                if (itemNameTextField.getText().equals(c.getItemName())) {
                    noticeMessageBox("Error", "This item already exists! Please specify another name or use the item which is already in the list.");
                    conditionMet = false;
                }
            });
            if (!itemNameTextField.getText().isEmpty() && !itemAmountTextField.getText().isEmpty() && conditionMet) {
                this.allIngameItems.add(new IngameItem(Integer.parseInt(itemAmountTextField.getText()), checkBoxAuction.isSelected(), itemNameTextField.getText(), imagePathSelected));
                this.refreshItemList();
                addItemStage.close();
            }
        });

        HBox hboxforSendAndCheckBox = new HBox();
        hboxforSendAndCheckBox.setSpacing(20);
        hboxforSendAndCheckBox.getChildren().addAll(itemAmountTextField, checkBoxAuction);

        VBox vboxInsideHboxItemAddCenter = new VBox();
        vboxInsideHboxItemAddCenter.setSpacing(10);
        vboxInsideHboxItemAddCenter.setPadding(new Insets(20, 0, 0, 20));
        vboxInsideHboxItemAddCenter.getChildren().addAll(itemNameLabel, itemNameTextField, itemAmountLabel, hboxforSendAndCheckBox);

        HBox hboxItemAddCenter = new HBox();
        hboxItemAddCenter.setSpacing(30);
        hboxItemAddCenter.getChildren().addAll(vboxInsideHboxItemAddCenter, vboxForImageButton);

        HBox hboxItemAddBottom = new HBox();
        hboxItemAddBottom.getChildren().add(createItemAdd);
        hboxItemAddBottom.setPadding(new Insets(0, 0, 20, 120));
        //hboxItemAddBottom.setAlignment(Pos.CENTER);

        borderPaneAddItem.setCenter(hboxItemAddCenter);
        borderPaneAddItem.setBottom(hboxItemAddBottom);

        Scene sceneItemAdd = new Scene(borderPaneAddItem,550,200);

        addItemStage.setScene(sceneItemAdd);
        addItemStage.setResizable(false);
        addItemStage.show();
    }

    private void showItemStage() {
        Stage showAuctionItemStage = new Stage();
        showAuctionItemStage.setTitle("Current Offers");

        BorderPane borderPaneShowItem = new BorderPane();

        IngameItem currentItem = this.itemListView.getSelectionModel().getSelectedItem();


        /**
         * If-clause to check if the URL still leads to a legit picture, otherwise show the 404 picture
         */
        this.itemImage = new ImageView(new Image(currentItem.getImageURL()));

        if(this.itemImage.getImage().getHeight()==0) {
            this.itemImage = new ImageView(new Image(this.defaultImagePath));
        }

        Button changeItemImage = new Button();
        changeItemImage.setText("Change Image");
        changeItemImage.setOnAction(e -> {
            String imageName = this.addImageViaFileChooser();
            if (!imageName.equals("None selected")) {
                currentItem.updateImageURL(this.imagePathSelected);
            } else {
                currentItem.updateImageURL(this.defaultImagePath);
            }
            this.itemImage = new ImageView(new Image(currentItem.getImageURL()));
            showAuctionItemStage.close();
            this.showItemStage();
        });

        Button deleteItemImage = new Button();
        deleteItemImage.setText("Delete image");
        deleteItemImage.setOnAction(a -> {
            currentItem.updateImageURL(this.defaultImagePath);
            this.itemImage = new ImageView(new Image(currentItem.getImageURL()));
            showAuctionItemStage.close();
            this.showItemStage();
        });

        HBox hboxButtonsUnderImageLeft = new HBox();
        hboxButtonsUnderImageLeft.setSpacing(15);
        hboxButtonsUnderImageLeft.setAlignment(Pos.CENTER);
        hboxButtonsUnderImageLeft.getChildren().addAll(changeItemImage, deleteItemImage);

        VBox vboxItemImageLeft = new VBox();
        vboxItemImageLeft.setAlignment(Pos.CENTER);
        vboxItemImageLeft.setPadding(new Insets(0, 0, 0, 40));
        vboxItemImageLeft.setSpacing(25);
        vboxItemImageLeft.getChildren().addAll(itemImage, hboxButtonsUnderImageLeft);

        if(currentItem.getAuctionState()) {
            Button addOfferToItem = new Button();
            addOfferToItem.setText("Add offer");
            addOfferToItem.setOnAction(a -> {
                addOfferToItemStage(currentItem);
            });

            Button deleteOfferFromItem = new Button();
            deleteOfferFromItem.setText("Delete offer");
            deleteOfferFromItem.setOnAction(e -> {
                currentItem.removeOffer(this.itemOffersListView.getSelectionModel().getSelectedItem());
                this.refreshItemOfferList(currentItem);
            });

            this.refreshItemOfferList(currentItem);

            HBox hboxItemShowCenterListView = new HBox();
            itemOffersListView.setPrefWidth(itemImage.getImage().getWidth()+200);
            hboxItemShowCenterListView.setPrefHeight(this.itemImage.getImage().getHeight());
            hboxItemShowCenterListView.getChildren().add(this.itemOffersListView);

            HBox hboxItemShowCenterButtons = new HBox();
            hboxItemShowCenterButtons.setPadding(new Insets(25, 0, 0, 220));
            hboxItemShowCenterButtons.setSpacing(30);
            hboxItemShowCenterButtons.getChildren().addAll(addOfferToItem, deleteOfferFromItem);

            VBox vboxItemShowCenter = new VBox();
            vboxItemShowCenter.setPadding(new Insets(30, 0, 0, 30));
            vboxItemShowCenter.getChildren().addAll(hboxItemShowCenterListView, hboxItemShowCenterButtons);
            borderPaneShowItem.setCenter(vboxItemShowCenter);
        } else {
//            HBox amountChange
        }

        borderPaneShowItem.setLeft(vboxItemImageLeft);
        Scene sceneShowItem = new Scene(borderPaneShowItem,itemImage.getImage().getWidth()+700,itemImage.getImage().getHeight()+100);
        showAuctionItemStage.setScene(sceneShowItem);
        showAuctionItemStage.setResizable(false);
        showAuctionItemStage.show();
    }

    private void addOfferToItemStage(IngameItem currentItem) {
        Stage addOfferStage = new Stage();
        addOfferStage.setTitle("Add offer");

        BorderPane borderPaneAddOffer = new BorderPane();

        Label perinLabel = new Label();
        perinLabel.setPrefWidth(90);
        perinLabel.setText("Perin: ");
        perinLabel.setStyle("-fx-font-weight: bold");

        Label penyaLabel = new Label();
        penyaLabel.setPrefWidth(90);
        penyaLabel.setText("Penya: ");
        penyaLabel.setStyle("-fx-font-weight: bold");

        Label tradeItemLabel = new Label();
        tradeItemLabel.setPrefWidth(90);
        tradeItemLabel.setText("Trade items: ");
        tradeItemLabel.setStyle("-fx-font-weight: bold");

        Label bidderNameLabel = new Label();
        bidderNameLabel.setPrefWidth(90);
        bidderNameLabel.setText("Player name: ");
        bidderNameLabel.setStyle("-fx-font-weight: bold");

        TextField perinTextField = new TextField();
        perinTextField.setPrefWidth(350);

        TextField penyaTextField = new TextField();
        penyaTextField.setPrefWidth(350);

        TextField tradeItemTextField = new TextField();
        tradeItemTextField.setPrefWidth(350);

        TextField bidderNameTextField = new TextField();
        bidderNameTextField.setPrefWidth(350);

        Button saveOffer = new Button();
        saveOffer.setText("Save");
        saveOffer.setOnAction(e -> {
            if (!perinTextField.getText().isEmpty() && !penyaTextField.getText().isEmpty() && !bidderNameTextField.getText().isEmpty()) {
                currentItem.addOffer(Integer.parseInt(perinTextField.getText()), Integer.parseInt(perinTextField.getText()), tradeItemTextField.getText(), bidderNameTextField.getText());
                this.refreshItemOfferList(currentItem);
                addOfferStage.close();
            }
        });

        HBox hboxAddOfferFirstLine = new HBox();
        hboxAddOfferFirstLine.setSpacing(5);
        hboxAddOfferFirstLine.getChildren().addAll(perinLabel, perinTextField);

        HBox hboxAddOfferSecondLine = new HBox();
        hboxAddOfferSecondLine.setSpacing(5);
        hboxAddOfferSecondLine.getChildren().addAll(penyaLabel, penyaTextField);

        HBox hboxAddOfferThirdLine = new HBox();
        hboxAddOfferThirdLine.setSpacing(5);
        hboxAddOfferThirdLine.getChildren().addAll(tradeItemLabel, tradeItemTextField);

        HBox hboxAddOfferFourthLine = new HBox();
        hboxAddOfferFourthLine.setSpacing(5);
        hboxAddOfferFourthLine.getChildren().addAll(bidderNameLabel, bidderNameTextField);

        HBox hboxAddOfferButton = new HBox();
        hboxAddOfferButton.setPadding(new Insets(0, 0, 0, 120));
        hboxAddOfferButton.getChildren().add(saveOffer);

        VBox vboxAddOfferCenter = new VBox();
        vboxAddOfferCenter.setSpacing(10);
        vboxAddOfferCenter.setPadding(new Insets(20, 0, 0, 20));
        vboxAddOfferCenter.getChildren().addAll(hboxAddOfferFirstLine, hboxAddOfferSecondLine, hboxAddOfferThirdLine, hboxAddOfferFourthLine, hboxAddOfferButton);

        borderPaneAddOffer.setCenter(vboxAddOfferCenter);

        Scene addOfferScene = new Scene(borderPaneAddOffer,500,200);
        addOfferStage.setScene(addOfferScene);
        addOfferStage.setResizable(false);
        addOfferStage.show();
    }

    private void noticeMessageBox(String title, String message) {
        Stage noticeMessageStage = new Stage();
        noticeMessageStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane borderPaneMessageBox = new BorderPane();

        noticeMessageStage.setTitle(title);

        Text messageText = new Text();
        messageText.setWrappingWidth(200);
        messageText.setText(message);

        Button closeNoticeMessageButton = new Button();
        closeNoticeMessageButton.setText("OK");
        closeNoticeMessageButton.setPrefWidth(100);
        closeNoticeMessageButton.setOnAction(e -> noticeMessageStage.close());

        VBox vboxTextAndButton = new VBox();
        vboxTextAndButton.setSpacing(10);
        vboxTextAndButton.setAlignment(Pos.CENTER);
        vboxTextAndButton.getChildren().addAll(messageText,closeNoticeMessageButton);

        borderPaneMessageBox.setCenter(vboxTextAndButton);

        noticeMessageStage.setScene(new Scene(borderPaneMessageBox, 250, message.length()));
        noticeMessageStage.show();
    }

    private String addImageViaFileChooser() {
        FileChooser fileChooser = new FileChooser();
        File chosenFile = fileChooser.showOpenDialog(new Stage());
        if(chosenFile!=null) {
            try {
                this.imagePathSelected = "file:///"+chosenFile.getCanonicalPath();
                return chosenFile.getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("lol");
        this.imagePathSelected = this.defaultImagePath;
        return "None selected";
    }

    /**
     * Everytime something is added/deleted from the ListView, call this method to see the change~
     */
    public void refreshItemList() {
        itemListView.setItems(FXCollections.observableList(this.allIngameItems));
    }

    private void refreshItemOfferList(IngameItem currentItem) {
        itemOffersListView.setItems(FXCollections.observableList(currentItem.getOfferList()));
    }

    public void transmitItemList(List<IngameItem> allIngameItems) {
        this.allIngameItems = allIngameItems;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
