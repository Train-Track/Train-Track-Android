package uk.co.traintrackapp.traintrack.model;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class Operator {

    private int id;
    private String name;
    private String code;
    private String twitter;
    private String delayRepayUrl;
    private String numericCode;

    public Operator() {
    }

    public Operator(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.name = json.getString("name");
            this.code = json.getString("code");
            this.twitter = json.getString("twitter");
            this.delayRepayUrl = json.getString("delay_repay_url");
            this.numericCode = json.getString("numeric_code");
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the twitter handle
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * @return the delayRepayUrl
     */
    public String getDelayRepayUrl() {
        return delayRepayUrl;
    }

    /**
     * @return the nmeric code
     */
    public String getNumericCode() {
        return numericCode;
    }

    /**
     * @return the name
     */
    @Override
    public String toString() {
        return getName();
    }

}
