package insanityFlyff;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kounex on 26.06.15.
 *
 * Class to realise the main app. GUI based on JavaFX
 *
 * List<IngameItem> allIngameItems: all items actively used for bidding
 * ListView<IngameItem> itemListView: the list presenting allIngameItems (for scene)
 */
public class AppStart extends Application {

    List<IngameItem> allIngameItems = new ArrayList<>();
    ListView<IngameItem> itemListView = new ListView<>();
//    TableView<Offer> itemOffersListView = new TableView<>();
    ListView<Offer> itemOffersListView = new ListView<>();
    ListView<SellHistory> itemShopHistoryView = new ListView<>();
    Label imageNameLabel = new Label();
    ImageView itemImage;
    String defaultImagePath = "insanityFlyff/images/404-not-found.jpg";
    String imagePathSelected = defaultImagePath;
    boolean conditionMet;
    Label totalPerinAmountLabel = new Label("0");
    Label totalPenyaAmountLabel = new Label("0");
    ListView totalTradeItemsListView = new ListView();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Insanity Flyff - Shop Control");
        LoadSaveItems.loadObject(this.allIngameItems, this);
        primaryStage.setOnCloseRequest(c -> {
            LoadSaveItems.saveObject(this.allIngameItems);
        });

        /**
         * ListView settings
         */
        this.itemListView.setStyle("-fx-font-weight: bold;-fx-font-size: 16");
        this.itemOffersListView.setStyle("-fx-font-weight: bold");
        this.totalTradeItemsListView.setStyle("-fx-font-weight: bold");
        this.itemShopHistoryView.setStyle("-fx-font-weight: bold;-fx-font-size: 14");

        /**
         * Total Penya/Perin Labels settings
         */
        this.totalPerinAmountLabel.setStyle("-fx-text-fill: blue");
        this.totalPenyaAmountLabel.setStyle("-fx-text-fill: blue");

        /**
         * The SimpleStringProperty which would be used in order to correctly use the TableView are not
         * serializable, therefore it cannot be saved. Thats why ListView is going to be used again
         */

//        TableColumn perinCol = new TableColumn("Perin");
//        perinCol.setMinWidth(100);
//        perinCol.setCellValueFactory(
//                new PropertyValueFactory<Offer, String>("perin"));
//
//        TableColumn penyaCol = new TableColumn("Penya");
//        penyaCol.setMinWidth(100);
//        penyaCol.setCellValueFactory(
//                new PropertyValueFactory<Offer, String>("penya"));
//
//        TableColumn tradeCol = new TableColumn("Trade");
//        tradeCol.setMinWidth(100);
//        tradeCol.setCellValueFactory(
//                new PropertyValueFactory<Offer, String>("tradeItems"));
//
//        TableColumn bidderNameCol = new TableColumn("Bidder");
//        bidderNameCol.setMinWidth(100);
//        bidderNameCol.setCellValueFactory(
//                new PropertyValueFactory<Offer, String>("bidderName"));
//
//        this.itemOffersListView.getColumns().addAll(perinCol, penyaCol, tradeCol, bidderNameCol);

        this.itemListView.setOnMouseClicked(c -> {
            if (c.getButton() == MouseButton.PRIMARY) {
                if (c.getClickCount() == 2) {
                    showItemStage();
                }
            }
        });

        BorderPane borderPane = new BorderPane();

        Label totalEarningsHeadlineLabel = new Label();
        totalEarningsHeadlineLabel.setText("Total earnings");
        totalEarningsHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-underline: true");

        Label totalPerinAmountheadlineLabel = new Label();
        totalPerinAmountheadlineLabel.setText("Perin");
        totalPerinAmountheadlineLabel.setStyle("-fx-font-weight: bold");

        Label totalPenyaAmountheadlineLabel = new Label();
        totalPenyaAmountheadlineLabel.setText("Penya");
        totalPenyaAmountheadlineLabel.setStyle("-fx-font-weight: bold");

        Label madeBy = new Label();
        madeBy.setText("Coded by, Kounex");
        madeBy.setStyle("-fx-font-weight: bolder;-fx-font-family: monospace");

        HBox topImage = new HBox();
        topImage.setPadding(new Insets(240, 0, 0, 0));
        topImage.setStyle("-fx-background-image: url('insanityFlyff/images/insanity_logo.PNG');-fx-background-size: auto");


        this.refreshItemList();

        Button addItemButton = new Button();
        addItemButton.setText("Add new Item");
        addItemButton.setPrefWidth(150);
        addItemButton.setOnAction(e -> addItemStage());

        Button renameItemButton = new Button();
        renameItemButton.setText("Rename item");
        renameItemButton.setPrefWidth(150);
        renameItemButton.setOnAction(e -> {
            if (this.itemListView.getSelectionModel().getSelectedItems() != null) {
                renameItem(this.itemListView.getSelectionModel().getSelectedItem());
            }
        });

