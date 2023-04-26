package com.yan.work.report.utils;

import com.google.gson.Gson;
import com.yan.work.report.model.WorkData;
import com.yan.work.report.model.WorkMatter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class WorkDataJSonUtil extends AbstractJsonCurd<WorkData>{

    public WorkDataJSonUtil(String recordFolderPath, String fileName, String recodeFileFullPath) {
        super(recordFolderPath, fileName, recodeFileFullPath);
    }

    @Override
    public Optional<WorkData> select() {
        return super.select("select WorkData success", "select WorkData error");
    }

    @Override
    public Boolean insert(Object insertObj) {
        Optional<WorkData> workDataOptional = this.select();
        boolean jsonFileIsEmpty = !workDataOptional.isPresent();

        WorkData workData = workDataOptional.orElse(new WorkData());
        int count = jsonFileIsEmpty ? 1 : workData.getLastNumber() + 1;
        Map<String, WorkMatter> workMatters = jsonFileIsEmpty ? new HashMap<>() : workData.getWorkMatters();

        workMatters.put(Integer.toString(count), (WorkMatter) insertObj);
        workData.setWorkMatters(workMatters);
        workData.setLastNumber(count);

        return super.insert(workData,"insert WorkData success","insert WorkData error");
    }

    @Override
    public Boolean update(String key, Object updateObj) {
        Optional<WorkData> workDataOptional = select();
        boolean jsonFileIsEmpty = !workDataOptional.isPresent();

        if (!jsonFileIsEmpty){
            WorkData workData =  workDataOptional.get();
            Map<String,WorkMatter> workMatters = workData.getWorkMatters();
            workMatters.put(key, (WorkMatter) updateObj);

            return super.clearInset(workData,"update WorkData success","update WorkData error");
        }
        return false;
    }

    @Override
    public Boolean delete(Object key) {
        Optional<WorkData> workDataOptional = select();
        boolean jsonFileIsEmpty = !workDataOptional.isPresent();

        if (!jsonFileIsEmpty) {
            WorkData workData = workDataOptional.get();
            Map<String, WorkMatter> workMatters = workData.getWorkMatters();
            workMatters.remove((String) key);
            workData.setWorkMatters(workMatters);

            try {
                String workDataJsonStr = new Gson().toJson(workData);
                write(recodeFileFullPath, workDataJsonStr);
                log.info("[WorkDataJSonUtil] ----- delete key:{} success - {}", key, LocalDate.now());
                return true;
            } catch (IOException ex) {
                log.error("[WorkDataJSonUtil] ----- delete key:{} error - {} \n{}", key, LocalDate.now(), ex.getMessage());
                return false;
            }
        }
        log.info("[WorkDataJSonUtil] ----- data is empty - {}", LocalDate.now());
        return false;
    }
}
