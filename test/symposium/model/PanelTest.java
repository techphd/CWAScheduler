package symposium.model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PanelTest {

	@Test
	public void testConstructor() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		constraints.add("New-Panelist:2");
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

		assertTrue(panel.NAME.equals(panelName));
		assertTrue(panel.PANELISTS.get(0).equals("Joey"));
		assertTrue(panel.AVAILABILITY.equals(range));
		assertTrue(panel.CATEGORIES.get(0).equals("newCategory"));

		boolean newConstraintFound = false;
		for (Constraint c : panel.CONSTRAINTS) {
			if (c instanceof NewPanelistConstraint) {
				newConstraintFound = true;
				break;
			}
		}
		assertTrue(newConstraintFound);
	}

	@Test
	public void testSetDifficulty() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

		panel.setDifficulty(2);

		assertTrue(panel.getDifficulty() == 2);

	}

	@Test
	public void testGetDifficulty() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

		panel.setDifficulty(2);

		assertTrue(panel.getDifficulty() == 2);

	}

	@Test
	public void testSetVenueTime() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

		List<TimeRange> ranges = new ArrayList<>(3);
		ranges.add(new TimeRange(10, 20));
		ranges.add(new TimeRange(30, 40));
		ranges.add(new TimeRange(50, 60));

		Venue venue = new Venue("NewVenue", 5, 2, ranges);

		VenueTime vt = new VenueTime(new TimeRange(10, 20), venue);

		panel.setVenueTime(vt);

		assertTrue(panel.getVenueTime().equals(vt));

	}

	@Test
	public void testGetVenueTime() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

		List<TimeRange> ranges = new ArrayList<>(3);
		ranges.add(new TimeRange(10, 20));
		ranges.add(new TimeRange(30, 40));
		ranges.add(new TimeRange(50, 60));

		Venue venue = new Venue("NewVenue", 5, 2, ranges);

		VenueTime vt = new VenueTime(new TimeRange(10, 20), venue);

		panel.setVenueTime(vt);

		assertTrue(panel.getVenueTime().equals(vt));

	}

	@Test
	public void testGetName() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

		assertTrue(panel.getName().equals(panelName));

	}

	@Test
	public void testGetAvailability() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

		assertTrue(panel.getAvailability().equals(range));

	}

	@Test
	public void testAddMessage() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

		panel.addMessage("messageTest1");
		panel.addMessage("messageTest2");
		List<String> panelMessages = panel.getMessages();
		assertTrue(panelMessages.get(0).equals("messageTest1"));
		assertTrue(panelMessages.get(1).equals("messageTest2"));
	}

	@Test
	public void testCompareTo() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel1 = new Panel(panelName, panelists, range, newCategory, constraints);

		panel1.setDifficulty(1);

		Panel panel2 = new Panel(panelName, panelists, range, newCategory, constraints);

		panel2.setDifficulty(2);

		Panel panel3 = new Panel(panelName, panelists, range, newCategory, constraints);

		panel3.setDifficulty(2);

		assertTrue(panel1.compareTo(panel2) < 0);
		assertTrue(panel2.compareTo(panel1) > 0);
		assertTrue(panel2.compareTo(panel3) == 0);
	}

	@Test
	public void testToString() throws Exception {

		String panelName = "NewPanel";
		List<String> newCategory = new ArrayList<String>();
		newCategory.add("newCategory");

		List<String> constraints = new ArrayList<String>();
		List<String> panelists = new ArrayList<String>();
		panelists.add("Joey");
		panelists.add("Yousef");

		Range range = new TimeRange(10, 20);

		Panel panel1 = new Panel(panelName, panelists, range, newCategory, constraints);

		Panel panel2 = new Panel(panelName, panelists, range, newCategory, constraints);
		panel2.addMessage("messageTest1");
		panel2.addMessage("messageTest2");

		Panel panel3 = new Panel(panelName, panelists, range, newCategory, constraints);
		Range time = new TimeRange(0, 100);
		List<TimeRange> ranges = new ArrayList<>(4);
		ranges.add((TimeRange) time);
		VenueTime vt = new VenueTime(time, new Venue("newVenue", 5, 1, ranges));
		panel3.setVenueTime(vt);

		assertTrue(panel1.toString().equals(panelName + "\n\tUnscheduled\n\tMessages: No Messages"));
		assertTrue(panel2.toString().equals(panelName + "\n\tUnscheduled\n\tMessages:\n\t\tmessageTest1\n" +
				"\t\tmessageTest2"));
		panel2.setVenueTime(vt);
		assertTrue(panel2.toString().equals(panelName + "\n\t" + vt + "\n\tMessages:\n\t\tmessageTest1\n" +
				"\t\tmessageTest2"));
		assertTrue(panel3.toString().equals(panelName + "\n\t" + vt + "\n\tMessages: No Messages"));

	}
}