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
    ListView<SellHistory> itemShopHistoryView = new ListView<>();
    Label imageNameLabel = new Label();
    ImageView itemImage;
    String defaultImagePath = "insanityFlyff/images/404-not-found.jpg";
    String imagePathSelected = defaultImagePath;
    boolean conditionMet;

    /**
     * Those are attributes now, because for proper formatting the sellHistoryList, i need to get the width of the scene in which it is in
     * but as the logic is needed, the hbox is defined earlier. I couldn't get the scene information before it has been created
     * but because the hbox NEEDS to be defined earlier, i have to put it as an attribute so i can use it whenever i want
     */
    HBox hboxItemShopListView = new HBox();
    boolean addedAlready = false;


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

        this.itemListView.setStyle("-fx-font-weight: bold; -fx-font-size: 16");

        this.itemOffersListView.setStyle("-fx-font-weight: bold;-fx-font-size: 16");

        this.itemListView.setOnMouseClicked(c -> {
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

        VBox vboxLeft = new VBox();
        //vboxBottom.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%)");
        //vboxBottom.setStyle("-fx-background-color: linear-gradient(#C8DCA6 0%, #DBFFC0 25%, #E0FFCF 75%, #FFFFFF 100%)");
        vboxLeft.setStyle("-fx-background-image: url('insanityFlyff/images/insanity_sidebar.png');-fx-background-size: cover");
        vboxLeft.setSpacing(15);
        vboxLeft.setPadding(new Insets(50, 10, 10, 10));
        vboxLeft.getChildren().addAll(addItemButton, renameItemButton, deleteItemButton, deleteAllItemsButton);

        VBox.setMargin(deleteAllItemsButton, new Insets(150, 0, 0, 0));

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
            showAuctionItemStage.setTitle(this.itemListView.getSelectionModel().getSelectedItem().getItemName() + " [Auction]");
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
            this.itemOffersListView.setPrefWidth(this.itemImage.getImage().getWidth()+200);
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
            showAuctionItemStage.setTitle(this.itemListView.getSelectionModel().getSelectedItem().getItemName() + " [Shop]");

            this.itemShopHistoryView.setItems(FXCollections.observableList(currentItem.getSellHistoryList()));

            Label totalAmountHeadlineLabel = new Label();
            totalAmountHeadlineLabel.setText("Amount available");
            totalAmountHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 24");

            Label totalAmountLabel = new Label();
            totalAmountLabel.setPrefWidth(150);
            totalAmountLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 16;-fx-text-fill: limegreen");
            totalAmountLabel.setText(String.valueOf(currentItem.getAmountAvailable()));

            Label shopPriceLabel = new Label();
            shopPriceLabel.setText("Shop Price");
            shopPriceLabel.setPadding(new Insets(25, 0, 0, 0));
            shopPriceLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 24");

            Label shopPerinHeadlineLabel = new Label();
            shopPerinHeadlineLabel.setText("Perin: ");
            shopPerinHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 16");

            Label shopPerinLabel = new Label();
            shopPerinLabel.setPrefWidth(50);
            shopPerinLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 16;-fx-text-fill: cornflowerblue");
            shopPerinLabel.setText(String.valueOf(currentItem.getShopPerin()));

            Label shopPenyaHeadlineLabel = new Label();
            shopPenyaHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 16");
            shopPenyaHeadlineLabel.setText("Penya: ");

            Label shopPenyaLabel = new Label();
            shopPenyaLabel.setPrefWidth(100);
            shopPenyaLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 16;-fx-text-fill: cornflowerblue");
            shopPenyaLabel.setText(String.valueOf(currentItem.getShopPenya()));

            Label shopEachLabel = new Label();
            shopEachLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 16");
            shopEachLabel.setText("each");

            Button changeAmountButton = new Button();
            changeAmountButton.setText("Change");
            HBox.setMargin(changeAmountButton, new Insets(0, 0, 0, 185));
            changeAmountButton.setPrefWidth(100);
            changeAmountButton.setOnAction(e -> {
                Stage changeAmountStage = new Stage();
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
            HBox.setMargin(setShopPriceButton, new Insets(0, 0, 0, 15));
            setShopPriceButton.setOnAction(e -> {
                Stage setPriceStage = new Stage();
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
                        currentItem.getSellHistoryList().forEach(System.out::println);
                        soldItemStage.close();
                    }
                });

                VBox vboxSoldItemAll = new VBox();
                vboxSoldItemAll.setPadding(new Insets(15,15,15,15));
                vboxSoldItemAll.setSpacing(15);
                vboxSoldItemAll.setAlignment(Pos.CENTER);
                vboxSoldItemAll.getChildren().addAll(soldItemAmountTextField, soldItemAmountLabel, soldItemAmountButton);

                borderPaneItemSold.setCenter(vboxSoldItemAll);

                soldItemStage.setScene(new Scene(borderPaneItemSold, soldItemAmountLabel.getText().length()*7, 150));
                soldItemStage.show();
            });

            HBox hboxTotalAmountTextChangeButton = new HBox();
            hboxTotalAmountTextChangeButton.setSpacing(20);
            hboxTotalAmountTextChangeButton.getChildren().addAll(totalAmountLabel, changeAmountButton);

            HBox hboxSoldAmountPerinPenyaSoldButton = new HBox();
            //hboxSoldAmountPerinPenyaSoldButton.setPadding(new Insets(20,0,0,0));
            hboxSoldAmountPerinPenyaSoldButton.setSpacing(10);
            hboxSoldAmountPerinPenyaSoldButton.getChildren().addAll(shopPerinHeadlineLabel, shopPerinLabel, shopPenyaHeadlineLabel, shopPenyaLabel, shopEachLabel, setShopPriceButton);

            HBox hboxSoldButtonOnly = new HBox();
            hboxSoldButtonOnly.setPadding(new Insets(0, 0, 0, 352));
            hboxSoldButtonOnly.getChildren().add(soldItemsButton);


            if(!this.addedAlready) {
                hboxItemShopListView.getChildren().add(this.itemShopHistoryView);
            }

            this.addedAlready = true;

            VBox vboxAllStuff = new VBox();
            vboxAllStuff.setPadding(new Insets(25, 0, 0, 50));
            vboxAllStuff.setSpacing(15);
            vboxAllStuff.getChildren().addAll(totalAmountHeadlineLabel, hboxTotalAmountTextChangeButton, shopPriceLabel, hboxSoldAmountPerinPenyaSoldButton, hboxSoldButtonOnly, hboxItemShopListView);

            borderPaneShowItem.setCenter(vboxAllStuff);
        }

        borderPaneShowItem.setLeft(vboxItemImageLeft);
        if(currentItem.getAuctionState()) {
            Scene sceneShowItem = new Scene(borderPaneShowItem, this.itemImage.getImage().getWidth() + 700, this.itemImage.getImage().getHeight() + 100);
            showAuctionItemStage.setScene(sceneShowItem);
        } else {
            Scene sceneShowItem = new Scene(borderPaneShowItem, this.itemImage.getImage().getWidth() + 600, this.itemImage.getImage().getHeight() + 250);
            /**
             * Here is the location why i needed to make the hbox as attribute!
             */
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

        noticeMessageStage.setScene(new Scene(borderPaneMessageBox, 250, 75+(message.length()/35)*25));
        noticeMessageStage.show();
    }

    private void renameItem(IngameItem selectedItem) {
        Stage renameItemStage = new Stage();
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
