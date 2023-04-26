package com.yan.work.report.utils;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
public abstract class AbstractJsonCurd<T> extends JsonIO implements ICrudUtil<T>{

    protected final String recordFolderPath;
    protected final String fileName;
    protected final String recodeFileFullPath;

    public AbstractJsonCurd(String recordFolderPath, String fileName, String recodeFileFullPath) {
        this.recordFolderPath = recordFolderPath;
        this.fileName = fileName;
        this.recodeFileFullPath = recodeFileFullPath;
        verifyDataFolderExist();
        verifyJsonFileExist();
    }

    private void verifyDataFolderExist() {
        File recordFolder = new File(recordFolderPath);
        if (!recordFolder.exists()) {
            try {
                Files.createDirectories(recordFolder.toPath());
                log.info("[AbstractJsonCurd] ----- folder generate success - {}", LocalDate.now());
            }catch (IOException ex){
                log.error("[AbstractJsonCurd] ----- folder generate error - {} \n{}", LocalDate.now(), ex.getMessage());
            }
        }
    }

    private void verifyJsonFileExist() {
        File jsonFile = new File(recodeFileFullPath);
        if (!jsonFile.exists()){
            try {
                Files.createFile(jsonFile.toPath());
                log.info("[AbstractJsonCurd] ----- json file generate success - {} \n", LocalDate.now());
            } catch (IOException ex) {
                log.error("[AbstractJsonCurd] ----- json file generate error - {} \n{}", LocalDate.now(), ex.getMessage());
            }
        }
    }

    @Override
    public Optional<T> select() {
        verifyDataFolderExist();
        verifyJsonFileExist();

        T selectObject = null;
        try {
            String jsonStr = read(recodeFileFullPath);
            selectObject = new Gson().fromJson(jsonStr,
                    (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        } catch (IOException ex) {
            log.error("[AbstractJsonCurd] ----- select data error - {} \n{}", LocalDate.now(), ex.getMessage());
        }

        return Optional.ofNullable(selectObject);
    }

    public Optional<T> select(String successMessage, String errorMessage) {
        verifyDataFolderExist();
        verifyJsonFileExist();

        T selectObject = null;
        try {
            String jsonStr = read(recodeFileFullPath);
            selectObject = new Gson().fromJson(jsonStr,
                    (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
            log.debug("[AbstractJsonCurd] ----- {} - {} \n", successMessage, LocalDate.now());
        } catch (IOException ex) {
            log.error("[AbstractJsonCurd] ----- {} - {} \n{}", errorMessage, LocalDate.now(), ex.getMessage());
        }

        return Optional.ofNullable(selectObject);
    }

    @Override
    public Boolean insert(Object insertObj) {
        verifyDataFolderExist();
        verifyJsonFileExist();

        try {
            String jsonStr = new Gson().toJson((T)insertObj);
            write(recodeFileFullPath, jsonStr);
            return true;
        } catch (IOException ex) {
            log.error("[AbstractJsonCurd] ----- insert data error - {} \n{}", LocalDate.now(), ex.getMessage());
            return false;
        }
    }

    public Boolean insert(Object insertObj, String successMessage, String errorMessage) {
        verifyDataFolderExist();
        verifyJsonFileExist();

        try {
            String jsonStr = new Gson().toJson((T)insertObj);
            write(recodeFileFullPath, jsonStr);
            log.debug("[AbstractJsonCurd] ----- {} - {}",successMessage, LocalDate.now());
            return true;
        } catch (IOException ex) {
            log.error("[AbstractJsonCurd] ----- {} - {} \n{}", errorMessage, LocalDate.now(), ex.getMessage());
            return false;
        }
    }

    public Boolean clearInset(Object insertObj, String successMessage, String errorMessage){
        try {
            String jsonStr = new Gson().toJson((T)insertObj);
            clearWrite(recodeFileFullPath, jsonStr);
            log.debug("[AbstractJsonCurd] ----- {} - {}",successMessage, LocalDate.now());
            return true;
        } catch (IOException ex) {
            log.error("[AbstractJsonCurd] ----- {} - {} \n{}", errorMessage, LocalDate.now(), ex.getMessage());
            return false;
        }
    }
}
