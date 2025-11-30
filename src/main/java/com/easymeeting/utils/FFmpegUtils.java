package com.easymeeting.utils;

import com.easymeeting.entity.constants.Constants;
import com.easymeeting.entity.enums.FileTypeEnum;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 基于 FFmpeg/FFprobe 的媒体处理工具。
 */
@Component
public class FFmpegUtils {

    private static final String CMD_TRANSFER_IMAGE = "ffmpeg -y -i \"%s\" \"%s\"";
    private static final String CMD_CREATE_IMAGE_THUMBNAIL = "ffmpeg -y -i \"%s\" -vf scale=200:-1 \"%s\"";
    private static final String CMD_GET_VIDEO_CODEC = "ffprobe -v error -select_streams v:0 -show_entries stream=codec_name -of default=noprint_wrappers=1:nokey=1 \"%s\"";
    private static final String CMD_CONVERT_TO_MP4 = "ffmpeg -y -i \"%s\" -c:v libx264 -c:a copy \"%s\"";
    private static final String CMD_EXTRACT_VIDEO_COVER = "ffmpeg -y -i \"%s\" -ss 00:00:01 -vframes 1 -vf scale=200:-1 \"%s\"";

    /**
     * 统一图片后缀（转为 jpg）。
     */
    public String transferImageType(File tempFile, String targetPath) {
        ProcessUtils.executeCommand(String.format(CMD_TRANSFER_IMAGE, tempFile.getAbsolutePath(), targetPath));
        deleteQuietly(tempFile);
        return targetPath;
    }

    /**
     * 非 mp4 或 HEVC 编码时，转为 mp4；否则直接复制。
     */
    public void transferVideoType(File tempFile, String targetPath, String fileSuffix) throws IOException {
        String codec = getVideoCodec(tempFile.getAbsolutePath());
        boolean needConvert = Constants.vIDEo_CODE_HEVC.equalsIgnoreCase(codec)
                || !Constants.VIDEO_SUFFIX.equalsIgnoreCase(fileSuffix);
        if (needConvert) {
            convertVideoToMp4(tempFile.getAbsolutePath(), targetPath);
        } else {
            FileUtils.copyFile(tempFile, new File(targetPath));
        }
        deleteQuietly(tempFile);
    }

    /**
     * 生成图片缩略图（输入已经落盘的文件）。
     */
    public void createImageThumbnail(String filePath) {
        String thumbnailPath = StringTools.getImageThumbnail(filePath);
        createImageThumbnail(new File(filePath), new File(thumbnailPath));
    }

    /**
     * 生成图片缩略图（临时文件输入，目标路径为字符串）。
     */
    public void createImageThumbnail(File tempFile, String filePath) {
        createImageThumbnail(tempFile, new File(filePath));
    }

    /**
     * 生成图片缩略图（临时文件输入）。
     */
    public void createImageThumbnail(File source, File target) {
        ProcessUtils.executeCommand(String.format(CMD_CREATE_IMAGE_THUMBNAIL,
                source.getAbsolutePath(), target.getAbsolutePath()));
    }

    /**
     * 获取视频编码。
     */
    public String getVideoCodec(String filePath) {
        String output = ProcessUtils.executeCommandWithResult(String.format(CMD_GET_VIDEO_CODEC, filePath));
        return StringTools.isEmpty(output) ? null : output.trim().toLowerCase();
    }

    private void convertVideoToMp4(String sourcePath, String targetPath) {
        ProcessUtils.executeCommand(String.format(CMD_CONVERT_TO_MP4, sourcePath, targetPath));
    }

    /**
     * 从视频中提取封面图（第一秒的帧）。
     */
    public void createVideoCover(String videoPath, String coverPath) {
        ProcessUtils.executeCommand(String.format(CMD_EXTRACT_VIDEO_COVER, videoPath, coverPath));
    }

    /**
     * 从视频中提取封面图（临时文件输入）。
     */
    public void createVideoCover(File videoFile, String coverPath) {
        createVideoCover(videoFile.getAbsolutePath(), coverPath);
    }

    /**
     * 根据文件类型获取标准后缀。
     */
    public String getStandardSuffix(FileTypeEnum fileTypeEnum) {
        return fileTypeEnum == null ? null : fileTypeEnum.getSuffix();
    }

    private void deleteQuietly(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}

