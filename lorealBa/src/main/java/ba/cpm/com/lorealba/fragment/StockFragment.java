package ba.cpm.com.lorealba.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.ProductMaster;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockFragment extends Fragment {
    ExpandableListView lvExp_audit;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CATEGORY_ID = "category_id";

    static ArrayList<ProductMaster> productMasters = new ArrayList<ProductMaster>();
    private static final String ARG_INDEX_ID = "index";
    // TODO: Rename and change types of parameters
    //private int mCategory_id;
    private String mCategory_id;
    private int tabPosition;
    private OnFragmentInteractionListener mListener;
    Lorealba_Database db;
    Context context;
    int minteger = 0;
    //usk
    ArrayList<ProductMaster> listDataHeader;
    ArrayList<ProductMaster> questionList;
    HashMap<ProductMaster, List<ProductMaster>> listDataChild;
    boolean checkflag = true;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();
    ExpandableListAdapter listAdapter;
    FloatingActionButton fab;
    private SharedPreferences preferences;
    String store_cd = "1", visit_date, user_type, username;
    private ViewPager mViewPager;
    String sigature_id;

    public StockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StockFragment.
     */
    // TODO: Rename and change types and number of parameters
    // public static StockFragment newInstance(Integer param1) {
    // public static StockFragment newInstance(String param1) {
    public static StockFragment newInstance(ArrayList<ProductMaster> productMaster, int position) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        // args.putInt(ARG_CATEGORY_ID, param1);
        productMasters = productMaster;
        String axeName = productMasters.get(position).getAxeName();

        // args.putString(productMasters, axeName);
        args.putString(ARG_CATEGORY_ID, axeName);
        args.putInt(ARG_INDEX_ID, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //  mCategory_id = getArguments().getInt(ARG_CATEGORY_ID);
            mCategory_id = getArguments().getString(ARG_CATEGORY_ID);
            tabPosition = getArguments().getInt(ARG_INDEX_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stock, container, false);
        lvExp_audit = (ExpandableListView) rootView.findViewById(R.id.lvExp_audit);
        fab = (FloatingActionButton) rootView.findViewById(R.id.store_fab);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.container);

        stockUiData();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    lvExp_audit.clearFocus();
                lvExp_audit.invalidateViews();
                listAdapter.notifyDataSetChanged();
                //if (validateData(listDataChild, listDataHeader)) {
                db.open();
                db.insertStockData(store_cd, sigature_id, visit_date, mCategory_id, listDataChild, listDataHeader);
                if (tabPosition == productMasters.size() - 1){
                    getActivity().finish();
                }

                mViewPager.setCurrentItem(getItem(+1), true); //getItem(-1) for previous


            }/*else {

                    Snackbar.make(lvExp_audit, "Please fill Sku Stock", Snackbar.LENGTH_SHORT).show();
                }
*/
            //  }
        });

