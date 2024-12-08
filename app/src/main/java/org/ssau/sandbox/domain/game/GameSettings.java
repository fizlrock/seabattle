package org.ssau.sandbox.domain.game;

import java.util.List;

/**
 * Структура хранит информацию о правилах игры, а именно о
 * <ol>
 * <li>Размерах поля
 * <li>Количестве и размерах кораблей
 * </ol>
 * Есть проверка на корректность размеров поля. И контроль длины корабля(можно
 * ли его разместить в заданном поле)
 */
public record GameSettings(GameMapSettings mapSettings, List<BoatType> boatTypes) {
  // TODO проверки

}
