package org.ssau.sandbox.domain.game.field;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.ssau.sandbox.domain.game.BoatCord;
import org.ssau.sandbox.domain.game.GameSettings;



/**
 * <p>
 * Игровое поле
 * </p>
 * 
 * Реализует следующий функционал:
 * <ul>
 * <li>Хранение состояния поля
 * <li>Отображение поля для владельца и противника
 * <li>Логика расстановки кораблей
 * <li>Логика выстрела
 * <li>Посчет живых кораблей на поле
 * </ul>
 */
@Getter
public class GameField {


  

  private static record Point(int x, int y) {
  }

  private final int[][] ownerMap;

  private final int[][] opponentMap;

  private final int width, height;
  private int aliveShips = 0;

  Map<Integer, Integer> stilBoats = new HashMap<>();

  public GameField(GameSettings gameSettings) {
    this.width = gameSettings.mapSettings().width();
    this.height = gameSettings.mapSettings().height();

    gameSettings.boatTypes().stream()
        .forEach(x -> stilBoats.put(x.size(), x.count()));

    System.out.println(stilBoats);

    ownerMap = new int[width][height];
    opponentMap = new int[width][height];
  }

  /**
   * Сделать выстрел по координатам(отсчет от нуля)
   * 
   * @param x
   * @param y
   * @return
   */
  public int makeShot(int x, int y) {
    OwnerCageState prevState = getOwnerState(x, y);

    switch (prevState) {
      case Void -> {
        setOwnerState(x, y, OwnerCageState.ShotIntoVoid);
        setOpponentState(x, y, OpponentCageState.CheckedEmpty);
      }
      case Ship -> {
        setOwnerState(x, y, OwnerCageState.ShotIntoShip);
        setOpponentState(x, y, OpponentCageState.BlowedShip);
        aliveShips--;
        markEmptyCages(x, y);
      }
      case ShotIntoShip, ShotIntoVoid -> {
      }
    }

    return aliveShips;
  }

  public synchronized void placeBoat(BoatCord cords) {
    List<Point> points = getPoints(cords);
    var error = points.stream()
        .map(this::checkPoint)
        .filter(x -> x != null)
        .findAny();
    error.ifPresent(e -> {
      throw e;
    });

    stilBoats.compute(points.size(), (Integer size, Integer still) -> still - 1);
    points.stream().forEach(p -> setOwnerState(p.x, p.y, OwnerCageState.Ship));
    aliveShips += points.size();

  }

  /**
   * Проверка корректности координат корабля и получение его точек
   * 
   * @param cords
   * @return
   */
  public List<Point> getPoints(BoatCord cords) {

    // Проверка на выход за границы
    if (cords.xs() < 0
        || cords.xs() > width
        || cords.xe() < 0
        || cords.xe() > width
        || cords.ys() < 0
        || cords.ys() > height
        || cords.ye() < 0
        || cords.ye() > height)
      throw new IllegalArgumentException("Недопустимые корды");

    // TODO тут можно проебаться
    // // Проверка на перепутанные координаты

    // Проверка на диагональные корабли и расчет длины
    boolean horizontal = false;
    boolean vertical = false;
    int length;

    if (cords.ys() == cords.ye())
      horizontal = true;
    if (cords.xs() == cords.xe())
      vertical = true;

    if (horizontal && !vertical)
      // Горизонтальный корабль
      length = cords.xe() - cords.xs() + 1;
    else if (!horizontal && vertical)
      // Вертикальный корабль
      length = cords.ys() - cords.ye() + 1;
    else if (horizontal && vertical)
      // Корабль в одну клеточку
      length = 1;
    else
      // Недопустимые корды
      throw new IllegalArgumentException("Нельзя ставить корабли по диагонали");
    System.out.println(length);

    int stil = stilBoats.getOrDefault(length, -1);
    switch (stil) {
      case -1 -> throw new IllegalArgumentException("Недопустимая длина корабля: " + length);
      case 0 -> throw new IllegalArgumentException("Достигнуто максимальное число кораблей длины " + length);
    }

    List<Point> points = new ArrayList<>();

    if (horizontal && vertical)
      points.add(new Point(cords.xe(), cords.ye()));
    else if (horizontal)
      for (int x = cords.xs(); x <= cords.xe(); x++)
        points.add(new Point(x, cords.ye()));
    else if (vertical)
      for (int Y = cords.ys(); Y <= cords.ye(); Y++)
        points.add(new Point(cords.xs(), Y));

    return points;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int[][] getOwnerMap() {
    return ownerMap;
  }

  public int[][] getOpponentMap() {
    return opponentMap;
  }

  <T> void swap(Supplier<T> getterA, Consumer<T> setterA, Supplier<T> getterB, Consumer<T> setterB) {
    T buffer = getterA.get();
    setterA.accept(getterB.get());
    setterB.accept(buffer);
  }

  private void setOwnerState(int x, int y, OwnerCageState state) {
    ownerMap[x][y] = state.ordinal();
  }

  private void setOpponentState(int x, int y, OpponentCageState state) {
    opponentMap[x][y] = state.ordinal();
  }

  private OwnerCageState getOwnerState(int x, int y) {
    return OwnerCageState.values()[ownerMap[x][y]];
  }

  private OpponentCageState getOpponentState(int x, int y) {
    return OpponentCageState.values()[opponentMap[x][y]];
  }

  private void markEmptyCages(int x, int y) {
    // TODO
  }

  /**
   * Возвращает исключение, если нельзя поставить корабль в указанных координатах
   * 
   * @return
   */
  private IllegalArgumentException checkPoint(Point point) {
    int x = point.x;
    int y = point.y;

    if (getOwnerState(x, y) == OwnerCageState.Ship)
      return formatExcepiton("В клетке с координатами %d:%d уже стоит корабль", x, y);

    var sidePoint = Stream.of(
        new Point(x, y + 1),
        new Point(x + 1, y),
        new Point(x, y - 1),
        new Point(x - 1, y + 1),
        new Point(x - 1, y - 1),
        new Point(x + 1, y + 1),
        new Point(x + 1, y - 1))
        .filter(p -> p.x >= 0 & p.x < width & p.y >= 0 & p.y < height)
        .filter(p -> getOwnerState(p.x, p.y) == OwnerCageState.Ship)
        .findAny();
    if (sidePoint.isPresent())
      return formatExcepiton("Корабли соприкасаются в точках (%d:%d) (%d:%d)",
          x, y, sidePoint.get().x, sidePoint.get().y);
    return null;
  }

  private IllegalArgumentException formatExcepiton(String template, Object... args) {
    return new IllegalArgumentException(String.format(template, args));
  }
}
