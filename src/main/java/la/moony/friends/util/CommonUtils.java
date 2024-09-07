package la.moony.friends.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

public class CommonUtils {

    public static String parseAndTruncateHtml2Text(String html, int length) {
        if (StringUtils.isBlank(html)) {
            return "";
        }

        String text = Jsoup.parse(html).text();
        if (text.length() <= length) {
            return text;
        } else {
            text = text.substring(0, length);
            text += "...";
        }
        return text;
    }


}
