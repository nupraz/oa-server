package com.graduation.oa.common.util.excelUtil;

import cn.hutool.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
