package com.football.fmp.outbound.adapters.persistence.repository;

import com.football.fmp.outbound.adapters.persistence.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordJpaRepository extends JpaRepository<RecordEntity, Long> {
    @Query(value = """
                WITH pair_match_overlap AS (
                  SELECT
                    LEAST(r1.player_id, r2.player_id) AS player1_id,
                    GREATEST(r1.player_id, r2.player_id) AS player2_id,
                    r1.match_id,
                    GREATEST(
                      0,
                      LEAST(COALESCE(r1.to_minutes, 90), COALESCE(r2.to_minutes, 90))
                      - GREATEST(r1.from_minutes, r2.from_minutes)
                    ) AS overlap_minutes
                  FROM records r1
                  JOIN records r2
                    ON r1.match_id = r2.match_id
                   AND r1.player_id < r2.player_id
                ),
                pair_match_sum AS (
                  SELECT player1_id, player2_id, match_id, SUM(overlap_minutes) AS minutes
                  FROM pair_match_overlap
                  WHERE overlap_minutes > 0
                  GROUP BY player1_id, player2_id, match_id
                )
                SELECT player1_id, player2_id, match_id, minutes
                FROM pair_match_sum
                WHERE (player1_id, player2_id) IN (
                    SELECT player1_id, player2_id
                    FROM pair_match_sum
                    GROUP BY player1_id, player2_id
                    HAVING SUM(minutes) = (
                        SELECT MAX(total)
                        FROM (
                            SELECT SUM(minutes) AS total
                            FROM pair_match_sum
                            GROUP BY player1_id, player2_id
                        ) totals
                    )
                )
                ORDER BY player1_id, player2_id, match_id
            """, nativeQuery = true)
    List<PlayerPairProjection> findLongestPlayingPair();
}