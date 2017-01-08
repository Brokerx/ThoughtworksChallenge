package com.thuoghtworks.android.challenge.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig battleDaoConfig;
    private final DaoConfig kingDaoConfig;

    private final BattleDao battleDao;
    private final KingDao kingDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        battleDaoConfig = daoConfigMap.get(BattleDao.class).clone();
        battleDaoConfig.initIdentityScope(type);

        kingDaoConfig = daoConfigMap.get(KingDao.class).clone();
        kingDaoConfig.initIdentityScope(type);

        battleDao = new BattleDao(battleDaoConfig, this);
        kingDao = new KingDao(kingDaoConfig, this);

        registerDao(Battle.class, battleDao);
        registerDao(King.class, kingDao);
    }
    
    public void clear() {
        battleDaoConfig.getIdentityScope().clear();
        kingDaoConfig.getIdentityScope().clear();
    }

    public BattleDao getBattleDao() {
        return battleDao;
    }

    public KingDao getKingDao() {
        return kingDao;
    }

}
