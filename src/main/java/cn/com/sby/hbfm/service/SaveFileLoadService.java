package cn.com.sby.hbfm.service;

import java.io.File;

import cn.com.sby.common.FileUtil;
import cn.com.sby.common.JaxbUtil;
import cn.com.sby.hbfm.model.PersistenceObject;

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
