package tv.dfyc.yckt.beans;

/**
 * Created by android on 2017/11/23.
 */

public class ThemeListItem {
    private int subject_id;
    private String title;
    private String thumb_app_h;
    private int library_id;

    @Override
    public String toString() {
        return "ThemeListItem{" +
                "subject_id=" + subject_id +
                ", title='" + title + '\'' +
                ", thumb_app_h='" + thumb_app_h + '\'' +
                ", library_id=" + library_id +
                '}';
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb_app_h() {
        return thumb_app_h;
    }

    public void setThumb_app_h(String thumb_app_h) {
        this.thumb_app_h = thumb_app_h;
    }

    public int getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(int library_id) {
        this.library_id = library_id;
    }
}
