package cn.com.sby.hbfm.gui.dbanay;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import cn.com.sby.common.StringUtil;
import cn.com.sby.hbfm.NdddApplication;
import cn.com.sby.hbfm.gui.common.table.TableModelExt;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.DBConfigItem;
import cn.com.sby.hbfm.service.database.mysql.AbstractMysqlAnaylizerService;
import cn.com.sby.hbfm.service.database.mysql.Mysql55AnaylizerService;
import cn.com.sby.hbfm.service.database.mysql.MysqlAnaylizerServiceI;

/**
 * 数据库分析界面<br>
 * 用来分析数据库的参数信息
 * 
 *
 *
 */
public class DataBaseAnaylizeFrame extends JFrame {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9136440186683714470L;

	/**
	 * 基本信息表格
	 */
	private JTable baseInfoTable = null;
	/**
	 * 警告信息表格
	 */
	private JTable warnInfoTable = null;

	/**
	 * 默认的构造方法
	 * 
	 * @param frame
	 *            主界面
	 */
	public DataBaseAnaylizeFrame() {
		super();
		initGui();// 初始化界面
	}

	/**
	 * 初始化界面
	 */
	private void initGui() {

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JTabbedPane panel = initCenterPanel();
		this.add(panel);

		Dimension size = new Dimension(800, 600);
		this.setSize(size);// 设置初始化大小
		this.setMinimumSize(size);
		this.setLocationRelativeTo(null);// 设置到屏幕的中间
	}

	/**
	 * 初始化中间的面板：中间的面板为一个tabbedpanel
	 * 
	 * @return
	 */
	private JTabbedPane initCenterPanel() {

		ConnectionInfo info = NdddApplication.getInstance().getCurrentConnectionInfo();

		MysqlAnaylizerServiceI service = new Mysql55AnaylizerService();
		List<DBConfigItem> variables = service.getVariables(info);

		JTabbedPane tp = new JTabbedPane();

		tp.add("重要信息", initWarnPanel(variables));
		tp.add("所有信息", initTotalCommentPanel(variables));

		return tp;
	}

	private JScrollPane initWarnPanel(List<DBConfigItem> variables) {

		JScrollPane jsp = new JScrollPane();

		TableModelExt dtm = new TableModelExt(new String[] { "KEY", "VALUE", "说明" }, 0);
		dtm.setCellEditable(false);

		warnInfoTable = new JTable(dtm);
		warnInfoTable.setFocusable(false);
		warnInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		warnInfoTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		warnInfoTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		warnInfoTable.getColumnModel().getColumn(2).setPreferredWidth(350);
		warnInfoTable.setRowHeight(30);

		warnInfoTable.addMouseListener(new MouseAdapter() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					showDataBaseAnaylizeDetailViewDialog(DataBaseAnaylizeFrame.this.warnInfoTable);
				}
			}
		});

		for (DBConfigItem keyValue : variables) {

			String key = keyValue.getKey();

			if ("interactive_timeout".equals(key) || "wait_timeout".equals(key)) {

				String value = keyValue.getValue();
				Long longValue = Long.parseLong(value);

				// String valueDesc = caculateMillSecondsTime(longValue);
				String valueDesc = caculateSecondsTime(longValue);
				String describe = valueDesc + "\r\n"
						+ AbstractMysqlAnaylizerService.VARIABLES_DESC.get(keyValue.getKey());

				dtm.addRow(new String[] { keyValue.getKey(), keyValue.getValue(), describe });
			} else {

				if ("max_connections".equals(key)) {
					dtm.addRow(new String[] { keyValue.getKey(), keyValue.getValue(), StringUtil.defaultIfEmpty(
							AbstractMysqlAnaylizerService.VARIABLES_DESC.get(keyValue.getKey()), "暂无说明") });
				}

			}

		}

		jsp.setViewportView(warnInfoTable);

		return jsp;
	}

	// private String caculateMillSecondsTime(Long longValue) {
	//
	// StringBuffer sb = new StringBuffer();
	// sb.append(longValue);
	// sb.append("毫秒=");
	// long second = longValue / 1000;
	// sb.append(second);
	// sb.append("秒");
	//
	// if (second <= 60) {
	// return sb.toString();
	// }
	//
	// long minute = second / 60;
	// sb.append("=");
	// sb.append(minute);
	// sb.append("分");
	//
	// if (minute <= 60) {
	// return sb.toString();
	// }
	//
	// long hour = minute / 60;
	// sb.append("=");
	// sb.append(hour);
	// sb.append("小时");
	//
	// return sb.toString();
	// }

	private String caculateSecondsTime(Long longValue) {

		StringBuffer sb = new StringBuffer();
		sb.append(longValue);
		sb.append("秒");

		long minute = longValue / 60;
		sb.append("=");
		sb.append(minute);
		sb.append("分");

		if (minute <= 60) {
			return sb.toString();
		}

		long hour = minute / 60;
		sb.append("=");
		sb.append(hour);
		sb.append("小时");

		if (hour <= 24) {
			return sb.toString();
		}

		long day = hour / 60;
		sb.append("=");
		sb.append(day);
		sb.append("天");

		return sb.toString();
	}

	private JScrollPane initTotalCommentPanel(List<DBConfigItem> variables) {

		JScrollPane jsp = new JScrollPane();

		TableModelExt dtm = new TableModelExt(new String[] { "KEY", "VALUE", "说明" }, 0);
		dtm.setCellEditable(false);

		baseInfoTable = new JTable(dtm);
		baseInfoTable.setFocusable(false);
		baseInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		baseInfoTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		baseInfoTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		baseInfoTable.getColumnModel().getColumn(2).setPreferredWidth(350);
		baseInfoTable.setRowHeight(30);
		// table.setDefaultRenderer(Object.class, new
		// TableCellTextAreaRenderer());

		baseInfoTable.addMouseListener(new MouseAdapter() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					showDataBaseAnaylizeDetailViewDialog(DataBaseAnaylizeFrame.this.baseInfoTable);
				}
			}
		});

		for (DBConfigItem keyValue : variables) {
			dtm.addRow(new String[] { keyValue.getKey(), keyValue.getValue(), StringUtil
					.defaultIfEmpty(AbstractMysqlAnaylizerService.VARIABLES_DESC.get(keyValue.getKey()), "暂无说明") });
		}

		jsp.setViewportView(baseInfoTable);

		return jsp;
	}

	protected void showDataBaseAnaylizeDetailViewDialog(JTable table) {

		int row = table.getSelectedRow();

		DBConfigItem item = new DBConfigItem(String.valueOf(table.getValueAt(row, 0)),
				String.valueOf(table.getValueAt(row, 1)), String.valueOf(table.getValueAt(row, 2)));

		DataBaseAnaylizeDetailViewDialog dialog = new DataBaseAnaylizeDetailViewDialog(this, item);
		dialog.setVisible(true);
	}
}