        Button deleteItemButton = new Button();
        deleteItemButton.setText("Delete Item");
        deleteItemButton.setPrefWidth(150);
        deleteItemButton.setOnAction(e -> {
            Stage deleteItemStage = new Stage();
            deleteItemStage.setResizable(false);
            deleteItemStage.initModality(Modality.APPLICATION_MODAL);
            deleteItemStage.setTitle("Warning");

            BorderPane borderPaneDeleteItem = new BorderPane();
            //borderPaneDeleteAllItems.setPadding(new Insets(10, 0, 0, 0));

            Text deleteItemText = new Text("Are you sure you want to delete the selected item? This action can't be undone!");
            deleteItemText.setWrappingWidth(225);

            Button deleteItemYesButton = new Button();
            deleteItemYesButton.setText("Yes");
            deleteItemYesButton.setPrefWidth(75);
            deleteItemYesButton.setOnAction(a -> {
                this.allIngameItems.remove(this.itemListView.getSelectionModel().getSelectedItem());
                this.refreshItemList();
                deleteItemStage.close();
            });

            Button deleteItemNoButton = new Button();
            deleteItemNoButton.setText("No");
            deleteItemNoButton.setPrefWidth(75);
            deleteItemNoButton.setOnAction(a -> {
                deleteItemStage.close();
            });

            HBox hboxForButtons = new HBox();
            hboxForButtons.setSpacing(15);
            hboxForButtons.setAlignment(Pos.CENTER);
            hboxForButtons.getChildren().addAll(deleteItemYesButton, deleteItemNoButton);

            VBox vboxForTextAndButtons = new VBox();
            vboxForTextAndButtons.setSpacing(20);
            vboxForTextAndButtons.setPadding(new Insets(15, 15, 0, 15));
            vboxForTextAndButtons.getChildren().addAll(deleteItemText, hboxForButtons);

            borderPaneDeleteItem.setCenter(vboxForTextAndButtons);
            deleteItemStage.setScene(new Scene(borderPaneDeleteItem, 250, 125));
            deleteItemStage.show();
        });

        Button deleteAllItemsButton = new Button();
        deleteAllItemsButton.setText("Delete all");
        deleteAllItemsButton.setPrefWidth(150);
        deleteAllItemsButton.setOnAction(e -> {
            Stage deleteAllItemsStage = new Stage();
            deleteAllItemsStage.initModality(Modality.APPLICATION_MODAL);
            deleteAllItemsStage.setTitle("Warning");

            BorderPane borderPaneDeleteAllItems = new BorderPane();
            //borderPaneDeleteAllItems.setPadding(new Insets(10, 0, 0, 0));

            Text deleteAllItemsText = new Text("Are you sure you want to delete all items? This action can't be undone!");
            deleteAllItemsText.setWrappingWidth(225);

            Button deleteAllItemsYesButton = new Button();
            deleteAllItemsYesButton.setText("Yes");
            deleteAllItemsYesButton.setPrefWidth(75);
            deleteAllItemsYesButton.setOnAction(a -> {
                this.allIngameItems.removeAll(allIngameItems);
                this.refreshItemList();
                deleteAllItemsStage.close();
            });

            Button deleteAllItemsNoButton = new Button();
            deleteAllItemsNoButton.setText("No");
            deleteAllItemsNoButton.setPrefWidth(75);
            deleteAllItemsNoButton.setOnAction(a -> {
                deleteAllItemsStage.close();
            });

            HBox hboxForButtons = new HBox();
            hboxForButtons.setSpacing(15);
            hboxForButtons.setAlignment(Pos.CENTER);
            hboxForButtons.getChildren().addAll(deleteAllItemsYesButton, deleteAllItemsNoButton);

            VBox vboxForTextAndButtons = new VBox();
            vboxForTextAndButtons.setSpacing(20);
            vboxForTextAndButtons.setPadding(new Insets(15, 15, 0, 15));
            vboxForTextAndButtons.getChildren().addAll(deleteAllItemsText, hboxForButtons);

            borderPaneDeleteAllItems.setCenter(vboxForTextAndButtons);
            deleteAllItemsStage.setScene(new Scene(borderPaneDeleteAllItems, 250, 100));
            deleteAllItemsStage.show();
        });

