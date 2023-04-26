package com.yan.work.report.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonIO {
    public JsonIO write(String filePath, String jsonStr) throws IOException{
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))){
            writer.write(jsonStr);
        }catch (IOException ex){
            throw new IOException();
        }

        return this;
    };

    public JsonIO clearWrite(String filePath, String jsonStr) throws IOException{
        File file = new File(filePath);
        file.createNewFile();
        return write(filePath, jsonStr);
    }

    public String read(String filePath) throws IOException{
        try (FileInputStream inputStream = new FileInputStream(filePath)){
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IOException();
        }
    };
}
