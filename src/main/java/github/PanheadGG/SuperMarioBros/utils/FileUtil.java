package github.PanheadGG.SuperMarioBros.utils;

import jdk.nashorn.api.scripting.URLReader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class FileUtil {
    static public String readFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator()).append(s);
            }
            return result.toString();
        } catch (IOException e) {
            return null;
        }
    }
    static public String readFile(File file){
        return readFile(file.getAbsolutePath());
    }
    static public String readFile(String filePath, String charset){
        String string = readFile(filePath);
        if(string == null) return null;
        try {
            return new String(string.getBytes(), charset);
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }
    static public String readFile(File file, String charset){
        return readFile(file.getAbsolutePath(), charset);
    }
    static public boolean writeFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(content);
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }
    static public boolean writeFile(File file, String content) {
        return writeFile(file.getAbsolutePath(), content);
    }
    static public boolean writeFile(String filePath, String content, String charset) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(new String(content.getBytes(), charset));
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }
    static public boolean writeFile(File file, String content, String charset) {
        return writeFile(file.getAbsolutePath(), content, charset);
    }

    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                result.append(tempString).append("\n");
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return result.toString();
    }

    public static String readFile(URL url) {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new URLReader(url));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                result.append(tempString).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return result.toString();
    }

    public static void main(String[] args) throws MalformedURLException {
        String filePath = "E:\\Code\\Java\\IdealC\\SuperMarioBros\\src\\main\\resources\\assets\\map\\level_1.json";
        URL url = new File(filePath).toURI().toURL();
        System.out.println(readFile(url));
    }

}
