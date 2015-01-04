package uk.co.traintrackapp.traintrack.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import uk.co.traintrackapp.traintrack.utils.Utils;

@ParseClassName("Operator")
public class Operator extends ParseObject {

    public Operator() {
    }

    /**
     * @return the code
     */
    public String getCode() {
        return getString("code");
    }

    /**
     * @return the name
     */
    public String getName() {
        return getString("name");
    }

    /**
     * @return the delayRepayUrl
     */
    public String getDelayRepayUrl() {
        return getString("delay_repay_url");
    }


    /**
     * @return the twitter handle
     */
    public String getTwitter() {
        return getString("twitter");
    }


    /**
     * @return the name
     */
    @Override
    public String toString() {
        return this.getName();
    }

}