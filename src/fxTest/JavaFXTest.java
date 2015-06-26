package fxTest;

/**
 * Created by Kounex on 25.06.15.
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class JavaFXTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane layout = new BorderPane();
//        layout.setStyle("-fx-background-color: cornflowerblue");
        layout.setStyle("-fx-background-image: url('FXTest/cole.jpg');-fx-background-size: cover");

        Label headline = new Label("Hallo Laura!");
        headline.setStyle("-fx-text-fill: azure;-fx-font-size: 64;-fx-effect: innershadow(gaussian, red, 1, 1.0, 1, 1);-fx-font-family: monospace;-fx-font-weight: bolder");
        BorderPane.setAlignment(headline, Pos.CENTER);

        Button clickHereBttn = new Button();
        clickHereBttn.setText("Click me, please!");
        clickHereBttn.setOnAction(e -> {
            Stage secondaryStage = new Stage();

            BorderPane secondLayout = new BorderPane();
            secondLayout.setStyle("-fx-background-color: chartreuse");

            Label laurasLabel = new Label();
            laurasLabel.setText("Du wolltest das so!!");
            laurasLabel.setStyle("-fx-text-fill: black");

            BorderPane.setAlignment(laurasLabel, Pos.BOTTOM_CENTER);
            Button closeBttn = new Button();
            closeBttn.setText("Close me please!!");
            closeBttn.setOnAction(a -> {
                secondaryStage.hide();
            });

            secondLayout.setTop(laurasLabel);
            secondLayout.setCenter(closeBttn);
            Scene secondScene = new Scene(secondLayout, 200, 200);
            secondaryStage.setScene(secondScene);
            secondaryStage.show();
        });

        HBox hboxBttn = new HBox();
        hboxBttn.getChildren().add(clickHereBttn);
        hboxBttn.setStyle("-fx-alignment: top-center; -fx-padding: 20px");

        layout.setBottom(hboxBttn);
        layout.setTop(headline);
        Scene scene = new Scene(layout,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
