
package com.freightfox.jsondatasetapi.dto;

import java.util.List;

import tools.jackson.databind.JsonNode;

public class SortRecordsResponse {

    private List<JsonNode> sortedRecords;

    public SortRecordsResponse(List<JsonNode> sortedRecords) {
        this.sortedRecords = sortedRecords;
    }

    public List<JsonNode> getSortedRecords() {
        return sortedRecords;
    }

    public void setSortedRecords(List<JsonNode> sortedRecords) {
        this.sortedRecords = sortedRecords;
    }
}
