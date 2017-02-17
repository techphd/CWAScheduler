package symposium.model;

/**
 * The class TimeConstraint is an abstract which inherits from Constraints, @see
 * Constraint for documentation. The class is the parent class, which represents
 * any constraints that tests the panel or panelist on a specific time. For
 * example, the New Panelist Constraint is a time constraint, such as we test
 * whether the panelist on the first day is new or not. <I>Testing the first day
 * means we are testing a specific time.</I> Constraint is violated when the
 * checked time doesn't satisfy the wanted time
 */
abstract class TimeConstraint extends Constraint {

	/**
	 * Constructs for the TimeConstraint class.
	 *
	 * @param priority
	 *          enum which determines if a constraint is REQUIRED, VERY_IMPORTANT,
	 *          or DESIRED.
	 * @param panel
	 *          The Panel that the constraint is part of.
	 */
	public TimeConstraint(ConstraintPriority priority, Panel panel) {
		super(priority, panel);
	}

	/**
	 * <b>Dependencies:</b> checkTime method
	 *
	 * The method tests whether checkTime is violated or not. The method has a
	 * reverse logic to it, meaning when the checked time is true, the constraint
	 * is not violated. Reason for that is the checkTime method returns false when
	 * the constraint is violated
	 *
	 * @param venueTime
	 * @return true if checkTime is false, returns false if checkTime is true.
	 */
	@Override
	public boolean isConstraintViolated(VenueTime venueTime) {
		boolean violated;
		if (checkTime(venueTime)) {
			violated = false;
		} else {
			violated = true;
		}
		cache.put(venueTime, violated);
		return violated;
	}

	/**
	 * Method to be implemented in the child classes. The method tests if the
	 * venueTime violates the constraints; it depends on the constraints.
	 * 
	 * @param venueTime
	 *          The venueTime being checked
	 * @return bool true if the constraint is not violated false if violated
	 */
	abstract boolean checkTime(VenueTime venueTime);
}