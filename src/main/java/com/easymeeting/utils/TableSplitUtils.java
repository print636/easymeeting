package com.easymeeting.utils;

public class TableSplitUtils {
    private static final String SPLIT_TABLE_MEETING_CHAT_MESSAGE = "meeting_chat_message";

    private static final Integer SPLIT_TABLE_COUNT = 32;

    private static String getTableName(String prefix, Integer tableCount, String key){
        int hashCode = Math.abs(key.hashCode());
        int tableNum = hashCode % tableCount + 1;
        int tableNumLength = String.valueOf(tableCount).length();
        return prefix + "_" + String.format("%0" + tableNumLength + "d", tableNum);
    }

    public static String getMeetingChatMessageTable(String meetingId){
        return getTableName(SPLIT_TABLE_MEETING_CHAT_MESSAGE, SPLIT_TABLE_COUNT, meetingId);
    }
}
