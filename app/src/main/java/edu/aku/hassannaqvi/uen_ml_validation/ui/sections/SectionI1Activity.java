package edu.aku.hassannaqvi.uen_ml_validation.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Clear;
import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.aku.hassannaqvi.uen_ml_validation.R;
import edu.aku.hassannaqvi.uen_ml_validation.contracts.ChildContract;
import edu.aku.hassannaqvi.uen_ml_validation.contracts.FamilyMembersContract;
import edu.aku.hassannaqvi.uen_ml_validation.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_ml_validation.core.MainApp;
import edu.aku.hassannaqvi.uen_ml_validation.databinding.ActivitySectionI1Binding;
import edu.aku.hassannaqvi.uen_ml_validation.ui.other.EndingActivity;
import edu.aku.hassannaqvi.uen_ml_validation.utils.Util;
import kotlin.Pair;

import static edu.aku.hassannaqvi.uen_ml_validation.ui.list_activity.FamilyMembersListActivity.mainVModel;

public class SectionI1Activity extends AppCompatActivity {

    ActivitySectionI1Binding bi;
    private FamilyMembersContract fmc_child, res_child;
    private Pair<List<Integer>, List<String>> childLst, resList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_i1);
        bi.setCallback(this);

        setUIComponent();
        setListener();

    }

    private void setUIComponent() {

        childLst = mainVModel.getAllUnder5();

        List<String> childLst = new ArrayList<String>() {
            {
                add("....");
                addAll(SectionI1Activity.this.childLst.getSecond());
            }
        };

        bi.i100.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, childLst));
    }

    private void populateRespondentSpinner() {
        resList = mainVModel.getAllRespondent();
        List<String> reponList = new ArrayList<String>() {
            {
                add("....");
                addAll(SectionI1Activity.this.resList.getSecond());
            }
        };

        bi.i10res.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, reponList));
    }

    private void setListener() {

        bi.i100.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                fmc_child = mainVModel.getMemberInfo(childLst.getFirst().get(bi.i100.getSelectedItemPosition() - 1));
                if (fmc_child.getMother_serial().equals("97")) {
                    bi.respondentSpinner.setVisibility(View.VISIBLE);
                    populateRespondentSpinner();
                } else {
                    bi.respondentSpinner.setVisibility(View.GONE);
                    res_child = mainVModel.getMemberInfo(Integer.valueOf(fmc_child.getSerialno()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bi.i10res.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                res_child = mainVModel.getMemberInfo(resList.getFirst().get(bi.i10res.getSelectedItemPosition() - 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bi.i101.setOnCheckedChangeListener(((radioGroup, i) -> {
            if (i == bi.i101b.getId()) {
                Clear.clearAllFields(bi.fldGrpi01);
            }
        }));

        bi.i201.setOnCheckedChangeListener(((radioGroup, i) -> {
            if (i == bi.i201b.getId()) {
                Clear.clearAllFields(bi.fldGrpi02);
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
                startActivity(new Intent(this, EndingActivity.class));
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
        long rowID = db.addChild(MainApp.child);
        if (rowID > 0) {
            MainApp.child.set_ID(String.valueOf(rowID));
            MainApp.child.setUID(MainApp.child.getDeviceId() + MainApp.child.get_ID());
            db.updatesChildColumn(ChildContract.SingleChild.COLUMN_UID, MainApp.child.getUID());
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void SaveDraft() throws JSONException {

        MainApp.child = new ChildContract();
        MainApp.child.set_UUID(MainApp.fc.get_UID());
        MainApp.child.setDeviceId(MainApp.appInfo.getDeviceID());
        MainApp.child.setDevicetagID(MainApp.fc.getDevicetagID());
        MainApp.child.setFormDate(MainApp.fc.getFormDate());
        MainApp.child.setUser(MainApp.fc.getUser());


        JSONObject json = new JSONObject();

        json.put("hhno", MainApp.fc.getHhno());
        json.put("cluster_no", MainApp.fc.getClusterCode());
        json.put("_luid", MainApp.fc.getLuid());
        json.put("appversion", MainApp.appInfo.getAppVersion());

        if (bi.i101a.isChecked()) {
            json.put("i1_fm_uid", fmc_child.getUid());
            json.put("i1_fm_serial", fmc_child.getSerialno());

            if (bi.respondentSpinner.getVisibility() == View.VISIBLE) {
                json.put("i1_res_fm_uid", res_child.getUid());
                json.put("i1_res_fm_serial", res_child.getSerialno());
            }

            json.put("i100", bi.i100.getSelectedItem().toString());
        }

        json.put("i101", bi.i101a.isChecked() ? "1" :
                bi.i101b.isChecked() ? "2" : "0");

        json.put("i102", bi.i102.getText().toString());

        json.put("i105", bi.i105a.isChecked() ? "1" :
                bi.i105b.isChecked() ? "2" : "0");

        json.put("i125", bi.i125a.isChecked() ? "1" :
                bi.i125b.isChecked() ? "2" : "0");

        json.put("i126a", bi.i126a.isChecked() ? "1" : "0");
        json.put("i126b", bi.i126b.isChecked() ? "2" : "0");
        json.put("i126c", bi.i126c.isChecked() ? "3" : "0");
        json.put("i126d", bi.i126d.isChecked() ? "4" : "0");
        json.put("i126e", bi.i126e.isChecked() ? "5" : "0");

        json.put("i201", bi.i201a.isChecked() ? "1" :
                bi.i201b.isChecked() ? "2" : "0");

        json.put("i202", bi.i202.getText().toString());

        json.put("i207", bi.i207a.isChecked() ? "1" :
                bi.i207b.isChecked() ? "2" : "0");

        json.put("i225", bi.i225a.isChecked() ? "1" :
                bi.i225b.isChecked() ? "2" : "0");

        json.put("i226a", bi.i226a.isChecked() ? "1" : "0");
        json.put("i226b", bi.i226b.isChecked() ? "2" : "0");
        json.put("i226c", bi.i226c.isChecked() ? "3" : "0");
        json.put("i226d", bi.i226d.isChecked() ? "4" : "0");
        json.put("i226e", bi.i226e.isChecked() ? "5" : "0");
        json.put("i226f", bi.i226f.isChecked() ? "6" : "0");

        json.put("j101", bi.j101a.isChecked() ? "1" :
                bi.j101b.isChecked() ? "2" :
                        bi.j101c.isChecked() ? "3" : "0");

        MainApp.child.setsI1(String.valueOf(json));


    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.fldGrpSectioni01);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back", Toast.LENGTH_SHORT).show();
    }
}
