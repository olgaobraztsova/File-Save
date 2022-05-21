import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {

        String path = "D://Games/savegames/";

        // создаем объекты с сохранением игры
        GameProgress gameSave1 = new GameProgress(9, 20, 4, 2.3);
        GameProgress gameSave2 = new GameProgress(5, 25, 7, 12.4);
        GameProgress gameSave3 = new GameProgress(3, 12, 9, 15.3);

        //сохраняем объекты в файлы
        File fileSave1 = createGameSave(gameSave1, path + "game1.dat");
        File fileSave2 = createGameSave(gameSave2, path + "game2.dat");
        File fileSave3 = createGameSave(gameSave3, path + "game3.dat");

        //создаем список сохраненных объектов
        String[] fileList = new String[]{
                fileSave1.getCanonicalPath(),
                fileSave2.getCanonicalPath(),
                fileSave3.getCanonicalPath()
        };

        //упаковываем в zip архив
        zipFiles(fileList, path + "archive.zip");


        //удаляем файлы *.dat не в архиве
        if (fileSave1.delete()) {
            System.out.println("Файл " + fileSave1.getName() + " успешно удален");
        }
        ;

        if (fileSave2.delete()) {
            System.out.println("Файл " + fileSave2.getName() + " успешно удален");
        }
        ;

        if (fileSave3.delete()) {
            System.out.println("Файл " + fileSave3.getName() + " успешно удален");
        }
        ;

        //распаковываем архив
        openZip(path + "archive.zip", path);

        //десериализуем первое сохранение игры и выводим на экран
        System.out.println(openProgress(path + "game1.dat"));
    }


    // функция для сохранения объекта в файле
    public static File createGameSave(GameProgress gameProgress, String path) {
        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    // функция для упаковки (сериализации) нескольких файлов в архив
    public static void zipFiles(String[] files, String path) {
        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path));

            for (String file : files) {
                File f = new File(file);
                FileInputStream fis = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(f.getName());
                zout.putNextEntry(entry);
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                zout.write(bytes);
                zout.closeEntry();
                fis.close();
            }
            zout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //функция для разархивирования
    public static void openZip(String zipPath, String folderPath) {

        try (FileInputStream fis = new FileInputStream(zipPath);
             ZipInputStream zin = new ZipInputStream(fis)) {

            ZipEntry entry;
            String name;

            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(folderPath + name);
                for (int i = zin.read(); i != -1; i = zin.read()) {
                    fout.write(i);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //функция для десериализации объекта

    public static GameProgress openProgress(String path) {
        GameProgress game = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            game = (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return game;
    }
}
