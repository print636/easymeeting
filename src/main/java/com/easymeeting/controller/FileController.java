package com.easymeeting.controller;

import com.easymeeting.annotation.GlobalInterceptor;
import com.easymeeting.config.AppConfig;
import com.easymeeting.entity.constants.Constants;
import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.enums.DateTimePatternEnum;
import com.easymeeting.entity.enums.FileTypeEnum;
import com.easymeeting.entity.enums.ResponseCodeEnum;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.utils.DateUtil;
import com.easymeeting.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Date;

@Slf4j
@Validated
@RestController
@RequestMapping("/file")
public class FileController extends ABaseController {

    @Resource
    private AppConfig appConfig;

    @RequestMapping("/getResource")
    @GlobalInterceptor(checkLogin = false)
    public void getResource(HttpServletResponse response,
                            @RequestHeader(required = false, name = "range") String range,
                            @NotNull Long messageId,
                            @NotNull Long sendTime,
                            @NotNull Integer fileType,
                            @NotEmpty String token,
                            Boolean thumbnail) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        FileTypeEnum fileTypeEnum = FileTypeEnum.getByType(fileType);
        if (fileTypeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        thumbnail = thumbnail == null ? Boolean.FALSE : thumbnail;
        String month = DateUtil.format(new Date(sendTime), DateTimePatternEnum.YYYYMM.getPattern());
        String filePath = appConfig.getProjectFolder()
                + Constants.FILE_FOLDER_FILE
                + month + "/"
                + messageId
                + fileTypeEnum.getSuffix();

        switch (fileTypeEnum) {
            case IMAGE:
                response.setHeader("Cache-Control", "max-age=" + 30 * 24 * 60 * 60);
                response.setContentType("image/jpg");
                break;
            case VIDEO:
                response.setContentType("video/mp4");
                break;
            default:
                throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        readFile(response, range, filePath, thumbnail);
    }

    @RequestMapping("/downloadFile")
    @GlobalInterceptor(checkLogin = false)
    public void downloadFile(HttpServletResponse response,
                             @NotEmpty String token,
                             @NotNull Long messageId,
                             @NotNull Long sendTime,
                             @NotEmpty String suffix) throws IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        String month = DateUtil.format(new Date(sendTime), DateTimePatternEnum.YYYYMM.getPattern());
        String filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + month + "/" + messageId + suffix;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        response.setContentType("application/x-msdownload; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;");
        response.setContentLengthLong(file.length());
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
    }

    @RequestMapping("/getAvatar")
    @GlobalInterceptor(checkLogin = false)
    public void getAvatar(HttpServletResponse response,
                          @NotNull String userId,
                          @NotEmpty String token) throws IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        String filePath = appConfig.getProjectFolder()
                + Constants.FILE_FOLDER_FILE
                + Constants.FILE_FOLDER_AVATAR_NANE
                + userId
                + Constants.IMAGE_SUFFIX;
        response.setContentType("image/jpg");
        File file = new File(filePath);
        if (!file.exists()) {
            readLocalFile(response);
            return;
        }
        readFile(response, null, filePath, false);
    }

    protected void readFile(HttpServletResponse response, String range, String filePath, Boolean thumbnail) {
        String actualPath = Boolean.TRUE.equals(thumbnail) ? StringTools.getImageThumbnail(filePath) : filePath;
        File file = new File(actualPath);
        if (!file.exists()) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        try (ServletOutputStream out = response.getOutputStream();
             RandomAccessFile randomFile = new RandomAccessFile(file, "r")) {
            long contentLength = randomFile.length();
            int start = 0;
            int end = 0;
            if (range != null && range.startsWith("bytes=")) {
                String[] values = range.split("=")[1].split("-");
                start = Integer.parseInt(values[0]);
                if (values.length > 1 && !StringTools.isEmpty(values[1])) {
                    end = Integer.parseInt(values[1]);
                }
            }
            int requestSize;
            if (end != 0 && end >= start) {
                requestSize = end - start + 1;
            } else {
                requestSize = Integer.MAX_VALUE;
            }

            byte[] buffer = new byte[4096];
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Last-Modified", new Date().toString());
            if (range == null) {
                response.setHeader("Content-length", String.valueOf(contentLength));
            } else {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                long requestStart = start;
                long requestEnd = end > 0 ? end : contentLength - 1;
                long length = requestEnd - requestStart + 1;
                response.setHeader("Content-length", String.valueOf(length));
                response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
            }

            int needsize = requestSize;
            randomFile.seek(start);
            while (needsize > 0) {
                int len = randomFile.read(buffer);
                if (len == -1) {
                    break;
                }
                if (needsize > buffer.length) {
                    out.write(buffer, 0, buffer.length);
                } else {
                    out.write(buffer, 0, Math.min(len, needsize));
                    if (len < buffer.length) {
                        break;
                    }
                }
                needsize -= buffer.length;
            }
        } catch (Exception e) {
            log.error("读取文件信息失败", e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }

    private void readLocalFile(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "max-age=" + 30 * 24 * 60 * 60);
        response.setContentType("image/jpg");
        ClassPathResource classPathResource = new ClassPathResource(Constants.DEFAULT_AVATAR);
        try (OutputStream out = response.getOutputStream();
             InputStream in = classPathResource.getInputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("读取本地文件异常", e);
            throw e;
        }
    }
}

