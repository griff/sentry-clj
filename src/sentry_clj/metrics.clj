(ns sentry-clj.metrics
  (:require [sentry-clj.core :as core])
  (:import (io.sentry MeasurementUnit$Custom MeasurementUnit$Duration MeasurementUnit$Fraction MeasurementUnit$Information Sentry)))

(defn keyword->measurement-unit [unit]
  (case unit
    :nanosecond MeasurementUnit$Duration/NANOSECOND
    :microsecond MeasurementUnit$Duration/MICROSECOND
    :millisecond MeasurementUnit$Duration/MILLISECOND
    :second MeasurementUnit$Duration/SECOND
    :minute MeasurementUnit$Duration/MINUTE
    :hour MeasurementUnit$Duration/HOUR
    :day MeasurementUnit$Duration/DAY
    :week MeasurementUnit$Duration/WEEK

    :bit MeasurementUnit$Information/BIT
    :byte MeasurementUnit$Information/BYTE
    :kilobyte MeasurementUnit$Information/KILOBYTE
    :kibibyte MeasurementUnit$Information/KIBIBYTE
    :megabyte MeasurementUnit$Information/MEGABYTE
    :mebibyte MeasurementUnit$Information/MEBIBYTE
    :gigabyte MeasurementUnit$Information/GIGABYTE
    :gibibyte MeasurementUnit$Information/GIBIBYTE
    :terrabyte MeasurementUnit$Information/TERABYTE
    :tebibyte MeasurementUnit$Information/TEBIBYTE
    :petabyte MeasurementUnit$Information/PETABYTE
    :pebibyte MeasurementUnit$Information/PEBIBYTE
    :exabyte MeasurementUnit$Information/EXABYTE
    :exbibyte MeasurementUnit$Information/EXBIBYTE

    :ratio MeasurementUnit$Fraction/RATIO
    :percent MeasurementUnit$Fraction/PERCENT

    (MeasurementUnit$Custom (name unit))))

(defn increment-counter
  ([key]
   (increment-counter key 1.0))
  ([key value]
   (increment-counter key value nil))
  ([key value unit]
   (increment-counter key value unit nil))
  ([key value unit tags]
   (increment-counter key value unit tags nil))
  ([key value unit tags timestamp]
   (increment-counter key value unit tags timestamp 1))
  ([key value unit tags timestamp stack-level]
   (.increment (Sentry/metrics)
               key
               value
               (some-> unit keyword->measurement-unit)
               (some-> tags core/java-util-hashmappify-vals)
               timestamp stack-level)))

(defn emit-distribution
  ([key value]
   (emit-distribution key value nil))
  ([key value unit]
   (emit-distribution key value unit nil))
  ([key value unit tags]
   (emit-distribution key value unit tags nil))
  ([key value unit tags timestamp]
   (emit-distribution key value unit tags timestamp 1))
  ([key value unit tags timestamp stack-level]
   (.distribution (Sentry/metrics)
                  key
                  value
                  (some-> unit keyword->measurement-unit)
                  (some-> tags core/java-util-hashmappify-vals)
                  timestamp stack-level)))

(defn emit-set
  ([key value]
   (emit-set key value nil))
  ([key value unit]
   (emit-set key value unit nil))
  ([key value unit tags]
   (emit-set key value unit tags nil))
  ([key value unit tags timestamp]
   (emit-set key value unit tags timestamp 1))
  ([key value unit tags timestamp stack-level]
   (.set (Sentry/metrics)
                  key
                  value
                  (some-> unit keyword->measurement-unit)
                  (some-> tags core/java-util-hashmappify-vals)
                  timestamp stack-level)))

(defn emit-gauge
  ([key value]
   (emit-gauge key value nil))
  ([key value unit]
   (emit-gauge key value unit nil))
  ([key value unit tags]
   (emit-gauge key value unit tags nil))
  ([key value unit tags timestamp]
   (emit-gauge key value unit tags timestamp 1))
  ([key value unit tags timestamp stack-level]
   (.gauge (Sentry/metrics)
           key
           value
           (some-> unit keyword->measurement-unit)
           (some-> tags core/java-util-hashmappify-vals)
           timestamp stack-level)))

(defn measure-timing
  ([key callback]
   (measure-timing key callback nil))
  ([key callback unit]
   (measure-timing key callback unit nil))
  ([key callback unit tags]
   (measure-timing key callback unit tags nil))
  ([key callback unit tags]
   (measure-timing key callback unit tags 1))
  ([key callback unit tags stack-level]
   (.timing (Sentry/metrics)
            key
            callback
            (some-> unit keyword->measurement-unit)
            (some-> tags core/java-util-hashmappify-vals)
            stack-level)))