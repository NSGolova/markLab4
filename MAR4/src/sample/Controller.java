package sample;

import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Arrays;

public class Controller
{
    @FXML
    GridPane functionGrid;
    @FXML
    GridPane systemGrid;
    @FXML
    TextField params;
    @FXML
    TextField limits;
    @FXML
    Label functionLabel;

    TextField paramsFields[], limitsFields[][];
    ComboBox<String> boxes[];

    @FXML
    public void initialize()
    {
    }

    @FXML
    public void generate()
    {
        functionGrid.getChildren().clear();
        systemGrid.getChildren().clear();

        functionLabel.setOpacity(1);

        int paramsCount = Integer.parseInt(params.getText());
        int limitsCount = Integer.parseInt(limits.getText());

        paramsFields = new TextField[paramsCount];
        limitsFields = new TextField[limitsCount][paramsCount + 1];
        boxes = new ComboBox[limitsCount];

        functionGrid.setPrefWidth(paramsCount * 60);
        systemGrid.setPrefHeight(limitsCount * 27);

        for (int i = 0; i < limitsCount; i++) {
            for (int j = 0; j < paramsCount * 2 + 1; j += 2) {

                if (i == 0 && j < paramsCount * 2) {
                    Label x = new Label(" X" + (j / 2 + 1));
                    x.setMinWidth(20);
                    x.setMaxWidth(20);
                    x.setPrefWidth(20);
                    functionGrid.add(x, j + 1, 0);

                    TextField param = new TextField();
                    param.setMinWidth(40);
                    param.setMaxWidth(40);
                    param.setPrefWidth(40);
                    functionGrid.add(param, j, 0);
                    paramsFields[j / 2] = param;
                }

                TextField systemParam = new TextField();
                systemParam.setMaxWidth(30);
                if (j < paramsCount * 2) {
                    Label x = new Label("X" + (j / 2 + 1));
                    x.setMinWidth(30);
                    x.setMaxWidth(30);
                    x.setPrefWidth(30);
                    systemGrid.add(x, j + 1, i);
                    systemGrid.add(systemParam, j, i);
                } else {
                    ComboBox<String> box = new ComboBox<>();
                    box.setItems(new ObservableListBase<String>() {
                        @Override
                        public String get(int index) {
                            switch (index)
                            {
                                case 0:
                                    return "<";
                                case 1:
                                    return "=";
                                case 2:
                                    return ">";
                                default:
                                    return "=";
                            }
                        }

                        @Override
                        public int size() {
                            return 3;
                        }
                    });

                    box.setMinWidth(60);
                    box.setMaxWidth(60);
                    box.setPrefWidth(60);
                    systemGrid.add(box, j, i);
                    systemGrid.add(systemParam, j + 1, i);
                    boxes[i] = box;
                }



                limitsFields[i][j / 2] = systemParam;

            }
        }
    }

    @FXML
    public void calculate()
    {
        double params[] = new double[paramsFields.length];
        double limits[][] = new double[limitsFields.length][limitsFields[0].length];
        boolean sign[] = new boolean[limitsFields.length];

        for (int i = 0; i < params.length; i++)
        {
            try {
                params[i] = Double.parseDouble(paramsFields[i].getText());
                paramsFields[i].setStyle("");
            } catch (Exception e) {
                paramsFields[i].setStyle("-fx-background-color: red");
            }
        }
        for (int i = 0; i < limits.length; i++)
        {
            for (int j = 0; j < limits[i].length; j++)
            {
                try {
                    limits[i][j] = Double.parseDouble(limitsFields[i][j].getText());
                    limitsFields[i][j].setStyle("");
                } catch (Exception e) {
                    limitsFields[i][j].setStyle("-fx-background-color: red");
                }
            }
            try {
                sign[i] = boxes[i].getValue().equalsIgnoreCase(">");
                boxes[i].setStyle("");
            } catch (Exception e) {
                boxes[i].setStyle("-fx-background-color: red");
            }
        }

        int choosenColumn = -1, choosenRow = -1;

        for (double i = 0, max = 0; i < params.length; i++)
        {
            if (Math.abs(params[(int)i]) > max)
            {
                max = Math.abs(params[(int)i]);
                choosenColumn = (int)i;
            }
        }
        if (choosenColumn > -1)
        {
            double max = 0;
            for (int i = 0; i < limits.length; i++)
            {
                double div = limits[i][limits[i].length - 1] * 1.0 / limits[i][choosenColumn];
                if (Math.abs(div) > max)
                {
                    choosenRow = i;
                    max = Math.abs(div);
                }
            }
        }

        if (choosenRow > -1 && choosenColumn > -1) {

            limitsFields[choosenRow][choosenColumn].setStyle("-fx-background-color: green");

            double paramsCopy[] = new double[paramsFields.length];
            double limitsCopy[][] = new double[limitsFields.length][limitsFields[0].length];
            paramsCopy[choosenColumn] = params[choosenColumn] / limits[choosenRow][choosenColumn];
            for (int i = 0; i < limits.length; i++) {
                if (i != choosenRow) {
                    limitsCopy[i][choosenColumn] = limits[i][choosenColumn] / limits[choosenRow][choosenColumn];
                } else {
                    limitsCopy[i][choosenColumn] = 1.0 / limits[choosenRow][choosenColumn];
                }
            }
            for (int i = 0; i < limits.length; i++) {
                if (i != choosenRow) {
                    for (int j = 0; j < limits[i].length; j++) {
                        if (j != choosenColumn) {
                            limitsCopy[i][j] = (limits[i][j] * limits[choosenRow][choosenColumn] - limits[choosenRow][j] * limits[i][choosenColumn]) / limits[choosenRow][choosenColumn];
                        }
                    }
                }
            }


            for (int i = 0; i < params.length; i++) {
                paramsFields[i].setText((paramsCopy[i] + "").substring(0, 3));
            }
            for (int i = 0; i < limits.length; i++) {
                for (int j = 0; j < limitsCopy[i].length; j++) {
                    limitsFields[i][j].setText((limitsCopy[i][j] + "").substring(0, 3));
                }
            }
        } else {

        }
    }
}
