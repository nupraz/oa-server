package com.graduation.oa.util.excelUtil;

import cn.hutool.json.JSONObject;
import com.bestvike.oa.data.EmpSchedule;
import com.bestvike.oa.data.projEntity.EmpDaily;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelWriteUtil {
    /**
     * 自定义导出行事历或者日报Excel，若isDaily为false，则导出empSchedules，若isDaily为true,导出empDailies
     * @param titles
     * @param empSchedules
     * @param empDailies
     * @param path
     * @param fileName
     * @param isDaily
     */
    public static void exportScheduleOrDailyData(List<String> titles, List<EmpSchedule> empSchedules, List<EmpDaily> empDailies, String path, String fileName, boolean isDaily) {
        try {
            File file = new File(path);
            //如果文件夹不存在
            if(!file.exists()) {
                //创建文件夹
                file.mkdirs();
            }
            file = new File(path+"/"+fileName);
            //XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            //大数据量导出,防止内存溢出
            SXSSFWorkbook xssfWorkbook = new SXSSFWorkbook(100);
            for (String title : titles) {
                //sheet 标题
                //跨年度会使sheet重名
                /*String sheetName = title.substring(5);
                sheetName = sheetName.substring(0, sheetName.length());
                switch (sheetName) {
                    case "01":
                        sheetName = "一月";
                        break;
                    case "02":
                        sheetName = "二月";
                        break;
                    case "03":
                        sheetName = "三月";
                        break;
                    case "04":
                        sheetName = "四月";
                        break;
                    case "05":
                        sheetName = "五月";
                        break;
                    case "06":
                        sheetName = "六月";
                        break;
                    case "07":
                        sheetName = "七月";
                        break;
                    case "08":
                        sheetName = "八月";
                        break;
                    case "09":
                        sheetName = "九月";
                        break;
                    case "10":
                        sheetName = "十月";
                        break;
                    case "11":
                        sheetName = "十一月";
                        break;
                    case "12":
                        sheetName = "十二月";
                        break;
                }*/
                //创建sheet
                //Sheet sheet = xssfWorkbook.createSheet(sheetName);
                Sheet sheet = xssfWorkbook.createSheet(title);
                //隐藏所有表格线
                sheet.setDisplayGridlines(false);
                //设置列宽
                sheet.setColumnWidth(0, 256*3+184);
                sheet.setColumnWidth(1, 256*18+184);
                sheet.setColumnWidth(2, 256*18+184);
                sheet.setColumnWidth(3, 256*18+184);
                sheet.setColumnWidth(4, 256*18+184);
                sheet.setColumnWidth(5, 256*18+184);
                sheet.setColumnWidth(6, 256*18+184);
                sheet.setColumnWidth(7, 256*18+184);
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 5));
                //设置标题单元格
                Cell titleCell = sheet.createRow(2).createCell(1);
                String titleName = title.replace("-", "年")+"月";
                titleCell.setCellValue(titleName);
                //标题样式
                CellStyle titleCellStyle = xssfWorkbook.createCellStyle();
                //左上对齐
                titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                Font font = xssfWorkbook.createFont();
                //字体
                font.setFontName("微软雅黑");
                //设置字体大小
                font.setFontHeightInPoints((short) 40);
                //字体颜色
                font.setColor(HSSFColor.LIGHT_ORANGE.index);
                titleCellStyle.setFont(font);
                titleCell.setCellStyle(titleCellStyle);

                //设置表头行
                Row headRow = sheet.createRow(3);
                headRow.setHeight((short)600);
                Cell headCell1 = headRow.createCell(1);
                Font headFont = xssfWorkbook.createFont();
                //字体
                headFont.setFontName("微软雅黑");
                //设置字体大小
                headFont.setFontHeightInPoints((short) 9);
                headFont.setBold(true);
                //字体颜色
                headFont.setColor(HSSFColor.LIGHT_ORANGE.index);

                CellStyle cellStyle1 = xssfWorkbook.createCellStyle();
                cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                cellStyle1.setBottomBorderColor(HSSFColor.LIGHT_ORANGE.index);
                cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                cellStyle1.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
                cellStyle1.setTopBorderColor(HSSFColor.LIGHT_ORANGE.index);
                cellStyle1.setFont(headFont);
                headCell1.setCellStyle(cellStyle1);
                headCell1.setCellValue("星期一");

                Cell headCell2 = headRow.createCell(2);
                CellStyle cellStyle2 = xssfWorkbook.createCellStyle();
                cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                cellStyle2.setBottomBorderColor(HSSFColor.LIGHT_ORANGE.index);
                cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
                cellStyle2.setTopBorderColor(HSSFColor.LIGHT_ORANGE.index);
                cellStyle2.setFont(headFont);
                headCell2.setCellStyle(cellStyle2);
                headCell2.setCellValue("星期二");

                Cell headCell3 = headRow.createCell(3);
                headCell3.setCellStyle(cellStyle2);
                headCell3.setCellValue("星期三");

                Cell headCell4 = headRow.createCell(4);
                headCell4.setCellStyle(cellStyle2);
                headCell4.setCellValue("星期四");

                Cell headCell5 = headRow.createCell(5);
                headCell5.setCellStyle(cellStyle2);
                headCell5.setCellValue("星期五");

                Cell headCell6 = headRow.createCell(6);
                headCell6.setCellStyle(cellStyle2);
                headCell6.setCellValue("星期六");

                Cell headCell7 = headRow.createCell(7);
                CellStyle cellStyle7 = xssfWorkbook.createCellStyle();
                cellStyle7.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyle7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                cellStyle7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                cellStyle7.setBottomBorderColor(HSSFColor.LIGHT_ORANGE.index);
                cellStyle7.setBorderRight(HSSFCellStyle.BORDER_THIN);
                cellStyle7.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                cellStyle7.setBorderTop(HSSFCellStyle.BORDER_THIN);
                cellStyle7.setTopBorderColor(HSSFColor.LIGHT_ORANGE.index);
                cellStyle7.setFont(headFont);
                headCell7.setCellStyle(cellStyle7);
                headCell7.setCellValue("星期日");

                //画表格
                String month = title.substring(5);
                month = month.substring(0, month.length());
                List<String> list = getMonthFullDay(Integer.parseInt(title.substring(0, 4)), Integer.parseInt(month));
                int rowIndex = 4;
                int rowProj = 5;
                int rowBriefing = 6;
                Row indexRow = sheet.createRow(rowIndex);
                Row projRow = sheet.createRow(rowProj);
                Row briefingRow = sheet.createRow(rowBriefing);
                //表格首行无日期单元格标志
                boolean flag = true;
                //最后一行最后一个日期单元格
                int finalCell = 0;
                //定义三行样式
                //定义样式
                Font indexFont = xssfWorkbook.createFont();
                //字体
                indexFont.setFontName("Cambria");
                //设置字体大小
                indexFont.setFontHeightInPoints((short) 11);
                //字体颜色
                indexFont.setColor(HSSFColor.LIGHT_ORANGE.index);
                CellStyle indexCellStyle = xssfWorkbook.createCellStyle();
                indexCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                indexCellStyle.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                //左上对齐
                indexCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                indexCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                indexCellStyle.setFont(indexFont);

                //定义样式
                Font projFont = xssfWorkbook.createFont();
                //字体
                projFont.setFontName("宋体");
                //设置字体大小
                projFont.setFontHeightInPoints((short) 11);
                //字体颜色
                projFont.setColor(HSSFColor.LIGHT_ORANGE.index);
                CellStyle projCellStyle = xssfWorkbook.createCellStyle();
                projCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                projCellStyle.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                //左上对齐
                projCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                projCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                //换行
                projCellStyle.setWrapText(true);
                projCellStyle.setFont(projFont);

                //定义样式
                Font briefingFont = xssfWorkbook.createFont();
                //字体
                briefingFont.setFontName("宋体");
                //设置字体大小
                briefingFont.setFontHeightInPoints((short) 10);
                //字体颜色
                briefingFont.setColor(HSSFColor.ORANGE.index);
                CellStyle briefingCellStyle = xssfWorkbook.createCellStyle();
                briefingCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                briefingCellStyle.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                briefingCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                briefingCellStyle.setBottomBorderColor(HSSFColor.LIGHT_ORANGE.index);
                //左上对齐
                briefingCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                briefingCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                //换行
                briefingCellStyle.setWrapText(true);
                briefingCellStyle.setFont(briefingFont);
                for (String date : list) {
                    //遍历数据
                    String projName = "";
                    String briefingAll = "";
                    int indexFlag = 1;
                    // 判断是行事历还是日报
                    if(isDaily){
                        for (EmpDaily empDaily : empDailies) {
                            if (date.equals(empDaily.getDailyDate())) {
                                if (indexFlag == 1) {
                                    projName = empDaily.getProjName();
                                    briefingAll = empDaily.getWorkComment();
                                }else {
                                    projName = projName+"\n"+empDaily.getProjName();
                                    briefingAll = briefingAll+"\n"+empDaily.getWorkComment();
                                }
                                indexFlag++;
                            }
                        }
                    }else {
                        for (EmpSchedule empSchedule : empSchedules) {
                            if (date.equals(empSchedule.getScheduleDate())) {
                                if (indexFlag == 1) {
                                    projName = empSchedule.getProjName();
                                    briefingAll = empSchedule.getBriefing();
                                }else {
                                    projName = projName+"\n"+empSchedule.getProjName();
                                    briefingAll = briefingAll+"\n"+empSchedule.getBriefing();
                                }
                                indexFlag++;
                            }
                        }
                    }

                    indexRow.setHeight((short)300);
                    projRow.setHeight((short)600);
                    briefingRow.setHeight((short)3000);
                    //2019-02-01
                    String index = date.substring(date.length()-2, date.length());
                    //创建序号行
                    int week = dayForWeek(date);

                    //处理首行无日期单元格
                    if (flag) {
                        for (int i = 1; i < week; i++) {
                            Cell flagCell = indexRow.createCell(i);
                            Cell flagCell1 = projRow.createCell(i);
                            Cell flagCell2 = briefingRow.createCell(i);
                            CellStyle flagCellStyle1 = xssfWorkbook.createCellStyle();
                            CellStyle flagCellStyle2 = xssfWorkbook.createCellStyle();
                            CellStyle flagCellStyle3 = xssfWorkbook.createCellStyle();
                            if (i == 1) {
                                flagCellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                flagCellStyle1.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                                flagCellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                flagCellStyle1.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                                //左上对齐
                                flagCellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                                flagCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                                flagCellStyle1.setFont(indexFont);

                                flagCellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                flagCellStyle2.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                                flagCellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                flagCellStyle2.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                                //左上对齐
                                flagCellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                                flagCellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                                flagCellStyle2.setFont(projFont);

                                flagCellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                flagCellStyle3.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                                flagCellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                flagCellStyle3.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                                flagCellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                                flagCellStyle3.setBottomBorderColor(HSSFColor.LIGHT_ORANGE.index);
                                //左上对齐
                                flagCellStyle3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                                flagCellStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                                flagCellStyle3.setFont(briefingFont);
                            } else {
                                flagCellStyle1 = indexCellStyle;
                                flagCellStyle2 = projCellStyle;
                                flagCellStyle3 = briefingCellStyle;
                            }
                            flagCell.setCellStyle(flagCellStyle1);
                            flagCell1.setCellStyle(flagCellStyle2);
                            flagCell2.setCellStyle(flagCellStyle3);
                        }
                        flag = false;
                    }

                    if (week == 7) {
                        finalCell = week;
                        //周天
                        //索引行
                        Cell indexCell = indexRow.createCell(week);
                        indexCell.setCellStyle(indexCellStyle);
                        // 定义单元格为字符串类型
                        //indexCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        indexCell.setCellValue(index);
                        rowIndex = rowIndex + 3;
                        indexRow = sheet.createRow(rowIndex);

                        //项目名称行
                        Cell projCell = projRow.createCell(week);
                        projCell.setCellStyle(projCellStyle);
                        //换行
                        projCell.setCellValue(projName);
                        rowProj = rowProj + 3;
                        projRow = sheet.createRow(rowProj);

                        //简报行
                        Cell briefingCell = briefingRow.createCell(week);
                        briefingCell.setCellStyle(briefingCellStyle);
                        //换行
                        briefingCell.setCellValue(briefingAll);
                        rowBriefing = rowBriefing + 3;
                        briefingRow = sheet.createRow(rowBriefing);
                    } else {
                        CellStyle indexCellStyle1 = xssfWorkbook.createCellStyle();
                        CellStyle projCellStyle1 = xssfWorkbook.createCellStyle();
                        CellStyle briefingCellStyle1 = xssfWorkbook.createCellStyle();
                        if (week == 1) {
                            indexCellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            indexCellStyle1.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                            indexCellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            indexCellStyle1.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                            //左上对齐
                            indexCellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                            indexCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                            indexCellStyle1.setFont(indexFont);

                            projCellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            projCellStyle1.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                            projCellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            projCellStyle1.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                            //左上对齐
                            projCellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                            projCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                            //换行
                            projCellStyle1.setWrapText(true);
                            projCellStyle1.setFont(projFont);

                            briefingCellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            briefingCellStyle1.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                            briefingCellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            briefingCellStyle1.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                            briefingCellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                            briefingCellStyle1.setBottomBorderColor(HSSFColor.LIGHT_ORANGE.index);
                            //左上对齐
                            briefingCellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                            briefingCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                            //换行
                            briefingCellStyle1.setWrapText(true);
                            briefingCellStyle1.setFont(briefingFont);
                        } else {
                            indexCellStyle1 = indexCellStyle;
                            projCellStyle1 = projCellStyle;
                            briefingCellStyle1 = briefingCellStyle;
                        }
                        finalCell = week;
                        Cell indexCell = indexRow.createCell(week);
                        indexCell.setCellStyle(indexCellStyle1);
                        // 定义单元格为字符串类型
                        //indexCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        indexCell.setCellValue(index);

                        Cell projCell = projRow.createCell(week);
                        projCell.setCellStyle(projCellStyle1);
                        //换行
                        projCell.setCellValue(projName);

                        Cell briefingCell = briefingRow.createCell(week);
                        briefingCell.setCellStyle(briefingCellStyle1);
                        //换行
                        briefingCell.setCellValue(briefingAll);
                    }
                }
                //添加末行空白单元格
                if (finalCell != 7) {
                    for (int i = finalCell+1; i <= 7; i++) {
                        Cell flagCell = indexRow.createCell(i);
                        Cell flagCell1 = projRow.createCell(i);
                        Cell flagCell2 = briefingRow.createCell(i);
                        flagCell.setCellStyle(indexCellStyle);
                        flagCell1.setCellStyle(projCellStyle);
                        flagCell2.setCellStyle(briefingCellStyle);

                    }
                }else {
                    rowBriefing = rowBriefing - 3;
                }

                //添加备注行
                Row remarkRow1 = sheet.createRow(rowBriefing+1);
                remarkRow1.setHeight((short)300);
                Row remarkRow2 = sheet.createRow(rowBriefing+2);
                remarkRow2.setHeight((short)600);
                Row remarkRow3 = sheet.createRow(rowBriefing+3);
                remarkRow3.setHeight((short)3000);
                Cell remarkCell1 = remarkRow1.createCell(1);
                Cell remarkCell2 = remarkRow2.createCell(1);
                Cell remarkCell3 = remarkRow3.createCell(1);
                CellStyle indexCellStyle1 = xssfWorkbook.createCellStyle();
                CellStyle projCellStyle1 = xssfWorkbook.createCellStyle();
                CellStyle briefingCellStyle1 = xssfWorkbook.createCellStyle();
                indexCellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                indexCellStyle1.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                indexCellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                indexCellStyle1.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                //左上对齐
                indexCellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                indexCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                projCellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                projCellStyle1.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                projCellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                projCellStyle1.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                //左上对齐
                projCellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                projCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                briefingCellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                briefingCellStyle1.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
                briefingCellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                briefingCellStyle1.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                briefingCellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                briefingCellStyle1.setBottomBorderColor(HSSFColor.LIGHT_ORANGE.index);
                //左上对齐
                briefingCellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                briefingCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                remarkCell1.setCellStyle(indexCellStyle1);
                remarkCell2.setCellStyle(projCellStyle1);
                remarkCell3.setCellStyle(briefingCellStyle1);

                Cell remarkCell4 = remarkRow1.createCell(2);
                Cell remarkCell5 = remarkRow2.createCell(2);
                Cell remarkCell6 = remarkRow3.createCell(2);
                remarkCell4.setCellStyle(indexCellStyle);
                remarkCell5.setCellStyle(projCellStyle);
                remarkCell6.setCellStyle(briefingCellStyle);

                sheet.addMergedRegion(new CellRangeAddress(rowBriefing+1, rowBriefing+1, 3, 7));
                //Cell remarkCell7 = remarkRow1.createCell(3);
                //定义样式
                Font remarkFont = xssfWorkbook.createFont();
                //字体
                remarkFont.setFontName("微软雅黑");
                //设置字体大小
                remarkFont.setFontHeightInPoints((short) 9);
                //字体颜色
                remarkFont.setColor(HSSFColor.LIGHT_ORANGE.index);
                CellStyle remarkCellStyle = xssfWorkbook.createCellStyle();
                remarkCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                remarkCellStyle.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
                //左上对齐
                remarkCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                remarkCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                remarkCellStyle.setFont(remarkFont);
                //remarkCell7.setCellStyle(remarkCellStyle);
                //remarkCell7.setCellValue("备注：");
                //解决合并单元格边框不显示问题
                for (int i=3;i<=7;i++) {
                    Cell remarkCell = remarkRow1.createCell(i);
                    if (i==3){
                        remarkCell.setCellValue("备注：");
                    }
                    remarkCell.setCellStyle(remarkCellStyle);
                }

                sheet.addMergedRegion(new CellRangeAddress(rowBriefing+2, rowBriefing+3, 3, 7));
                //Cell remarkCell8 = remarkRow2.createCell(3);
                //remarkCell8.setCellStyle(briefingCellStyle);
                //解决合并单元格边框不显示问题
                for (int i=3;i<=7;i++) {
                    Cell remarkCell = remarkRow2.createCell(i);
                    remarkCell.setCellStyle(briefingCellStyle);
                    Cell remarkCell_ = remarkRow3.createCell(i);
                    remarkCell_.setCellStyle(briefingCellStyle);
                }
            }
            if(xssfWorkbook !=null){
                try {
                    OutputStream outputStream = new FileOutputStream(file);
                    xssfWorkbook.write(outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    xssfWorkbook.dispose();
                }
                /*try {
                    FileOutputStream out = new FileOutputStream("E:\\schedule\\"+"行事历demo导出.xlsx");
                    xssfWorkbook.write(out);
                    out.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置合并单元格样式
     * @param sheet
     * @param region
     * @param cs
     */
    public static void setRegionStyle(Sheet sheet, CellRangeAddress region, CellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = CellUtil.getRow(i, sheet);
            for (int j = region.getFirstColumn(); j <= region.getFirstColumn(); j++) {
                Cell cell = CellUtil.getCell(row, (short) j);
                cell.setCellStyle(cs);
            }
        }
    }
    /**
     * 无模板导出简单Excel
     * @param header 表格头内容
     * @param datas 表格数据
     * @param path 导出路径
     * @param fileName 文件名
     */
    public static void exportDataToExcel(String[] header, List<String[]> datas, String path,String fileName) {
        File file = new File(path);
        //如果文件夹不存在
        if(!file.exists()) {
            //创建文件夹
            file.mkdirs();
        }
        file = new File(path+"/"+fileName);
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(100);
        Sheet sheet = sxssfWorkbook.createSheet();
        //创建head行
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(header[i]);
        }
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                //从第二行开始遍历创建行
                Row row = sheet.createRow(i + 1);
                String[] d = datas.get(i);
                for (int j = 0; j < d.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(d[j]);
                }
            }
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            sxssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sxssfWorkbook.dispose();
        }
    }


    /**
     * 删除临时目录
     *
     * @param path
     */
    public static void deleteFileDirectory(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                deleteFileDirectory(children[i].getPath());
            }
        }
        dir.delete();
    }

    /**
     * null转半角空格
     *
     * @param change 转换元
     * @return 转换结果
     */
    public static String NullToEmpty(String change) {
        if (StringUtils.isEmpty(change)) {
            return " ";
        } else {
            return change;
        }
    }
    /**
     *  java 获取 获取某年某月 所有日期（yyyy-mm-dd格式字符串）
     * @param year 年
     * @param month 月
     * @return
     */
    public static List<String> getMonthFullDay(int year , int month){
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        List<String> fullDayList = new ArrayList<>(32);
        // 获得当前日期对象
        Calendar cal = Calendar.getInstance();
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        // 1月从0开始
        cal.set(Calendar.MONTH, month-1 );
        // 当月1号
        cal.set(Calendar.DAY_OF_MONTH,1);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 1; j <= count ; j++) {
            fullDayList.add(dateFormatYYYYMMDD.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        return fullDayList;
    }
    /**
     * 判断当前日期是星期几
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     */
    public static int dayForWeek(String pTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int dayForWeek = 0;
        try{
            c.setTime(format.parse(pTime));
            if(c.get(Calendar.DAY_OF_WEEK) == 1){
                dayForWeek = 7;
            }else{
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return dayForWeek;
    }

    public static List<String> getMonthBetween(String minDate, String maxDate){
        ArrayList<String> result = new ArrayList<>();
        //格式化为年月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 获取每月的起始日和终止日
     * @param month yyyy-MM
     * @return
     */
    public static List<String> getMonthStartAndEnd(String month) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(month));
            c.add(Calendar.MONTH, 0);
            //设置为1号,当前日期既为本月第一天
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.setTime(sdf.parse(month));
            c1.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH));
            c1.set(Calendar.HOUR_OF_DAY, 23);
            c1.set(Calendar.MINUTE, 59);
            c1.set(Calendar.SECOND, 59);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> list = new ArrayList<>();
        list.add(sdf1.format(c.getTime()));
        list.add(sdf1.format(c1.getTime()));
        return list;
    }



    /**
     * 无模板导出简单Excel
     * @param header 表格头内容
     * @param datas 表格数据
     * @param path 导出路径
     * @param fileName 文件名
     */
    public static void exportTravelDataToExcel(String[] header, List<String[]> datas, String path,String fileName) {
        File file = new File(path);
        //如果文件夹不存在
        if(!file.exists()) {
            //创建文件夹
            file.mkdirs();
        }
        file = new File(path+"/"+fileName);
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(100);
        Sheet sheet = sxssfWorkbook.createSheet();

        sheet.setColumnWidth(0, 256*18+184);
        sheet.setColumnWidth(1, 256*18+184);
        sheet.setColumnWidth(2, 256*18+184);
        sheet.setColumnWidth(3, 256*18+184);
        sheet.setColumnWidth(4, 256*18+184);
        sheet.setColumnWidth(5, 256*18+184);
        sheet.setColumnWidth(6, 256*18+184);
        sheet.setColumnWidth(7, 256*18+184);
        sheet.setColumnWidth(8, 256*18+184);
        sheet.setColumnWidth(9, 256*18+184);
        sheet.setColumnWidth(10, 256*18+184);
        sheet.setColumnWidth(11, 256*18+184);
        sheet.setColumnWidth(12, 256*18+184);
        sheet.setColumnWidth(13, 256*18+184);
        sheet.setColumnWidth(14, 256*18+184);
        sheet.setColumnWidth(15, 256*18+184);
        sheet.setColumnWidth(16, 256*18+184);

        //创建head行
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(header[i]);
        }
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                //从第二行开始遍历创建行
                Row row = sheet.createRow(i + 1);
                String[] d = datas.get(i);
                for (int j = 0; j < d.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(d[j]);
                }
            }
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            sxssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sxssfWorkbook.dispose();
        }
    }

    /**
     * 设定列宽导出简单Excel
     * @param header 表格头内容
     * @param datas 表格数据
     * @param path 导出路径
     * @param fileName 文件名
     */
    public static void exportOrderDataToExcel(String[] header, List<String[]> datas, String path,String fileName) {
        File file = new File(path);
        //如果文件夹不存在
        if(!file.exists()) {
            //创建文件夹
            file.mkdirs();
        }
        file = new File(path+"/"+fileName);
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(100);
        Sheet sheet = sxssfWorkbook.createSheet();
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 40 * 256);
        sheet.setColumnWidth(2, 80 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        //创建head行
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(header[i]);
        }
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                //从第二行开始遍历创建行
                Row row = sheet.createRow(i + 1);
                String[] d = datas.get(i);
                for (int j = 0; j < d.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(d[j]);
                }
            }
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            sxssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sxssfWorkbook.dispose();
        }
    }

    /**
     * 项目周报模板
     * @param datas 表格数据
     * @param path 导出路径
     * @param fileName 文件名
     */
    public static void exportProjWeeklyDataToExcel(List<Map<String, Object>> datas, String path, String fileName) {
        File file = new File(path);
        //如果文件夹不存在
        if(!file.exists()) {
            //创建文件夹
            file.mkdirs();
        }
        file = new File(path+"/"+fileName);
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(100);
        //文本样式
        CellStyle titleCellStyle = sxssfWorkbook.createCellStyle();
        titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐居中
        titleCellStyle.setWrapText(true); // 设置为自动换行
        Font font = sxssfWorkbook.createFont();
        font.setFontName("等线");
        font.setFontHeightInPoints((short) 15);
        titleCellStyle.setFont(font);
        titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
//        文本样式2（底色）
        CellStyle titleCellStyle1 = sxssfWorkbook.createCellStyle();
        titleCellStyle1.setFillForegroundColor(IndexedColors. LIGHT_TURQUOISE.getIndex());
        titleCellStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleCellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐居中
        titleCellStyle1.setWrapText(true); // 设置为自动换行
        Font titleCellStyle_font = sxssfWorkbook.createFont();
        titleCellStyle_font.setFontName("等线");
        titleCellStyle_font.setFontHeightInPoints((short) 15);
        titleCellStyle1.setFont(titleCellStyle_font);
        titleCellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        titleCellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        titleCellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        titleCellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
//        表头样式(加粗底色)
        CellStyle headerStyle1 = sxssfWorkbook.createCellStyle();// 创建标题样式1
        headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle1.setWrapText(true);
        headerStyle1.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        headerStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font headerFont1 =sxssfWorkbook.createFont();
        headerFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
        headerFont1.setFontName("等线");
        headerFont1.setFontHeightInPoints((short) 15);
        headerStyle1.setFont(headerFont1);
        headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
//        表头单行（底色）
        CellStyle headerStyle2 = sxssfWorkbook.createCellStyle();
        headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐居中
        headerStyle2.setWrapText(true); // 设置为自动换行
        Font headerFont2 = sxssfWorkbook.createFont();
        headerFont2.setFontName("等线");
        headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerFont2.setFontHeightInPoints((short) 15);
        headerStyle2.setFont(headerFont2);
        headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        for (int i = 0; i < datas.size(); i++) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String projId = (String) datas.get(i).get("projId");
            String projName = (String) datas.get(i).get("projName");
//            String startDate = dateFormat.format(datas.get(i).get("startDate")==null?"":datas.get(i).get("startDate"));
//            String endDate = dateFormat.format(datas.get(i).get("endDate")==null?"":datas.get(i).get("endDate"));
            String startDate = datas.get(i).get("startDate").toString().replace("-","/");
            String endDate = datas.get(i).get("endDate").toString().replace("-","/");
            String projManage = (String) datas.get(i).get("projManage");
            String empName = (String) datas.get(i).get("empName");
            String jsonStr = (String) datas.get(i).get("contentDetail");
            JSONObject jsonObject = new JSONObject(jsonStr);
            List<Map<String,Object>> detailList = (List<Map<String,Object>>)jsonObject.get("details");
            List<Map<String,Object>> riskList = (List<Map<String, Object>>) jsonObject.get("risks");
            List<Map<String,Object>> nextList = (List<Map<String, Object>>) jsonObject.get("nexts");
            Date date = null;
            try {
                date = dateFormat.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(date);
            int weekYear = calendar.get(Calendar.WEEK_OF_YEAR);
            Sheet sheet = sxssfWorkbook.createSheet("(" + projId + ")" + empName + " 第" + weekYear + "周");
            //创建行
            Row row_0 = sheet.createRow(0);
            row_0.setHeight((short)750);
            Cell cell_0_0 = row_0.createCell(0);
            cell_0_0.setCellValue("项目编号");
            Cell cell_0_1 = row_0.createCell(1);
            cell_0_1.setCellValue(projId);
            Cell cell_0_2 = row_0.createCell(2);
            cell_0_2.setCellValue("项目名称");
            Cell cell_0_3 = row_0.createCell(3);
            cell_0_3.setCellValue(projName);
            Cell cell_0_4 = row_0.createCell(4);
            cell_0_4.setCellValue("项目经理");
            Cell cell_0_5 = row_0.createCell(5);
            cell_0_5.setCellValue(projManage);
            Row row_1 = sheet.createRow(1);
            row_1.setHeight((short)750);
            Cell cell_1_0 = row_1.createCell(0);
            cell_1_0.setCellValue("周开始日期");
            Cell cell_1_1 = row_1.createCell(1);
            cell_1_1.setCellValue(startDate);
            Cell cell_1_2 = row_1.createCell(2);
            cell_1_2.setCellValue("周结束日期");
            Cell cell_1_3 = row_1.createCell(3);
            cell_1_3.setCellValue(endDate);
            Cell cell_1_4 = row_1.createCell(4);
            cell_1_4.setCellValue("报告人");
            Cell cell_1_5 = row_1.createCell(5);
            cell_1_5.setCellValue(empName);
            Row row_2 = sheet.createRow(2);
            row_2.setHeight((short)700);
            cell_0_0.setCellStyle(headerStyle1);
            cell_0_1.setCellStyle(titleCellStyle1);
            cell_0_2.setCellStyle(headerStyle1);
            cell_0_3.setCellStyle(titleCellStyle1);
            cell_0_4.setCellStyle(headerStyle1);
            cell_0_5.setCellStyle(titleCellStyle1);
            cell_1_0.setCellStyle(headerStyle1);
            cell_1_1.setCellStyle(titleCellStyle1);
            cell_1_2.setCellStyle(headerStyle1);
            cell_1_3.setCellStyle(titleCellStyle1);
            cell_1_4.setCellStyle(headerStyle1);
            cell_1_5.setCellStyle(titleCellStyle1);
            //设置列宽度
            sheet.setColumnWidth((short) 0, (short) 4000);
            sheet.setColumnWidth((short) 1, (short) 7000);
            sheet.setColumnWidth((short) 2, (short) 4000);
            sheet.setColumnWidth((short) 3, (short) 7000);
            sheet.setColumnWidth((short) 4, (short) 4000);
            sheet.setColumnWidth((short) 5, (short) 7000);
            CellRangeAddress cra = new CellRangeAddress(2,2,3,4);
            sheet.addMergedRegion(cra);
            RegionUtil.setBorderBottom(1, cra, sheet,sxssfWorkbook);
            //sheet.addMergedRegion(new CellRangeAddress(2,2,3,4));
            Cell cell_2_0 = row_2.createCell(0);
            cell_2_0.setCellValue("序号");
            Cell cell_2_1 = row_2.createCell(1);
            cell_2_1.setCellValue("工作主题");
            Cell cell_2_2 = row_2.createCell(2);
            cell_2_2.setCellValue("参与人");
            Cell cell_2_3 = row_2.createCell(3);
            cell_2_3.setCellValue("工作内容");
            Cell cell_2_5 = row_2.createCell(5);
            cell_2_5.setCellValue("完成情况");
            cell_2_0.setCellStyle(headerStyle1);
            cell_2_1.setCellStyle(headerStyle1);
            cell_2_2.setCellStyle(headerStyle1);
            cell_2_3.setCellStyle(headerStyle1);
            cell_2_5.setCellStyle(headerStyle1);

            Row row_3 = sheet.createRow(3);
            row_3.setHeight((short)700);
//            sheet.addMergedRegion(new CellRangeAddress(3,3,0,5));
            CellRangeAddress cra3305 = new CellRangeAddress(3,3,0,5);
            sheet.addMergedRegion(cra3305);
            RegionUtil.setBorderBottom(1, cra3305, sheet,sxssfWorkbook);
            RegionUtil.setBorderRight(1, cra3305, sheet,sxssfWorkbook);
            Cell cell_3_0 = row_3.createCell(0);
            cell_3_0.setCellValue("本周工作内容");
            RegionUtil.setBorderBottom(1, cra, sheet,sxssfWorkbook);
            cell_3_0.setCellStyle(headerStyle2);
            int startRow = 4;
            if (detailList != null && detailList.size() > 0) {
                for (int k = 0; k < detailList.size(); k++) {
                    //从第k + startRow行开始遍历创建行
                    Row row = sheet.createRow(k + startRow);
                    row.setHeight((short)2500);
//                    sheet.addMergedRegion(new CellRangeAddress(k + startRow,k + startRow,3,4));
                    CellRangeAddress cra34 = new CellRangeAddress(k + startRow,k + startRow,3,4);
                    sheet.addMergedRegion(cra34);
                    RegionUtil.setBorderBottom(1, cra34, sheet,sxssfWorkbook);
                    String workNum = detailList.get(k).get("workNum").toString().equals("null")?"":detailList.get(k).get("workNum").toString();
                    String workTheme = detailList.get(k).get("workTheme").toString().equals("null")?"":detailList.get(k).get("workTheme").toString();
                    String workPartner = detailList.get(k).get("workPartner").toString().equals("null")?"":detailList.get(k).get("workPartner").toString();
                    String workContent = detailList.get(k).get("workContent").toString().equals("null")?"":detailList.get(k).get("workContent").toString();
                    String workSchedule = detailList.get(k).get("workSchedule").toString().equals("null")?"":detailList.get(k).get("workSchedule").toString();
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(workNum);
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(workTheme);
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(workPartner);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(workContent);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(workSchedule);
                    cell0.setCellStyle(titleCellStyle);
                    cell1.setCellStyle(titleCellStyle);
                    cell2.setCellStyle(titleCellStyle);
                    cell3.setCellStyle(titleCellStyle);
                    cell5.setCellStyle(titleCellStyle);
                }
            }
            if(true){
                if(detailList != null && detailList.size() > 0){
                    startRow = detailList.size() + 4;
                }
                Row row = sheet.createRow(startRow);
                row.setHeight((short)700);
                sheet.addMergedRegion(new CellRangeAddress(startRow, startRow,3,4));
                Cell cell0 = row.createCell(0);
                cell0.setCellValue("序号");
                Cell cell1 = row.createCell(1);
                cell1.setCellValue("风险概述");
                Cell cell2 = row.createCell(2);
                cell2.setCellValue("负责人");
                Cell cell3 = row.createCell(3);
                cell3.setCellValue("需解决和协调的事宜、问题");
                Cell cell5 = row.createCell(5);
                cell5.setCellValue("解决措施");
                cell0.setCellStyle(headerStyle1);
                cell1.setCellStyle(headerStyle1);
                cell2.setCellStyle(headerStyle1);
                cell3.setCellStyle(headerStyle1);
                cell5.setCellStyle(headerStyle1);
            }
            startRow++;
            if (riskList != null && riskList.size() > 0) {
                for (int k = 0; k < riskList.size(); k++) {
                    //从第k + startRow行开始遍历创建行
                    Row row = sheet.createRow(k + startRow);
                    row.setHeight((short)1800);
//                    sheet.addMergedRegion(new CellRangeAddress(k + startRow,k + startRow,3,4));
                    CellRangeAddress cra34 = new CellRangeAddress(k + startRow,k + startRow,3,4);
                    sheet.addMergedRegion(cra34);
                    RegionUtil.setBorderTop(1, cra34, sheet,sxssfWorkbook);
                    RegionUtil.setBorderBottom(1, cra34, sheet,sxssfWorkbook);
                    String num = riskList.get(k).get("num").toString().equals("null")?"" : riskList.get(k).get("num").toString();
                    String riskConcept = riskList.get(k).get("riskConcept").toString().equals("null")?"" : riskList.get(k).get("riskConcept").toString();
                    String principal = riskList.get(k).get("principal").toString().equals("null")?"" : riskList.get(k).get("principal").toString();
                    String issue = riskList.get(k).get("issue").toString().equals("null")?"" : riskList.get(k).get("issue").toString();
                    String solution =  riskList.get(k).get("solution").toString().equals("null")?"" : riskList.get(k).get("solution").toString();
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(num);
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(riskConcept);
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(principal);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(issue);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(solution);
                    cell0.setCellStyle(titleCellStyle);
                    cell1.setCellStyle(titleCellStyle);
                    cell2.setCellStyle(titleCellStyle);
                    cell3.setCellStyle(titleCellStyle);
                    cell5.setCellStyle(titleCellStyle);
                }
            }
            if(true){
                if(riskList != null && riskList.size() > 0){
                    startRow = startRow + riskList.size();
                }
                Row row = sheet.createRow(startRow);
                row.setHeight((short)700);
//                sheet.addMergedRegion(new CellRangeAddress(startRow, startRow,0,5));
                CellRangeAddress cra05 = new CellRangeAddress(startRow,startRow,0,5);
                sheet.addMergedRegion(cra05);
                RegionUtil.setBorderRight(1, cra05, sheet,sxssfWorkbook);
                RegionUtil.setBorderTop(1, cra05, sheet,sxssfWorkbook);
                Cell cell = row.createCell(0);
                cell.setCellValue("下周工作计划");
                cell.setCellStyle(headerStyle2);
            }
            startRow++;
            if (nextList != null && nextList.size() > 0) {
                for (int k = 0; k < nextList.size(); k++) {
                    //从第k + startRow行开始遍历创建行
                    Row row = sheet.createRow(k + startRow);
                    row.setHeight((short)1500);
//                    sheet.addMergedRegion(new CellRangeAddress(k + startRow,k + startRow,3,4));
                    CellRangeAddress cra15 = new CellRangeAddress(k + startRow,k + startRow,3,4);
                    sheet.addMergedRegion(cra15);
                    RegionUtil.setBorderTop(1, cra15, sheet,sxssfWorkbook);
                    RegionUtil.setBorderBottom(1, cra15, sheet,sxssfWorkbook);
                    RegionUtil.setBorderRight(1, cra15, sheet,sxssfWorkbook);
                    String num = nextList.get(k).get("num").toString().equals("null")?"":nextList.get(k).get("num").toString();
                    String theme = nextList.get(k).get("theme").toString().equals("null")?"":nextList.get(k).get("theme").toString();
                    String partner = nextList.get(k).get("partner").toString().equals("null")?"":nextList.get(k).get("partner").toString();
                    String detail = nextList.get(k).get("detail").toString().equals("null")?"":nextList.get(k).get("detail").toString();
                    String schedule = nextList.get(k).get("schedule").toString().equals("null")?"":nextList.get(k).get("schedule").toString();
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(num);
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(theme);
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(partner);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(detail);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(schedule);
                    cell0.setCellStyle(titleCellStyle);
                    cell1.setCellStyle(titleCellStyle);
                    cell2.setCellStyle(titleCellStyle);
                    cell3.setCellStyle(titleCellStyle);
                    cell5.setCellStyle(titleCellStyle);
                }
            }
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            sxssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sxssfWorkbook.dispose();
        }
    }


}
