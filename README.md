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
