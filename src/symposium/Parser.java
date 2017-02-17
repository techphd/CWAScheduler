package symposium;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import symposium.model.Panel;
import symposium.model.Range;
import symposium.model.ScheduleData;
import symposium.model.TimeFormat;
import symposium.model.TimeRange;
import symposium.model.Venue;

/**
 * Parser takes in a JSON file and extracts information from it in order to
 * build the data structure used for the algorithm
 */
public class Parser {

	/**
	 * This is the main method to call to parse the input file. Take in the JSON
	 * and create the object of parser, such as initVenues and initPanels The
	 * 'try' and 'catch' are in case of exception or errors in the input file
	 * 
	 * @param inputFile
	 *          the JSON file
	 */
	public static void parse(String inputFile) {
		JSONParser parser = new JSONParser();
		JSONObject root = null;
		try {
			root = (JSONObject) parser.parse(new FileReader(inputFile));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		initVenues(root);
		initPanels(root);
	}

	/**
	 * Looks for the venue and venue-time sections of the json file First, we loop
	 * throw venues to get their names, size and priority values Then, we extract
	 * time information from the venue-time section and convert it into our
	 * timerange format We map each venue time to its venue, create a collection
	 * of venue objects and send that information to ScheduleData This method also
	 * determines how many days the event will last
	 * 
	 * @param jsonObject
	 *          is the json file
	 */
	private static void initVenues(JSONObject jsonObject) {
		List<Venue> venues = new ArrayList<Venue>();

		JSONArray json_venues = (JSONArray) jsonObject.get("Venues");
		JSONArray json_venue_times = (JSONArray) jsonObject.get("Venue-Times");

		Map<String, Integer> sizes = new HashMap<String, Integer>();
		Map<String, Integer> priorities = new HashMap<>();
		for (Object o : json_venues) {
			JSONObject item = (JSONObject) o;
			String venue_name = (String) item.get("name");
			String venue_size = item.get("size").toString();
			String venue_priority = item.get("priority").toString();
			sizes.put(venue_name, Integer.valueOf(venue_size));
			priorities.put(venue_name, Integer.valueOf(venue_priority));
		}

		int lastTimePoint = -1; // the last time anything could be scheduled (for number of days);
		Map<String, List<TimeRange>> ranges = new HashMap<String, List<TimeRange>>();
		for (Object o : json_venue_times) {
			JSONObject item = (JSONObject) o;
			String venue_name = (String) item.get("name");
			String venue_time = (String) item.get("time");

			TimeRange timeRange = (TimeRange) TimeFormat.normalToAbsolute(venue_time);
			List<TimeRange> timeRanges; //list of all timeRanges for this venue
			if (ranges.containsKey(venue_name)) {
				timeRanges = ranges.get(venue_name);
			} else {
				timeRanges = new ArrayList<TimeRange>();
			}
			timeRanges.add(timeRange);
			ranges.put(venue_name, timeRanges);
			// update last time point
			lastTimePoint = (lastTimePoint > timeRange.END ? lastTimePoint : timeRange.END);
		}
		for (String key : ranges.keySet()) {
			int venue_size = sizes.get(key);
			int venue_priority = priorities.get(key);
			List<TimeRange> timeRanges = ranges.get(key);
			venues.add(new Venue(key, venue_size, venue_priority, timeRanges));
		}
		Collections.sort(venues);
		ScheduleData.init(venues, TimeFormat.getNumberOfDay(lastTimePoint) + 1); // +1 because days begin with 0;
	}

	/**
	 * This method loops over the panelists section of the json file and gathers
	 * the names and available times of each panelist, as well as, checks to see
	 * if they're new or not.
	 *
	 * Next it loops over the panels section, getting all the necessary
	 * information for each panel. For each panel, it finds the panelists from the
	 * above step that are needed and calculates the intersection of their
	 * available times. A panel object is then created using the name, list of
	 * panelists, and category from the json file, along with the available time
	 * as mentioned above, and a list of string names for all constraints that the
	 * json file assigns to the panel.
	 *
	 * After doing this for all panels, it gives ScheduleData a list of all Panel
	 * objects
	 * 
	 * @param jsonObject
	 *          is the json file
	 */
	private static void initPanels(JSONObject jsonObject) {
		List<symposium.model.Panel> panels = new ArrayList<Panel>();

		JSONArray json_panelists = (JSONArray) jsonObject.get("Panelists");
		JSONArray json_panels = (JSONArray) jsonObject.get("Panels");

		// Setting up panelists
		Map<String, Range> panelists = new HashMap<String, Range>();
		List<String> new_panelists = new ArrayList<String>();
		for (Object o : json_panelists) {
			JSONObject item = (JSONObject) o;
			String panelist_name = (String) item.get("name");
			JSONArray json_times = (JSONArray) item.get("times");
			List<Range> panelist_times = new ArrayList<Range>();
			for (Object time_slot : json_times) {
				String panelist_time = (String) time_slot;
				TimeRange timeRange = (TimeRange) TimeFormat.normalToAbsolute(panelist_time);
				panelist_times.add(timeRange); //panelist_time
			}
			Range panelistTime = panelist_times.get(0).union(panelist_times);

			if (item.get("new").equals("yes")) {
				new_panelists.add(panelist_name);
			}
			panelists.put(panelist_name, panelistTime);
		}

		// Creating Panels
		for (Object o : json_panels) {
			JSONObject item = (JSONObject) o;
			String panel_name = (String) item.get("name");
			JSONArray panel_panelists = (JSONArray) item.get("panelists");
			if (panel_panelists.isEmpty()) { // handle empty panels
				ScheduleData.instance().addWarningMessage("Panel: " + panel_name + " Does not have any Panelists");
				continue;
			} // TODO: Better implementation needed for catching panels without panelists
			JSONArray json_constraints = (JSONArray) item.get("constraints");
			String categories = (String) item.get("category");
			int new_count = 0;

			List<String> names = new ArrayList<>();
			List<Range> panelistAvailabilities = new ArrayList<>();

			for (Object panelist : panel_panelists) {
				String name = (String) panelist;
				names.add(name);
				panelistAvailabilities.add(panelists.get(name));
				if (new_panelists.contains(name)) {
					new_count += 1;
				}
			}

			Range panelAvailability = null;
			if (panelistAvailabilities.size() > 0) {
				panelAvailability = panelistAvailabilities.get(0).intersect(panelistAvailabilities);
			}
			if (panelAvailability == null) { // handle panels with no intersections
				ScheduleData.instance()
						.addWarningMessage("Panel: " + panel_name + " does not have availability based on panelist overlap.");
				continue;
			}

			List<String> categoryList = new ArrayList<String>();
			for (String category : categories.split(",")) {
				if (!category.trim().isEmpty()) { // handle empty category
					categoryList.add(category);
				}
			}
			List<String> constraints = new ArrayList<String>();
			String new_panelists_string = null;
			for (Object k : json_constraints) {
				String constraint = (String) k;
				if (constraint.contains("New-Panelist")) {
					new_panelists_string = constraint;
					continue;
				}
				constraints.add(constraint);
			}
			if (new_panelists_string != null && new_count == names.size()) {
				constraints.add(new_panelists_string);
			}
			panels.add(new Panel(panel_name, names, panelAvailability, categoryList, constraints));

		}
		ScheduleData.instance().initPanels(panels);
	}
}