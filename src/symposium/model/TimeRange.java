package symposium.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * A single range of absolute time.
 *
 * This class documentation is well and better explained
 * {@link symposium.model.Range Range}
 *
 * The methods don't have a documentation since they will be reiterating the
 * same thing as {@link symposium.model.Range Range}
 *
 */
public class TimeRange implements Range {
	public final int	START;
	public final int	END;

	public TimeRange(int start, int end) {
		if (end < start) {
			throw new IllegalArgumentException("Expecting TimeRange(start, end), start <= end");
		}

		if (start < 0 || end < 0) {
			throw new IllegalArgumentException("Expecting start & end both >= 0");
		}
		this.START = start;
		this.END = end;
	}

	public int compareTo(Range other) {
		if (other == null) {
			throw new NullPointerException("Cannot compare to null");
		}
		int compareStart = START - other.getStart();
		return compareStart == 0 ? END - other.getEnd() : compareStart;
	}

	public boolean doesIntersect(Range other) {
		return !(this.intersect(other) == null);
	}

	public boolean isInside(int point) {
		return point >= START && point <= END;
	}

	public Range intersect(Range other) {
		if (other == null) {
			return null;
		}

		if (other instanceof TimeRange) {
			// if they intersect, the intersection will be bounded by the greater start and  the smaller end.
			// observe
			//         [ ]          |   [     ]      |    [               ]
			//             [    ]   |      [     ]   |        [       ]
			//  if they don't intersect , end is < than start
			int greaterStart = this.START > other.getStart() ? this.START : other.getStart();
			int smallerEnd = this.END < other.getEnd() ? this.END : other.getEnd();

			if (greaterStart <= smallerEnd) {
				return new TimeRange(greaterStart, smallerEnd);
			} else {
				return null;
			}

		} else {
			return other.intersect(this);
			// TimeRangesSeries should know how to intersect with timeRange.
		}
	}

	@Override
	public Range intersect(Collection<Range> others) {
		Range result = this;
		for (Range r : others) {
			result = r.intersect(result);
		}
		return result;
	}

	public Range union(Collection<Range> others) {
		if (others == null || others.isEmpty()) {
			return this;
		}

		others = new TreeSet<>(others);
		others.add(this);

		// if there is TimeRangeSeries object in others, it should do the union.
		for (Range range : others) {
			if (range instanceof TimeRangeSeries) {
				return range.union(others);
			}
		}

		// all ranges in others are TimeRanges
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Collection<TimeRange> simplified = TimeRangeSeries.simplify((Collection) others);
		//for(TimeRange r : simplified) { 
		//}
		if (simplified.size() == 1) {
			return simplified.iterator().next();
		} else {
			return new TimeRangeSeries(simplified);
		}
	}

	@Override
	public Range union(Range other) {
		return this.union(Arrays.asList(other));
	}

	public int getStart() {
		return START;
	}

	public int getEnd() {
		return END;
	}

	public Iterator<TimeRange> iterator() {
		TimeRange this__ = this;
		/**
		 * make an iterator instance with only one element to return
		 */
		return new Iterator<TimeRange>() {
			private boolean isNext = true;

			@Override
			public boolean hasNext() {
				return isNext;
			}

			@Override
			public TimeRange next() {
				if (isNext) {
					isNext = false;
					return this__;
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public String toString() {
		return "[" + START + ", " + END + "]";
	}

	public boolean equals(Range other) {
		if (!(other instanceof TimeRange)) {
			return false;
		}
		return equals((TimeRange) other);
	}

	public boolean equals(TimeRange other) {
		if (other.START == this.START && other.END == this.END) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		return this.START +
				31 * (this.END);
	}

	public int length() {
		return END - START + 1; // + 1 because range is inclusive.
	}

	public boolean doesEnclose(Range range) {
		if (this.START <= range.getStart() && this.END >= range.getEnd()) {
			return true;
		}
		return false;
	}
}
