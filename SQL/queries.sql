# 1 Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.
SELECT ip, count(logDate)
    FROM log_entry
WHERE logDate BETWEEN ? AND ?
GROUP BY ip
    HAVING count(logDate) >= ?;

# EX
SELECT ip, count(logDate) as total
  FROM log_entry
WHERE logDate BETWEEN STR_TO_DATE('2017-01-01.13:00:00', '%Y-%m-%d.%H:%i:%s') AND STR_TO_DATE('2017-01-01.14:00:00', '%Y-%m-%d.%H:%i:%s')
GROUP BY ip
  HAVING count(logDate) >= 100;

# 2 Write MySQL query to find requests made by a given IP
SELECT * FROM log_entry
WHERE ip = ?;

# EX
SELECT * FROM log_entry
WHERE ip = '192.168.106.134';