package cn.com.spbun.nddd.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 数据连接信息
 * 
 * @author NOLY DAKE
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "connectionInfos")
@XmlType(name = "connectionInfo", propOrder = { "name", "jdbcDriverName", "jdbcConnString", "username", "password",
		"packageNameString", "tablePrefixString", "saveModelPathString" })
public class ConnectionInfo {

	/**
	 * 包路径名称
	 */
	@XmlElement(name = "packageNameString")
	private String packageNameString = null;

	/**
	 * model存放路径
	 */
	@XmlElement(name = "saveModelPathString")
	private String saveModelPathString = null;

	/**
	 * 表前缀
	 */
	@XmlElement(name = "tablePrefixString")
	private String tablePrefixString = null;

	/**
	 * 连接字符串
	 */
	@XmlElement(name = "jdbcConnString")
	private String jdbcConnString = null;

	/**
	 * JDBC驱动名称
	 */
	@XmlElement(name = "jdbcDriverName")
	private String jdbcDriverName = null;

	/**
	 * 连接名称
	 */
	@XmlElement(name = "name")
	private String name = null;

	/**
	 * 密码
	 */
	@XmlElement(name = "password")
	private String password = null;

	/**
	 * 用户名
	 */
	@XmlElement(name = "username")
	private String username = null;

	public String getJdbcConnString() {
		return jdbcConnString;
	}

	public String getJdbcDriverName() {
		return jdbcDriverName;
	}

	public String getName() {
		return name;
	}

	public String getPackageNameString() {
		return packageNameString;
	}

	public String getPassword() {
		return password;
	}

	public String getSaveModelPathString() {
		return saveModelPathString;
	}

	public String getTablePrefixString() {
		return tablePrefixString;
	}

	public String getUsername() {
		return username;
	}

	public void setJdbcConnString(String jdbcConnString) {
		this.jdbcConnString = jdbcConnString;
	}

	public void setJdbcDriverName(String jdbcDriverName) {
		this.jdbcDriverName = jdbcDriverName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPackageNameString(String packageNameString) {
		this.packageNameString = packageNameString;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSaveModelPathString(String saveModelPathString) {
		this.saveModelPathString = saveModelPathString;
	}

	public void setTablePrefixString(String tablePrefixString) {
		this.tablePrefixString = tablePrefixString;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "ConnectionInfo [packageNameString=" + packageNameString + ", saveModelPathString=" + saveModelPathString
				+ ", tablePrefixString=" + tablePrefixString + ", jdbcConnString=" + jdbcConnString
				+ ", jdbcDriverName=" + jdbcDriverName + ", name=" + name + ", password=" + password + ", username="
				+ username + "]";
	}

}
