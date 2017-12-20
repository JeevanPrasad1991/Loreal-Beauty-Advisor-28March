package intelre.cpm.com.intelre.dailyentry;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestionGetterSetter;

public class StoreAuditActivity extends AppCompatActivity {
    INTEL_RE_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username, Error_Message;
    ExpandableListView lvExp_audit;
    FloatingActionButton storeAudit_fab;
    List<AuditQuestion> listDataHeader;
    List<AuditQuestion> questionList;
    HashMap<AuditQuestion, List<AuditQuestion>> listDataChild;
    ExpandableListAdapter listAdapter;
    static int grp_position = -1;
    String _pathforcheck, _path, img1 = "";
    boolean checkflag = true;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_audit);
        auditUI();
        //save audit data
        storeAudit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp_audit.clearFocus();
                lvExp_audit.invalidateViews();
                listAdapter.notifyDataSetChanged();
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreAuditActivity.this);
                    builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertStoreAuditData(store_cd, listDataChild, listDataHeader);
                            finish();
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Snackbar.make(lvExp_audit, Error_Message, Snackbar.LENGTH_LONG).show();
                }

            }
        });

    }

    private void auditUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvExp_audit = findViewById(R.id.lvExp_audit);
        storeAudit_fab = findViewById(R.id.storeAudit_fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        getSupportActionBar().setTitle("Store Audit -" + visit_date);
        db = new INTEL_RE_DB(this);
        db.open();
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        lvExp_audit.setAdapter(listAdapter);
    }

    private void prepareListData() {
        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        listDataHeader = db.getStoreAuditHeaderData();
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                questionList = db.getStoreAuditInsertedData(store_cd, listDataHeader.get(i).getQuestionCategoryId());
                if (questionList.size() > 0) {
                    // storeAudit_fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getStoreAuditChildData(listDataHeader.get(i).getQuestionCategoryId());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<AuditQuestion> _listDataHeader;
        private HashMap<AuditQuestion, List<AuditQuestion>> _listDataChild;

        public ExpandableListAdapter(Context context, List<AuditQuestion> listDataHeader,
                                     HashMap<AuditQuestion, List<AuditQuestion>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final AuditQuestion childText = (AuditQuestion) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_parent_storeaudit, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.audit_spin = convertView.findViewById(R.id.audit_spin);
                holder.audit_came = convertView.findViewById(R.id.audit_came);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TextView txtListChild = convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getQuestion());

            if (childText.getImageAllow().equalsIgnoreCase("true")) {
                holder.audit_came.setVisibility(View.VISIBLE);
            } else {
                holder.audit_came.setVisibility(View.GONE);
            }

            holder.audit_came.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    grp_position = groupPosition;
                    _pathforcheck = store_cd + "_" +
                            childText.getQuestionId() + "_AUDITIMG_" + visit_date.replace("/", "") + "_"
                            + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity(0);
                }
            });


            if (!img1.equalsIgnoreCase("")) {
                if (groupPosition == grp_position) {
                    childText.setAudit_cam(img1);
                    img1 = "";
                }
            }
            if (childText.getAudit_cam() != null && !childText.getAudit_cam().equals("")) {
                holder.audit_came.setImageResource(R.mipmap.camera_green);
            }

            //for reason spinner
            final ArrayList<AuditQuestion> reason_list = db.getauditAnswerData(childText.getQuestionId().toString());
            AuditQuestion non = new AuditQuestion();
            non.setAnswer("-Select Answer-");
            non.setAnswerId(0);
            reason_list.add(0, non);
            holder.audit_spin.setAdapter(new ReasonSpinnerAdapter(_context, R.layout.spinner_text_view, reason_list));

            for (int i = 0; i < reason_list.size(); i++) {
                if (reason_list.get(i).getAnswerId().toString().equals(childText.getCurrectanswerCd().toString())) {
                    holder.audit_spin.setSelection(i);
                    break;
                }
            }
            holder.audit_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (pos != 0) {
                        AuditQuestion ans = reason_list.get(pos);
                        childText.setCurrectanswerCd(ans.getAnswerId().toString());
                        childText.setCurrectanswer(ans.getAnswer().toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final AuditQuestion headerTitle = (AuditQuestion) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_storeaudit, null);
            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setText(headerTitle.getQuestionCategory());
            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            } else {
                lblListHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class ViewHolder {
        Spinner audit_spin;
        CardView cardView;
        ImageView audit_came;
    }


    boolean validateData(HashMap<AuditQuestion, List<AuditQuestion>> listDataChild2, List<AuditQuestion> listDataHeader2) {
        boolean flag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String spinValue = listDataChild2.get(listDataHeader2.get(i)).get(j).getCurrectanswerCd();
                String cameraFlag = listDataChild2.get(listDataHeader2.get(i)).get(j).getImageAllow();
                String audit_img = listDataChild2.get(listDataHeader2.get(i)).get(j).getAudit_cam();
                if (spinValue.equals("0")) {
                    flag = false;
                    checkflag = false;
                    Error_Message = CommonString.KEY_FOR_SPINNER_DROP_DOWN;
                    break;
                } else if (cameraFlag.equalsIgnoreCase("true") &&
                        audit_img.equals("")) {
                    checkflag = false;
                    flag = false;
                    Error_Message = CommonString.KEY_FOR_CAMERA_C;
                    break;
                } else {
                    checkflag = true;
                    flag = true;
                }
            }

            if (checkflag == false) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                break;
            }

        }
        listAdapter.notifyDataSetChanged();
        return checkflag;
    }


    public class ReasonSpinnerAdapter extends ArrayAdapter<AuditQuestion> {
        List<AuditQuestion> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<AuditQuestion> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            AuditQuestion cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getAnswer());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            AuditQuestion cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getAnswer());

            return view;
        }

    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());


        return cdate;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);


        switch (requestCode) {
            case 0:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                            img1 = _pathforcheck;
                            lvExp_audit.invalidateViews();
                            _pathforcheck = "";
                        }
                    }
                } else {
                    Log.i("MakeMachine", "User cancelled");
                    _pathforcheck = "";
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void startCameraActivity(int position) {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
