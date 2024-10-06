package org.ssau.seabattle.model;

/**
 * AsciiGameMapSerializer
 */
public class AsciiGameMapSerializer implements GameMapSerializer {

  private static final char[] ownerMatrix = new char[] { '.', 'S', 'x', 'X' };
  private static final char[] opponentMatrix = new char[] { '.', 'O', 'O', 'X' };

  @Override
  public String serializeOpponentMap(GameField field) {
    return makeView(field, field.getOpponentMap(), opponentMatrix);
  }

  @Override
  public String serializeOwnerMap(GameField field) {

    return makeView(field, field.getOwnerMap(), ownerMatrix);
  }

  private String makeView(GameField field, int[][] map, char[] matrix) {

    int width = field.getWidth(), height = field.getHeight();
    int arraySize = height * (width + 1);
    char[] result = new char[arraySize];

    for (int i = 0; i < arraySize; i++)

      if ((i > 0) & ((i + 1) % (width + 1) == 0))
        result[i] = '\n';
      else {
        int x = i % (width + 1); // Ширина поля + 1 символ на перенос
        int y = (i - x) / (width + 1);
        result[i] = matrix[map[x][y]];
      }

    return String.valueOf(result);
  }

}
