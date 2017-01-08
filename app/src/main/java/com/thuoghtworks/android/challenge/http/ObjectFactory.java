package com.thuoghtworks.android.challenge.http;

import com.thuoghtworks.android.challenge.BattleApplication;
import com.thuoghtworks.android.challenge.http.services.BattleService;

/**
 * Created by Govind
 */

public class ObjectFactory {
    private static ObjectFactory mObjectFactory;
    private BattleService battleService;

    public static synchronized ObjectFactory getInstance() {
        if (null == mObjectFactory) {
            synchronized (ObjectFactory.class) {
                // Double checked singleton lazy initialization.
                mObjectFactory = new ObjectFactory();
            }
        }
        return mObjectFactory;
    }


    public synchronized BattleService getBattleService() {
        if (null == this.battleService) {
            this.battleService = SingletonRestClient.createService(BattleService.class, BattleApplication.getContext());
        }

        return this.battleService;
    }

}
