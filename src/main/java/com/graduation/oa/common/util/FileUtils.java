package com.graduation.oa.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.graduation.oa.common.util.DateUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

public class FileUtils {
    private static final int STREAM_BUFFER_LENGTH = 1024;

    public FileUtils() {
    }

    public static String getContentType(String fileName) {
        String extName = getExtName(fileName);
        if (StringUtils.isEmpty(extName)) {
            return "";
        } else {
            String contentType = "";
            if (extName.equals("png")) {
                contentType = "image/png";
            } else if (!extName.equals("jpeg") && !extName.equals("jpg")) {
                if (extName.equals("gif")) {
                    contentType = "image/gif";
                } else if (extName.equals("bmp")) {
                    contentType = "image/x-bmp";
                } else if (extName.equals("zip")) {
                    contentType = "application/zip";
                } else if (extName.equals("rar")) {
                    contentType = "application/x-rar-compressed";
                } else if (extName.equals("exe")) {
                    contentType = "application/x-msdownload";
                } else if (extName.equals("pdf")) {
                    contentType = "application/pdf";
                } else if (extName.equals("doc")) {
                    contentType = "application/msword";
                } else if (extName.equals("docx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                } else if (extName.equals("xls")) {
                    contentType = "application/vnd.ms-excel";
                } else if (extName.equals("xlsx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                } else if (extName.equals("ppt")) {
                    contentType = "application/vnd.ms-powerpoint";
                } else if (extName.equals("pptx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
                } else if (extName.equals("apk")) {
                    contentType = "application/vnd.android.package-archive";
                } else {
                    contentType = "application/x-msdownload";
                }
            } else {
                contentType = "image/jpeg";
            }

            return contentType;
        }
    }

    public static String getExtNameByContentType(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return "";
        } else if (contentType.contains("application/vnd.ms-excel")) {
            return ".xls";
        } else {
            return contentType.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ? ".xlsx" : "";
        }
    }

    public static String getExtName(String fileName) {
        return !StringUtils.isEmpty(fileName) && fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
    }

    public static String getFileName(String fileName) {
        return fileName;
    }

    public static String encodeFileName(String userAgent, String fileName) {
        fileName = getFileName(fileName);
        if (!StringUtils.isEmpty(userAgent) && !StringUtils.isEmpty(fileName)) {
            if (userAgent.indexOf("Windows") == -1 || userAgent.indexOf("MSIE") != -1 || userAgent.indexOf("Trident") != -1 || userAgent.indexOf("Edge") != -1) {
                try {
                    return URLEncoder.encode(fileName, "UTF8");
                } catch (UnsupportedEncodingException var4) {
                    return fileName;
                }
            }

            if (userAgent.indexOf("Mozilla") != -1) {
                try {
                    return "=?UTF-8?B?" + new String(Base64Utils.encode(fileName.getBytes("UTF-8"))) + "?=";
                } catch (UnsupportedEncodingException var3) {
                    return fileName;
                }
            }
        }

        return fileName;
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
        download(request, response, (InputStream)(new FileInputStream(file)), file.getName());
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
        download(request, response, getTempFile(fileName));
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, String downloadFileWithPath, String originFileName) throws IOException {
        if (!StringUtils.isEmpty(downloadFileWithPath)) {
            if (downloadFileWithPath.indexOf(File.separator) > 0) {
                download(request, response, (InputStream)(new FileInputStream(downloadFileWithPath)), originFileName);
            } else {
                download(request, response, (InputStream)(new FileInputStream(getTempFile(downloadFileWithPath))), originFileName);
            }
        }

    }

    public static void download(HttpServletRequest request, HttpServletResponse response, InputStream in, String fileName) throws IOException {
        response.setContentType(getContentType(fileName));
        response.setHeader("content-disposition", "attachment; filename=\"" + encodeFileName(request.getHeader("USER-AGENT"), fileName) + "\"");
        if (in instanceof FileInputStream) {
            response.setContentLength(((FileInputStream)in).available());
        }

        ServletOutputStream os = response.getOutputStream();
        byte[] buf = new byte[4096];

        int readLength;
        while((readLength = in.read(buf)) != -1) {
            os.write(buf, 0, readLength);
        }

        os.flush();
        in.close();
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, byte[] b, String fileName) throws IOException {
        response.setContentType(getContentType(fileName));
        response.setHeader("content-disposition", "attachment; filename=\"" + encodeFileName(request.getHeader("USER-AGENT"), fileName) + "\"");
        ServletOutputStream os = response.getOutputStream();
        os.write(b, 0, b.length);
        os.flush();
    }

    public static File getTempFile(String fileName) {
        return fileName.contains(File.separator) ? new File(fileName) : new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
    }

    public static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int read;
        while((read = in.read(buffer)) > -1) {
            baos.write(buffer, 0, read);
        }

        baos.flush();
        return baos.toByteArray();
    }

    public static String readString(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int read;
        while((read = in.read(buffer)) > -1) {
            baos.write(buffer, 0, read);
        }

        baos.flush();
        return baos.toString("utf-8");
    }

    public static void save(byte[] in, OutputStream os) throws IOException {
        os.write(in);
        os.close();
    }

    public static Integer save(InputStream in, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];

        Integer fileSize;
        int read;
        for(fileSize = 0; (read = in.read(buffer)) > -1; fileSize = fileSize + read) {
            os.write(buffer, 0, read);
        }

        in.close();
        os.close();
        return fileSize;
    }

    public static void delete(File file, File recycleFile) {
        if (recycleFile != null) {
            file.renameTo(recycleFile);
        } else {
            file.delete();
        }

    }

    public static InputStream getInputStream(String fileName) throws IOException {
        if (!StringUtils.isEmpty(fileName)) {
            if (fileName.startsWith("classpath:")) {
                ResourceLoader loader = new DefaultResourceLoader();
                return loader.getResource(fileName).getInputStream();
            } else {
                return new FileInputStream(fileName);
            }
        } else {
            return null;
        }
    }

    public static String filePath(String excelPath) {
        File file = new File(excelPath + File.separator + DateUtils.formatDate(new Date(), "yyyy_MM"));
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath() + File.separator;
    }

    public static String extType(String extName) {
        if (StringUtils.isEmpty(extName)) {
            return "";
        } else {
            extName = extName.toLowerCase();
            if (!extName.equals("jpg") && !extName.equals("png") && !extName.equals("bmp") && !extName.equals("jpeg") && !extName.equals("gif")) {
                if (!extName.equals("doc") && !extName.equals("docx")) {
                    if (!extName.equals("xls") && !extName.equals("xlsx")) {
                        if (!extName.equals("htm") && !extName.equals("html")) {
                            return !extName.equals("pdf") && !extName.equals("txt") && !extName.equals("js") && !extName.equals("xml") ? "UNKNOWN" : extName.toUpperCase();
                        } else {
                            return "HTML";
                        }
                    } else {
                        return "XLS";
                    }
                } else {
                    return "DOC";
                }
            } else {
                return "PIC";
            }
        }
    }
}