        Button showTotalTradeItemsButton = new Button();
        showTotalTradeItemsButton.setText("Trade items");
        showTotalTradeItemsButton.setPrefWidth(150);
        showTotalTradeItemsButton.setOnAction(e -> {
            Stage showTotalTradeItemsStage = new Stage();
            showTotalTradeItemsStage.setTitle("All trade items");
            showTotalTradeItemsStage.initModality(Modality.APPLICATION_MODAL);
            showTotalTradeItemsStage.setResizable(false);

            BorderPane borderPaneTotalTradeItems = new BorderPane();
            borderPaneTotalTradeItems.setStyle("-fx-background-image: url('insanityFlyff/images/31725.jpg')");
            borderPaneTotalTradeItems.setPadding(new Insets(25,25,25,25));

            List<String> localAllTradesList = new ArrayList<>();
            for(IngameItem ing:this.allIngameItems) {
                if(ing.getOfferWon()!=null) {
                    if(!ing.getOfferWon().getTradeItems().isEmpty()) {
                        localAllTradesList.add("Got:\t\t" + ing.getOfferWon().getTradeItems() + "\nSold:\t" + ing.getItemName() + "\nTo:\t\t" + ing.getOfferWon().getBidderName() + "\t[" + ing.getOfferWon().getDateOfferAccepted() + "]");
                    }
                }
            }
            this.totalTradeItemsListView.setItems(FXCollections.observableList(localAllTradesList));

            Button closeTotalTradeItems = new Button();
            closeTotalTradeItems.setText("Close");
            closeTotalTradeItems.setOnAction(a -> {
                showTotalTradeItemsStage.close();
            });

            VBox vboxTotalTradeItems = new VBox();
            vboxTotalTradeItems.setSpacing(25);
            vboxTotalTradeItems.setAlignment(Pos.CENTER);
            vboxTotalTradeItems.getChildren().addAll(this.totalTradeItemsListView, closeTotalTradeItems);

            borderPaneTotalTradeItems.setCenter(vboxTotalTradeItems);

            showTotalTradeItemsStage.setScene(new Scene(borderPaneTotalTradeItems, 500, 500));
            showTotalTradeItemsStage.show();
        });

        VBox vboxLeft = new VBox();
        //vboxBottom.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%)");
        //vboxBottom.setStyle("-fx-background-color: linear-gradient(#C8DCA6 0%, #DBFFC0 25%, #E0FFCF 75%, #FFFFFF 100%)");
        vboxLeft.setStyle("-fx-background-image: url('insanityFlyff/images/insanity_sidebar.png');-fx-background-size: cover");
        vboxLeft.setSpacing(15);
        vboxLeft.setPadding(new Insets(50, 10, 0, 10));
        VBox.setMargin(madeBy, new Insets(20,0,0,0));
        vboxLeft.getChildren().addAll(addItemButton, renameItemButton, deleteItemButton, deleteAllItemsButton,
                totalEarningsHeadlineLabel, totalPerinAmountheadlineLabel, this.totalPerinAmountLabel,
                totalPenyaAmountheadlineLabel, this.totalPenyaAmountLabel, showTotalTradeItemsButton, madeBy);

        VBox.setMargin(deleteAllItemsButton, new Insets(75, 0, 0, 0));

