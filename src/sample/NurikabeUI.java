package sample;/**
 * Created by smrut on 5/20/2017.
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

public class NurikabeUI extends Application {
    NurikabeConfig newConfig;
    String[][] grid;
    Label result=new Label();

    public static void main(String[] args) {
        launch(args);
    }
    public void init(){
        Application.Parameters params=getParameters();

        try {
            System.out.println(params.getRaw());
             newConfig=new NurikabeConfig(params.getRaw().get(0));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        System.out.println(newConfig.toString());
    }

    @Override
    public void start(Stage primaryStage) {
        ObservableList<String> level= FXCollections.observableArrayList("1","2","3","4","5");


        result.setText("");
        Label levelTag=new Label("Level");
        ComboBox levels=new ComboBox(level);
        BorderPane borderPane=new BorderPane();
        GridPane pane=new GridPane();
        pane.setPrefWidth(200);
        pane.setPrefHeight(200);
        NurikabeConfig succ=new NurikabeConfig(newConfig);
        for (int i = 0; i <newConfig.getDimY(); i++) {//(Integer.parseInt(params.getRaw().get(0)) )
            for (int j = 0; j < newConfig.getDimX(); j++) {
//                final int x=i+1;
                Button btn2 = new Button("");
                btn2.setTextAlignment(TextAlignment.RIGHT);
                btn2.setPrefHeight(80);
                btn2.setPrefWidth(80);
                btn2.setText(newConfig.getCell(j,i));
//                btn2.setId(Integer.toString(i));
//                pane.getChildren().add(btn2);
                btn2.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

                pane.add(btn2,i,j);
                final int x,y;
                x=i;
                y=j;
                btn2.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
                    @Override
                    public void handle(javafx.scene.input.MouseEvent event) {
                        MouseButton button=event.getButton();
                        if(button==MouseButton.PRIMARY && !btn2.getText().matches("[0-9]")){
                            btn2.setText("@");

                            succ.setCell(y,x,"@");
//                            checkValid(succ);
//                            System.out.println(succ);
                        }
                        if(button==MouseButton.SECONDARY && !btn2.getText().matches("[0-9]")){
                            btn2.setText("#");
                            succ.setCell(y,x,"#");
//                            checkValid(succ);
                        }
                    }
                });
            }

        }
        VBox hbox=new VBox();
        Button check=new Button("Check");
        check.setOnAction(e-> checkValid(succ));
//        borderPane.setRight(check);
        Button reset=new Button("Reset");
        reset.setOnAction(e->start(primaryStage));
        hbox.getChildren().addAll(check,reset);
        borderPane.setRight(hbox);
        borderPane.setBottom(result);
//        borderPane.getBottom();
        result.setAlignment(Pos.CENTER);
        HBox levelBox=new HBox();
        levelBox.getChildren().addAll(levelTag,levels);
        levels.getSelectionModel().selectFirst();




        borderPane.setLeft(levelBox);
        levels.setOnAction((Event e) -> {
            levels.setValue(levels.getValue());
//            levels.getSelectionModel().select(levels.getValue());
            changeLevel(levels.getValue().toString(), primaryStage, levels);
        });


        borderPane.setCenter(pane);
        primaryStage.setScene(new Scene(borderPane, 300, 275));
        primaryStage.show();


    }
    private Boolean checkValid(NurikabeConfig config){
        System.out.println(config);
        if(!config.isValid()  ){
            System.out.println("Invalid");
            result.setText("You lose!");


        }
        else{
            checkGoal(config);
        }


        return true;
    }
    private Boolean checkGoal(NurikabeConfig config){

        if(config.isGoal()){
//            result.setText("You Win!");
            if(config.isValid()) {
                result.setText("You Win!");
                return true;
            }
            else{
                result.setText("You Lose!");
                return true;
            }

        }

        return false;
    }

    private void changeLevel(String level,Stage S,ComboBox C){

        System.out.println(level);
        try {
            newConfig=new NurikabeConfig("data/input0"+level +".txt");
            start(S);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
