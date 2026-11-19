
package com.freightfox.jsondatasetapi.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import com.freightfox.jsondatasetapi.entity.DatasetRecord;
import com.freightfox.jsondatasetapi.repository.DatasetRecordRepository;

@Service
public class DatasetRecordService {

    private final DatasetRecordRepository repository;
    private final ObjectMapper objectMapper;

    public DatasetRecordService(
            DatasetRecordRepository repository,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    // Add a JSON record to a dataset
    public DatasetRecord addRecord(
            String datasetName, JsonNode recordData) {

        if (datasetName == null || datasetName.isBlank()) {
            throw new IllegalArgumentException(
                    "Dataset name cannot be empty");
        }

        if (recordData == null || !recordData.isObject()) {
            throw new IllegalArgumentException(
                    "Record must be a valid JSON object");
        }

        DatasetRecord record = new DatasetRecord(
                datasetName,
                recordData.toString()
        );

        return repository.save(record);
    }

    // Group records dynamically by any JSON field
    public Map<String, List<JsonNode>> groupRecords(
            String datasetName, String field) {

        validateQuery(datasetName, field);

        List<DatasetRecord> records =
                repository.findByDatasetName(datasetName);

        Map<String, List<JsonNode>> groupedRecords =
                new LinkedHashMap<>();

        for (DatasetRecord record : records) {

            JsonNode json =
                    parseRecord(record.getRecordData());

            JsonNode value = json.get(field);

            String groupKey;

            if (value == null) {
                groupKey = "(missing)";
            } else if (value.isNull()) {
                groupKey = "null";
            } else if (value.isObject() || value.isArray()) {
                groupKey = value.toString();
            } else {
                groupKey = value.asText();
            }

            groupedRecords
                    .computeIfAbsent(groupKey, key -> new ArrayList<>())
                    .add(json);
        }

        return groupedRecords;
    }

    // Sort records dynamically by any JSON field
    public List<JsonNode> sortRecords(
            String datasetName, String field, String order) {

        validateQuery(datasetName, field);

        if (order == null ||
                (!order.equalsIgnoreCase("asc")
                && !order.equalsIgnoreCase("desc"))) {

            throw new IllegalArgumentException(
                    "Order must be either 'asc' or 'desc'");
        }

        List<DatasetRecord> records =
                repository.findByDatasetName(datasetName);

        List<JsonNode> jsonRecords = new ArrayList<>();

        for (DatasetRecord record : records) {
            jsonRecords.add(
                    parseRecord(record.getRecordData()));
        }

        int direction =
                order.equalsIgnoreCase("asc") ? 1 : -1;

        jsonRecords.sort((first, second) -> {

            JsonNode firstValue = first.get(field);
            JsonNode secondValue = second.get(field);

            boolean firstMissing =
                    firstValue == null || firstValue.isNull();

            boolean secondMissing =
                    secondValue == null || secondValue.isNull();

            // Keep missing and null values at the end
            if (firstMissing && secondMissing) {
                return 0;
            }

            if (firstMissing) {
                return 1;
            }

            if (secondMissing) {
                return -1;
            }

            return direction *
                    compareValues(firstValue, secondValue);
        });

        return jsonRecords;
    }

    // Compare numbers numerically and other values as text
    private int compareValues(
            JsonNode first, JsonNode second) {

        if (first.isNumber() && second.isNumber()) {
            return first.decimalValue()
                    .compareTo(second.decimalValue());
        }

        if (first.isNumber() != second.isNumber()) {
            return first.isNumber() ? -1 : 1;
        }

        return first.asText()
                .compareTo(second.asText());
    }

    // Convert stored JSON text back into a JSON object
    private JsonNode parseRecord(String recordData) {

        try {
            return objectMapper.readTree(recordData);

        } catch (Exception exception) {

            throw new IllegalStateException(
                    "Unable to read stored JSON record",
                    exception);
        }
    }

    // Validate dataset name and requested field
    private void validateQuery(
            String datasetName, String field) {

        if (datasetName == null || datasetName.isBlank()) {
            throw new IllegalArgumentException(
                    "Dataset name cannot be empty");
        }

        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException(
                    "Field name cannot be empty");
        }
    }
}
