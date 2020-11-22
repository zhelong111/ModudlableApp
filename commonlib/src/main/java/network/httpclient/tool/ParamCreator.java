package network.httpclient.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class ParamCreator {

	private List<BasicNameValuePair> params;

    public ParamCreator() {
        params = new ArrayList<BasicNameValuePair>();
    }

    public ParamCreator add(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
        return this;
    }

    public List<BasicNameValuePair> build() {
        return params;
    }

    public static void addExtraParam(JSONObject dstParam, Map<String, Object> extraParam) {
        if (extraParam != null && extraParam.size() > 0) {
            try {
                Set<String> keys = extraParam.keySet();
                for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
                    String key = iterator.next();
                    Object value = extraParam.get(key);
                    dstParam.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
