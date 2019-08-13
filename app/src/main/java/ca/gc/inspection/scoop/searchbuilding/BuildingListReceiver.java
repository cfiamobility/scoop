package ca.gc.inspection.scoop.searchbuilding;

import org.json.JSONArray;

public interface BuildingListReceiver {

    void setBuildingsData(JSONArray response);
}
