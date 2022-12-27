package br.com.dbc.chronosapi.service;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.dbc.chronosapi.dto.calendario.DiaCalendarioEdicaoDTO;
import br.com.dbc.chronosapi.entity.enums.Status;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CalendarioExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<DiaCalendarioEdicaoDTO> listCalendario;

    public CalendarioExcelExporter(List<DiaCalendarioEdicaoDTO> listUsers) {
        this.listCalendario = listUsers;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Dia", style);
        createCell(row, 1, "Etapa", style);
        createCell(row, 2, "Processo", style);
        createCell(row, 3, "Crítico", style);
        createCell(row, 4, "Feriado", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (DiaCalendarioEdicaoDTO calendario : listCalendario) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, calendario.getDia().toString(), style);
            createCell(row, columnCount++, calendario.getEtapa(), style);
            createCell(row, columnCount++, calendario.getProcesso(), style);
            if(calendario.getCritico() == null){
                calendario.setCritico(Status.INATIVO);
            }
            createCell(row, columnCount++, calendario.getCritico().toString(), style);
            createCell(row, columnCount++, calendario.getFeriado(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}