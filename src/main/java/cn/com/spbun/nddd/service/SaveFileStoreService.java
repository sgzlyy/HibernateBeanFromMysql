package cn.com.spbun.nddd.service;

import java.io.File;

import cn.com.spbun.common.FileUtil;
import cn.com.spbun.common.JaxbUtil;
import cn.com.spbun.nddd.model.PersistenceObject;

public class SaveFileStoreService {

    public void save(PersistenceObject object, File save2File) {
        FileUtil.write2File(save2File, JaxbUtil.convertToXml(object, "UTF-8"), "UTF-8");
    }
}
