-- ============================================================
--  NIDS DATABASE SCHEMA
--  Demonstrates: DDL, PK/FK, Triggers, Procedures, Indexing,
--  Transactions, Normalization (BCNF), Weak Entity
-- ============================================================

CREATE DATABASE IF NOT EXISTS nids_db;
USE nids_db;

-- 1. NETWORK_DEVICE
CREATE TABLE NETWORK_DEVICE (
    device_id   INT AUTO_INCREMENT PRIMARY KEY,
    device_name VARCHAR(50)  NOT NULL,
    ip_address  VARCHAR(20)  NOT NULL UNIQUE,
    device_type ENUM('PC','Router','Server','Switch') NOT NULL,
    location    VARCHAR(100)
);

-- 2. INTRUSION_RULE
CREATE TABLE INTRUSION_RULE (
    rule_id     INT AUTO_INCREMENT PRIMARY KEY,
    rule_name   VARCHAR(100) NOT NULL,
    protocol    ENUM('TCP','UDP','ICMP','ANY') NOT NULL,
    port        INT,
    threshold   INT          NOT NULL COMMENT 'Max allowed packets',
    severity    ENUM('LOW','MEDIUM','HIGH') NOT NULL
);

-- 3. NETWORK_LOG
CREATE TABLE NETWORK_LOG (
    log_id      INT AUTO_INCREMENT PRIMARY KEY,
    device_id   INT          NOT NULL,
    src_ip      VARCHAR(20)  NOT NULL,
    dst_ip      VARCHAR(20)  NOT NULL,
    protocol    ENUM('TCP','UDP','ICMP') NOT NULL,
    port        INT,
    packets     INT          NOT NULL,
    log_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES NETWORK_DEVICE(device_id)
);

-- 4. ALERT  (resolves M:N between NETWORK_LOG and INTRUSION_RULE)
CREATE TABLE ALERT (
    alert_id    INT AUTO_INCREMENT PRIMARY KEY,
    log_id      INT NOT NULL,
    rule_id     INT NOT NULL,
    alert_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (log_id)  REFERENCES NETWORK_LOG(log_id),
    FOREIGN KEY (rule_id) REFERENCES INTRUSION_RULE(rule_id)
);

-- 5. ALERT_REVIEW  (Weak Entity — depends on ALERT)
CREATE TABLE ALERT_REVIEW (
    review_id   INT AUTO_INCREMENT,
    alert_id    INT NOT NULL,
    analyst     VARCHAR(80)  NOT NULL,
    status      ENUM('CONFIRMED','FALSE_POSITIVE','UNDER_REVIEW') NOT NULL,
    remarks     TEXT,
    review_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (review_id, alert_id),
    FOREIGN KEY (alert_id) REFERENCES ALERT(alert_id)
);

-- ============================================================
--  INDEXES for performance
-- ============================================================
CREATE INDEX idx_log_protocol  ON NETWORK_LOG(protocol);
CREATE INDEX idx_log_port      ON NETWORK_LOG(port);
CREATE INDEX idx_log_time      ON NETWORK_LOG(log_time);
CREATE INDEX idx_alert_rule    ON ALERT(rule_id);

-- ============================================================
--  TRIGGER: Auto-generate alerts on new log insert
-- ============================================================
DELIMITER $$

CREATE TRIGGER trg_check_intrusion
AFTER INSERT ON NETWORK_LOG
FOR EACH ROW
BEGIN
    -- Insert an alert for every matching rule
    INSERT INTO ALERT (log_id, rule_id)
    SELECT NEW.log_id, r.rule_id
    FROM INTRUSION_RULE r
    WHERE (r.protocol = NEW.protocol OR r.protocol = 'ANY')
      AND (r.port     = NEW.port     OR r.port IS NULL)
      AND  r.threshold < NEW.packets;
END$$

-- ============================================================
--  STORED PROCEDURE: Alert summary report
-- ============================================================
CREATE PROCEDURE sp_alert_summary()
BEGIN
    SELECT
        r.severity,
        COUNT(a.alert_id)  AS total_alerts,
        MAX(a.alert_time)  AS latest_alert
    FROM ALERT a
    JOIN INTRUSION_RULE r ON a.rule_id = r.rule_id
    GROUP BY r.severity
    ORDER BY FIELD(r.severity,'HIGH','MEDIUM','LOW');
END$$

-- ============================================================
--  STORED PROCEDURE: Device threat report
-- ============================================================
CREATE PROCEDURE sp_device_threats()
BEGIN
    SELECT
        d.device_name,
        d.ip_address,
        COUNT(a.alert_id) AS alert_count
    FROM NETWORK_DEVICE d
    JOIN NETWORK_LOG    l ON d.device_id = l.device_id
    JOIN ALERT          a ON l.log_id    = a.log_id
    GROUP BY d.device_id
    ORDER BY alert_count DESC;
END$$

DELIMITER ;

-- ============================================================
--  SEED DATA
-- ============================================================
INSERT INTO NETWORK_DEVICE (device_name, ip_address, device_type, location) VALUES
('Gateway-Router', '192.168.1.1',  'Router', 'Server Room'),
('Web-Server',     '192.168.1.10', 'Server', 'Data Center'),
('Admin-PC',       '192.168.1.50', 'PC',     'Admin Office'),
('DB-Server',      '192.168.1.20', 'Server', 'Data Center');

INSERT INTO INTRUSION_RULE (rule_name, protocol, port, threshold, severity) VALUES
('Port Scan Detection',   'TCP',  NULL, 100, 'HIGH'),
('DNS Flood',             'UDP',  53,   200, 'MEDIUM'),
('HTTP Flood',            'TCP',  80,   150, 'HIGH'),
('ICMP Flood',            'ICMP', NULL,  50, 'LOW'),
('SSH Brute Force',       'TCP',  22,    30, 'HIGH'),
('FTP Suspicious Access', 'TCP',  21,    20, 'MEDIUM');
