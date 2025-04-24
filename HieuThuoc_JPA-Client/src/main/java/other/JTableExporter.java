package other;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

public class JTableExporter {
    
    public static void openFile(String file) throws IOException {
        File path = new File(file);
        Desktop.getDesktop().open(path);
    }
    
//    public static void exportJTableToExcel(JTable table,Timestamp tg, String nv) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Chọn đường dẫn lưu file Excel");
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
//        fileChooser.setFileFilter(filter);
//        fileChooser.setAcceptAllFileFilterUsed(false);
//        
//        int userChoice = fileChooser.showSaveDialog(null);
//        if (userChoice == JFileChooser.APPROVE_OPTION) {
//            try {
//                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
//                if (!filePath.toLowerCase().endsWith(".xlsx")) {
//                    filePath += ".xlsx";
//                }
//                
//                TableModel model = table.getModel();
//                Workbook workbook = new XSSFWorkbook();
//                Sheet sheet = workbook.createSheet("Sheet1");
//
//                // row mặc định tên là Thống kê doanh thu Yên Tâm
//                // row thời gian
//                //row tên nv
//                
//                // Create header row
//                CellStyle cellStyle = createStyleForHeader(sheet);
//                Row headerRow = sheet.createRow(0);
//                for (int i = 0; i < model.getColumnCount(); i++) {
//                    Cell headerCell = headerRow.createCell(i);
//                    headerCell.setCellValue(model.getColumnName(i));
//                    headerCell.setCellStyle(cellStyle);
//                }
//
//                // Create data rows
//                for (int i = 0; i < model.getRowCount(); i++) {
//                    Row dataRow = sheet.createRow(i + 1);
//                    for (int j = 0; j < model.getColumnCount(); j++) {
//                        Cell dataCell = dataRow.createCell(j);
//                        Object value = model.getValueAt(i, j);
//                        if (value != null) {
//                            dataCell.setCellValue(value.toString());
//                        }
//                    }
//                }
//
//                // Resize all columns to fit the content size
//                for (int i = 0; i < model.getColumnCount(); i++) {
//                    sheet.autoSizeColumn(i);
//                }
//
//                // Write the output to a file
//                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//                    workbook.write(fileOut);
//                }
//                
//                workbook.close();
//                openFile(filePath);
//            } catch (IOException ex) {
//                MessageDialog.error(null, "Lỗi đọc file!");
//            }
//        }
//    }
//    
//    
//    private static CellStyle createStyleForHeader(Sheet sheet) {
//        // Create font
//        Font font = sheet.getWorkbook().createFont();
//        font.setFontName("Times New Roman");
//        font.setBold(true);
//        font.setFontHeightInPoints((short) 14); // font size
//        font.setColor(IndexedColors.WHITE.getIndex()); // text color
//
//        // Create CellStyle
//        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
//        cellStyle.setFont(font);
//        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setBorderBottom(BorderStyle.THIN);
//        return cellStyle;
//    }
    public static void exportJTableToExcel(JTable table, Timestamp tg, String nv) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn lưu file Excel");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userChoice = fileChooser.showSaveDialog(null);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                TableModel model = table.getModel();
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Sheet1");

                // Create title row
                Row titleRow = sheet.createRow(0);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Hiệu Thuốc Yên Tâm");
                
                CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
                Font titleFont = sheet.getWorkbook().createFont();
                titleFont.setFontName("Times New Roman");
                titleFont.setFontHeightInPoints((short) 20);
                titleFont.setBold(true);
                titleStyle.setFont(titleFont);
                titleCell.setCellStyle(titleStyle);
                titleFont.setColor(IndexedColors.BLUE.getIndex());
                // Merge cells for title
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, model.getColumnCount() - 1));

                // Create time row
                Row timeRow = sheet.createRow(1);
                timeRow.createCell(0).setCellValue("Thời gian:");
                timeRow.createCell(1).setCellValue(tg.toString());

                // Create employee row
                Row employeeRow = sheet.createRow(2);
                employeeRow.createCell(0).setCellValue("Nhân viên xuất:");
                employeeRow.createCell(1).setCellValue(nv);

                // Create header row
                CellStyle headerStyle = createStyleForHeader(sheet);
                Row headerRow = sheet.createRow(3);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell headerCell = headerRow.createCell(i);
                    headerCell.setCellValue(model.getColumnName(i));
                    headerCell.setCellStyle(headerStyle);
                }

                // Create data rows
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row dataRow = sheet.createRow(i + 4); // Start from row 4 (0-based index)
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell dataCell = dataRow.createCell(j);
                        Object value = model.getValueAt(i, j);
                        if (value != null) {
                            dataCell.setCellValue(value.toString());
                        }
                    }
                }

                // Resize all columns to fit the content size
                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // Write the output to a file
                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                }

                workbook.close();
                openFile(filePath);
            } catch (IOException ex) {
                MessageDialog.error(null, "Lỗi đọc file!");
            }
        }
    }

    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex()); // Background color
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        return cellStyle;
    }

    
}
