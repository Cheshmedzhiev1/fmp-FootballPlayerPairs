package com.football.fmp.application.port.driving;

import com.football.fmp.domain.model.PlayerPairResult;

public interface ForFindLongestPlayingPair {
    PlayerPairResult findLongestPlayingPair();
}
// driving port
// implemented by PlayerPairApplicationService, called by PlayerController (rest endpoint get / api / players / longest