package dyl.anjon.es.traintrack.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import dyl.anjon.es.traintrack.utils.Utils;

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
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * @param context
	 * @param id
	 * @return the operator selected
	 */
	public static Operator getByCode(String code) {
		ParseQuery<Operator> query = ParseQuery.getQuery(Operator.class);
		query.fromLocalDatastore();
		query.whereEqualTo("code", code);
		Operator operator = null;
		try {
			operator = query.getFirst();
		} catch (ParseException e) {
			Utils.log(e.getMessage());
		}
		return operator;
	}

}
