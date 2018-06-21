package com.github.eliux.mega.auth;

import com.github.eliux.mega.MegaSession;
import com.github.eliux.mega.cmd.MegaCmdLogin;

public class MegaAuthFolder extends MegaAuth {
    private final String folderPath;

    public MegaAuthFolder(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    @Override
    public MegaSession login() {
        final MegaCmdLogin megaCmdLogin = new MegaCmdLogin(folderPath);
        megaCmdLogin.run();

        return new MegaSession(this);
    }
}