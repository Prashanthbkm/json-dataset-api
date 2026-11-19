
package com.freightfox.jsondatasetapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.JsonNode;

import com.freightfox.jsondatasetapi.dto.AddRecordResponse;
import com.freightfox.jsondatasetapi.dto.GroupRecordsResponse;
import com.freightfox.jsondatasetapi.dto.SortRecordsResponse;
import com.freightfox.jsondatasetapi.entity.DatasetRecord;
import com.freightfox.jsondatasetapi.service.DatasetRecordService;

@RestController
@RequestMapping("/api/dataset")
public class DatasetRecordController {

    private final DatasetRecordService service;

    public DatasetRecordController(DatasetRecordService service) {
        this.service = service;
    }

    // API 1: Add a JSON record
    @PostMapping("/{datasetName}/record")
    public ResponseEntity<AddRecordResponse> addRecord(
            @PathVariable String datasetName,
            @RequestBody JsonNode recordData) {

        DatasetRecord savedRecord =
                service.addRecord(datasetName, recordData);

        AddRecordResponse response = new AddRecordResponse(
                "Record added successfully",
                savedRecord.getDatasetName(),
                savedRecord.getId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // API 2: Group or sort dataset records
    @GetMapping("/{datasetName}/query")
    public ResponseEntity<?> queryRecords(
            @PathVariable String datasetName,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {

        boolean groupRequested =
                groupBy != null && !groupBy.isBlank();

        boolean sortRequested =
                sortBy != null && !sortBy.isBlank();

        if (groupRequested == sortRequested) {
            throw new IllegalArgumentException(
                    "Provide exactly one parameter: groupBy or sortBy");
        }

        if (groupRequested) {

            if (order != null && !order.isBlank()) {
                throw new IllegalArgumentException(
                        "The order parameter can only be used with sortBy");
            }

            Map<String, List<JsonNode>> result =
                    service.groupRecords(datasetName, groupBy);

            return ResponseEntity.ok(
                    new GroupRecordsResponse(result)
            );
        }

        List<JsonNode> result =
                service.sortRecords(
                        datasetName,
                        sortBy,
                        order == null || order.isBlank() ? "asc" : order
                );

        return ResponseEntity.ok(
                new SortRecordsResponse(result)
        );
    }
}
