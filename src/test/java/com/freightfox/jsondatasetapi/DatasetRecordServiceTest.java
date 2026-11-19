
package com.freightfox.jsondatasetapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import com.freightfox.jsondatasetapi.entity.DatasetRecord;
import com.freightfox.jsondatasetapi.repository.DatasetRecordRepository;
import com.freightfox.jsondatasetapi.service.DatasetRecordService;

@ExtendWith(MockitoExtension.class)
class DatasetRecordServiceTest {

    @Mock
    private DatasetRecordRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DatasetRecordService service;

    private ObjectMapper testObjectMapper;

    @BeforeEach
    void setUp() {
        testObjectMapper = new ObjectMapper();
    }

    @Test
    void shouldAddRecordSuccessfully() throws Exception {

        JsonNode input = testObjectMapper.readTree(
                "{\"name\":\"Alice\",\"age\":25}");

        DatasetRecord savedRecord =
                new DatasetRecord("employees", input.toString());

        when(repository.save(any(DatasetRecord.class)))
                .thenReturn(savedRecord);

        DatasetRecord result =
                service.addRecord("employees", input);

        assertEquals("employees", result.getDatasetName());

        verify(repository).save(any(DatasetRecord.class));
    }

    @Test
    void shouldRejectNonObjectJsonRecord() throws Exception {

        JsonNode input = testObjectMapper.readTree("[1,2,3]");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addRecord("employees", input)
        );
    }

    @Test
    void shouldGroupRecordsByDepartment() throws Exception {

        DatasetRecord engineering = new DatasetRecord(
                "employees",
                "{\"name\":\"John\",\"department\":\"Engineering\"}");

        DatasetRecord hr = new DatasetRecord(
                "employees",
                "{\"name\":\"Alice\",\"department\":\"HR\"}");

        when(repository.findByDatasetName("employees"))
                .thenReturn(List.of(engineering, hr));

        when(objectMapper.readTree(any(String.class)))
                .thenAnswer(invocation -> {
                    String json =
                            invocation.getArgument(0, String.class);
                    return testObjectMapper.readTree(json);
                });

        var result =
                service.groupRecords("employees", "department");

        assertEquals(2, result.size());
        assertEquals(1, result.get("Engineering").size());
        assertEquals(1, result.get("HR").size());
    }

    @Test
    void shouldSortRecordsInAscendingOrder() throws Exception {

        DatasetRecord age35 = new DatasetRecord(
                "employees",
                "{\"name\":\"Robert\",\"age\":35}");

        DatasetRecord age25 = new DatasetRecord(
                "employees",
                "{\"name\":\"Alice\",\"age\":25}");

        when(repository.findByDatasetName("employees"))
                .thenReturn(List.of(age35, age25));

        when(objectMapper.readTree(any(String.class)))
                .thenAnswer(invocation -> {
                    String json =
                            invocation.getArgument(0, String.class);
                    return testObjectMapper.readTree(json);
                });

        var result =
                service.sortRecords("employees", "age", "asc");

        assertEquals(25, result.get(0).get("age").asInt());
        assertEquals(35, result.get(1).get("age").asInt());
    }

    @Test
    void shouldSortRecordsInDescendingOrder() throws Exception {

        DatasetRecord age25 = new DatasetRecord(
                "employees",
                "{\"name\":\"Alice\",\"age\":25}");

        DatasetRecord age35 = new DatasetRecord(
                "employees",
                "{\"name\":\"Robert\",\"age\":35}");

        when(repository.findByDatasetName("employees"))
                .thenReturn(List.of(age25, age35));

        when(objectMapper.readTree(any(String.class)))
                .thenAnswer(invocation -> {
                    String json =
                            invocation.getArgument(0, String.class);
                    return testObjectMapper.readTree(json);
                });

        var result =
                service.sortRecords("employees", "age", "desc");

        assertEquals(35, result.get(0).get("age").asInt());
        assertEquals(25, result.get(1).get("age").asInt());
    }
}
