-- 1) 정책 삭제
SELECT remove_continuous_aggregate_policy('power_production_1min');
SELECT remove_continuous_aggregate_policy('power_production_1hour');
SELECT remove_continuous_aggregate_policy('power_production_1day');
SELECT remove_continuous_aggregate_policy('power_production_1month');
SELECT remove_continuous_aggregate_policy('power_production_1year');

-- 2) 뷰(머터라이즈드 뷰) 삭제
DROP MATERIALIZED VIEW IF EXISTS power_production_1min;
DROP MATERIALIZED VIEW IF EXISTS power_production_1hour;
DROP MATERIALIZED VIEW IF EXISTS power_production_1day;
DROP MATERIALIZED VIEW IF EXISTS power_production_1month;
DROP MATERIALIZED VIEW IF EXISTS power_production_1year;

ALTER DATABASE cheongju SET timescaledb.partial_aggregate TO 'on';
SET timescaledb.partial_aggregate = on;

===========================분단위================================================
CREATE MATERIALIZED VIEW power_production_1min
WITH (timescaledb.continuous) AS
SELECT
  time_bucket('1 minute', "timestamp") AS bucket,
  site_id,
  max(today_kwh)    AS max_today_kwh,
  avg(pv_current)   AS avg_pv_current,
  avg(pv_power_kw)  AS avg_pv_power_kw,
  avg(pv_voltage)   AS avg_pv_voltage,
  avg(frequency_hz) AS avg_frequency_hz
FROM public.power_production
GROUP BY bucket, site_id
WITH NO DATA;

SELECT add_continuous_aggregate_policy(
  'power_production_1min',
  start_offset      => INTERVAL '2 minutes',
  end_offset        => INTERVAL '0 seconds',
  schedule_interval => INTERVAL '1 minute'
);

===========================시단위================================================
CREATE MATERIALIZED VIEW power_production_1hour
WITH (timescaledb.continuous) AS
SELECT
  time_bucket('1 hour', "timestamp") AS bucket,
  site_id,
  max(today_kwh)    AS max_today_kwh,
  avg(pv_current)   AS avg_pv_current,
  avg(pv_power_kw)  AS avg_pv_power_kw,
  avg(pv_voltage)   AS avg_pv_voltage,
  avg(frequency_hz) AS avg_frequency_hz
FROM public.power_production
GROUP BY bucket, site_id
WITH NO DATA;

SELECT add_continuous_aggregate_policy(
  'power_production_1hour',
  start_offset      => INTERVAL '2 hours',
  end_offset        => INTERVAL '0 seconds',
  schedule_interval => INTERVAL '1 minute'
);

===========================일단위================================================
CREATE MATERIALIZED VIEW power_production_1day
WITH (timescaledb.continuous) AS
SELECT
  time_bucket('1 day', "timestamp") AS bucket,
  site_id,
  max(today_kwh)      AS max_today_kwh,
  avg(pv_current)     AS avg_pv_current,
  avg(pv_power_kw)    AS avg_pv_power_kw,
  avg(pv_voltage)     AS avg_pv_voltage,
  avg(frequency_hz)   AS avg_frequency_hz
FROM public.power_production
GROUP BY bucket, site_id
WITH NO DATA;

SELECT add_continuous_aggregate_policy(
  'power_production_1day',
  start_offset      => INTERVAL '2 days',
  end_offset        => INTERVAL '0 seconds',
  schedule_interval => INTERVAL '10 minutes'
);

===========================월단위================================================
CREATE MATERIALIZED VIEW power_production_1month
WITH (timescaledb.continuous) AS
SELECT
  time_bucket('1 month', "timestamp") AS bucket,
  site_id,
  max(today_kwh)    AS max_today_kwh,
  avg(pv_current)   AS avg_pv_current,
  avg(pv_power_kw)  AS avg_pv_power_kw,
  avg(pv_voltage)   AS avg_pv_voltage,
  avg(frequency_hz) AS avg_frequency_hz
FROM public.power_production
GROUP BY bucket, site_id
WITH NO DATA;


SELECT add_continuous_aggregate_policy(
  'power_production_1month',
  start_offset      => INTERVAL '2 months',
  end_offset        => INTERVAL '0 seconds',
  schedule_interval => INTERVAL '1 day'
);



===========================조회================================================
select * from power_production;

select * from power_production_1min;

select * from power_production_1hour;

select * from power_production_1day;

select * from power_production_1month;


SELECT
  a.bucket,
  a.site_id,
  a.max_today_kwh         AS agg_max_today_kwh,
  b.max_today_kwh         AS raw_max_today_kwh,
  a.avg_pv_current        AS agg_avg_pv_current,
  b.avg_pv_current        AS raw_avg_pv_current,
  a.avg_pv_power_kw       AS agg_avg_pv_power_kw,
  b.avg_pv_power_kw       AS raw_avg_pv_power_kw,
  a.avg_pv_voltage        AS agg_avg_pv_voltage,
  b.avg_pv_voltage        AS raw_avg_pv_voltage,
  a.avg_frequency_hz      AS agg_avg_frequency_hz,
  b.avg_frequency_hz      AS raw_avg_frequency_hz
FROM power_production_1min a
JOIN (
    SELECT
        time_bucket('1 minute', "timestamp") AS bucket,
        site_id,
        MAX(today_kwh)    AS max_today_kwh,
        AVG(pv_current)   AS avg_pv_current,
        AVG(pv_power_kw)  AS avg_pv_power_kw,
        AVG(pv_voltage)   AS avg_pv_voltage,
        AVG(frequency_hz) AS avg_frequency_hz
    FROM public.power_production
    GROUP BY bucket, site_id
) b
ON a.bucket = b.bucket AND a.site_id = b.site_id
ORDER BY a.bucket DESC, a.site_id;


SELECT
  a.bucket,
  a.site_id,
  ROUND(a.max_today_kwh - b.max_today_kwh, 5)       AS diff_max_today_kwh,
  ROUND(a.avg_pv_current - b.avg_pv_current, 5)     AS diff_avg_pv_current,
  ROUND(a.avg_pv_power_kw - b.avg_pv_power_kw, 5)   AS diff_avg_pv_power_kw,
  ROUND(a.avg_pv_voltage - b.avg_pv_voltage, 5)     AS diff_avg_pv_voltage,
  ROUND(a.avg_frequency_hz - b.avg_frequency_hz, 5) AS diff_avg_frequency_hz
FROM power_production_1min a
JOIN (
    SELECT
        time_bucket('1 minute', "timestamp") AS bucket,
        site_id,
        MAX(today_kwh)    AS max_today_kwh,
        AVG(pv_current)   AS avg_pv_current,
        AVG(pv_power_kw)  AS avg_pv_power_kw,
        AVG(pv_voltage)   AS avg_pv_voltage,
        AVG(frequency_hz) AS avg_frequency_hz
    FROM public.power_production
    GROUP BY bucket, site_id
) b
ON a.bucket = b.bucket AND a.site_id = b.site_id
WHERE
    ROUND(a.max_today_kwh - b.max_today_kwh, 5) <> 0 OR
    ROUND(a.avg_pv_current - b.avg_pv_current, 5) <> 0 OR
    ROUND(a.avg_pv_power_kw - b.avg_pv_power_kw, 5) <> 0 OR
    ROUND(a.avg_pv_voltage - b.avg_pv_voltage, 5) <> 0 OR
    ROUND(a.avg_frequency_hz - b.avg_frequency_hz, 5) <> 0
ORDER BY a.bucket DESC, a.site_id;
