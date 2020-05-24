package com.mono40.movil.app.upgrade.action;

import com.mono40.movil.PhoneNumber;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class AppUpgradeAction3 extends AppUpgradeAction {

    static AppUpgradeAction3 create(SessionManager sessionManager, PosBridge posBridge) {
        return new AppUpgradeAction3(sessionManager, posBridge);
    }

    private final SessionManager sessionManager;
    private final PosBridge posBridge;

    private AppUpgradeAction3(SessionManager sessionManager, PosBridge posBridge) {
        this.sessionManager = ObjectHelper.checkNotNull(sessionManager, "sessionManager");
        this.posBridge = ObjectHelper.checkNotNull(posBridge, "posBridge");
    }

    @Override
    public int id() {
        return 3;
    }

    @Override
    public void run() throws Exception {
        if (!this.posBridge.isAvailable()) {
            return;
        }
        if (!this.sessionManager.isUserSet()) {
            return;
        }
        final PhoneNumber phoneNumber = this.sessionManager.getUser()
                .phoneNumber();
        this.posBridge.unregister(phoneNumber);
    }
}