package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

public class Journey {

	private ArrayList<JourneyLeg> journeyLegs;

	public Journey() {
		this.journeyLegs = new ArrayList<JourneyLeg>();
	}

	/**
	 * @return the journeyLegs
	 */
	public ArrayList<JourneyLeg> getJourneyLegs() {
		return journeyLegs;
	}

	/**
	 * @param journeyLeg
	 *            the journeyLeg to add
	 */
	public void addJourneyLeg(JourneyLeg journeyLeg) {
		this.journeyLegs.add(journeyLeg);
	}

	/**
	 * @return the origin
	 */
	public TrainStation getOrigin() {
		if (this.getJourneyLegs().isEmpty()) {
			return null;
		}

		return this.getJourneyLegs().get(0).getOrigin();
	}


	/**
	 * @return the destination
	 */
	public TrainStation getDestination() {
		if (this.getJourneyLegs().isEmpty()) {
			return null;
		}

		int last = this.getJourneyLegs().size();
		return this.getJourneyLegs().get(last - 1).getDestination();
	}


	/**
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getOrigin().toString() + " to "
				+ this.getDestination().toString();
	}

}
