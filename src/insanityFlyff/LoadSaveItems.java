package insanityFlyff;

import java.io.*;
import java.util.List;

/**
 * Created by Kounex on 26.06.2015.
 */
public class LoadSaveItems {

    private static String fileName = "savedItems.bin";

    public static void saveObject(List<IngameItem> allIngameItems) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName))) {
            os.writeObject(allIngameItems);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadObject(List<IngameItem> allIngameItems, AppStart mainApp) {
        if(!new File(LoadSaveItems.fileName).exists()) {
            try {
                new File(LoadSaveItems.fileName).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName))) {
                allIngameItems = (List<IngameItem>) is.readObject();
                mainApp.refreshItemList(allIngameItems);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
