package utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {
    public static FileInputStream fis;
    public static FileOutputStream fos;
    public static XSSFWorkbook wb;
    public static XSSFSheet ws;
    public static XSSFRow row;
    public static XSSFCell cell;

    public static int getRowCount(String xlFile, String xlSheet) throws IOException {
        fis = new  FileInputStream(xlFile);
        wb = new XSSFWorkbook (fis);
        ws = wb.getSheet(xlSheet);
        int row_count = ws.getLastRowNum();
        wb.close();
        fis.close();
        return row_count;
    }

    public static int getCellCount(String xlFile, String xlSheet, int row_num) throws IOException {
        fis = new FileInputStream(xlFile);
        wb = new XSSFWorkbook(fis);
        ws = wb.getSheet(xlSheet);
        row = ws.getRow(row_num);
        int cell_count = row.getLastCellNum();
        wb.close();
        fis.close();
        return cell_count;
    }

    public static String getCellData(String xlFile, String xlSheet, int row_num, int column) throws IOException {
        fis = new FileInputStream(xlFile);
        wb = new XSSFWorkbook(fis);
        ws = wb.getSheet(xlSheet);
        row = ws.getRow(row_num);
        cell= row.getCell(column);
        String data;
        try{
            DataFormatter formatter = new DataFormatter();
            String cellData = formatter.formatCellValue(cell);
            return cellData;
        }catch (Exception e){
            data = "";
        }
        wb.close();
        fis.close();
        return data;
    }

    public static void setCellData(String xlFile, String xlSheet, int row_num, int column, String data) throws IOException {
        fis = new FileInputStream(xlFile);
        wb = new XSSFWorkbook(fis);
        ws = wb.getSheet(xlSheet);
        row = ws.getRow(row_num);
        cell= row.getCell(column);
        cell.setCellValue(data);
        fos= new FileOutputStream(xlFile);
        wb.write(fos);
        wb.close();
        fis.close();
        fos.close();
    }
}
