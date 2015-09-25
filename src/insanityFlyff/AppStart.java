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
    Label imageCounterLabel = new Label();
    List<ImageView> itemImages = new ArrayList<>();
    ImageView itemImage;
    String defaultImagePath = "insanityFlyff/images/sample_item_info.png";
    String imagePathSelected = defaultImagePath;
    boolean conditionMet;
    boolean decisionBoxAnswer;
    /**
     * The labels which will display the total earnings -> the longs are the values calculated
     */
    Label totalPerinAmountLabel = new Label("0");
    Label totalPenyaAmountLabel = new Label("0");
    long totalPerinAmountValue;
    long totalPenyaAmountValue;

    ListView totalTradeItemsListView = new ListView();
    int imageIndex = 0;
    String currentImageDisplayedURL;
    /**
     * Those will be used to calculate the total earning from each shopItem itself
     */
    long penyaFromSellHistorySum;
    long perinFromSellHistorySum;
    Text textItemSoldSum = new Text();
    /**
     * This HBox is a field because i have to resize the box to match the scene, but the scene size is defined way later
     * and in another if-statement, therefore i wouldn't be able to change it without being a field
     */
    HBox hboxForSoldTextOnly = new HBox();

    /**
     * ListView to display all images as ImageViews added via the addItemStage
     */

    ListView<ImageView> addedImagesListView = new ListView<>();

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

        Label totalPerinAmountHeadlineLabel = new Label();
        totalPerinAmountHeadlineLabel.setText("Perin");
        totalPerinAmountHeadlineLabel.setStyle("-fx-font-weight: bold");

        Label totalPenyaAmountHeadlineLabel = new Label();
        totalPenyaAmountHeadlineLabel.setText("Penya");
        totalPenyaAmountHeadlineLabel.setStyle("-fx-font-weight: bold");

        Text madeBy = new Text();
        madeBy.setText("\u00a9 Kounex");

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
            if (this.itemListView.getSelectionModel().getSelectedItem() != null) {
                renameItem(this.itemListView.getSelectionModel().getSelectedItem());
            }
        });

        Button deleteItemButton = new Button();
        deleteItemButton.setText("Delete Item");
        deleteItemButton.setPrefWidth(150);
        deleteItemButton.setOnAction(e -> {
            if(this.itemListView.getSelectionModel().getSelectedItem() != null && this.decisionMessageBox("Warning", "Are you sure you want to delete the selected item? This action can't be undone!")) {
                this.allIngameItems.remove(this.itemListView.getSelectionModel().getSelectedItem());
                this.refreshItemList();
            }
        });

        Button deleteAllItemsButton = new Button();
        deleteAllItemsButton.setText("Delete all items");
        deleteAllItemsButton.setPrefWidth(150);
        deleteAllItemsButton.setOnAction(e -> {
            if(this.decisionMessageBox("Warning", "Are you sure you want to delete all items? This action can't be undone")) {
                this.allIngameItems.removeAll(allIngameItems);
                this.refreshItemList();
            }
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
        VBox.setMargin(madeBy, new Insets(60,65,0,0));
        vboxLeft.getChildren().addAll(addItemButton, renameItemButton, deleteItemButton, deleteAllItemsButton,
                totalEarningsHeadlineLabel, totalPerinAmountHeadlineLabel, this.totalPerinAmountLabel,
                totalPenyaAmountHeadlineLabel, this.totalPenyaAmountLabel, showTotalTradeItemsButton, madeBy);

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
        addItemStage.initModality(Modality.APPLICATION_MODAL);
        addItemStage.setTitle("Add new item");

        /**
         * This is the list which contains all the image paths selected
         */
        List<String> selectedImages = new ArrayList<>();

        /**
         * To display the text as soon as the stage is shown
         */
        this.imageCounterLabel.setText("0 image(s) selected");
        this.addedImagesListView.setFixedCellSize(200);
        this.imageCounterLabel.setStyle("-fx-font-weight: bold; -fx-font-style: oblique");

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

        Button manageImages = new Button();
        manageImages.setText("Manage Images");
        manageImages.setOnAction(e -> {
            Stage manageImagesStage = new Stage();
            manageImagesStage.initModality(Modality.APPLICATION_MODAL);
            manageImagesStage.setTitle("Manage Images");

            BorderPane borderPaneManageImages = new BorderPane();
            borderPaneManageImages.setStyle("-fx-background-image: url('insanityFlyff/images/31725.jpg')");

            Button removeAllImages = new Button();
            removeAllImages.setText("Remove All");
            removeAllImages.setPrefWidth(150);
            removeAllImages.setOnAction(a -> {
                selectedImages.clear();
                this.refreshManageImageListView(selectedImages);
            });

            Button removeSingleImage = new Button();
            removeSingleImage.setText("Remove Image");
            removeSingleImage.setPrefWidth(150);
            removeSingleImage.setOnAction(a -> {
                if (this.addedImagesListView.getSelectionModel().getSelectedItem() != null) {
                    selectedImages.remove(this.addedImagesListView.getSelectionModel().getSelectedIndex());
                    this.refreshManageImageListView(selectedImages);
                }
            });

            Button changeSingleImage = new Button();
            changeSingleImage.setText("Change Image");
            changeSingleImage.setPrefWidth(150);
            changeSingleImage.setOnAction(a -> {
                if (this.addedImagesListView.getSelectionModel().getSelectedItem() != null) {
                    int cacheIndex = this.addedImagesListView.getSelectionModel().getSelectedIndex();
                    this.addImageViaFileChooser();
                    if (!this.imagePathSelected.equals("None selected")) {
                        selectedImages.remove(cacheIndex);
                        selectedImages.add(cacheIndex, this.imagePathSelected);
                        this.refreshManageImageListView(selectedImages);
                    }
                }
            });

            Button addImage = new Button();
            addImage.setText("Add Image");
            addImage.setPrefWidth(150);
            addImage.setOnAction(a -> {
                String imageName = this.addImageViaFileChooser();
                if (!imageName.equals("None selected")) {
                    selectedImages.add(this.imagePathSelected);
                    this.refreshManageImageListView(selectedImages);
                }
            });

            Button doneManageImages = new Button();
            doneManageImages.setText("Done");
            doneManageImages.setPrefWidth(150);
            doneManageImages.setOnAction(a -> {
                manageImagesStage.close();
            });

            refreshManageImageListView(selectedImages);

            VBox vboxAllButtonsManageImages = new VBox();
            vboxAllButtonsManageImages.setSpacing(25);
            vboxAllButtonsManageImages.setPadding(new Insets(0, 60, 0, 30));
            vboxAllButtonsManageImages.setAlignment(Pos.CENTER);
            vboxAllButtonsManageImages.getChildren().addAll(removeAllImages, removeSingleImage, changeSingleImage, addImage, doneManageImages);

            HBox hboxForImagesListView = new HBox();
            hboxForImagesListView.setPrefSize(400,660);
            hboxForImagesListView.setPadding(new Insets(25,0,25,25));
            hboxForImagesListView.getChildren().add(this.addedImagesListView);

            borderPaneManageImages.setCenter(hboxForImagesListView);
            borderPaneManageImages.setRight(vboxAllButtonsManageImages);

            Scene manageImagesScene = new Scene(borderPaneManageImages, 550, 660);
            manageImagesStage.setScene(manageImagesScene);
            manageImagesStage.setResizable(false);
            manageImagesStage.show();
        });

        HBox hboxForImageButton = new HBox();
        hboxForImageButton.setPadding(new Insets(47, 0, 0, 0));
        hboxForImageButton.setSpacing(10);
        hboxForImageButton.getChildren().add(manageImages);

        VBox vboxForImageButton = new VBox();
        vboxForImageButton.setSpacing(15);
        vboxForImageButton.getChildren().addAll(hboxForImageButton, this.imageCounterLabel);

        Button createItemAdd = new Button();
        createItemAdd.setText("Create");
        createItemAdd.setPrefWidth(100);
        createItemAdd.setOnAction(a -> {
            this.conditionMet = true;
            this.itemListView.getItems().forEach(c -> {
                if (itemNameTextField.getText().equals(c.getItemName())) {
                    noticeMessageBox("Error", "This item already exists! Please specify another name or use the item which is already in the list.");
                    this.conditionMet = false;
                }
            });
            if (!itemNameTextField.getText().isEmpty() && Integer.parseInt(itemAmountTextField.getText()) >= 0 && this.conditionMet) {
                if(selectedImages.isEmpty()) {
                    selectedImages.add(this.defaultImagePath);
                }
                this.allIngameItems.add(new IngameItem(Integer.parseInt(itemAmountTextField.getText()), checkBoxAuction.isSelected(), itemNameTextField.getText(), selectedImages));
                this.refreshItemList();
                addItemStage.close();
            }
        });

        Button cancelCreate = new Button();
        cancelCreate.setText("Cancel");
        cancelCreate.setPrefWidth(100);
        cancelCreate.setOnAction(e -> {
            addItemStage.close();
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
        hboxItemAddBottom.getChildren().addAll(createItemAdd, cancelCreate);
        hboxItemAddBottom.setSpacing(10);
        hboxItemAddBottom.setPadding(new Insets(0, 0, 20, 0));
        hboxItemAddBottom.setAlignment(Pos.CENTER);

        borderPaneAddItem.setCenter(hboxItemAddCenter);
        borderPaneAddItem.setBottom(hboxItemAddBottom);

        Scene sceneItemAdd = new Scene(borderPaneAddItem,500,200);

        addItemStage.setScene(sceneItemAdd);
        addItemStage.setResizable(false);
        addItemStage.show();
    }

    private void showItemStage() {
        Stage showItemStage = new Stage();
        showItemStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane borderPaneShowItem = new BorderPane();
        borderPaneShowItem.setStyle("-fx-background-image: url('insanityFlyff/images/comm__flyff_screen_by_unrealsmoker-d5awftg.jpg');-fx-background-size: cover");

        IngameItem currentItem = this.itemListView.getSelectionModel().getSelectedItem();

        /**
         * To clear the list everytime a new item is opened, it's a cache list
         */
        this.itemImages = new ArrayList<>();
        /**
         * If-clause to check if the URLS still lead to legit pictures, otherwise show the sample item picture
         */
        currentItem.getImageURLS().forEach(url -> {
            /**
             * Old Version where the images were just created with the original size. If too big images were chosen
             * the showItemStage would be way off and shitty
             */
//            this.itemImages.add(new ImageView(new Image(url)));
            /**
             * New Version where all images will have the size but keep the aspect ratio, therefore bigger images will
             * be scaled down to fit into the fix stage
             */
            this.itemImages.add((new ImageView(new Image(url,450,450,true,true))));
        });

        this.itemImages.forEach(image -> {
            if (image.getImage().getHeight() == 0) {
                this.itemImages.remove(image);
            }
        });

        if(this.itemImages.size()==0) {
            this.itemImages.add(new ImageView(new Image(this.defaultImagePath)));
            currentItem.addImageURL(this.defaultImagePath);
        }

        /**
         * Set the current displaying image to the first one of the imageList of the currentItem
         * Alternative: Every IngameItem-Object could have its own imageIndex (field). By doing so, it would be possible
         * to save the last displayed image and start the showItemStage with the last shown image.
         * Note: By deleting an image the showItemStage method is recalled to start the stage with the first
         * image. But if the alternative implementation is realised, it shouldn't start with the first image,
         * rather with the image before (imageIndex--). If the first image is deleted, the imageIndex obviously
         * can't be decreased to -1 because an index out of bound would occur. If the first one is deleted
         * the imageIndex would have to stay.
         */
        this.imageIndex = 0;
        this.itemImage = this.itemImages.get(this.imageIndex);
        this.currentImageDisplayedURL = currentItem.getImageURLS().get(this.imageIndex);

        /**
         * Old version using one single image each item
         */
//        this.itemImage = new ImageView(new Image(currentItem.getImageURL()));

//        if(this.itemImage.getImage().getHeight()==0) {
//            this.itemImage = new ImageView(new Image(this.defaultImagePath));
//        }

        Button changeItemImage = new Button();
        changeItemImage.setText("Change Image");
        changeItemImage.setOnAction(e -> {
            this.addImageViaFileChooser();
            if (!this.imagePathSelected.equals("None selected")) {
                currentItem.updateImageURL(this.currentImageDisplayedURL, this.imagePathSelected);
                this.imagePathSelected = this.defaultImagePath;
            } else {
                currentItem.updateImageURL(this.currentImageDisplayedURL, this.defaultImagePath);
            }
            /**
             * Commented out -> Reason: after this action, this window is closed and reopened via calling. Therefore
             * The itemImage is going to be assigned at the start of the method again -> This should not be necessary!
             */
//            this.itemImage = new ImageView(new Image(currentItem.getImageURL()));
            showItemStage.close();
            this.showItemStage();
        });

        Button addItemImage = new Button();
        addItemImage.setText("Add Image");
        addItemImage.setOnAction(e -> {
            this.addImageViaFileChooser();
            if (!this.currentImageDisplayedURL.equals(this.defaultImagePath)) {
                if (!imagePathSelected.equals("None Selected") && !currentItem.getImageURLS().contains(this.imagePathSelected)) {
                    currentItem.addImageURL(this.imagePathSelected);
                }
            } else {
                if (!imagePathSelected.equals("None Selected")) {
                    currentItem.updateImageURL(this.defaultImagePath, this.imagePathSelected);
                }
            }
            this.imagePathSelected = this.defaultImagePath;
            showItemStage.close();
            this.showItemStage();
        });


        Button deleteItemImage = new Button();
        deleteItemImage.setText("Delete image");
        deleteItemImage.setOnAction(a -> {
            if (this.itemImages.size() > 1) {
                currentItem.deleteSingleImageURL(this.currentImageDisplayedURL);
            } else {
                currentItem.updateImageURL(this.currentImageDisplayedURL, this.defaultImagePath);
            }
            /**
             * Same reason as above
             */
//            this.itemImage = new ImageView(new Image(currentItem.getImageURL()));
            showItemStage.close();
            this.showItemStage();
        });

        Button deleteAllItemImages = new Button();
        deleteAllItemImages.setText("Delete all images");
        deleteAllItemImages.setOnAction(e -> {
            currentItem.deleteImageURLS();
            showItemStage.close();
            this.showItemStage();
        });

        Button prevImageButton = new Button();
        prevImageButton.setText("<");
        if(this.imageIndex==0) {
            prevImageButton.setVisible(false);
        }

        Button nextImageButton = new Button();
        nextImageButton.setText(">");
        if(this.imageIndex>=this.itemImages.size()-1) {
            nextImageButton.setVisible(false);
        }

        HBox hboxButtonsUnderImageLeft1 = new HBox();
        hboxButtonsUnderImageLeft1.setSpacing(15);
        hboxButtonsUnderImageLeft1.setAlignment(Pos.CENTER);
        hboxButtonsUnderImageLeft1.getChildren().addAll(changeItemImage, deleteItemImage);

        HBox hboxButtonsUnderImageLeft2 = new HBox();
        hboxButtonsUnderImageLeft2.setSpacing(15);
        hboxButtonsUnderImageLeft2.setAlignment(Pos.CENTER);
        hboxButtonsUnderImageLeft2.getChildren().addAll(addItemImage, deleteAllItemImages);

        /**
         * The image is getting resized to a specific size with the same aspect ratio! Because images may have
         * different base width's and height's the resize is working different, if the image is wider,
         * the resize is wider as well and the same for the height. Thats why the Hbox has to be bigger then
         * the resize of the image to avoid outsizing the hbox and destroying the formating. Those sizes
         * may still be wrong for some "extreme" images, but seems to work for now
         */
        HBox hboxForImageViewOnly = new HBox();
        hboxForImageViewOnly.setPrefSize(500, 500);
        hboxForImageViewOnly.setAlignment(Pos.CENTER);
        hboxForImageViewOnly.getChildren().add(this.itemImage);

        HBox hboxImageAndPrevNextButtons = new HBox();
        hboxImageAndPrevNextButtons.setSpacing(5);
        hboxImageAndPrevNextButtons.setAlignment(Pos.CENTER);
        hboxImageAndPrevNextButtons.getChildren().addAll(prevImageButton, hboxForImageViewOnly, nextImageButton);

        /**
         * The actual events are programmed later because to work properly those methods need access to
         * the Hboxes where the image is added in and those are declared after the button section.
         */
        nextImageButton.setOnAction(e -> {
            prevImageButton.setVisible(true);
            this.imageIndex++;
            if (this.imageIndex == this.itemImages.size() - 1) {
                nextImageButton.setVisible(false);
            }
            this.itemImage = this.itemImages.get(this.imageIndex);
            this.currentImageDisplayedURL = currentItem.getImageURLS().get(this.imageIndex);
//            hboxImageAndPrevNextButtons.getChildren().clear();
//            hboxImageAndPrevNextButtons.getChildren().addAll(prevImageButton, hboxForImageViewOnly, nextImageButton);
            hboxForImageViewOnly.getChildren().clear();
            hboxForImageViewOnly.getChildren().add(this.itemImage);
        });

        prevImageButton.setOnAction(e -> {
            nextImageButton.setVisible(true);
            this.imageIndex--;
            if (this.imageIndex == 0) {
                prevImageButton.setVisible(false);
            }
            this.itemImage = this.itemImages.get(this.imageIndex);
            this.currentImageDisplayedURL = currentItem.getImageURLS().get(this.imageIndex);
//            hboxImageAndPrevNextButtons.getChildren().clear();
//            hboxImageAndPrevNextButtons.getChildren().addAll(prevImageButton, hboxForImageViewOnly, nextImageButton);
            hboxForImageViewOnly.getChildren().clear();
            hboxForImageViewOnly.getChildren().add(this.itemImage);
        });

        VBox vboxItemImageLeft = new VBox();
        vboxItemImageLeft.setAlignment(Pos.CENTER);
        vboxItemImageLeft.setPadding(new Insets(0, 0, 0, 15));
        vboxItemImageLeft.setSpacing(15);
        vboxItemImageLeft.getChildren().addAll(hboxImageAndPrevNextButtons, hboxButtonsUnderImageLeft1, hboxButtonsUnderImageLeft2);

        /**
         * showItemStage is divided in two parts by the following big if define. If the if-statement results true, the
         * selected item has been marked as an auction-item (via the checkbox in the addItemStage). If the if-statement results
         * false the item is a shop-item. Both item-kinds need different layouts and instead of creating two methods
         * it is combined in one because both uses the same logic to set the size via the imagesize and more stuff they
         * have in common
         */
        if(currentItem.getAuctionState()) {
            showItemStage.setTitle(this.itemListView.getSelectionModel().getSelectedItem().getItemName() + " [Auction]");

            /**
             * hboxBlaBla.getChildren().add(NodeX) ... later ... hboxBlaBla.getChildren().add(NodeX)
             * Even though its the same Node which is added, it won't refresh the Node, it will in fact add it a second time.
             * Therefore if a Node is meant to be refreshed the Node should be removed explicit or the whole hbox has to be
             * cleared and the node has to be re-added
             */
            this.hboxForSoldTextOnly.getChildren().clear();

            Text showItemAuctionSoldText = new Text();
//            showItemAuctionSoldText.setStyle("-fx-font-weight: bold;-fx-font-size: 14;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            showItemAuctionSoldText.setStyle("-fx-font-weight: bold;-fx-font-size: 14");
            if(currentItem.getOfferWon()!=null) {
                showItemAuctionSoldText.setText("Sold for: " + currentItem.getOfferWon().toString());
            } else {
                showItemAuctionSoldText.setText("Not sold yet!");
            }

            Button addOfferToItem = new Button();
            addOfferToItem.setText("Add offer");
            addOfferToItem.setPrefWidth(100);
            addOfferToItem.setOnAction(a -> {
                if (currentItem.getOfferWon() == null) {
                    this.addOfferToItemStage(currentItem);
                }
            });

            Button deleteOfferFromItem = new Button();
            deleteOfferFromItem.setText("Delete offer");
            deleteOfferFromItem.setPrefWidth(100);
            deleteOfferFromItem.setOnAction(e -> {
                if (currentItem.getOfferWon() == null) {
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

            this.hboxForSoldTextOnly.setStyle("-fx-background-color: whitesmoke");
            this.hboxForSoldTextOnly.getChildren().add(showItemAuctionSoldText);

            VBox vboxItemShowCenter = new VBox();
            vboxItemShowCenter.setPadding(new Insets(30, 0, 0, 30));

            VBox.setMargin(hboxForSoldTextOnly, new Insets(15,0,40,0));

            vboxItemShowCenter.getChildren().addAll(hboxItemShowOfferListView, hboxForSoldTextOnly, hboxItemShowCenterButtons);
            borderPaneShowItem.setCenter(vboxItemShowCenter);
        } else {
            showItemStage.setTitle(this.itemListView.getSelectionModel().getSelectedItem().getItemName() + " [Shop]");

            this.itemShopHistoryView.setItems(FXCollections.observableList(currentItem.getSellHistoryList()));

            Text totalAmountHeadlineLabel = new Text();
            totalAmountHeadlineLabel.setText("Amount available");
            totalAmountHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 28;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");

            Text totalAmountLabel = new Text();
            //totalAmountLabel.setPrefWidth(150);
            totalAmountLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            totalAmountLabel.setText(String.format("%,d",currentItem.getAmountAvailable()));

            Text shopPriceLabel = new Text();
            shopPriceLabel.setText("Shop Price");
            shopPriceLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");

            Text shopPerinHeadlineLabel = new Text();
            shopPerinHeadlineLabel.setText("Perin: ");
            shopPerinHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");

            Text shopPerinLabel = new Text();
            shopPerinLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            shopPerinLabel.setText(String.format("%,d",currentItem.getShopPerin()));

            Text shopPenyaHeadlineLabel = new Text();
            shopPenyaHeadlineLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            shopPenyaHeadlineLabel.setText("Penya: ");

            Text shopPenyaLabel = new Text();
            shopPenyaLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-fill: white;-fx-stroke: black;-fx-str-width: 1");
            shopPenyaLabel.setText(String.format("%,d",currentItem.getShopPenya()));

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
                    if (changeAmountTextField.getText() != null && Integer.parseInt(changeAmountTextField.getText()) >= 0) {
                        currentItem.updateAmountAvailable(Integer.parseInt(changeAmountTextField.getText()));
                        totalAmountLabel.setText(String.format("%,d",Integer.valueOf(changeAmountTextField.getText())));
                        this.refreshItemList();
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
                soldItemAmountLabel.setText("out of " + String.format("%,d",currentItem.getAmountAvailable()) + " available " + currentItem.getItemName() + "(s)");

                Button soldItemAmountButton = new Button();
                soldItemAmountButton.setText("Sold");
                soldItemAmountButton.setOnAction(a -> {
                    if (!soldItemAmountTextField.getText().isEmpty() && Integer.parseInt(soldItemAmountTextField.getText()) > 0 && Integer.parseInt(soldItemAmountTextField.getText()) <= currentItem.getAmountAvailable()) {
                        currentItem.updateSellHistory(currentItem.getShopPerin(), currentItem.getShopPenya(), Integer.parseInt(soldItemAmountTextField.getText()));
                        this.itemShopHistoryView.setItems(FXCollections.observableList(currentItem.getSellHistoryList()));
                        totalAmountLabel.setText(String.valueOf(currentItem.getAmountAvailable()));
//                        currentItem.getSellHistoryList().forEach(System.out::println);
                        this.refreshItemList();
                        this.calculateEarningPerItem(currentItem);
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

            this.calculateEarningPerItem(currentItem);

            HBox hboxItemSoldTextSum = new HBox();
            hboxItemSoldTextSum.setMaxWidth(hboxItemShopListView.getWidth());
            hboxItemSoldTextSum.setStyle("-fx-background-color: whitesmoke");
            hboxItemSoldTextSum.getChildren().add(this.textItemSoldSum);

            VBox vboxAllStuff = new VBox();
            vboxAllStuff.setPadding(new Insets(25, 0, 0, 15));
            vboxAllStuff.setSpacing(15);
            vboxAllStuff.getChildren().addAll(totalAmountHeadlineLabel, hboxUpperShopItemStuff, hboxItemShopListView, hboxItemSoldTextSum);

            borderPaneShowItem.setCenter(vboxAllStuff);
        }

        borderPaneShowItem.setLeft(vboxItemImageLeft);

        /**
         * Old Version which calculated the scene size via the item image. Dynamic but gets buggy (obviously) if the image
         * is just too big! Basically another way could be to set a max size and let the image downscale to the max size
         * if the original image is bigger then that. Otherwise let the scene be resized to fit the image
         * BUT: Still looks ugly
         */
//        if(currentItem.getAuctionState()) {
//            Scene sceneShowItem = new Scene(borderPaneShowItem, this.itemImage.getImage().getWidth() + 700, this.itemImage.getImage().getHeight() + 100);
//            this.itemOffersListView.setPrefSize(sceneShowItem.getWidth() - this.itemImage.getImage().getWidth() - 150, this.itemImage.getImage().getHeight()-50);
//            this.itemOffersListView.setMaxHeight(this.itemImage.getImage().getHeight() - 50);
//            this.hboxForSoldTextOnly.setMaxWidth(sceneShowItem.getWidth() - this.itemImage.getImage().getWidth() - 150);
//            showItemStage.setScene(sceneShowItem);
//        } else {
//            Scene sceneShowItem = new Scene(borderPaneShowItem, this.itemImage.getImage().getWidth() + 600, this.itemImage.getImage().getHeight() + 250);
//            this.itemShopHistoryView.setPrefSize(sceneShowItem.getWidth() - this.itemImage.getImage().getWidth() - 115, this.itemImage.getImage().getHeight()-150);
//            showItemStage.setScene(sceneShowItem);
//        }

        /**
         * New Version where the size of the scene and the size of the hbox which contains the image is the same. The image
         * will be rescaled to the same size everytime but as explained earlier may have different aspect ratio from the beginning
         * and could "destroy" the format. Thats why the hbox is bigger then the image itself to avoid outscaling!
         * Still not tested extreme image sizes where the ratio is very high like 1px height and 1000px width...
         */
        if(currentItem.getAuctionState()) {
            Scene sceneShowItem = new Scene(borderPaneShowItem, 1150, 650);
            this.itemOffersListView.setPrefSize(500, 500);
            this.hboxForSoldTextOnly.setMaxWidth(500);
            showItemStage.setScene(sceneShowItem);
        } else {
            Scene sceneShowItem = new Scene(borderPaneShowItem, 1150, 650);
            this.itemShopHistoryView.setPrefSize(500, 300);
            showItemStage.setScene(sceneShowItem);
        }
        showItemStage.setResizable(false);
        showItemStage.show();
    }

    private void addOfferToItemStage(IngameItem currentItem) {
        Stage addOfferStage = new Stage();
        addOfferStage.initModality(Modality.APPLICATION_MODAL);
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

    private boolean decisionMessageBox(String title, String message) {
        Stage deleteItemStage = new Stage();
        deleteItemStage.setResizable(false);
        deleteItemStage.initModality(Modality.APPLICATION_MODAL);
        deleteItemStage.setTitle(title);

        this.decisionBoxAnswer = false;

        BorderPane borderPaneDeleteItem = new BorderPane();
        //borderPaneDeleteAllItems.setPadding(new Insets(10, 0, 0, 0));

        Text deleteItemText = new Text(message);
        deleteItemText.setWrappingWidth(225);

        Button deleteItemYesButton = new Button();
        deleteItemYesButton.setText("Yes");
        deleteItemYesButton.setPrefWidth(75);
        deleteItemYesButton.setOnAction(a -> {
            this.decisionBoxAnswer = true;
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
        deleteItemStage.showAndWait();
        return this.decisionBoxAnswer;
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
        this.totalPerinAmountValue = 0;
        this.totalPenyaAmountValue = 0;
        for(IngameItem ign:this.allIngameItems) {
            if(ign.getAuctionState()) {
                if(ign.getOfferWon()!=null) {
                    if((this.totalPenyaAmountValue + ign.getOfferWon().getPenya()) >= 100000000) {
                        this.totalPerinAmountValue += ign.getOfferWon().getPerin() + 1;
                        this.totalPenyaAmountValue += ign.getOfferWon().getPenya() - 100000000;
                    } else {
                        this.totalPerinAmountValue += ign.getOfferWon().getPerin();
                        this.totalPenyaAmountValue += ign.getOfferWon().getPenya();
                    }
                }
            } else {
                for(SellHistory sh:ign.getSellHistoryList()) {
                    if((this.totalPenyaAmountValue + sh.getPenyaGot()) >= 100000000) {
                        this.totalPerinAmountValue += sh.getPerinGot() + 1;
                        this.totalPenyaAmountValue += sh.getPenyaGot() - 100000000;
                    } else {
                        this.totalPerinAmountValue += sh.getPerinGot();
                        this.totalPenyaAmountValue += sh.getPenyaGot();
                    }
                }
            }
        }
        this.totalPerinAmountLabel.setText(String.format("%,d",this.totalPerinAmountValue));
        this.totalPenyaAmountLabel.setText(String.format("%,d",this.totalPenyaAmountValue));
        this.itemListView.setItems(FXCollections.observableList(new ArrayList<IngameItem>()));
        this.itemListView.setItems(FXCollections.observableList(this.allIngameItems));
    }

    private void refreshManageImageListView(List<String> selectedImages) {
        List<ImageView> localImageViewsFromSelectedImages = new ArrayList<>();
        selectedImages.forEach(a -> {
            localImageViewsFromSelectedImages.add(new ImageView(new Image(a, 200, 200, true, true)));
        });
        this.addedImagesListView.setItems(FXCollections.observableList(localImageViewsFromSelectedImages));
        this.imageCounterLabel.setText(selectedImages.size()+ " image(s) selected");
    }

    private void refreshItemOfferList(IngameItem currentItem) {
        this.itemOffersListView.setItems(FXCollections.observableList(currentItem.getOfferList()));
    }

    private void calculateEarningPerItem(IngameItem currentItem) {
        this.textItemSoldSum.setStyle("-fx-font-weight: bold;-fx-font-size: 14");
        this.perinFromSellHistorySum = 0;
        this.penyaFromSellHistorySum = 0;
        currentItem.getSellHistoryList().forEach(sh -> {
            this.penyaFromSellHistorySum += sh.getPenyaGot();
            this.perinFromSellHistorySum += sh.getPerinGot();
        });
        if(this.penyaFromSellHistorySum >= 100000000) {
            this.perinFromSellHistorySum += this.penyaFromSellHistorySum/100000000;
            this.penyaFromSellHistorySum -= (this.penyaFromSellHistorySum/100000000)*100000000;
        }
        this.textItemSoldSum.setText("Total earning via this item: " + String.format("%,d",this.perinFromSellHistorySum) + " Perin " + String.format("%,d",this.penyaFromSellHistorySum) + " Penya   [" + String.format("%,d",currentItem.getAmountSold()) + " sold]");

    }

    public void transmitItemList(List<IngameItem> allIngameItems) {
        this.allIngameItems = allIngameItems;
    }

    public static void main(String[] args) {
        launch(args);
    }
}