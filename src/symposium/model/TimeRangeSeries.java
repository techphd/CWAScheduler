package symposium.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Series of range of absolute time.
 *
 * This class documentation is well and better explained in
 * {@link symposium.model.Range Range}
 *
 * The methods don't have a documentation since they will be reiterating the
 * same thing as {@link symposium.model.Range Range}
 */

public class TimeRangeSeries implements Range {
	private final SortedSet<TimeRange> ranges;

	/**
	 * The ranges should be checked using isSimple and call simplify if necessary.
	 * Also ranges.size() should be greater than 1 before calling constructor,
	 * because if there is only 1 TimeRange, there is no need for TimeRangeSeries.
	 *
	 * @param ranges
	 *          to be used and updated.
	 */
	public TimeRangeSeries(Collection<TimeRange> ranges) {
		if (ranges == null) {
			throw new NullPointerException("ranges should be a list object");
		} else if (ranges.size() < 2) {
			throw new IllegalArgumentException("cannot make TimeRangeSeries from zero or one range");
		} else if (!TimeRangeSeries.isSimple(ranges)) {
			throw new IllegalArgumentException("ranges are not simple");
		}
		// create new collection because ranges of one timeRangeSeries shouldn't be possible to change from outside.
		this.ranges = new TreeSet<>(ranges);
	}

	public int compareTo(Range other) {
		if (other == null) {
			throw new NullPointerException("Cannot compare to null");
		}
		int compareStart = this.getStart() - other.getStart();
		return compareStart == 0 ? this.getEnd() - other.getEnd() : compareStart;
	}

	public boolean doesIntersect(Range other) {
		if (other == null) {
			return false;
		}
		if ((this.getEnd() < other.getStart()) || (other.getEnd() < this.getStart())) {
			return false;
		}

		Iterator<TimeRange> myRangesItr = this.iterator();
		Iterator<TimeRange> otherRangesItr = other.iterator();

		TimeRange myEarliest = myRangesItr.next();
		TimeRange otherEarliest = otherRangesItr.next();

		while (true) {
			// we are guaranteed a TimeRange object return value because intersecting two TimeRange's will make a TimeRange
			if (myEarliest.doesIntersect(otherEarliest)) {
				return true;
			}

			// skip the range with the smaller end. because if one of the two current ranges will intersect something later,
			// it will the one with the greater end.
			// Observe :
			//   [  ]    [     ]
			//        [    ]
			//  So, we increment the range with smaller end.
			//  if no next ranges either "this" or "other", there is no intersection

			if (myEarliest.END < otherEarliest.END) {
				if (!myRangesItr.hasNext()) {
					// no more ranges to check intersection
					return false;
				}
				myEarliest = myRangesItr.next();
			} else {
				if (!otherRangesItr.hasNext()) {
					// no more ranges to check intersection
					return false;
				}
				otherEarliest = otherRangesItr.next();
			}
		}
	}

	public Range intersect(Range other) {
		if (other == null) {
			return null;
		}

		Iterator<TimeRange> myRangesItr = this.iterator();
		Iterator<TimeRange> otherRangesItr = other.iterator();

		TimeRange myEarliest = myRangesItr.next();
		TimeRange otherEarliest = otherRangesItr.next();

		List<TimeRange> result = new ArrayList<>();
		while (true) {
			// we are guaranteed a TimeRange object return value because intersecting two TimeRange's will make a TimeRange
			TimeRange intersection = (TimeRange) myEarliest.intersect(otherEarliest);
			if (intersection != null) {
				result.add(intersection);
			}

			// skip the range with the smaller end. because if one of the two current ranges will have another intersection
			//  with different ranges later. it will the one with the greater end.
			// Observe :
			//   [           ]           |       [         ]
			// [    ]  [  ]    [   ]     |     [    ]  [       ]
			//  So, we increment the range with smaller end.
			//  if no next ranges in one of them, the intersection is ready!.

			if (myEarliest.END < otherEarliest.END) {
				if (!myRangesItr.hasNext()) {
					// no more ranges to intersect
					break;
				}
				myEarliest = myRangesItr.next();
			} else {
				if (!otherRangesItr.hasNext()) {
					// no more ranges to intersect
					break;
				}
				otherEarliest = otherRangesItr.next();
			}
		}
		if (result.isEmpty()) {
			return null;
		} else if (result.size() == 1) {
			return result.get(0);
		} else {
			return new TimeRangeSeries(result);
		}
	}

	public int length() {
		int result = 0;
		for (TimeRange tr : this.ranges) {
			result += tr.length();
		}
		return result;
	}

	@Override
	public Range intersect(Collection<Range> others) {
		Range result = this;
		for (Range r : others) {
			result = r.intersect(result);
		}
		return result;
	}

	public Iterator<TimeRange> iterator() {
		return this.ranges.iterator();
	}

	public Range union(Range other) {
		return this.union(Arrays.asList(other));
	}

