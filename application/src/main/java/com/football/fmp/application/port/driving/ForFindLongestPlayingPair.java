package com.football.fmp.application.port.driving;

import com.football.fmp.domain.model.PlayerPairResult;

import java.util.List;

public interface ForFindLongestPlayingPair {
   List<PlayerPairResult> findLongestPlayingPair();
}
// driving port
// implemented by PlayerPairApplicationService, called by PlayerController (rest endpoint get / api / players / longest