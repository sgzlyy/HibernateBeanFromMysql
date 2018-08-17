package cn.com.spbun.nddd.service;

import java.io.File;

import cn.com.spbun.common.FileUtil;
import cn.com.spbun.common.JaxbUtil;
import cn.com.spbun.nddd.model.PersistenceObject;

public class SaveFileLoadService {

    public PersistenceObject load(File openFile) {

        PersistenceObject object = null;

        if (openFile != null && openFile.exists()) {

            object = JaxbUtil.converyToJavaBean(FileUtil.read2String(openFile, "UTF-8"), PersistenceObject.class);

        }

        if (object == null) {
            return mkDefaultPersistenceObject();
        }

        return object;
    }

    private PersistenceObject mkDefaultPersistenceObject() {
        PersistenceObject object = new PersistenceObject();
        return object;
    }
}
