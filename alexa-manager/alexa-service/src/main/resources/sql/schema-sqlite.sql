-- Alexa可控设备
CREATE TABLE IF NOT EXISTS T_ALEXA_ENDPOINT (
  ID                VARCHAR(32)  NOT NULL,
  FRIENDLY_NAME     VARCHAR(50)  NOT NULL,
  DESCRIPTION       VARCHAR(255) DEFAULT NULL,
  MANUFACTURER_NAME VARCHAR(100) DEFAULT NULL,
  _CATEGORY         VARCHAR(100) NOT NULL,
  USER_ID           VARCHAR(32)  NOT NULL,
  GATEWAY           VARCHAR(12)  NOT NULL,
  _OPER_ID          TEXT         NOT NULL,
  CREATION_TIME     BIGINT       NOT NULL,
  CONSTRAINT T_ALEXA_endpoint PRIMARY KEY (ID)
);

-- Alexa可控设备功能
CREATE TABLE IF NOT EXISTS T_ALEXA_CAPABILITY (
  ID          VARCHAR(32) NOT NULL,
  TYPE        VARCHAR(20) NOT NULL,
  INTERFACE   VARCHAR(50) NOT NULL,
  VERSION     VARCHAR(10) NOT NULL,
  ENDPOINT_ID VARCHAR(32) NOT NULL,
  CONSTRAINT T_ALEXA_CAPABILITY PRIMARY KEY (ID)
);

-- Alexa可控设备功能属性
CREATE TABLE IF NOT EXISTS T_ALEXA_PROPERTY (
  ID                   VARCHAR(32) NOT NULL,
  PROACTIVELY_REPORTED TINYINT DEFAULT NULL,
  RETRIEVABLE          TINYINT DEFAULT NULL,
  CAPABILITY_ID        VARCHAR(32) NOT NULL,
  CONSTRAINT T_ALEXA_PROPERTY PRIMARY KEY (ID)
);

-- Alexa可控设备功能属性支持
DROP TABLE IF EXISTS T_ALEXA_SUPPORT;

CREATE TABLE IF NOT EXISTS T_ALEXA_SUPPORT (
  INTERFACE VARCHAR(50) NOT NULL,
  NAME      VARCHAR(50) NOT NULL
);
