package tv.dfyc.yckt.util;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class LessonDetail {
    private String mLessonImageUrl;
    private String mLessonName;
    private String mLessonTimes;
    private String mLessonVideoUrl;
    private int mLessonImageID;
    private int libraryID;
    private int isCharge;
    private int HistoryId;
    private int VideoId;
    private int Last_play_time;
    private String VideoDurtion;
    private int Type;
    private int video_is_free;

    public int getVideo_is_free() {
        return video_is_free;
    }

    public void setVideo_is_free(int video_is_free) {
        this.video_is_free = video_is_free;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getHistoryId() {
        return HistoryId;
    }

    public void setHistoryId(int historyId) {
        HistoryId = historyId;
    }

    public int getVideoId() {
        return VideoId;
    }

    public void setVideoId(int videoId) {
        VideoId = videoId;
    }

    public int getLast_play_time() {
        return Last_play_time;
    }

    public void setLast_play_time(int last_play_time) {
        Last_play_time = last_play_time;
    }

    public String getVideoDurtion() {
        return VideoDurtion;
    }

    public void setVideoDurtion(String videoDurtion) {
        VideoDurtion = videoDurtion;
    }

    public int getLibraryID() {
        return libraryID;
    }

    public void setLibraryID(int libraryID) {
        this.libraryID = libraryID;
    }


    public LessonDetail() {
    }

    public String getmLessonImageUrl() {
        return mLessonImageUrl;
    }

    public void setmLessonImageUrl(String mLessonImageUrl) {
        this.mLessonImageUrl = mLessonImageUrl;
    }

    public String getmLessonName() {
        return mLessonName;
    }

    public void setmLessonName(String mLessonName) {
        this.mLessonName = mLessonName;
    }

    public String getmLessonTimes() {
        return mLessonTimes;
    }

    public void setmLessonTimes(String mLessonTimes) {
        this.mLessonTimes = mLessonTimes;
    }


    public String getmLessonVideoUrl() {
        return mLessonVideoUrl;
    }

    public void setmLessonVideoUrl(String mLessonVideoUrl) {
        this.mLessonVideoUrl = mLessonVideoUrl;
    }

    public int getmLessonImageID() {
        return mLessonImageID;
    }

    public void setmLessonImageID(int mLessonImageID) {
        this.mLessonImageID = mLessonImageID;
    }

    public int getIsCharge() {
        return isCharge;
    }
    public void setIsCharge(int isCharge) {
        this.isCharge = isCharge;
    }
}
