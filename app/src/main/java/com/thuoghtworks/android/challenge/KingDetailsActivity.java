package com.thuoghtworks.android.challenge;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.thuoghtworks.android.challenge.db.King;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_king_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("King Details");

        King king = getIntent().getExtras().getParcelable(Constants.KEY_KING);
        initScreen(king);
    }

    private void initScreen(King king) {
        ((TextView) findViewById(R.id.king_name)).setText(king.getName());
        ((TextView) findViewById(R.id.rating)).setText("Highest Rating: "+king.getRating().toString());
        ((TextView) findViewById(R.id.battles_won)).setText(king.getTotal_won_count().toString());
        ((TextView) findViewById(R.id.battles_lost)).setText(king.getTotal_loss_count().toString());
        String strength = "";
        if (king.getTotal_won_count() <= 0) {
            strength = "Never won a battle";
        } else if (king.getAttack_won_count() > king.getDefend_won_count()) {
            strength += "Attack";
        } else if (king.getAttack_won_count() < king.getDefend_won_count()) {
            strength += "Defence";
        } else if (king.getAttack_won_count() == king.getDefend_won_count()) {
            strength += "Both";
        }
        ((TextView) findViewById(R.id.battle_strength)).setText(strength);
        String strengthType = "";
        List<Integer> strengthTypeCounts = new ArrayList<>();
        strengthTypeCounts.add(king.getBtype_ambush_count());
        strengthTypeCounts.add(king.getBtype_pitched_count());
        strengthTypeCounts.add(king.getBtype_razing_count());
        strengthTypeCounts.add(king.getBtype_seige_count());
        Collections.sort(strengthTypeCounts, Collections.<Integer>reverseOrder());

        if(strengthTypeCounts.get(0) == 0) {
            strengthType += "Never_Won ";
        } else if (king.getBtype_ambush_count() == strengthTypeCounts.get(0)) {
            strengthType += "Ambus ";
        } else if (king.getBtype_pitched_count() == strengthTypeCounts.get(0)) {
            strengthType += "Pitched Battle ";
        } else if (king.getBtype_razing_count() == strengthTypeCounts.get(0)) {
            strengthType += "Razing ";
        } else if (king.getBtype_seige_count() == strengthTypeCounts.get(0)) {
            strengthType += "Siege ";
        }
        if(strengthType.trim().contains(" ")) {
            strengthType = strengthType.trim().replace(" ","/");
        }
        ((TextView) findViewById(R.id.type_strength)).setText(strengthType);

    }

}
