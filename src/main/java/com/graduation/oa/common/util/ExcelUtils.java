package com.graduation.oa.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.graduation.oa.common.util.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.common.Context;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReader;
import org.jxls.util.JxlsHelper;
import org.xml.sax.SAXException;

public final class ExcelUtils {
    public ExcelUtils() {
    }

    public static void exportFile(HttpServletRequest request, HttpServletResponse response, String templatePath, String templateFileName, String exportFileName, Map<String, Object> varMap) throws IOException {
        Context context = new Context(varMap);
        response.setContentType(FileUtils.getContentType(exportFileName));
        response.setHeader("content-disposition", "attachment; filename=\"" + FileUtils.encodeFileName(request.getHeader("USER-AGENT"), exportFileName) + "\"");
        ServletOutputStream os = response.getOutputStream();
        InputStream is = FileUtils.getInputStream(templatePath + File.separator + templateFileName);
        JxlsHelper.getInstance().processTemplate(is, os, context);
        is.close();
    }

    public static String exportFile(String templatePath, String templateFileName, String exportFileName, Map<String, Object> varMap) throws IOException {
        Context context = new Context(varMap);
        File file = FileUtils.getTempFile(exportFileName);
        OutputStream os = new FileOutputStream(file);
        InputStream is = FileUtils.getInputStream(templatePath + File.separator + templateFileName);
        JxlsHelper.getInstance().processTemplate(is, os, context);
        is.close();
        os.close();
        return file.getAbsolutePath();
    }

    public static Map<String, Object> exportFile(String templatePath, String templateFileName, String exportFileName, String originName, Map<String, Object> varMap) throws IOException {
        exportFile(templatePath, templateFileName, exportFileName, varMap);
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("fileName", exportFileName);
        resultMap.put("originName", originName);
        return resultMap;
    }

    public static void exportGridFile(HttpServletRequest request, HttpServletResponse response, String templatePath, String templateFileName, String exportFileName, Map<String, Object> varMap) throws IOException {
        Context context = new Context(varMap);
        response.setContentType(FileUtils.getContentType(exportFileName));
        response.setHeader("content-disposition", "attachment; filename=\"" + FileUtils.encodeFileName(request.getHeader("USER-AGENT"), exportFileName) + "\"");
        ServletOutputStream os = response.getOutputStream();
        InputStream is = FileUtils.getInputStream(templatePath + File.separator + templateFileName);
        JxlsHelper.getInstance().processGridTemplate(is, os, context, (String)varMap.get("props"));
        is.close();
    }

    public static String exportGridFile(String templatePath, String templateFileName, String exportFileName, Map<String, Object> varMap) throws IOException {
        Context context = new Context(varMap);
        File file = FileUtils.getTempFile(exportFileName);
        OutputStream os = new FileOutputStream(file);
        InputStream is = FileUtils.getInputStream(templatePath + File.separator + templateFileName);
        JxlsHelper.getInstance().processGridTemplate(is, os, context, (String)varMap.get("props"));
        is.close();
        os.close();
        return file.getAbsolutePath();
    }

    public static Map<String, Object> exportGridFile(String templatePath, String templateFileName, String exportFileName, String originName, Map<String, Object> varMap) throws IOException {
        exportGridFile(templatePath, templateFileName, exportFileName, varMap);
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("fileName", exportFileName);
        resultMap.put("originName", originName);
        return resultMap;
    }

    public static void importFile(String templatePath, String templateXmlName, InputStream inputStream, Map<String, Object> varMap) throws IOException, SAXException, InvalidFormatException {
        InputStream is = FileUtils.getInputStream(templatePath + File.separator + templateXmlName);
        XLSReader reader = ReaderBuilder.buildFromXML(is);
        reader.read(inputStream, varMap);
        is.close();
        inputStream.close();
    }
}
