
package com.freightfox.jsondatasetapi.dto;

import java.util.List;
import java.util.Map;

import tools.jackson.databind.JsonNode;

public class GroupRecordsResponse {

    private Map<String, List<JsonNode>> groupedRecords;

    public GroupRecordsResponse(
            Map<String, List<JsonNode>> groupedRecords) {
        this.groupedRecords = groupedRecords;
    }

    public Map<String, List<JsonNode>> getGroupedRecords() {
        return groupedRecords;
    }

    public void setGroupedRecords(
            Map<String, List<JsonNode>> groupedRecords) {
        this.groupedRecords = groupedRecords;
    }
}
