package edu.aku.hassannaqvi.uen_ml_validation.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.uen_ml_validation.R;
import edu.aku.hassannaqvi.uen_ml_validation.contracts.KishMWRAContract;
import edu.aku.hassannaqvi.uen_ml_validation.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_ml_validation.core.MainApp;
import edu.aku.hassannaqvi.uen_ml_validation.databinding.ActivitySectionH102Binding;
import edu.aku.hassannaqvi.uen_ml_validation.utils.JSONUtils;
import edu.aku.hassannaqvi.uen_ml_validation.utils.Util;

public class SectionH102Activity extends AppCompatActivity {

    ActivitySectionH102Binding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_h1_02);
        bi.setCallback(this);

        setupSkips();

    }

    private void setupSkips() {


        /*//h121
        bi.h121.setOnCheckedChangeListener((group, checkedId) -> {

            Clear.clearAllFields(bi.fldGrpCVh122);
            Clear.clearAllFields(bi.fldGrpCVh123);
            Clear.clearAllFields(bi.fldGrpCVh124);
            bi.fldGrpCVh122.setVisibility(View.GONE);
            bi.fldGrpCVh123.setVisibility(View.GONE);
            bi.fldGrpCVh124.setVisibility(View.GONE);

            if (checkedId == bi.h121a.getId()) {
                bi.fldGrpCVh122.setVisibility(View.VISIBLE);
                bi.fldGrpCVh123.setVisibility(View.VISIBLE);
            } else if (checkedId == bi.h121b.getId()) {
                bi.fldGrpCVh124.setVisibility(View.VISIBLE);
            }
        });*/


    }

    public void BtnContinue() {
        if (formValidation()) {
            try {
                SaveDraft();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (UpdateDB()) {
                finish();
                startActivity(new Intent(this, SectionH2Activity.class));
            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void BtnEnd() {

        Util.openEndActivity(this);
    }

    private boolean UpdateDB() {

        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        int updcount = db.updatesKishMWRAColumn(KishMWRAContract.SingleKishMWRA.COLUMN_SH1, MainApp.kish.getsH1());
        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();



        try {
            JSONObject s4_merge = JSONUtils.mergeJSONObjects(new JSONObject(MainApp.kish.getsH1()), json);

            MainApp.kish.setsH1(String.valueOf(s4_merge));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.fldGrpSectionH2);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back", Toast.LENGTH_SHORT).show();
    }
}
