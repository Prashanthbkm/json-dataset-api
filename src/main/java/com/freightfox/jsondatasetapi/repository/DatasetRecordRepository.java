
package com.freightfox.jsondatasetapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freightfox.jsondatasetapi.entity.DatasetRecord;

@Repository
public interface DatasetRecordRepository
        extends JpaRepository<DatasetRecord, Long> {

    List<DatasetRecord> findByDatasetName(String datasetName);

    boolean existsByDatasetName(String datasetName);

    long countByDatasetName(String datasetName);
}
