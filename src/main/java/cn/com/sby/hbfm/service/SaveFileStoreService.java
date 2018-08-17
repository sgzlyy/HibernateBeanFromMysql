package cn.com.sby.hbfm.service;

import java.io.File;

import cn.com.sby.common.FileUtil;
import cn.com.sby.common.JaxbUtil;
import cn.com.sby.hbfm.model.PersistenceObject;

public class SaveFileStoreService {

    public void save(PersistenceObject object, File save2File) {
        FileUtil.write2File(save2File, JaxbUtil.convertToXml(object, "UTF-8"), "UTF-8");
    }
}
