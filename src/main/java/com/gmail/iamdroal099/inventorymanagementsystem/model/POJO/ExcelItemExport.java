package com.gmail.iamdroal099.inventorymanagementsystem.model.POJO;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.Item;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelItemExport {

    private List<Item> itemsList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelItemExport(List<Item> itemsList) {
        this.itemsList = itemsList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Items list");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Article", style);
        createCell(row, 1, "Item Name", style);
        createCell(row, 2, "Color", style);
        createCell(row, 3, "Characteristic", style);
        createCell(row, 4, "Cost", style);
        createCell(row, 5, "Quantity", style);
        createCell(row, 6, "Is Available", style);
        createCell(row, 7, "Creation date", style);
        createCell(row, 8, "Updating date", style);


    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Enum<?>) {
            cell.setCellValue(((Enum<?>) value).name());
        } else {
            cell.setCellValue(String.valueOf(value));
        }

        cell.setCellStyle(style);
    }


    private void write() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Item item : itemsList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, item.getArticle(), style);
            createCell(row, columnCount++, item.getItemName(), style);
            createCell(row, columnCount++, item.getColor(), style);
            createCell(row, columnCount++, item.getCharacteristics(), style);
            createCell(row, columnCount++, item.getCost(), style);
            createCell(row, columnCount++, item.getQuantity(), style);
            createCell(row, columnCount++, item.getIas(), style);
            createCell(row, columnCount++, item.getCreationDate(), style);
            createCell(row, columnCount++, item.getUpdatingDate(), style);

        }
    }

    public void generate(OutputStream outputStream) throws IOException {
        writeHeader();
        write();
        workbook.write(outputStream);
        workbook.close();
    }
}