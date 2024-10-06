package org.ssau.seabattle.model;

import java.util.List;

/**
 * GameSettings
 */
public record GameSettings(GameMapSettings mapSettings, List<BoatType> boatTypes) {

}
