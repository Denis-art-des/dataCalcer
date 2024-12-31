import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Controller {

    private String modelName;

    private String dataName;

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
                    this.readedDataMap.put("LL", parts.length);
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
        Field[] fields = this.modelInstance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isArray() && !field.getName().equals("temp")) {
                field.setAccessible(true);
                Object array = null;
                try {
                    array = field.get(this.modelInstance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                Object value = this.readedDataMap.get(field.getName());
                if (value != null && value.getClass().isArray()) {
                    double[] sourceArray = (double[]) value;

                    int length = sourceArray.length;
                    if (array instanceof double[]) {
                        double[] targetArray = (double[]) array;
                        for (int i = 0; i < length; i++) {
                            targetArray[i] = sourceArray[i];
                        }
                    }

                }
            } else if (field.getType().isPrimitive()) {
                try{
                    if (field.get(this.modelInstance) instanceof Integer){
                        field.set(this.modelInstance, this.readedDataMap.get(field.getName()));
                    }
                } catch (IllegalAccessException e) {
                    continue;
                }
            }
        }
    }

    public void runScriptFromFile(String fname) {

    }

    public void runScript(String script) {

    }

    public void getResultsAsTsv() {

    }
}
