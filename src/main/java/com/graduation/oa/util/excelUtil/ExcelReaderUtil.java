package com.graduation.oa.util.excelUtil;

import com.bestvike.commons.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 读取Excel
 * Created by szh on 2018/12/04.
 **/
@Component
public class ExcelReaderUtil {

    /**
     * excel2003扩展名
     */
    public static final String EXCEL03_EXTENSION = ".xls";

    /**
     * excel2007扩展名
     */
    public static final String EXCEL07_EXTENSION = ".xlsx";

    /**
     * 结果集
     */
    public static List<List<String>> dataList;

    /**
     * 重复项用Map
     */
    public static HashMap<String, String> isRepeatMap;

    /**
     * 重复项
     */
    public static String repeat;

    /**
     * 最大列
     */
    public static int maxCols;

    /**
     * 是否判断进行最大列check
     */
    public static boolean isChecked;

    /**
     * 标题A列内容
     */
    public static String firstTitleNm;

    /**
     * 每获取一条记录，加入到list中
     *
     * @param cellList
     */
    public synchronized static void sendRows(List<String> cellList) {
        List<String> data = new ArrayList<>();

        // 仅对标题行进行列数check以及A列内容check
        if (isChecked) {
            isChecked = false;
            if (cellList.size() != maxCols || !firstTitleNm.equals(cellList.get(0))) {
                throw new ServiceException("FF", "导入的数据文件不正确");
            }
        } else {
            // 标题行以外行
            for (int i = 0; i < cellList.size() && i < maxCols; i++) {
                data.add(cellList.get(i));
                // 判断A列是否有重复项
                if (i == 0) {
                    if (isRepeatMap.containsKey(cellList.get(i))) {
                        repeat += cellList.get(i) + " ,";
                    }
                    isRepeatMap.put(cellList.get(i), cellList.get(i));
                }
            }
            dataList.add(data);
        }
    }

    /**
     * 读取Excel
     *
     * @param fileName       文件名
     * @param titleRow       标题行
     * @param maxColumns     最大列
     * @param firstTitleName 第一个标题名
     * @throws Exception
     */
    public synchronized static void readExcel(String fileName, int titleRow, int maxColumns, String firstTitleName) throws Exception {
        dataList = new ArrayList<List<String>>();
        isRepeatMap = new HashMap<>();
        maxCols = maxColumns;
        firstTitleNm = firstTitleName;
        isChecked = true;
        repeat = "";

        int totalRows = 0;
        //处理excel2003文件
        if (fileName.endsWith(EXCEL03_EXTENSION)) {
            ExcelXlsReader excelXls = new ExcelXlsReader();
            totalRows = excelXls.process(fileName, titleRow - 1);
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {
            //处理excel2007文件
            ExcelXlsxReader excelXlsxReader = new ExcelXlsxReader();
            totalRows = excelXlsxReader.process(fileName, titleRow - 1);
        } else {
            throw new Exception("只支持上传xlsx或xls类型文件");
        }

        System.out.println("读取的总行数：" + totalRows);
    }
}