package cn.com.sby.hbfm.gui.connselect;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import cn.com.sby.common.ui.ext.JButtonExt;
import cn.com.sby.common.ui.ext.JPanelExt;
import cn.com.sby.common.ui.ext.JScrollPaneExt;
import cn.com.sby.common.ui.ext.JTableExt;
import cn.com.sby.common.ui.util.TableUtil;
import cn.com.sby.hbfm.NdddApplication;
import cn.com.sby.hbfm.gui.common.table.TableModelExt;
import cn.com.sby.hbfm.gui.major.MainFrame;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.PersistenceObject;

/**
 * 选择数据库连接
 * 
 *
 *
 */
public class ConnectionSelectFrame extends JFrame {

	/**
	 * 日志对象
	 */
	private static final Logger LOG = Logger.getLogger(ConnectionSelectFrame.class);

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3077796613352304006L;

	/**
	 * 用来记录所有的数据库连接
	 */
	private List<ConnectionInfo> cacheInfo = null;

	private JTableExt table = null;

	/**
	 * 默认的构造方法
	 */
	public ConnectionSelectFrame() {
		super();
		initGui();
		loadData();

	}

	/**
	 * 加载数据<br>
	 * 因为部分信息被保存起来了，因此我们需要重新加载
	 */
	private void loadData() {

		PersistenceObject object = NdddApplication.getInstance().getPersistenceObject();

		if (object != null) {
			cacheInfo = object.getConnectionInfos();
		} else {
			cacheInfo = new ArrayList<ConnectionInfo>();
		}

		flushConnectionView();
	}

	/**
	 * 将一条数据设置到界面上，因为我们读取到的数据是一个列表，但是没有办法把一个列表一下子放到界面上。因此使用一条一条增加的方式
	 * 
	 * @param info
	 */
	private void addRow2View(ConnectionInfo info) {
		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		dtm.addRow(new String[] { info.getName(), info.getJdbcConnString() });
	}

	/**
	 * 界面初始化
	 */
	private void initGui() {

		if (LOG.isDebugEnabled()) {
			LOG.debug("初始化界面开始...");
		}

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setUndecorated(true);
		this.setTitle("数据库小工具-项目开发部");

		table = new JTableExt();
		table.setRowHeight(35);

		// 不能编辑的
		TableModelExt dtm = new TableModelExt(new String[] { "连接名", "连接信息" }, 0);
		dtm.setCellEditable(false);

		table.setModel(dtm);
		table.setFocusable(false);// 设置不能获得焦点
		TableUtil.setTableHeaderStyle(table);

		// 双击事件
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					conn2ConnectionInfo();
				}
			}
		});

		JScrollPaneExt jsp = new JScrollPaneExt();
		jsp.setViewportView(table);

		this.add(jsp, BorderLayout.CENTER);

		JPanelExt footPanel = initFootPanel();

		this.add(footPanel, BorderLayout.SOUTH);

		this.setSize(new Dimension(600, 400));
		this.setLocationRelativeTo(null);

		if (LOG.isDebugEnabled()) {
			LOG.debug("初始化界面结束");
		}
	}

	/**
	 * 初始化页面底部的面板
	 * 
	 * @return 页面底部的面板
	 */
	private JPanelExt initFootPanel() {

		JPanelExt footPanel = new JPanelExt();
		{
			JButtonExt addBtn = new JButtonExt("新建");
			footPanel.add(addBtn);
			addBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					addBtnClick();
				}
			});
		}
		{
			JButtonExt btn1 = new JButtonExt("编辑");
			btn1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					editConnectionInfo();
				}
			});
			footPanel.add(btn1);
		}
		{
			JButtonExt btn1 = new JButtonExt("删除");
			btn1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					btnDeleteClick();
				}
			});
			footPanel.add(btn1);
		}

		{
			JButtonExt btn1 = new JButtonExt("连接");
			btn1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					conn2ConnectionInfo();
				}
			});
			footPanel.add(btn1);
		}

		{
			JButtonExt btn1 = new JButtonExt("退出");
			footPanel.add(btn1);
			btn1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ConnectionSelectFrame.this.dispose();
				}
			});
		}

		return footPanel;
	}

	protected void conn2ConnectionInfo() {

		int row = table.getSelectedRow();

		if (row == -1) {
			JOptionPane.showMessageDialog(this, "请先选择要连接的数据");
			return;
		}

		ConnectionInfo info = cacheInfo.get(row);

		NdddApplication.getInstance().setCurrentConnectionInfo(info);

		new MainFrame(info).setVisible(true);

		this.dispose();
	}

	private void btnDeleteClick() {

		int row = table.getSelectedRow();

		if (row == -1) {
			JOptionPane.showMessageDialog(this, "请先选择要删除的数据");
			return;
		}

		int result = JOptionPane.showConfirmDialog(this, "您确认要删除选择的数据吗？");

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		cacheInfo.remove(row);
		flushConnectionView();
		NdddApplication.getInstance().flushInfo2File();

	}

	protected void editConnectionInfo() {

		int row = table.getSelectedRow();

		ConnectionInfo connInfo = cacheInfo.get(row);

		if (LOG.isDebugEnabled()) {
			LOG.debug("您选择了第" + (row + 1) + "行！");
		}

		ConnectionInputDialog dialog = new ConnectionInputDialog(ConnectionSelectFrame.this, connInfo);

		dialog.setVisible(true);

		int result = dialog.getSelectedButton();

		if (ConnectionInputDialog.BTN_OK_CLICKED == result) {
			flushConnectionView();
			NdddApplication.getInstance().flushInfo2File();
		}
	}

	@SuppressWarnings("unchecked")
	private void flushConnectionView() {

		DefaultTableModel dtm = (DefaultTableModel) table.getModel();

		@SuppressWarnings("rawtypes")
		Vector v = dtm.getDataVector();
		v.removeAllElements();

		for (ConnectionInfo connectionInfo : cacheInfo) {

			@SuppressWarnings("rawtypes")
			Vector subV = new Vector();
			subV.add(connectionInfo.getName());
			subV.add(connectionInfo.getJdbcConnString());
			v.add(subV);
		}

		dtm.fireTableDataChanged();

	}

	protected void addBtnClick() {

		ConnectionInputDialog dialog = new ConnectionInputDialog(ConnectionSelectFrame.this);

		dialog.setVisible(true);

		int result = dialog.getSelectedButton();

		if (ConnectionInputDialog.BTN_OK_CLICKED == result) {

			ConnectionInfo info = dialog.getResultInfo();
			cacheInfo.add(info);
			addRow2View(info);
			flushConnectionView();
			NdddApplication.getInstance().flushInfo2File();
		}
	}
}
