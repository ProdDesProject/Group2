package com.oamkgroup2.heartbeat.model;

/**
 * The calculated shape of a graph of heartrate logs for one night. This shape
 * tells us how good the nights' sleep was.
 */
public enum ShapeResult {
    HAMMOCK, HILL, SLOPE, // TODO: check these values
    UNDEFINED
}
