package io.github.eliux.mega.cmd;

import java.util.Optional;

public abstract class AbstractMegaCmdPut extends AbstractMegaCmdPathHandler {

    public String getCmd() {
        return "put";
    }
}