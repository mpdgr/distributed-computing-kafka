package com.mpdgr.commonrepo.domain;

import com.mpdgr.commonrepo.enumeration.SupercomputerStateType;
import lombok.Data;

@Data
public class SupercomputerState {
    private SupercomputerStateType state;
}
