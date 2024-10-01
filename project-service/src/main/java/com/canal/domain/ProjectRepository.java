package com.canal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Iterable<ProjectEntity> findByUserSeqAndDeleted(Long userSeq,boolean deleted);
    ProjectEntity findByProjectNameAndUserSeqAndDeleted(String projectName, Long userSeq,boolean deleted);
    ProjectEntity findByProjectSeqAndDeleted(Long projectSeq, boolean deleted);
}
