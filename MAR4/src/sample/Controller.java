package sample;

import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

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
    @FXML
    GridPane resultGrid;

    TextField paramsFields[], limitsFields[][];
    ComboBox<String> boxes[];

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

        functionGrid.setPrefWidth(paramsCount * 70);
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
                    param.setMinWidth(50);
                    param.setMaxWidth(50);
                    param.setPrefWidth(50);
                    functionGrid.add(param, j, 0);
                    paramsFields[j / 2] = param;
                }

                TextField systemParam = new TextField();
                systemParam.setMaxWidth(50);
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
        resultGrid.getChildren().clear();
        double limits[][] = new double[limitsFields.length + 1][limitsFields[0].length];
        boolean sign[] = new boolean[limitsFields.length];

        for (int i = 0; i < paramsFields.length; i++)
        {
            try {
                limits[limits.length - 1][i + 1] = -1.0 * Double.parseDouble(paramsFields[i].getText());
                paramsFields[i].setStyle("");
            } catch (Exception e) {
                paramsFields[i].setStyle("-fx-background-color: red");
            }
        }
        limits[limits.length - 1][0] = 0.0;

        for (int i = 0; i < limitsFields.length; i++)
        {
            for (int j = 0; j < limitsFields[i].length; j++)
            {
                try {
                    if (j < limitsFields[i].length - 1) {
                        limits[i][j + 1] = Double.parseDouble(limitsFields[i][j].getText());
                    } else {
                        limits[i][0] = Double.parseDouble(limitsFields[i][j].getText());
                    }
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

        for (double l[] : limits)
        {
            for (double ll : l)
            {
                System.out.print(ll + "  ");
            }
            System.out.println();
        }

        double[] result;
        Model s = new Model(limits);
        result = s.Calculate();

        for (int i = 0; i < result.length - 1; i++)
        {
            Label x = new Label("X" + (i + 1) + " = ");
            x.setPrefWidth(40);
            x.setMinWidth(40);
            resultGrid.add(x, 0, i);
            resultGrid.add(new TextField("" + result[i]), 1, i);
        }

    }
}
