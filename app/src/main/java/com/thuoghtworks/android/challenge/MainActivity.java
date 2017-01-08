package com.thuoghtworks.android.challenge;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.thuoghtworks.android.challenge.db.Battle;
import com.thuoghtworks.android.challenge.db.BattleDao;
import com.thuoghtworks.android.challenge.db.King;
import com.thuoghtworks.android.challenge.db.KingDao;
import com.thuoghtworks.android.challenge.fragments.AboutFragment;
import com.thuoghtworks.android.challenge.fragments.AllBattles;
import com.thuoghtworks.android.challenge.fragments.PortfolioFragment;
import com.thuoghtworks.android.challenge.fragments.SummaryFragment;
import com.thuoghtworks.android.challenge.http.ObjectFactory;
import com.thuoghtworks.android.challenge.utils.SharedPreferencesUtil;
import com.thuoghtworks.android.challenge.widget.AppProgressDialog;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.Fragment currentFragment = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GOT — Westeros at war!");
        mContext = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initScreen();
    }

    private void initScreen() {
        boolean isDBInitialized = SharedPreferencesUtil.getSharedPreferencesBoolean(mContext, Constants.KEY_IS_DB_INITIALIZED, false);
        if(!isDBInitialized) {
            getAllBattles();
            return;
        }
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = new SummaryFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment).commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                KingDao kingDao = BattleApplication.getDaoSession().getKingDao();
                kingDao.deleteAll();
                BattleDao battleDao = BattleApplication.getDaoSession().getBattleDao();
                battleDao.deleteAll();
                getAllBattles();
                return true;
        }
        return false;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_summary) {
            android.support.v4.app.Fragment fragment = new SummaryFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment).commit();
        } else if (id == R.id.nav_battles) {
            android.support.v4.app.Fragment fragment = new AllBattles();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment).commit();
        } else if (id == R.id.nav_about) {
            android.support.v4.app.Fragment fragment = new AboutFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment).commit();
        } else if (id == R.id.nav_portfolio) {
            android.support.v4.app.Fragment fragment = new PortfolioFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getAllBattles() {
        final Dialog dialog = AppProgressDialog.show(mContext);

        ObjectFactory.getInstance().getBattleService().getAllBattles(new Callback<List<Battle>>() {
            @Override
            public void success(List<Battle> battles, Response response) {
                if (battles != null && !battles.isEmpty()) {
                    populateBattleRatings(battles);
                    SharedPreferencesUtil.putSharedPreferencesBoolean(mContext, Constants.KEY_IS_DB_INITIALIZED, true);
                    initScreen();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void populateBattleRatings(List<Battle> battles) {
        /*String battleString = battles.toString();
        Log.d("BATTLES", battleString);*/
        BattleDao battleDao = BattleApplication.getDaoSession().getBattleDao();
        KingDao kingDao = BattleApplication.getDaoSession().getKingDao();
        for (Battle battle : battles) {
            King attacker = getKingByName(kingDao, battle.getAttacker_king());
            float attackerCurrentRating = 0;
            float defenderCurrentRating = 0;
            Long attackerID, defenderID;
            if (attacker == null) {
                attacker = new King(battle.getAttacker_king());
                attackerID = kingDao.insert(attacker);
                attacker.setId(attackerID);
            } else {
                attackerID = attacker.getId();
                attackerCurrentRating = attacker.getRating();
            }
            battle.setAttacker_king_id(attackerID.intValue());
            King defender = getKingByName(kingDao, battle.getDefender_king());
            if (defender == null) {
                defender = new King(battle.getDefender_king());
                defenderID = kingDao.insert(defender);
                defender.setId(defenderID);
            } else {
                defenderID = defender.getId();
                defenderCurrentRating = defender.getRating();
            }
            battle.setDefender_king_id(defenderID.intValue());
            Double[] ratings = computeELORating(attackerCurrentRating, defenderCurrentRating, battle);
            attacker.setRating(ratings[0].floatValue());
            defender.setRating(ratings[1].floatValue());

            if(battle.getAttacker_outcome().equalsIgnoreCase("win")) {
                battle.setWinner_king_id(attackerID.intValue());
                attacker.setTotal_won_count(attacker.getAttack_won_count()+1);
                defender.setTotal_loss_count(defender.getTotal_loss_count()+1);
                attacker.setAttack_won_count(attacker.getAttack_won_count()+1);
                attacker = populateBattleStrengthCount(attacker, battle);
            } else if(battle.getAttacker_outcome().equalsIgnoreCase("draw")) {
                // draw
            } else {
                battle.setWinner_king_id(defenderID.intValue());
                defender.setTotal_won_count(defender.getAttack_won_count()+1);
                attacker.setTotal_loss_count(attacker.getTotal_loss_count()+1);
                defender.setDefend_won_count(defender.getDefend_won_count()+1);
                defender = populateBattleStrengthCount(defender, battle);
            }
            kingDao.update(attacker);
            kingDao.update(defender);
            battleDao.insert(battle);
        }

        /*List<Battle> battleList = battleDao.loadAll();
        List<King> kings = kingDao.loadAll();
        for(Battle battle: battleList) {
            Log.d("INSERTED Battle ", battle.toString());
        }
        for (King king : kings) {
            Log.d("INSERTED Battle ", king.toString());
        }*/

    }

    private King populateBattleStrengthCount(King king, Battle battle) {
        if(battle.getBattle_type().equalsIgnoreCase("pitched battle")) {
            king.setBtype_pitched_count(king.getBtype_pitched_count()+1);
        } else if(battle.getBattle_type().equalsIgnoreCase("ambush")) {
            king.setBtype_ambush_count(king.getBtype_ambush_count()+1);
        } else if(battle.getBattle_type().equalsIgnoreCase("siege")) {
            king.setBtype_seige_count(king.getBtype_seige_count()+1);
        } else if(battle.getBattle_type().equalsIgnoreCase("razing")) {
            king.setBtype_razing_count(king.getBtype_razing_count()+1);
        }

        return king;
    }

    private Double[] computeELORating(float attackerCurrentRating, float defenderCurrentRating, Battle battle) {
        int n = 400, kFactor = 32; //Assumption Or given in prob statement
        /*The first step is to compute the transformed rating for each King:
        R(1) = 10 raised to the power r(1)/400
        R(2) = 10 raised to the power r(2)/400
        */
        double attackerTransformedRating = Math.pow(10, (attackerCurrentRating / n));
        double defenderTransformedRating = Math.pow(10, (attackerCurrentRating / n));

        /* STEP: 2  we calculate the expected score for each King:
        E(1) = R(1) / (R(1) + R(2))
        E(2) = R(2) / (R(1) + R(2))
        */
        double attackerExpectedRating = attackerTransformedRating / (attackerTransformedRating + defenderTransformedRating);
        double defenderExpectedRating = defenderTransformedRating / (defenderTransformedRating + attackerTransformedRating);

        /* STEP: 3 Compute actual score in current battle
            S(1) = 1 if King 1 wins / 0.5 if draw / 0 if King 2 wins
            S(2) = 0 if King 1 wins / 0.5 if draw / 1 if King 2 wins
         */
        double attackerActualScore = 0,defenderActualScore=0;
        if(battle.getAttacker_outcome().equalsIgnoreCase("win")) {
            attackerActualScore = 1;
            defenderActualScore = 0;
        } else if(battle.getAttacker_outcome().equalsIgnoreCase("draw")) {
            attackerActualScore = 0.5;
            defenderActualScore = 0.5;
        } else {
            attackerActualScore = 0;
            defenderActualScore = 1;
        }

        /*STEP: 4 Now we can put it all together and  find out the updated Elo-rating for each King:
            r'(1) = r(1) + K * (S(1) – E(1))
            r'(2) = r(2) + K * (S(2) – E(2))
         */

        double attackerFinalRating = attackerCurrentRating+(kFactor*(attackerActualScore - attackerExpectedRating));
        double defenderFinalRating = defenderCurrentRating+(kFactor*(defenderActualScore - defenderExpectedRating));
        Double[] finalRatings = {attackerFinalRating, defenderFinalRating};

        return finalRatings;
    }

    private King getKingByName(KingDao kingDao, String kingName) {
        List<King> kings = kingDao.queryBuilder()
                .where(KingDao.Properties.Name.eq(kingName))
                .list();
        if (kings != null && kings.size() > 0) {
            return kings.get(0);
        }
        return null;
    }

}
