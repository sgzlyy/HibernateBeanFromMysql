package cn.com.spbun.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * 与swing有关的界面帮助类
 * 
 * @author NOLY DAKE
 *
 */
public class SwingUtil {

	public static Font FONT_SIMSUN = new Font("simsun", Font.PLAIN, 16);
	public static Font FONT_SIMSUN_B = new Font("simsun", Font.BOLD, 16);
	public static Font FONT_COURIER_NEW = new Font("Courier New", Font.PLAIN, 16);

	/**
	 * 表格相关颜色：行选择颜色
	 */
	public static final Color TABLE_COLOR_SELECTION = new Color(207, 228, 249);
	/**
	 * 表格相关颜色：奇数行颜色
	 */
	public static final Color TABLE_COLOR_EVENROW = new Color(233, 242, 241);
	/**
	 * 表格相关颜色：偶数行颜色
	 */
	public static final Color TABLE_COLOR_ODDROW = new Color(255, 255, 255);
	/**
	 * 表格相关颜色：网格颜色
	 */
	public static final Color TABLE_COLOR_GRID = new Color(236, 233, 216);
	/**
	 * 表格相关颜色：网格颜色
	 */
	public static final Color TABLE_COLOR_TITLE = new Color(200, 200, 200);

	/**
	 * 创建spbun特定的CellRender
	 */
	public static void resetSpbunCellRender(JTable table) {

		int count = table.getColumnCount();
		for (int i = 0; i < count; i++) {

			TableColumn column = table.getColumnModel().getColumn(i);

			DefaultTableCellRenderer headerCellRenderer = new DefaultTableCellRenderer() {
				/** * serialVersionUID */
				private static final long serialVersionUID = -9219880006098309464L;

				@Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
					comp.setBackground(TABLE_COLOR_TITLE);
					comp.setBorder(BorderFactory.createRaisedBevelBorder());
					return comp;
				}
			};
			column.setHeaderRenderer(headerCellRenderer);
		}
	}

	/**
	 * 设置全局字体<br>
	 * 该方法必须在main方法启动后，界面.setVisible(true)前就执行。否则不会生效
	 * 
	 * @param font
	 */
	public static void updateUIFont(Font font) {

		// String names[]={ "Label", "CheckBox", "PopupMenu","MenuItem",
		// "CheckBoxMenuItem",
		// "JRadioButtonMenuItem","ComboBox", "Button", "Tree", "ScrollPane",
		// "TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
		// "OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
		// "ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
		// "PasswordField","TextField", "Table", "Label", "Viewport",
		// "RadioButtonMenuItem","RadioButton", "DesktopPane", "InternalFrame"
		// };

		UIManager.put("Button.font", font);
		UIManager.put("CheckBox.font", font);
		UIManager.put("CheckBoxMenuItem.acceleratorFont", font);
		UIManager.put("CheckBoxMenuItem.font", font);
		UIManager.put("ColorChooser.font", font);
		UIManager.put("ComboBox.font", font);
		UIManager.put("DesktopIcon.font", font);
		UIManager.put("EditorPane.font", font);
		UIManager.put("FormattedTextField.font", font);
		UIManager.put("InternalFrame.titleFont", font);
		UIManager.put("Label.font", font);
		UIManager.put("List.font", font);
		UIManager.put("Menu.acceleratorFont", font);
		UIManager.put("Menu.font", font);
		UIManager.put("MenuBar.font", font);
		UIManager.put("MenuItem.acceleratorFont", font);
		UIManager.put("MenuItem.font", font);
		UIManager.put("OptionPane.font", font);
		UIManager.put("Panel.font", font);
		UIManager.put("PasswordField.font", font);
		UIManager.put("PopupMenu.font", font);
		UIManager.put("ProgressBar.font", font);
		UIManager.put("RadioButton.font", font);
		UIManager.put("RadioButtonMenuItem.acceleratorFont", font);
		UIManager.put("RadioButtonMenuItem.font", font);
		UIManager.put("ScrollPane.font", font);
		UIManager.put("Spinner.font", font);
		UIManager.put("TabbedPane.font", font);
		UIManager.put("Table.font", font);
		UIManager.put("TableHeader.font", font);
		UIManager.put("TextArea.font", font);
		UIManager.put("TextField.font", font);
		UIManager.put("TextPane.font", font);
		UIManager.put("TitledBorder.font", font);
		UIManager.put("ToggleButton.font", font);
		UIManager.put("ToolBar.font", font);
		UIManager.put("ToolTip.font", font);
		UIManager.put("Tree.font", font);
		UIManager.put("Viewport.font", font);
	}
}