	@Override
	public Range union(Collection<Range> others) {
		if (others == null || others.isEmpty()) {
			return this;
		}

		others = new TreeSet<>(others);
		others.add(this);

		SortedSet<Range> timeRanges = new TreeSet<>();
		for (Range range : others) {
			Iterator<TimeRange> itr = range.iterator();
			while (itr.hasNext()) {
				timeRanges.add(itr.next());
			}
		}

		return timeRanges.first().union(timeRanges);
	}

	@Override
	public int getStart() {
		return ranges.first().START;
	}

	@Override
	public int getEnd() {
		return ranges.last().END;
	}

	public boolean isInside(int point) {
		for (TimeRange r : ranges) {
			if (r.isInside(point))
				return true;
		}
		return false;
	}

	/**
	 * simplify a list of TimeRange objects. simplifying means replace
	 * intersectiong timeranges with the union of them.
	 *
	 * @param timeRanges
	 *          to be used and updated.
	 * @return a list of disjoint timeRanges that is equivalent to the passed
	 *         timeRanges
	 */
	public static SortedSet<TimeRange> simplify(Collection<TimeRange> timeRanges) {

		SortedSet<TimeRange> preSimplified = new TreeSet<>(timeRanges);
		SortedSet<TimeRange> simplified = new TreeSet<>();

		int tmpStart = -1;
		int tmpEnd = -1;

		for (TimeRange tr : preSimplified) {

			if (tmpStart == -1) {
				tmpStart = tr.START;
				tmpEnd = tr.END;
				continue;
			}

			if (tr.START <= tmpEnd || tmpEnd + 1 == tr.START) {
				// new timeRange intersects with tmpRange
				tmpStart = tmpStart < tr.START ? tmpStart : tr.START;
				tmpEnd = tmpEnd > tr.END ? tmpEnd : tr.END;

			} else {
				// if it doesn't intersect, then add the tmpRange to result. and start new one.
				simplified.add(new TimeRange(tmpStart, tmpEnd));
				tmpStart = tr.START;
				tmpEnd = tr.END;
			}
		}
		// add last tmpRange
		simplified.add(new TimeRange(tmpStart, tmpEnd));
		return simplified;
	}

	/**
	 * isSimple check a list of TimeRange objects if simple. Simple Range means it
	 * is disjoint and cases like [0, 5][6, 10] do not exist. Because case like
	 * [0, 5][6, 10] should equal [0, 10]
	 *
	 * @param timeRanges
	 *          to be used and updated.
	 * @return true if conditions above are met, false otherwise
	 */
	public static boolean isSimple(Collection<TimeRange> timeRanges) {

		SortedSet<TimeRange> ordered = new TreeSet<>(timeRanges);

		Iterator<TimeRange> timeRangesIterator = ordered.iterator();
		TimeRange previousRange = null;
		TimeRange currentRange = null;
		while (timeRangesIterator.hasNext()) {
			currentRange = timeRangesIterator.next();
			if (previousRange == null) {
				previousRange = currentRange;
				continue;
			}

			if (previousRange.doesIntersect(currentRange)) {
				return false;
			}
			if (previousRange.getEnd() + 1 == currentRange.getStart()) {
				// For a case like [0, 5][6, 10] == [0, 10]
				return false;
			}
			previousRange = currentRange;
		}
		return true;
	}

	public boolean equals(Range other) {

		Iterator<TimeRange> otherItr = other.iterator();
		Iterator<TimeRange> thisItr = this.iterator();

		while (true) {
			if ((thisItr.hasNext() && !otherItr.hasNext()) || (!thisItr.hasNext() && otherItr.hasNext())) {
				return false;
			}

			if (!thisItr.hasNext()) {
				break;
			}

			if (!thisItr.next().equals(otherItr.next())) {
				return false;
			}
		}
		return true;
	}

	public boolean equals(TimeRangeSeries other) {
		return equals((Range) other);
	}

	public int hashCode() {
		int hash = 0;
		int factor = 1;
		for (TimeRange curRange : ranges) {
			hash += curRange.hashCode() * factor;
			factor *= 31;
		}
		return hash;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();

		for (TimeRange range : ranges) {
			result.append(", ").append(range.toString());
		}

		result.delete(0, 2);
		result.insert(0, "{");
		result.append("}");

		return result.toString();
	}

	public boolean doesEnclose(Range range) {
		Iterator<TimeRange> myItr = this.iterator();
		Iterator<TimeRange> otherItr = range.iterator();
		TimeRange myRange = myItr.next();
		TimeRange otherRange = otherItr.next();

		while (true) {
			// does enclose
			if (myRange.doesEnclose(otherRange)) {
				if (otherItr.hasNext()) { // more ranges to check
					otherRange = otherItr.next();
				} else { // no more other ranges to check
					return true;
				}
			} else {
				if (myItr.hasNext()) {
					myRange = myItr.next();
				} else {
					return false; // does not enclose : because no more myRange and there is otherRange which is not enclosed
				}
			}
		}
	}
}
