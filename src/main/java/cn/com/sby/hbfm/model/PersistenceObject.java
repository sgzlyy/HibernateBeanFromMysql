package cn.com.sby.hbfm.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 持久化的对象
 * 
 *
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "persistence_object")
@XmlType(name = "persistence_object", propOrder = { "connectionInfos" })
public class PersistenceObject {

    @XmlElementWrapper(name = "connectionInfos")
    @XmlElement(name = "connectionInfo")
    private List<ConnectionInfo> connectionInfos = new ArrayList<ConnectionInfo>();

    public List<ConnectionInfo> getConnectionInfos() {
        return connectionInfos;
    }

    public void setConnectionInfos(List<ConnectionInfo> connectionInfos) {
        this.connectionInfos = connectionInfos;
    }

}
