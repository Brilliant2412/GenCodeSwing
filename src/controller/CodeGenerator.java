package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import exceptions.WrongExcelTypeException;
import model.ColumnProperty;
import model.TableInfo;
import model.TableSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;


public class CodeGenerator {
    static String url;
    static String pathString;
    static String pathOne;
    static String pathTwo;
    static String pathSubObjs;
    public static final byte[] BUFFER = new byte[1024];

    public CodeGenerator() {
        url="";
        pathString = "";
    }

    public CodeGenerator(String url,String path) {
        setUrl(url);
        setPathString(path);
    }

    public static String getPathString() {
        return pathString;
    }

    public static void setPathString(String pathString) {
        CodeGenerator.pathString = pathString;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        CodeGenerator.url = url;
    }

    public static String capitalize(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String uncapitalize(String s){
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public void GEN(int sheet, Vector<Integer> numSubObjs, int excelType) throws IOException, WrongExcelTypeException {
        TableSet tableSet = new TableSet(url, sheet, numSubObjs, excelType);
        pathOne = pathString+"\\"+ tableSet.tableInfo.tableName+"\\"+"service";
        pathTwo = pathString+"\\"+ tableSet.tableInfo.tableName+"\\"+"web";
        //pathSubObjs = pathString+"\\"+ tableSet.tableInfo.tableName+"\\"+"sub objects";
        Service.genService(tableSet ,pathOne);
        Web.genWeb(tableSet, pathTwo);
        for(int i = 0; i < tableSet.subTables.size(); i++){
            SubObj.genSubObj(tableSet.subTables.get(i), pathTwo,tableSet.tableInfo.tableName);
        }

        String jar_file = pathString+"\\" + tableSet.tableInfo.tableName +".jar";
        String input_folder = pathString+"\\"+ tableSet.tableInfo.tableName;

        File outputZipfile = new File(jar_file);
        File inputDir = new File(input_folder);
        createJarFile(inputDir, outputZipfile);
//        File current = new File(pathString +"\\"+tableSet.tableInfo.tableName);
//        deleteFolder(current);

    }
    public static void createJarFile(File inputDir, File outputJarFile){
        outputJarFile.getParentFile().mkdirs();
        String inputDirPath = inputDir.getAbsolutePath();

        // prepare Manifest file
        String version = "1.0.0";
        String author = "hung";
        Manifest manifest = new Manifest();
        Attributes global = manifest.getMainAttributes();
        global.put(Attributes.Name.MANIFEST_VERSION, version);
        global.put(new Attributes.Name("Created-By"), author);

        FileOutputStream fos = null;
        JarOutputStream jos = null;
        try {

            List<File> allFiles = listChildFiles(inputDir);

            // Tạo đối tượng JarOutputStream để ghi file jar.
            fos = new FileOutputStream(outputJarFile);
            jos = new JarOutputStream(fos, manifest);
            for (File file : allFiles) {
                String filePath = file.getAbsolutePath();

                System.out.println("Creating jar: " + filePath);
                // entryName: is a relative path.
                String entryName = filePath.substring(inputDirPath.length() + 1);

                // create JarEntry
                JarEntry je = new JarEntry(entryName);
                je.setComment("Creating Jar");
                je.setTime(Calendar.getInstance().getTimeInMillis());
                // Thêm entry vào file jar.
                jos.putNextEntry(je);

                // Đọc dữ liệu của file và ghi vào JarOutputStream.
                InputStream fileIs = new FileInputStream(filePath);
                int len;
                while ((len = fileIs.read(BUFFER)) > 0) {
                    jos.write(BUFFER, 0, len);
                }
                fileIs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(jos);
            closeStream(fos);
        }
    }
    private static void closeStream(OutputStream out) {
        try {
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static List<File> listChildFiles(File dir) throws IOException {
        List<File> allFiles = new ArrayList<>();

        File[] childFiles = dir.listFiles();
        for (File file : childFiles) {
            if (file.isFile()) {
                allFiles.add(file);
            } else {
                List<File> files = listChildFiles(file);
                allFiles.addAll(files);
            }
        }
        return allFiles;
    }
    private  static void deleteFolder(File file){
        for (File subFile : file.listFiles()) {
            if(subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        file.delete();
    }
}