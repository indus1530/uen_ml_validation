package edu.aku.hassannaqvi.uen_ml_validation.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Clear;
import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.uen_ml_validation.R;
import edu.aku.hassannaqvi.uen_ml_validation.contracts.KishMWRAContract;
import edu.aku.hassannaqvi.uen_ml_validation.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_ml_validation.core.MainApp;
import edu.aku.hassannaqvi.uen_ml_validation.databinding.ActivitySectionFBinding;
import edu.aku.hassannaqvi.uen_ml_validation.utils.Util;

public class SectionFActivity extends AppCompatActivity {

    ActivitySectionFBinding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_f);
        bi.setCallback(this);

        setUIComponent();
    }

    void setUIComponent() {

        /*bi.f101Name.setText(new StringBuilder(MainApp.selectedKishMWRA.getName().toUpperCase()).append("\n")
                .append(getResources().getString(R.string.d101))
                .append(":")
                .append(MainApp.selectedKishMWRA.getSerialno()));*/

        bi.f101.setOnCheckedChangeListener(((radioGroup, i) -> {
            if (i == bi.f101b.getId()) {
                Clear.clearAllFields(bi.fldGrpSectionFA);
            }
        }));
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
                startActivity(new Intent(this, SectionGActivity.class));
            }

        }
    }

    private boolean UpdateDB() {
        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        long updcount = db.addKishMWRA(MainApp.kish);
        MainApp.kish.set_ID(String.valueOf(updcount));
        if (updcount > 0) {
            MainApp.kish.setUID(MainApp.kish.getDeviceId() + MainApp.kish.get_ID());
            db.updatesKishMWRAColumn(KishMWRAContract.SingleKishMWRA.COLUMN_UID, MainApp.kish.getUID());
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void SaveDraft() throws JSONException {

        MainApp.kish = new KishMWRAContract();
        MainApp.kish.set_UUID(MainApp.fc.get_UID());
        MainApp.kish.setDeviceId(MainApp.appInfo.getDeviceID());
        MainApp.kish.setDevicetagID(MainApp.fc.getDevicetagID());
        MainApp.kish.setFormDate(MainApp.fc.getFormDate());
        MainApp.kish.setUser(MainApp.fc.getUser());

        JSONObject json = new JSONObject();

        json.put("fm_uid", MainApp.selectedKishMWRA.getUid());
        json.put("fm_serial", MainApp.selectedKishMWRA.getSerialno());
        json.put("hhno", MainApp.fc.getHhno());
        json.put("cluster_no", MainApp.fc.getClusterCode());
        json.put("_luid", MainApp.fc.getLuid());
        json.put("appversion", MainApp.appInfo.getAppVersion());

        json.put("f101", bi.f101a.isChecked() ? "1" :
                bi.f101b.isChecked() ? "2" : "0");

        json.put("f102a", bi.f102a.isChecked() ? "1" : "0");
        json.put("f102b", bi.f102b.isChecked() ? "2" : "0");
        json.put("f102c", bi.f102c.isChecked() ? "3" : "0");
        json.put("f102d", bi.f102d.isChecked() ? "4" : "0");

        json.put("f107", bi.f107a.isChecked() ? "1"
                : bi.f107b.isChecked() ? "2"
                : bi.f10796.isChecked() ? "96"
                : "0");
        json.put("f10796x", bi.f10796x.getText().toString());

        json.put("f112", bi.f112a.isChecked() ? "1" :
                bi.f112b.isChecked() ? "2" :
                        bi.f112c.isChecked() ? "98" : "0");

        json.put("f114", bi.f114a.isChecked() ? "1" :
                bi.f114b.isChecked() ? "2" : "0");

        json.put("f121", bi.f121a.isChecked() ? "1" :
                bi.f121b.isChecked() ? "2" : "0");


        MainApp.kish.setsF(String.valueOf(json));

    }

    private boolean formValidation() {

        return Validator.emptyCheckingContainer(this, bi.fldGrpSectionF);

    }

    public void BtnEnd() {

        Util.openEndActivity(this);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back", Toast.LENGTH_SHORT).show();
    }

}
