package pro3assi;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.*;

public class Calculator extends Application {

    private TextField num1Field, num2Field;
    private Label resultDisplay, historyDisplay;
    private final String FILE_NAME = "history.txt";

    @Override
    public void start(Stage primaryStage) {
        Label lblNum1 = new Label("Number 1:");
        num1Field = new TextField();
        
        Label lblNum2 = new Label("Number 2:");
        num2Field = new TextField();
        num2Field.setPromptText("Enter second number");

        Button addBtn = new Button("+");
        Button subBtn = new Button("-");
        Button mulBtn = new Button("*");
        Button divBtn = new Button("/");
        
        addBtn.setMinWidth(60); subBtn.setMinWidth(60); 
        mulBtn.setMinWidth(60); divBtn.setMinWidth(60);

        Label lblResultTitle = new Label("Result:");
        resultDisplay = new Label(""); // هنا ستظهر النتيجة

        Button clearBtn = new Button("Clear");
        Button historyBtn = new Button("History");
        clearBtn.setMinWidth(100); historyBtn.setMinWidth(100);

        Label lblHistoryTitle = new Label("History:");
        historyDisplay = new Label(""); 

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(25));

        grid.add(lblNum1, 0, 0); grid.add(num1Field, 1, 0);
        grid.add(lblNum2, 0, 1); grid.add(num2Field, 1, 1);

        HBox opsBox = new HBox(10, addBtn, subBtn, mulBtn, divBtn);
        opsBox.setAlignment(Pos.CENTER);
        grid.add(opsBox, 0, 2, 2, 1);

        grid.add(lblResultTitle, 0, 3, 2, 1);
        lblResultTitle.setAlignment(Pos.CENTER);
        lblResultTitle.setMaxWidth(Double.MAX_VALUE);
        
        grid.add(resultDisplay, 0, 4, 2, 1);
        resultDisplay.setAlignment(Pos.CENTER);
        resultDisplay.setMaxWidth(Double.MAX_VALUE);

        HBox controlBox = new HBox(15, clearBtn, historyBtn);
        controlBox.setAlignment(Pos.CENTER);
        grid.add(controlBox, 0, 5, 2, 1);

        grid.add(lblHistoryTitle, 0, 6, 2, 1);
        lblHistoryTitle.setAlignment(Pos.CENTER);
        lblHistoryTitle.setMaxWidth(Double.MAX_VALUE);

        grid.add(historyDisplay, 0, 7, 2, 1);
        historyDisplay.setAlignment(Pos.CENTER);

        addBtn.setOnAction(e -> process('+'));
        subBtn.setOnAction(e -> process('-'));
        mulBtn.setOnAction(e -> process('*'));
        divBtn.setOnAction(e -> process('/'));

        clearBtn.setOnAction(e -> {
            num1Field.clear();
            num2Field.clear();
            resultDisplay.setText("");
            historyDisplay.setText("");
            try { Files.deleteIfExists(Paths.get(FILE_NAME)); } catch (Exception ex) {}
        });

        historyBtn.setOnAction(e -> loadHistory());

        Scene scene = new Scene(grid, 450, 550);
        primaryStage.setTitle("JavaFX Calculator With History");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void process(char operator) {
        try {
            double n1 = Double.parseDouble(num1Field.getText());
            double n2 = Double.parseDouble(num2Field.getText());
            double res = 0;

            if (operator == '/' && n2 == 0) {
                resultDisplay.setText("Error: Division by zero");
                return;
            }

            switch (operator) {
                case '+': res = n1 + n2; break;
                case '-': res = n1 - n2; break;
                case '*': res = n1 * n2; break;
                case '/': res = n1 / n2; break;
            }

            String record = n1 + " " + operator + " " + n2 + " = " + res;
            resultDisplay.setText(String.valueOf(res));
            
            try (FileWriter fw = new FileWriter(FILE_NAME, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(record);
                bw.newLine();
            }
        } catch (Exception e) {
            resultDisplay.setText("Invalid Input!");
        }
    }

    private void loadHistory() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            historyDisplay.setText(content);
        } catch (IOException e) {
            historyDisplay.setText("No history available.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
