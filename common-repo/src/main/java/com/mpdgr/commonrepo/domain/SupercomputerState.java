package com.mpdgr.commonrepo.domain;

import com.mpdgr.commonrepo.enumeration.SupercomputerStateType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupercomputerState extends JobEvent{
    private SupercomputerStateType state;

    public SupercomputerState() {
        super("Supercomputer monitoring process");
    }

    public SupercomputerState(SupercomputerStateType state) {
        super("Supercomputer monitoring process");
        this.state = state;
    }
}

