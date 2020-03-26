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
import edu.aku.hassannaqvi.uen_ml_validation.databinding.ActivitySectionH1Binding;
import edu.aku.hassannaqvi.uen_ml_validation.utils.Util;

public class SectionH1Activity extends AppCompatActivity {

    ActivitySectionH1Binding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_h1);
        bi.setCallback(this);

        setupSkips();

    }

    private void setupSkips() {

        /*//h102
        bi.h102.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId != bi.h102a.getId()) {
                bi.fldGrpCVh103.setVisibility(View.VISIBLE);
                bi.fldGrpCVh104.setVisibility(View.VISIBLE);
            } else {
                Clear.clearAllFields(bi.fldGrpCVh103);
                Clear.clearAllFields(bi.fldGrpCVh104);
                bi.fldGrpCVh103.setVisibility(View.GONE);
                bi.fldGrpCVh104.setVisibility(View.GONE);
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
                startActivity(new Intent(this, SectionH102Activity.class));
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

        json.put("h112", bi.h112a.isChecked() ? "1"
                : bi.h112b.isChecked() ? "2"
                : bi.h112c.isChecked() ? "3"
                : "0");

        json.put("h121", bi.h121a.isChecked() ? "1" :
                bi.h121b.isChecked() ? "2" : "0");

        json.put("h132", bi.h132a.isChecked() ? "1" :
                bi.h132b.isChecked() ? "2" :
                        bi.h132c.isChecked() ? "3" : "0");

        json.put("h137", bi.h137a.isChecked() ? "1" :
                bi.h137b.isChecked() ? "2" : "0");

        json.put("h137aa", bi.h137aaa.isChecked() ? "1" :
                bi.h137aab.isChecked() ? "2" :
                        bi.h137aac.isChecked() ? "3" :
                                bi.h137aad.isChecked() ? "4" :
                                        bi.h137aae.isChecked() ? "5" : "0");

        json.put("h137bb", bi.h137bba.isChecked() ? "1" :
                bi.h137bbb.isChecked() ? "2" :
                        bi.h137bbc.isChecked() ? "3" :
                                bi.h137bbd.isChecked() ? "4" :
                                        bi.h137bbe.isChecked() ? "5" :
                                                bi.h137bbf.isChecked() ? "6" :
                                                        bi.h137bbg.isChecked() ? "7" :
                                                                bi.h137bb96.isChecked() ? "96" : "0");
        json.put("h137bb96x", bi.h137bb96x.getText().toString());

        json.put("h202", bi.h202a.isChecked() ? "1" :
                bi.h202b.isChecked() ? "2" : "0");

        json.put("h204", bi.h204a.isChecked() ? "1" :
                bi.h204b.isChecked() ? "2" :
                        bi.h204c.isChecked() ? "3" :
                                bi.h204d.isChecked() ? "4" : "0");

        json.put("h205", bi.h205a.isChecked() ? "1" :
                bi.h205b.isChecked() ? "2" :
                        bi.h205c.isChecked() ? "3" :
                                bi.h205d.isChecked() ? "4" :
                                        bi.h205e.isChecked() ? "5" : "0");

        json.put("h209", bi.h209a.isChecked() ? "1" :
                bi.h209b.isChecked() ? "2" : "0");

        json.put("h214", bi.h214a.isChecked() ? "1" :
                bi.h214b.isChecked() ? "2" :
                        bi.h214c.isChecked() ? "3" :
                                bi.h214d.isChecked() ? "4" : "0");


        MainApp.kish.setsH1(String.valueOf(json));
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back", Toast.LENGTH_SHORT).show();
    }

}
