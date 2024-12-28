package org.ssau.sandbox.domain.game;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * GameSessionRecord
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSessionRecord implements Persistable<UUID> {

  /**
   * Идентификатор сессии. Создается на клиенте
   */
  @Id
  @GeneratedValue
  private UUID sessionId;

  // Имеют ссылку на таблицу app_user. Столбец id
  /**
   * Идентификатор первого пользователя
   */
  private Long firstPlayerId;

  /**
   * Идентификатор второго пользователя
   */
  private Long secondPlayerId;

  /**
   * Идентификатор победителя
   */
  private Long winnerPlayerId;

  /**
   * Время начала игры
   */
  private LocalDateTime startedAt;

  /**
   * Время конца игры
   */
  private LocalDateTime endedAt;

  /**
   * Общее число выстрел в игре
   */
  private int totalShots = 0;

  /**
   * Число успешных (в палубу) выстрелов первого игрока
   */
  private int firstPlayerShotsInTarget;
  /**
   * Число успешных (в палубу) выстрелов второго игрока
   */
  private int secondPlayerShotsInTarget;

  @Override
  public UUID getId() {
    return sessionId;
  }

  @Override
  public boolean isNew() {
    return true; // TODO костыльный костыль убрать
  }
}
