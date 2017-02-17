package symposium.model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ConstraintFactoryTest {
	@Test
	public void testBuildConstraints() {
		List<String> sl = new ArrayList<String>();
		sl.add("Paired-Panelists:3");
		sl.add("Single-Category:2");
		sl.add("Max-Panels(3):1");
		sl.add("Min-Panels(1):1");
		//sl.add("Venue(Macky Auditorium [1]):1");
		sl.add("Time(4;14:30):1");
		sl.add("Minimum-Capacity(2):2");
		sl.add("Availability:1");
		sl.add("New-Panelist:2");

		String name1 = "new";
		List<String> panelists1 = new ArrayList<>(3);
		panelists1.add("Trey Lyons");
		panelists1.add("Jurek Martin");
		panelists1.add("Patrick Boel");
		List<TimeRange> avaliability1 = new ArrayList<>(5);
		avaliability1.add(new TimeRange(360, 1080));
		avaliability1.add(new TimeRange(1800, 2520));
		avaliability1.add(new TimeRange(3240, 3960));
		avaliability1.add(new TimeRange(4680, 5400));
		avaliability1.add(new TimeRange(6120, 6840));
		TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);

		List<String> category1 = new ArrayList<>(1);
		category1.add("IA");

		Panel p1 = new Panel(name1, panelists1, range1, category1, new ArrayList<String>());
		List<Constraint> cl = new ArrayList<Constraint>();
		cl = ConstraintFactory.buildConstraints(p1, sl);

		assertTrue(p1.CONSTRAINTS.size() == 4);
		assertTrue(cl.size() == 12);
	}
}
