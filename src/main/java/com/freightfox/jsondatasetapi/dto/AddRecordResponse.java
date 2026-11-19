
package com.freightfox.jsondatasetapi.dto;

public class AddRecordResponse {

    private String message;
    private String dataset;
    private Long recordId;

    public AddRecordResponse() {
    }

    public AddRecordResponse(String message, String dataset, Long recordId) {
        this.message = message;
        this.dataset = dataset;
        this.recordId = recordId;
    }

    public String getMessage() {
        return message;
    }

    public String getDataset() {
        return dataset;
    }

    public Long getRecordId() {
        return recordId;
    }
}
