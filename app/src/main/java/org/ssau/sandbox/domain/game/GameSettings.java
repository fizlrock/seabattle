package org.ssau.sandbox.domain.game;

import java.util.List;

/**
 * GameSettings
 */
public record GameSettings(GameMapSettings mapSettings, List<BoatType> boatTypes) {

}
