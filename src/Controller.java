import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Controller {

    private String modelName;

    private String dataName;

    private int LL;

    private Map<String, Object> readedDataMap = new LinkedHashMap<>();

    private Map<String, Object> calculatedDataMap = new LinkedHashMap<>();

    private Object modelInstance;

    public Controller(String modelName) {
        try {
            Constructor<?> constructor = Class.forName("models." + modelName).getDeclaredConstructor();
            this.modelInstance = constructor.newInstance();
            System.out.println(this.modelInstance.getClass());
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }


    }

    public void readDataFrom(String fname) {
        this.dataName = fname;


        try (BufferedReader reader = new BufferedReader(new FileReader(this.dataName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts[0].equals("LATA")) {
                    this.readedDataMap.put("LL", parts.length-1);
                    LL = parts.length-1;
                } else {
                    this.readedDataMap.put(parts[0], parts[1]);
                }

            }
            System.out.println("Ready");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runModel() {
        for (Map.Entry<String, Object> entry : readedDataMap.entrySet()) {
            String fieldName = entry.getKey();
            String valueAsString = entry.getValue().toString();

            try {

                Field field = this.modelInstance.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().isArray() && field.getType().getComponentType() == double.class) {
                    double[] initedArray = new double[this.LL];
                    if (fieldName.equals("twKI") || fieldName.equals("twKS") || fieldName.equals("twINW") || fieldName.equals("twEKS") || fieldName.equals("twIMP")){
                        for (int i = 1 ; i < this.LL ; i++){
                            initedArray[i] = Double.parseDouble(valueAsString);
                        }
                    }
                    initedArray[0] = Double.parseDouble(valueAsString);
                    field.set(this.modelInstance, initedArray);
                } else {

                    if (!field.getName().equals("temp")) {
                        int value = (int) Integer.parseInt(valueAsString);
                        field.set(this.modelInstance, value);
                    }

                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("No field found with name: " + fieldName);
            }
        }

        try {
            Method runMethod = this.modelInstance.getClass().getDeclaredMethod("run");
            runMethod.setAccessible(true);
            runMethod.invoke(this.modelInstance);
            System.out.println(1);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void runScriptFromFile(String fname) {

    }

    public void runScript(String script) {

    }

    public void getResultsAsTsv() {
        StringBuilder tsvBuilder = new StringBuilder();

        try {
            // Get all declared fields in the class
            Field[] fields = this.modelInstance.getClass().getDeclaredFields();

            // Iterate through each field
            for (Field field : fields) {
                field.setAccessible(true); // Access private fields

                // Get the field name and value
                String fieldName = field.getName();
                Object value = field.get(this.modelInstance);

                // Format the field value
                String valueString;
                if (value instanceof double[]) {
                    // Convert arrays to a comma-separated string
                    valueString = Arrays.toString((double[]) value)
                            .replace("[", "")
                            .replace("]", "")
                            .replace(",", "\t");
                } else if (value instanceof int[]) {
                    valueString = Arrays.toString((int[]) value)
                            .replace("[", "")
                            .replace("]", "")
                            .replace(",", "\t");
                } else {
                    // Convert other values to string directly
                    valueString = value != null ? value.toString() : "null";
                }

                // Append the field name and value to the TSV
                tsvBuilder.append(fieldName).append("\t").append(valueString).append("\n");

            }
            System.out.println(tsvBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