        borderPane.setTop(topImage);
        borderPane.setLeft(vboxLeft);
        borderPane.setCenter(this.itemListView);

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
                this.imageNameLabel.setText("Image name:\n" + imageName);
            } else {
                this.imageNameLabel.setText("Image name:\n None selected");
                this.imagePathSelected = this.defaultImagePath;
            }
            this.imageNameLabel.setStyle("-fx-font-weight: bold; -fx-font-style: oblique");
        });

        HBox hboxForImageButton = new HBox();
        hboxForImageButton.setPadding(new Insets(47, 0, 0, 0));
        hboxForImageButton.getChildren().add(selectItemImage);

        VBox vboxForImageButton = new VBox();
        vboxForImageButton.getChildren().addAll(hboxForImageButton, this.imageNameLabel);

        Button createItemAdd = new Button();
        createItemAdd.setText("Create");
        createItemAdd.setOnAction(a -> {
            this.conditionMet = true;
            this.itemListView.getItems().forEach(c -> {
                if (itemNameTextField.getText().equals(c.getItemName())) {
                    noticeMessageBox("Error", "This item already exists! Please specify another name or use the item which is already in the list.");
                    this.conditionMet = false;
                }
            });
            if (!itemNameTextField.getText().isEmpty() && !itemAmountTextField.getText().isEmpty() && this.conditionMet) {
                this.allIngameItems.add(new IngameItem(Integer.parseInt(itemAmountTextField.getText()), checkBoxAuction.isSelected(), itemNameTextField.getText(), imagePathSelected));
                this.imagePathSelected = defaultImagePath;
                this.refreshItemList();
                this.imageNameLabel.setText("");
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

        addItemStage.setOnCloseRequest(e -> {
            this.imageNameLabel.setText("");
        });

        addItemStage.setScene(sceneItemAdd);
        addItemStage.setResizable(false);
        addItemStage.show();
    }

    private void showItemStage() {
        Stage showAuctionItemStage = new Stage();

        BorderPane borderPaneShowItem = new BorderPane();
        borderPaneShowItem.setStyle("-fx-background-image: url('insanityFlyff/images/comm__flyff_screen_by_unrealsmoker-d5awftg.jpg');-fx-background-size: cover");

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
                this.imagePathSelected = defaultImagePath;
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
            showAuctionItemStage.setTitle(this.itemListView.getSelectionModel().getSelectedItem().getItemName() + " [Auction]");

            Text showItemAuctionSoldText = new Text();
            if(currentItem.getOfferWon()!=null) {
                showItemAuctionSoldText.setText("Sold for: " + currentItem.getOfferWon().toString());
            }
            showItemAuctionSoldText.setStyle("-fx-font-weight: bold;-fx-font-size: 18;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");

            Button addOfferToItem = new Button();
            addOfferToItem.setText("Add offer");
            addOfferToItem.setPrefWidth(100);
            addOfferToItem.setOnAction(a -> {
                if (currentItem.getOfferWon() == null) {
                    addOfferToItemStage(currentItem);
                }
            });

            Button deleteOfferFromItem = new Button();
            deleteOfferFromItem.setText("Delete offer");
            deleteOfferFromItem.setPrefWidth(100);
            deleteOfferFromItem.setOnAction(e -> {
                if(currentItem.getOfferWon()==null) {
                    currentItem.removeOffer(this.itemOffersListView.getSelectionModel().getSelectedItem());
                    this.refreshItemOfferList(currentItem);
                }
            });

            Button soldItemAuctionButton = new Button();
            soldItemAuctionButton.setText("Sold");
            soldItemAuctionButton.setPrefWidth(100);
            soldItemAuctionButton.setOnAction(e -> {
                if (currentItem.getOfferWon() == null && this.itemOffersListView.getSelectionModel().getSelectedItem() != null) {
                    Stage sellAuctionItemStage = new Stage();
                    sellAuctionItemStage.initModality(Modality.APPLICATION_MODAL);
                    sellAuctionItemStage.setTitle("Warning");

                    BorderPane borderPaneSellAuctionItem = new BorderPane();
                    //borderPaneDeleteAllItems.setPadding(new Insets(10, 0, 0, 0));

                    Text sellAuctionItemText = new Text("Are you sure you want to sell this item for the selected offer? This action can't be undone!");
                    sellAuctionItemText.setWrappingWidth(225);

                    Button sellAuctionItemYesButton = new Button();
                    sellAuctionItemYesButton.setText("Yes");
                    sellAuctionItemYesButton.setPrefWidth(75);
                    sellAuctionItemYesButton.setOnAction(a -> {
                        currentItem.updateAmountAvailable(0);
                        currentItem.updateOfferWon(this.itemOffersListView.getSelectionModel().getSelectedItem());
                        currentItem.getOfferWon().setDateOfferAccepted(LocalDate.now());
                        showItemAuctionSoldText.setText("Sold for: " + currentItem.getOfferWon().toString());
                        this.refreshItemList();
                        sellAuctionItemStage.close();
                    });

                    Button sellAuctionItemNoButton = new Button();
                    sellAuctionItemNoButton.setText("No");
                    sellAuctionItemNoButton.setPrefWidth(75);
                    sellAuctionItemNoButton.setOnAction(a -> {
                        sellAuctionItemStage.close();
                    });

                    HBox hboxForButtons = new HBox();
                    hboxForButtons.setSpacing(15);
                    hboxForButtons.setAlignment(Pos.CENTER);
                    hboxForButtons.getChildren().addAll(sellAuctionItemYesButton, sellAuctionItemNoButton);

                    VBox vboxForTextAndButtons = new VBox();
                    vboxForTextAndButtons.setSpacing(20);
                    vboxForTextAndButtons.setPadding(new Insets(15, 15, 0, 15));
                    vboxForTextAndButtons.getChildren().addAll(sellAuctionItemText, hboxForButtons);

                    borderPaneSellAuctionItem.setCenter(vboxForTextAndButtons);
                    sellAuctionItemStage.setScene(new Scene(borderPaneSellAuctionItem, 250, 150));
                    sellAuctionItemStage.show();
                }
            });

            this.refreshItemOfferList(currentItem);

            HBox hboxItemShowOfferListView = new HBox();
            hboxItemShowOfferListView.setPrefHeight(this.itemImage.getImage().getHeight());
            hboxItemShowOfferListView.getChildren().add(this.itemOffersListView);

            HBox hboxItemShowCenterButtons = new HBox();
            hboxItemShowCenterButtons.setPadding(new Insets(0, 0, 25, 100));
            hboxItemShowCenterButtons.setSpacing(30);
            hboxItemShowCenterButtons.getChildren().addAll(addOfferToItem, deleteOfferFromItem, soldItemAuctionButton);

            VBox vboxItemShowCenter = new VBox();
            vboxItemShowCenter.setPadding(new Insets(30, 0, 0, 30));
            VBox.setMargin(showItemAuctionSoldText, new Insets(15,0,40,0));
            vboxItemShowCenter.getChildren().addAll(hboxItemShowOfferListView, showItemAuctionSoldText, hboxItemShowCenterButtons);
            borderPaneShowItem.setCenter(vboxItemShowCenter);
        } else {
            showAuctionItemStage.setTitle(this.itemListView.getSelectionModel().getSelectedItem().getItemName() + " [Shop]");

            this.itemShopHistoryView.setItems(FXCollections.observableList(currentItem.getSellHistoryList()));

            Text totalAmountHeadlineLabel = new Text();
            totalAmountHeadlineLabel.setText("Amount available");
            totalAmountHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 28;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");

            Text totalAmountLabel = new Text();
            //totalAmountLabel.setPrefWidth(150);
            totalAmountLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            totalAmountLabel.setText(String.valueOf(currentItem.getAmountAvailable()));

            Text shopPriceLabel = new Text();
            shopPriceLabel.setText("Shop Price");
            shopPriceLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");

            Text shopPerinHeadlineLabel = new Text();
            shopPerinHeadlineLabel.setText("Perin: ");
            shopPerinHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");

            Text shopPerinLabel = new Text();
            shopPerinLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            shopPerinLabel.setText(String.valueOf(currentItem.getShopPerin()));

            Text shopPenyaHeadlineLabel = new Text();
            shopPenyaHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            shopPenyaHeadlineLabel.setText("Penya: ");

            Text shopPenyaLabel = new Text();
            shopPenyaLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            shopPenyaLabel.setText(String.valueOf(currentItem.getShopPenya()));

            Text shopEachLabel = new Text();
            shopEachLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
                    shopEachLabel.setText("each");

            Button changeAmountButton = new Button();
            changeAmountButton.setText("Change");
            HBox.setMargin(changeAmountButton, new Insets(0, 0, 0, 185));
            changeAmountButton.setPrefWidth(100);
            changeAmountButton.setOnAction(e -> {
                Stage changeAmountStage = new Stage();
                changeAmountStage.setResizable(false);
                changeAmountStage.initModality(Modality.APPLICATION_MODAL);
                changeAmountStage.setTitle("Change amount");

                BorderPane borderPaneChangeAmount = new BorderPane();

                TextField changeAmountTextField = new TextField();
                changeAmountTextField.setMaxWidth(60);

                Button changeAmountFinalButton = new Button();
                changeAmountFinalButton.setText("Change");
                changeAmountFinalButton.setOnAction(a -> {
                    if (changeAmountTextField.getText() != null) {
                        currentItem.updateAmountAvailable(Integer.parseInt(changeAmountTextField.getText()));
                        totalAmountLabel.setText(changeAmountTextField.getText());
                        changeAmountStage.close();
                    }
                });

                HBox hboxForTextFieldAndButton = new HBox();
                hboxForTextFieldAndButton.setSpacing(15);
                hboxForTextFieldAndButton.setPadding(new Insets(15, 15, 0, 15));
                hboxForTextFieldAndButton.getChildren().addAll(changeAmountTextField, changeAmountFinalButton);

                borderPaneChangeAmount.setCenter(hboxForTextFieldAndButton);

                changeAmountStage.setScene(new Scene(borderPaneChangeAmount, 175, 50));
                changeAmountStage.show();
                this.noticeMessageBox("Warning", "Careful! Please just change the amount via this button if the amount of that item changed without selling them! e.g. got more or spend some.");
            });

            Button setShopPriceButton = new Button();
            setShopPriceButton.setText("Set Price");
            setShopPriceButton.setPrefWidth(100);
            //HBox.setMargin(setShopPriceButton, new Insets(0, 0, 0, 15));
            setShopPriceButton.setLayoutX(600);
            setShopPriceButton.setOnAction(e -> {
                Stage setPriceStage = new Stage();
                setPriceStage.setResizable(false);
                setPriceStage.initModality(Modality.APPLICATION_MODAL);
                setPriceStage.setTitle("Shop Price");

                BorderPane borderPaneShopPrice = new BorderPane();
                borderPaneShopPrice.setPadding(new Insets(25, 0, 0, 25));

                Label setShopPricePerinLabel = new Label();
                setShopPricePerinLabel.setPrefWidth(50);
                setShopPricePerinLabel.setText("Perin:");

                Label setShopPricePenyaLabel = new Label();
                setShopPricePenyaLabel.setPrefWidth(50);
                setShopPricePenyaLabel.setText("Penya:");

                TextField setShopPricePerinTextField = new TextField();
                setShopPricePerinTextField.setText("0");

                TextField setShopPricePenyaTextField = new TextField();
                setShopPricePenyaTextField.setText("0");

                Button setShopPriceFinalButton = new Button();
                setShopPriceFinalButton.setText("Set price");
                setShopPriceFinalButton.setOnAction(a -> {
                    if (!setShopPricePerinTextField.getText().isEmpty() || !setShopPricePenyaTextField.getText().isEmpty()) {
                        if (Integer.parseInt(setShopPricePenyaTextField.getText()) >= 100000000) {
                            int perinsViaWrapAround = Integer.parseInt(setShopPricePenyaTextField.getText()) / 100000000;
                            currentItem.updateShopPerin(Integer.parseInt(setShopPricePerinTextField.getText()) + perinsViaWrapAround);
                            currentItem.updateShopPenya(Integer.parseInt(setShopPricePenyaTextField.getText()) - perinsViaWrapAround * 100000000);
                        } else {
                            currentItem.updateShopPerin(Integer.parseInt(setShopPricePerinTextField.getText()));
                            currentItem.updateShopPenya(Integer.parseInt(setShopPricePenyaTextField.getText()));
                        }
                        shopPerinLabel.setText(String.valueOf(currentItem.getShopPerin()));
                        shopPenyaLabel.setText(String.valueOf(currentItem.getShopPenya()));
                        setPriceStage.close();
                    }
                });

                Tooltip tooltipPenyaTextField = new Tooltip();
                tooltipPenyaTextField.setText("Value of 100.000.000 and above\nwill automatically be converted into Perins!");
                /**
                 * "Hack" to change the activation time for the tooltip to display. As seen, the activationTimer is a private field and usually can't be accessed
                 * and changed. Via reflection it is possible to change its value even though its private. Java 9 will have the possibility to change this value without this
                 * workaround
                 */
                try {
                    Field fieldBehavior = tooltipPenyaTextField.getClass().getDeclaredField("BEHAVIOR");
                    fieldBehavior.setAccessible(true);
                    Object objBehavior = fieldBehavior.get(tooltipPenyaTextField);

                    Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
                    fieldTimer.setAccessible(true);
                    Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

                    objTimer.getKeyFrames().clear();
                    objTimer.getKeyFrames().add(new KeyFrame(new Duration(0)));
                } catch (Exception a) {
                    a.printStackTrace();
                }

                Button buttonOnlyToolTip = new Button();
                buttonOnlyToolTip.setText("?");
                buttonOnlyToolTip.setTooltip(tooltipPenyaTextField);
                buttonOnlyToolTip.setStyle("-fx-background-radius: 15em;-fx-min-width: 3px;-fx-min-height: 3px;-fx-max-width: 33px;-fx-max-height: 33px;");

                HBox hboxPerinLabelAndTextField = new HBox();
                hboxPerinLabelAndTextField.getChildren().addAll(setShopPricePerinLabel, setShopPricePerinTextField);

                HBox hboxPenyaLabelAndTextField = new HBox();
                hboxPenyaLabelAndTextField.getChildren().addAll(setShopPricePenyaLabel, setShopPricePenyaTextField, buttonOnlyToolTip);

                HBox hboxShopButtons = new HBox();
                hboxShopButtons.setAlignment(Pos.CENTER);
                hboxShopButtons.getChildren().addAll(setShopPriceFinalButton);

                VBox vboxShopPriceAllHbox = new VBox();
                vboxShopPriceAllHbox.setSpacing(20);
                vboxShopPriceAllHbox.getChildren().addAll(hboxPerinLabelAndTextField, hboxPenyaLabelAndTextField, hboxShopButtons);

                borderPaneShopPrice.setCenter(vboxShopPriceAllHbox);
                setPriceStage.setScene(new Scene(borderPaneShopPrice, 300, 160));
                setPriceStage.show();
            });

            Button soldItemsButton = new Button();
            soldItemsButton.setText("Sold");
            soldItemsButton.setPrefWidth(100);
            soldItemsButton.setOnAction(e -> {
                Stage soldItemStage = new Stage();
                soldItemStage.setResizable(false);
                soldItemStage.initModality(Modality.APPLICATION_MODAL);
                soldItemStage.setTitle("Sold");

                BorderPane borderPaneItemSold = new BorderPane();

                TextField soldItemAmountTextField = new TextField();
                soldItemAmountTextField.setPrefWidth(60);
                soldItemAmountTextField.setMaxWidth(60);

                Label soldItemAmountLabel = new Label();
                soldItemAmountLabel.setText("out of " + currentItem.getAmountAvailable() + " available " + currentItem.getItemName() + "(s)");

                Button soldItemAmountButton = new Button();
                soldItemAmountButton.setText("Sold");
                soldItemAmountButton.setOnAction(a -> {
                    if (!soldItemAmountTextField.getText().isEmpty() && Integer.parseInt(soldItemAmountTextField.getText()) > 0 && Integer.parseInt(soldItemAmountTextField.getText()) <= currentItem.getAmountAvailable()) {
                        currentItem.updateSellHistory(currentItem.getShopPerin(), currentItem.getShopPenya(), Integer.parseInt(soldItemAmountTextField.getText()));
                        this.itemShopHistoryView.setItems(FXCollections.observableList(currentItem.getSellHistoryList()));
                        totalAmountLabel.setText(String.valueOf(currentItem.getAmountAvailable()));
//                        currentItem.getSellHistoryList().forEach(System.out::println);
                        refreshItemList();
                        soldItemStage.close();
                    }
                });

                VBox vboxSoldItemAll = new VBox();
                vboxSoldItemAll.setPadding(new Insets(15, 15, 15, 15));
                vboxSoldItemAll.setSpacing(15);
                vboxSoldItemAll.setAlignment(Pos.CENTER);
                vboxSoldItemAll.getChildren().addAll(soldItemAmountTextField, soldItemAmountLabel, soldItemAmountButton);

                borderPaneItemSold.setCenter(vboxSoldItemAll);

                soldItemStage.setScene(new Scene(borderPaneItemSold, soldItemAmountLabel.getText().length() * 7, 150));
                soldItemStage.show();
            });

            HBox hboxTotalAmountTextChangeButton = new HBox();
            hboxTotalAmountTextChangeButton.setSpacing(20);
            hboxTotalAmountTextChangeButton.getChildren().addAll(totalAmountLabel, changeAmountButton);

            HBox hboxSoldAmountPerinPenyaSoldButton = new HBox();
            hboxSoldAmountPerinPenyaSoldButton.setSpacing(10);
            hboxSoldAmountPerinPenyaSoldButton.getChildren().addAll(shopPerinHeadlineLabel, shopPerinLabel, shopPenyaHeadlineLabel, shopPenyaLabel, shopEachLabel, setShopPriceButton);

            VBox vboxAllButtonShowShopItem = new VBox();
            vboxAllButtonShowShopItem.setSpacing(50);
            vboxAllButtonShowShopItem.getChildren().addAll(changeAmountButton, setShopPriceButton, soldItemsButton);

            VBox vboxAllTextShowShopItem = new VBox();
            vboxAllTextShowShopItem.setMinWidth(360);
            vboxAllTextShowShopItem.setSpacing(50);
            vboxAllTextShowShopItem.getChildren().addAll(hboxTotalAmountTextChangeButton, hboxSoldAmountPerinPenyaSoldButton);

            HBox hboxUpperShopItemStuff = new HBox();
            hboxUpperShopItemStuff.setSpacing(30);
            hboxUpperShopItemStuff.getChildren().addAll(vboxAllTextShowShopItem, vboxAllButtonShowShopItem);

            HBox hboxItemShopListView = new HBox();
            hboxItemShopListView.getChildren().add(this.itemShopHistoryView);

            VBox vboxAllStuff = new VBox();
            vboxAllStuff.setPadding(new Insets(25, 0, 0, 50));
            vboxAllStuff.setSpacing(15);
            vboxAllStuff.getChildren().addAll(totalAmountHeadlineLabel, hboxUpperShopItemStuff, hboxItemShopListView);

            borderPaneShowItem.setCenter(vboxAllStuff);
        }

        borderPaneShowItem.setLeft(vboxItemImageLeft);
        if(currentItem.getAuctionState()) {
            Scene sceneShowItem = new Scene(borderPaneShowItem, this.itemImage.getImage().getWidth() + 700, this.itemImage.getImage().getHeight() + 100);
            this.itemOffersListView.setPrefSize(sceneShowItem.getWidth() - this.itemImage.getImage().getWidth() - 150, this.itemImage.getImage().getHeight()-50);
            this.itemOffersListView.setMaxHeight(this.itemImage.getImage().getHeight()-50);
            showAuctionItemStage.setScene(sceneShowItem);
        } else {
            Scene sceneShowItem = new Scene(borderPaneShowItem, this.itemImage.getImage().getWidth() + 600, this.itemImage.getImage().getHeight() + 250);
            this.itemShopHistoryView.setPrefSize(sceneShowItem.getWidth() - this.itemImage.getImage().getWidth() - 115, this.itemImage.getImage().getHeight()-50);
            showAuctionItemStage.setScene(sceneShowItem);
        }
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
        penyaLabel.setPrefWidth(59);
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
        perinTextField.setText("0");
        perinTextField.setPrefWidth(350);

        TextField penyaTextField = new TextField();
        penyaTextField.setText("0");
        penyaTextField.setPrefWidth(350);

        TextField tradeItemTextField = new TextField();
        tradeItemTextField.setPromptText("Leave it cleared if no item is offered");
        tradeItemTextField.setPrefWidth(350);

        TextField bidderNameTextField = new TextField();
        bidderNameTextField.setPrefWidth(350);

        Tooltip tooltipPenyaTextField = new Tooltip();
        tooltipPenyaTextField.setText("Value of 100.000.000 and above\nwill automatically be converted into Perins!");
        /**
         * "Hack" to change the activation time for the tooltip to display. As seen, the activationTimer is a private field and usually can't be accessed
         * and changed. Via reflection it is possible to change its value even though its private. Java 9 will have the possibility to change this value without this
         * workaround
         */
        try {
            Field fieldBehavior = tooltipPenyaTextField.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltipPenyaTextField);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(0)));
        } catch (Exception a) {
            a.printStackTrace();
        }

        Button buttonOnlyToolTip = new Button();
        buttonOnlyToolTip.setText("?");
        buttonOnlyToolTip.setTooltip(tooltipPenyaTextField);
        buttonOnlyToolTip.setStyle("-fx-background-radius: 15em;-fx-min-width: 3px;-fx-min-height: 3px;-fx-max-width: 33px;-fx-max-height: 33px;");

        Button saveOffer = new Button();
        saveOffer.setText("Save");
        saveOffer.setOnAction(e -> {
            if (!perinTextField.getText().isEmpty() && !penyaTextField.getText().isEmpty() && !bidderNameTextField.getText().isEmpty()) {
                currentItem.addOffer(Integer.parseInt(perinTextField.getText()), Integer.parseInt(penyaTextField.getText()), tradeItemTextField.getText(), bidderNameTextField.getText());
                this.refreshItemOfferList(currentItem);
                addOfferStage.close();
            }
        });

        HBox hboxAddOfferFirstLine = new HBox();
        hboxAddOfferFirstLine.setSpacing(5);
        hboxAddOfferFirstLine.getChildren().addAll(perinLabel, perinTextField);

        HBox hboxAddOfferSecondLine = new HBox();
        hboxAddOfferSecondLine.setSpacing(5);
        hboxAddOfferSecondLine.getChildren().addAll(penyaLabel, buttonOnlyToolTip, penyaTextField);

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
        noticeMessageStage.setResizable(false);
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

        noticeMessageStage.setScene(new Scene(borderPaneMessageBox, 250, 75+(message.length()/35)*25));
        noticeMessageStage.show();
    }

    private void renameItem(IngameItem selectedItem) {
        Stage renameItemStage = new Stage();
        renameItemStage.setResizable(false);
        renameItemStage.setTitle("Rename item");
        renameItemStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane borderPaneRenameItem = new BorderPane();
        borderPaneRenameItem.setPadding(new Insets(10,0,0,10));

        TextField renameTextField = new TextField();
        renameTextField.setText(selectedItem.getItemName());
        renameTextField.setPrefWidth(250);

        Button renameButton = new Button();
        renameButton.setText("Rename");
        renameButton.setOnAction(e -> {
            if(!renameTextField.getText().isEmpty()) {
                this.conditionMet = true;
                this.itemListView.getItems().forEach(a -> {
                    if(renameTextField.getText().equals(a.getItemName()))
                        this.conditionMet = false;
                });
                if(this.conditionMet) {
                    selectedItem.updateItemName(renameTextField.getText());
                    refreshItemList();
                    renameItemStage.close();
                } else {
                    noticeMessageBox("Error", "This name is already in use!");
                }
            }
        });

        HBox hboxforTextFieldAndButton = new HBox();
        hboxforTextFieldAndButton.setSpacing(15);
        hboxforTextFieldAndButton.getChildren().addAll(renameTextField,renameButton);

        borderPaneRenameItem.setCenter(hboxforTextFieldAndButton);

        renameItemStage.setScene(new Scene(borderPaneRenameItem, 350, 45));
        renameItemStage.show();
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
        this.imagePathSelected = this.defaultImagePath;
        return "None selected";
    }

    /**
     * Everytime something is added/deleted from the ListView, call this method to see the change~
     */
    public void refreshItemList() {
        this.allIngameItems.sort((s1, s2) -> s1.compareTo(s2));
        this.totalPerinAmountLabel.setText("0");
        this.totalPenyaAmountLabel.setText("0");
        for(IngameItem ign:this.allIngameItems) {
            if(ign.getAuctionState()) {
                if(ign.getOfferWon()!=null) {
                    if ((Integer.parseInt(this.totalPenyaAmountLabel.getText()) + ign.getOfferWon().getPenya()) >= 100000000) {
                        this.totalPerinAmountLabel.setText(String.valueOf(Integer.parseInt(this.totalPerinAmountLabel.getText()) + ign.getOfferWon().getPerin() + 1));
                        this.totalPenyaAmountLabel.setText(String.valueOf(Integer.parseInt(this.totalPenyaAmountLabel.getText()) + ign.getOfferWon().getPenya() - 100000000));
                    } else {
                        this.totalPerinAmountLabel.setText(String.valueOf(Integer.parseInt(this.totalPerinAmountLabel.getText()) + ign.getOfferWon().getPerin()));
                        this.totalPenyaAmountLabel.setText(String.valueOf(Integer.parseInt(this.totalPenyaAmountLabel.getText()) + ign.getOfferWon().getPenya()));
                    }
                }
            } else {
                for(SellHistory sh:ign.getSellHistoryList()) {
                    if((Integer.parseInt(this.totalPenyaAmountLabel.getText()) + sh.getPenyaGot()) >= 100000000) {
                        this.totalPerinAmountLabel.setText(String.valueOf(Integer.parseInt(this.totalPerinAmountLabel.getText()) + sh.getPerinGot() + 1));
                        this.totalPenyaAmountLabel.setText(String.valueOf(Integer.parseInt(this.totalPenyaAmountLabel.getText()) + sh.getPenyaGot() - 100000000));
                    } else {
                        this.totalPerinAmountLabel.setText(String.valueOf(Integer.parseInt(this.totalPerinAmountLabel.getText()) + sh.getPerinGot()));
                        this.totalPenyaAmountLabel.setText(String.valueOf(Integer.parseInt(this.totalPenyaAmountLabel.getText()) + sh.getPenyaGot()));
                    }
                }
            }
        }
        this.itemListView.setItems(FXCollections.observableList(new ArrayList<IngameItem>()));
        this.itemListView.setItems(FXCollections.observableList(this.allIngameItems));
    }

    private void refreshItemOfferList(IngameItem currentItem) {
        this.itemOffersListView.setItems(FXCollections.observableList(currentItem.getOfferList()));
    }

    public void transmitItemList(List<IngameItem> allIngameItems) {
        this.allIngameItems = allIngameItems;
    }

    public static void main(String[] args) {
        launch(args);
    }
}