/*        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvExp_audit.clearFocus();
                lvExp_audit.invalidateViews();
                listAdapter.notifyDataSetChanged();
                if (validateData(listDataChild, listDataHeader)) {
                    db.open();
                    db.insertSalesStockData(jcpGetset, listDataChild, listDataHeader);
                    finish();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });
        }



        */
        return rootView;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void stockUiData() {
        db = new Lorealba_Database(getActivity());
        db.open();

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        sigature_id = getActivity().getIntent().getStringExtra(CommonString.KEY_SIGNETURE_ID);
        prepareListData();
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        lvExp_audit.setAdapter(listAdapter);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            lvExp_audit.expandGroup(i);
        }


        lvExp_audit.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lvExp_audit.invalidate();

                int lastItem = firstVisibleItem + visibleItemCount;

                if (firstVisibleItem == 0) {
                    fab.show();//.setVisibility(View.VISIBLE);
                } else if (lastItem == totalItemCount) {
                    fab.hide();//setVisibility(View.INVISIBLE);
                } else {
                    fab.show();//setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    getActivity().getCurrentFocus().clearFocus();
                }
                lvExp_audit.invalidateViews();
            }
        });

        // Listview Group click listener
        lvExp_audit.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;
            }
        });

        // Listview Group expanded listener
        lvExp_audit.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getWindow().getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    getActivity().getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview Group collasped listener
        lvExp_audit.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getWindow().getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    getActivity().getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview on child click listener
        lvExp_audit.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });

    }

    private void prepareListData() {

        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        listDataHeader = db.getcategory_wise_brand_fromStockData(mCategory_id, sigature_id);
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                questionList = db.getStockInsertedData(store_cd, listDataHeader.get(i).getSubAxeName(), sigature_id);
                if (questionList.size() > 0) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.edit_txt));
                } else {
                    questionList = db.getbrand_wise_sku_fromStockData(listDataHeader.get(i).getSubAxeName(), sigature_id, store_cd);
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<ProductMaster> _listDataHeader;
        private HashMap<ProductMaster, List<ProductMaster>> _listDataChild;

        public ExpandableListAdapter(Context context, List<ProductMaster> listDataHeader,
                                     HashMap<ProductMaster, List<ProductMaster>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final ProductMaster childText = (ProductMaster) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_stock_entory, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.ed_Stock = convertView.findViewById(R.id.ed_Stock);
                holder.mrp = convertView.findViewById(R.id.mrp);
                holder.stock_img_plus = convertView.findViewById(R.id.stock_img_plus);
                holder.stock_img_minus = convertView.findViewById(R.id.stock_img_minus);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TextView txtListChild = convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getProductName());
            final ViewHolder finalHolder = holder;

            holder.stock_img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listAdapter.notifyDataSetChanged();
                    int minteger = childText.getStock();
                    ++minteger;

                    if (minteger <= 10) {
                        childText.setStock(minteger);
                    } else {
                        Snackbar.make(lvExp_audit, "Stock cannot be greater than 10.", Snackbar.LENGTH_SHORT).show();
                    }


                }
            });

            holder.stock_img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listAdapter.notifyDataSetChanged();
                    int minteger = childText.getStock();
                    --minteger;

                    if (minteger >= 0) {
                        childText.setStock(minteger);
                    } else {
                        Snackbar.make(lvExp_audit, "Stock cannot be less than 0.", Snackbar.LENGTH_SHORT).show();
                    }

                }
            });
            holder.ed_Stock.setText(childText.getStock() + "");
            holder.mrp.setText(" MRP = " + childText.getMrp() + "");


           /* if (!checkflag) {
                boolean tempflag = false;
                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getStock().equals("")) {
                    tempflag = true;
                }
                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            } else {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            }*/

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
            final ProductMaster headerTitle = (ProductMaster) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_stock_entry, null);
            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setText(headerTitle.getSubAxeName());
            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryLight));
                }
            } else {
                lblListHeader.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryLight));
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

        TextView ed_Stock, mrp;
        Button stock_img_plus, stock_img_minus;
        CardView cardView;
    }

    boolean validateData(HashMap<ProductMaster, List<ProductMaster>> listDataChild2,
                         List<ProductMaster> listDataHeader2) {
        boolean flag = false;
        checkHeaderArray.clear();
        loop1:
        for (int i = 0; i < listDataHeader2.size(); i++) {

            for (int j = 0; j < listDataChild2.get(listDataHeader.get(i)).size(); j++) {
                int stock = listDataChild.get(listDataHeader.get(i)).get(j).getStock();
                // if (stock == null || stock.equalsIgnoreCase("")) {
                if (stock == 0) {
                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                    }
                    flag = false;
                    break;
                } else {
                    flag = true;
                }

            }
            if (!flag) {
                break;
            }
        }
        if (flag) {
            return checkflag = true;
        } else {

            return checkflag = false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        db.open();

        db.insertStockData(store_cd, sigature_id, visit_date, mCategory_id, listDataChild, listDataHeader);

    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }
}
