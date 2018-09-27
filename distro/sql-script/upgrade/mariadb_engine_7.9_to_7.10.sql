-- https://app.camunda.com/jira/browse/CAM-9084
ALTER TABLE ACT_RE_PROCDEF
  ADD STARTABLE_ BOOLEAN NOT NULL DEFAULT TRUE;

-- https://app.camunda.com/jira/browse/CAM-9153
ALTER TABLE ACT_HI_VARINST
  ADD CREATE_TIME_ timestamp(3);

-- https://app.camunda.com/jira/browse/CAM-9215
ALTER TABLE ACT_HI_ATTACHMENT
  ADD CREATE_TIME_ timestamp(3);

-- https://app.camunda.com/jira/browse/CAM-9216
ALTER TABLE ACT_HI_DEC_IN
  ADD CREATE_TIME_ timestamp(3);

-- https://app.camunda.com/jira/browse/CAM-9217
ALTER TABLE ACT_HI_DEC_OUT
  ADD CREATE_TIME_ timestamp(3);

-- https://app.camunda.com/jira/browse/CAM-9199
ALTER TABLE ACT_HI_PROCINST
  ADD ROOT_PROCESS_INSTANCE_ID_ varchar(64);
create index ACT_IDX_HI_PRO_INST_ROOT_PI on ACT_HI_PROCINST(ROOT_PROCESS_INSTANCE_ID_);

-- https://app.camunda.com/jira/browse/CAM-9200
ALTER TABLE ACT_HI_PROCINST
  ADD REMOVAL_TIME_ datetime(3);
create index ACT_IDX_HI_PRO_INST_RM_TIME on ACT_HI_PROCINST(REMOVAL_TIME_);

-- https://app.camunda.com/jira/browse/CAM-9230
ALTER TABLE ACT_HI_BATCH
  ADD CREATE_USER_ID_ varchar(255);
ALTER TABLE ACT_RU_BATCH
  ADD CREATE_USER_ID_ varchar(255);

-- https://app.camunda.com/jira/browse/CAM-9270
ALTER TABLE ACT_HI_DECINST
  ADD ROOT_PROC_INST_ID_ varchar(64);
create index ACT_IDX_HI_DEC_INST_ROOT_PI on ACT_HI_DECINST(ROOT_PROC_INST_ID_);

-- https://app.camunda.com/jira/browse/CAM-9270
ALTER TABLE ACT_HI_DECINST
  ADD REMOVAL_TIME_ datetime(3);
create index ACT_IDX_HI_DEC_INST_RM_TIME on ACT_HI_DECINST(REMOVAL_TIME_);

-- https://app.camunda.com/jira/browse/CAM-9322
ALTER TABLE ACT_GE_BYTEARRAY
  ADD TYPE_ integer;

ALTER TABLE ACT_GE_BYTEARRAY
  ADD CREATE_TIME_ timestamp(3);

-- https://app.camunda.com/jira/browse/CAM-9370
ALTER TABLE ACT_RU_EXECUTION
  ADD ROOT_PROC_INST_ID_ varchar(64);

ALTER TABLE ACT_RU_EXECUTION
    ADD CONSTRAINT ACT_FK_EXE_ROOT_PROCINST
    FOREIGN KEY (ROOT_PROC_INST_ID_)
    REFERENCES ACT_RU_EXECUTION (ID_);

CREATE index ACT_IDX_EXEC_ROOT_PI ON ACT_RU_EXECUTION(ROOT_PROC_INST_ID_);