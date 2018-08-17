package cn.com.sby.hbfm.service;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.com.sby.common.StringUtil;
import cn.com.sby.hbfm.model.ColumnInfo;
import cn.com.sby.hbfm.model.FKInfo;
import cn.com.sby.hbfm.model.TableInfo;

/**
 * 将数据库表导出到Excel文件中。
 * 
 *
 *
 */
public class Export2ExcelService {

    /**
     * 导出数据库设计对象到Excel表中
     * 
     * @param tableInfos
     *            数据库表信心
     * @param exportFile
     *            要导出的文件
     */
    public void doExport(List<TableInfo> tableInfos, File exportFile) {

        XSSFWorkbook xwb = new XSSFWorkbook();

        // 导出数据库设计
        exportDBDesign(xwb, tableInfos);

        // 导出SQL
        exportSQL(xwb, tableInfos);

        try {
            xwb.write(new FileOutputStream(exportFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("处理完成！");
    }

    /**
     * 导出数据至Excel
     * 
     * @param xwb
     *            Excel文档信息
     * @param tableInfos
     *            数据库表信息
     */
    private void exportSQL(XSSFWorkbook xwb, List<TableInfo> tableInfos) {

        XSSFSheet exportSheet = xwb.createSheet("SQL设计");
        int offRow = 0;

        for (TableInfo tableInfo : tableInfos) {

            exportSheet.createRow(offRow++);

            XSSFRow rowTableInfo = exportSheet.createRow(offRow++);
            XSSFCell cellTableInfo = rowTableInfo.createCell(1);
            cellTableInfo.setCellValue(tableInfo.getTableName().toUpperCase());

            XSSFRow rowSQL = exportSheet.createRow(offRow++);
            XSSFCell cellSQL = rowSQL.createCell(1);
            cellSQL.setCellValue(tableInfo.getSql());
        }
    }

    /**
     * 导出数据库设计
     * 
     * @param xwb
     *            Excel文档信息
     * @param tableInfos
     *            数据库表信息
     */
    private void exportDBDesign(XSSFWorkbook xwb, List<TableInfo> tableInfos) {

        // 样式：标题的样式
        XSSFCellStyle titleStyle = createTitleStyle(xwb);
        // 样式：表格头的样式
        XSSFCellStyle tableHeaderStyle = createTableHeaderStyle(xwb);
        // 样式：表格内容的样式
        XSSFCellStyle tableContentStyle = createTableContentStyle(xwb);

        XSSFSheet exportSheet = xwb.createSheet("数据库设计");
        exportSheet.setColumnWidth(1, 6 * 256);
        exportSheet.setColumnWidth(2, 20 * 256);
        exportSheet.setColumnWidth(3, 16 * 256);
        exportSheet.setColumnWidth(4, 6 * 256);
        exportSheet.setColumnWidth(5, 30 * 256);
        exportSheet.setColumnWidth(6, 10 * 256);
        exportSheet.setColumnWidth(7, 20 * 256);

        int offRow = 0;

        for (TableInfo tableInfo : tableInfos) {

            exportSheet.createRow(offRow++);// 创建一个空行

            // 打印标题行
            printTitle(exportSheet, titleStyle, tableInfo, offRow++);
            // 打印表名称行
            printTableName(exportSheet, titleStyle, tableInfo, offRow++);
            // 打印主键字段
            printPKInfo(exportSheet, titleStyle, tableInfo, offRow++);
            // 打印字段标题
            printColumnTitle(exportSheet, tableHeaderStyle, offRow++);

            // ------------------------------------------
            // &&&&&&&&&&&&&&&&&&&打印字段
            // ------------------------------------------
            List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();

            for (int i = 0; i < columnInfos.size(); i++) {

                XSSFRow rowColumnInfo = exportSheet.createRow(offRow++);
                printColumn(tableContentStyle, rowColumnInfo, tableInfo.getForeignKeyInfos(), columnInfos.get(i), i);
            }

        }
    }

    /**
     * 导出字段行信息
     * 
     * @param exportSheet
     *            到导入的表
     * @param tableContentStyle
     * @param columnInfo
     * @param rowNo
     * @param i
     */
    private void printColumn(XSSFCellStyle tableContentStyle, XSSFRow rowColumnInfo,
            List<FKInfo> foreignKeyInfos, ColumnInfo columnInfo, int i) {

        XSSFCell cell = null;

        cell = rowColumnInfo.createCell(1);
        cell.setCellValue(i + 1);
        cell.setCellStyle(tableContentStyle);

        cell = rowColumnInfo.createCell(2);
        cell.setCellValue(columnInfo.getColumnNameEn().toUpperCase());
        cell.setCellStyle(tableContentStyle);

        cell = rowColumnInfo.createCell(3);
        if (!StringUtil.isEmpty(columnInfo.getColumnLength())) {
            cell.setCellValue(columnInfo.getColumnType() + "(" + columnInfo.getColumnLength() + ")");
        } else {
            cell.setCellValue(columnInfo.getColumnType());
        }
        cell.setCellStyle(tableContentStyle);

        // 主键
        cell = rowColumnInfo.createCell(4);
        cell.setCellValue(columnInfo.getIsPrimaryKey() ? "PRI" : "");
        cell.setCellStyle(tableContentStyle);

        // 外键
        String cellValue = null;
        for (FKInfo fk : foreignKeyInfos) {

            if (fk.getColumnInfo().equals(columnInfo)) {
                cellValue = fk.getReferenceTableName() + "." + fk.getReferenceColumnName();
                break;
            }
        }

        cell = rowColumnInfo.createCell(5);
        cell.setCellValue(cellValue == null ? "" : cellValue.toUpperCase());
        cell.setCellStyle(tableContentStyle);

        cell = rowColumnInfo.createCell(6);
        cell.setCellValue(columnInfo.getIsCanNull() ? "NULL" : "NOT NULL");
        cell.setCellStyle(tableContentStyle);

        cell = rowColumnInfo.createCell(7);
        cell.setCellValue(columnInfo.getDescription());
        cell.setCellStyle(tableContentStyle);
    }

    /**
     * 打印字段的标题
     * 
     * @param exportSheet
     * @param tableHeaderStyle
     * @param rowNo
     */
    private void printColumnTitle(XSSFSheet exportSheet, XSSFCellStyle tableHeaderStyle, int rowNo) {

        int i = 1;
        XSSFCell columnTitleCell = null;

        XSSFRow rowColumnTitle = exportSheet.createRow(rowNo);

        columnTitleCell = rowColumnTitle.createCell(i++);
        columnTitleCell.setCellValue("序号");
        columnTitleCell.setCellStyle(tableHeaderStyle);

        columnTitleCell = rowColumnTitle.createCell(i++);
        columnTitleCell.setCellValue("字段名称");
        columnTitleCell.setCellStyle(tableHeaderStyle);

        columnTitleCell = rowColumnTitle.createCell(i++);
        columnTitleCell.setCellValue("字段类型");
        columnTitleCell.setCellStyle(tableHeaderStyle);

        columnTitleCell = rowColumnTitle.createCell(i++);
        columnTitleCell.setCellValue("主键");
        columnTitleCell.setCellStyle(tableHeaderStyle);

        columnTitleCell = rowColumnTitle.createCell(i++);
        columnTitleCell.setCellValue("外键");
        columnTitleCell.setCellStyle(tableHeaderStyle);

        columnTitleCell = rowColumnTitle.createCell(i++);
        columnTitleCell.setCellValue("非空");
        columnTitleCell.setCellStyle(tableHeaderStyle);

        columnTitleCell = rowColumnTitle.createCell(i++);
        columnTitleCell.setCellValue("字段说明");
        columnTitleCell.setCellStyle(tableHeaderStyle);
    }

    private void printPKInfo(XSSFSheet exportSheet, XSSFCellStyle titleStyle, TableInfo tableInfo, int rowNo) {

        List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();

        // ------------------------------------------
        // &&&&&&&&&&&&&&&&&&&计算数据中的主键字段
        // ------------------------------------------
        List<ColumnInfo> pks = new ArrayList<ColumnInfo>();
        for (ColumnInfo columnInfo : columnInfos) {
            if (columnInfo.getIsPrimaryKey()) {
                pks.add(columnInfo);
            }
        }

        // ------------------------------------------
        // &&&&&&&&&&&&&&&&&&&打印表主键字段
        // ------------------------------------------
        XSSFRow rowPK = exportSheet.createRow(rowNo);

        CellRangeAddress regionPK = new CellRangeAddress(rowNo, rowNo, 1, 7); // 合并单元格
        exportSheet.addMergedRegion(regionPK); // 合并单元格

        XSSFCell cellPK = rowPK.createCell(1);

        if (pks.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (ColumnInfo columnInfo : pks) {
                if (sb.length() != 0) {
                    sb.append(",");
                }
                sb.append(columnInfo.getColumnNameEn());
            }

            cellPK.setCellValue("表主键字段：" + sb.toString().toUpperCase());
        }
        fillCellStyle(exportSheet, regionPK, titleStyle);// 设置合并后的单元格格式

    }

    /**
     * 打印表的名称
     * 
     * @param exportSheet
     * @param titleStyle
     * @param tableInfo
     * @param rowNo
     */
    private void printTableName(XSSFSheet exportSheet, XSSFCellStyle titleStyle, TableInfo tableInfo, int rowNo) {

        XSSFRow rowTableName = exportSheet.createRow(rowNo);
        CellRangeAddress regionTableName = new CellRangeAddress(rowNo, rowNo, 1, 7); // 合并单元格
        exportSheet.addMergedRegion(regionTableName); // 合并单元格

        XSSFCell cellTableName = rowTableName.createCell(1);

        cellTableName.setCellValue("表名称：" + tableInfo.getTableName().toUpperCase() + "("
                + tableInfo.getTableDescribe().toUpperCase() + ")");
        fillCellStyle(exportSheet, regionTableName, titleStyle);// 设置合并后的单元格格式
    }

    /**
     * 打印标题信息
     * 
     * @param exportSheet
     * @param titleStyle
     * @param tableInfo
     * @param rowNo
     */
    private void printTitle(XSSFSheet exportSheet, XSSFCellStyle titleStyle, TableInfo tableInfo, int rowNo) {

        XSSFRow rowTitle = exportSheet.createRow(rowNo);
        CellRangeAddress regionTitle = new CellRangeAddress(rowNo, rowNo, 1, 7); // 合并单元格
        exportSheet.addMergedRegion(regionTitle);// 合并单元格

        XSSFCell cellTitle = rowTitle.createCell(1);
        cellTitle.setCellValue(tableInfo.getTableName().toUpperCase());
        fillCellStyle(exportSheet, regionTitle, titleStyle);// 设置合并后的单元格格式
    }

    /**
     * 创建表格内容的样式
     * 
     * @param xwb
     * @return
     */
    private XSSFCellStyle createTableContentStyle(XSSFWorkbook xwb) {

        XSSFCellStyle style = xwb.createCellStyle();

        style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
        style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
        style.setBorderRight(CellStyle.BORDER_THIN);// 右边框

        // 设置字体
        XSSFFont font = xwb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);// 设置字体大小

        style.setFont(font);// 选择需要用到的字体格式

        return style;
    }

    /**
     * 创建表格标题的样式
     * 
     * @param xwb
     * @return
     */
    private XSSFCellStyle createTableHeaderStyle(XSSFWorkbook xwb) {

        XSSFCellStyle style = xwb.createCellStyle();

        // 设置背景色
        XSSFColor color = new XSSFColor(new Color(230, 230, 230));

        style.setFillForegroundColor(color);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
        style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
        style.setBorderRight(CellStyle.BORDER_THIN);// 右边框

        // 设置字体
        XSSFFont font = xwb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);// 设置字体大小
        font.setBold(true);

        style.setFont(font);// 选择需要用到的字体格式

        return style;
    }

    private void fillCellStyle(XSSFSheet sheet, CellRangeAddress region, XSSFCellStyle style) {

        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (region.getFirstColumn() != region.getLastColumn()) {
                for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                    XSSFCell cell = row.getCell((short) j);
                    if (cell == null) {
                        cell = row.createCell((short) j);
                    }
                    cell.setCellStyle(style);
                }
            }
        }
    }

    private XSSFCellStyle createTitleStyle(XSSFWorkbook xwb) {

        XSSFCellStyle style = xwb.createCellStyle();

        // 设置背景色
        XSSFColor color = new XSSFColor(new Color(230, 230, 230));

        style.setFillForegroundColor(color);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
        style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
        style.setBorderRight(CellStyle.BORDER_THIN);// 右边框

        // 设置字体
        XSSFFont font = xwb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12);// 设置字体大小

        style.setFont(font);// 选择需要用到的字体格式

        return style;
    }
}
