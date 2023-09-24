package com.mpdgr.commonrepo.domain;

import com.mpdgr.commonrepo.enumeration.SuperworkerStateType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuperworkerState extends JobEvent{
    private SuperworkerStateType state;

    public SuperworkerState() {
        super("Superworker monitoring process");
    }

    public SuperworkerState(SuperworkerStateType state) {
        super("Superworker monitoring process");
        this.state = state;
    }
}

