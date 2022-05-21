import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {

        // создаем объекты с сохранением игры
        GameProgress gameSave1 = new GameProgress(9, 20, 4, 2.3);
        GameProgress gameSave2 = new GameProgress(5, 25, 7, 12.4);
        GameProgress gameSave3 = new GameProgress(3, 12, 9, 15.3);

        //сохраняем объекты в файлы
        File fileSave1 = createGameSave(gameSave1, "D://Games/savegames/game1.dat");
        File fileSave2 = createGameSave(gameSave2, "D://Games/savegames/game2.dat");
        File fileSave3 = createGameSave(gameSave3, "D://Games/savegames/game3.dat");

        //создаем список сохраненных объектов
        String[] fileList = new String[] {
                fileSave1.getCanonicalPath(),
                fileSave2.getCanonicalPath(),
                fileSave3.getCanonicalPath()
        };

        //упаковываем в zip архив
        zipFiles(fileList, "D://Games/savegames/archive.zip");


        //удаляем файлы *.dat не в архиве
        if (fileSave1.delete()){
            System.out.println("Файл " + fileSave1.getName() + " успешно удален");
        };

        if (fileSave2.delete()){
            System.out.println("Файл " + fileSave2.getName() + " успешно удален");
        };

        if (fileSave3.delete()){
            System.out.println("Файл " + fileSave3.getName() + " успешно удален");
        };
    }


    // функция для сохранения объекта в файле
    public static File createGameSave(GameProgress gameProgress, String path ) {
        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    // фукнция для упаковки нескольких файлов в архив
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
}
