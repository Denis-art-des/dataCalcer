import com.sun.jdi.event.StepEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class Service {

    private static String selectedModelName;
    private static String selectedDataName;

    private static final String modelPath = "src/models";

    private static final String dataPath = "resources/data";

    public static ArrayList<String> findModels() {
        ArrayList<String> models = new ArrayList<>();
        File dir = new File(modelPath);
        File[] files = dir.listFiles((dir1, name) -> {
            return name.endsWith(".java");
        });

        if (files != null) {
            for (File file : files) {
                models.add(file.getName().replace(".java", ""));
            }
        }
        return models;
    }

    public static ArrayList<String> findData() {

        ArrayList<String> data = new ArrayList<>();
        File dir = new File(dataPath);
        File[] files = dir.listFiles((dir1, name) -> {
            return name.endsWith(".txt");
        });

        if (files != null){
            for (File file : files){
                data.add(file.getName().replace(".txt", ""));
            }
        }
        return data;
    }

    public static void setSelectedModelName(String model){
        selectedModelName = model;
        System.out.println(selectedModelName);
    }

    public static void setSelectedDataName(String data){
        selectedDataName = data;
        System.out.println(selectedDataName);
    }

    public static String getSelectedModelName(){
        return selectedModelName;
    }

    public static String getSelectedDataName(){
        return selectedDataName;
    }

    public static void runController(){
        Controller crl = new Controller(selectedModelName);
        crl.readDataFrom(dataPath + "/" + selectedDataName + ".txt");
        crl.runModel();
        crl.getResultsAsTsv();
    }

    public JPanel CalculatedDataPanel() {
        return null;
    }
}
