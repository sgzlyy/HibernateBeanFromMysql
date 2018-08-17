package cn.com.sby.hbfm.gui.major;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import cn.com.sby.common.ui.ext.JButtonExt;
import cn.com.sby.common.ui.ext.JFrameExt;
import cn.com.sby.common.ui.ext.JLabelExt;
import cn.com.sby.common.ui.ext.JPanelExt;
import cn.com.sby.hbfm.gui.connselect.ConnectionSelectFrame;
import cn.com.sby.hbfm.gui.dbanay.DataBaseAnaylizeFrame;
import cn.com.sby.hbfm.gui.mktmodel.TmodelMakePropertiesDialog;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.TableInfo;
import cn.com.sby.hbfm.service.Export2ExcelService;
import cn.com.sby.hbfm.service.database.mysql.Mysql55AnaylizerService;
import cn.com.sby.hbfm.service.database.mysql.MysqlAnaylizerServiceI;

/**
 * 系统主界面
 * 
 *
 *
 */
public class MainFrame extends JFrameExt {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3624949563548210214L;

	/**
	 * 数据库连接信息
	 */
	private ConnectionInfo connectionInfo = null;

	/**
	 * 主界面
	 */
	public MainFrame(ConnectionInfo connectionInfo) {
		super("数据库小工具-项目开发部");
		this.connectionInfo = connectionInfo;
		initGui();
	}

	private void initGui() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		this.add(initTopPanel(), BorderLayout.NORTH);
		this.add(initCenterPanel(), BorderLayout.CENTER);

		this.setSize(800, 600);
		this.setLocationRelativeTo(null);// 设置屏幕的中间
	}

	private Component initCenterPanel() {

		JPanelExt centerPanel = new JPanelExt();
		centerPanel.setBorder(BorderFactory.createTitledBorder(null, "操作", TitledBorder.LEADING,
				TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
		centerPanel.setLayout(new GridLayout(2, 2, 2, 2));

		JButtonExt btnExportExcel = new JButtonExt("生成数据库表Excel");
		btnExportExcel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnExportExcelClick();
			}
		});
		centerPanel.add(btnExportExcel);

		JButtonExt btnDBAnaylizer = new JButtonExt("数据库分析信息");
		btnDBAnaylizer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnDBAnaylizerClick();
			}
		});
		centerPanel.add(btnDBAnaylizer);

		JButtonExt btnMkTmodel = new JButtonExt("生成Tmodel");
		btnMkTmodel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnMkTmodelClick();
			}
		});
		centerPanel.add(btnMkTmodel);

		JButtonExt btnExit = new JButtonExt("退出");
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnExitClick();
			}
		});
		centerPanel.add(btnExit);

		return centerPanel;
	}

	private JPanelExt initTopPanel() {

		JPanelExt topPanel = new JPanelExt();
		topPanel.setLayout(new BorderLayout());

		topPanel.setBorder(BorderFactory.createTitledBorder(null, "说明", TitledBorder.LEADING,
				TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

		topPanel.add(new JLabelExt("当前使用数据库链接：" + connectionInfo.getName()), BorderLayout.CENTER);

		JButtonExt btn = new JButtonExt("重新选择链接");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ConnectionSelectFrame().setVisible(true);
				MainFrame.this.dispose();
			}
		});
		topPanel.add(btn, BorderLayout.EAST);

		return topPanel;
	}

	/**
	 * 将数据库中的模型生成到Java的Model文件
	 */
	protected void btnMkTmodelClick() {
		new TmodelMakePropertiesDialog(this, connectionInfo).setVisible(true);
	}

	/**
	 * 退出
	 */
	protected void btnExitClick() {
		this.dispose();
	}

	/**
	 * 数据库分析
	 */
	protected void btnDBAnaylizerClick() {
		new DataBaseAnaylizeFrame().setVisible(true);
	}

	/**
	 * 导出到Excel中
	 */
	protected void btnExportExcelClick() {

		File saveFile = null;

		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("请选择要保存Excel文件的位置！");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(new FileNameExtensionFilter("office2007格式文件", "xlsx"));
		jfc.setFileView(new FileView() {
			@Override
			public Icon getIcon(File f) {
				return FileSystemView.getFileSystemView().getSystemIcon(f);
			}
		});
		jfc.setPreferredSize(new Dimension(750, 500));
		jfc.setSelectedFile(new File(FileSystemView.getFileSystemView().getHomeDirectory(), "数据库设计文档.xlsx"));

		int result = jfc.showSaveDialog(this);

		if (result == JFileChooser.APPROVE_OPTION && jfc.getSelectedFile() != null) {
			saveFile = jfc.getSelectedFile();
		}

		if (saveFile == null) {
			JOptionPane.showMessageDialog(this, "操作取消");
			return;
		}

		// 首先进行数据库分析
		MysqlAnaylizerServiceI service = new Mysql55AnaylizerService();
		// 加载数据库中的所有表信息
		try {

			List<TableInfo> tableInfos = service.loadTableInfos(connectionInfo);

			Export2ExcelService exportService = new Export2ExcelService();

			exportService.doExport(tableInfos, saveFile);
			JOptionPane.showMessageDialog(this, "操作成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
