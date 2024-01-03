package com.issue.tracker.api.common;

import java.util.Optional;

public class RankUtils {
    public Optional<String> calculateMiddleRank(String first, String second) {
        long firstRank = BaseConversionUtil.convertBase43to10(first);
        long secondRank = BaseConversionUtil.convertBase43to10(second);

        long difference = secondRank - firstRank;
        if (difference < 2) {
            //There is no empty rank in between these values.
            return Optional.empty();
        } else {
            long middleRank = firstRank + difference / 2;
            return Optional.of(BaseConversionUtil.convertBase10to43(middleRank));
        }
    }
}
