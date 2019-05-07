package ba.cpm.com.lorealba.Database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.delegates.CoverageBean;
import ba.cpm.com.lorealba.gettersetter.GeotaggingBeans;
import ba.cpm.com.lorealba.gettersetter.GroomingGetterSetter;
import ba.cpm.com.lorealba.gettersetter.HeaderGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.AuditQuestion;
import ba.cpm.com.lorealba.gsonGetterSetter.ConsumerSalesHistory;
import ba.cpm.com.lorealba.gsonGetterSetter.DashboardDataGetter;
import ba.cpm.com.lorealba.gsonGetterSetter.InvoiceGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.InwardSalesPO;
import ba.cpm.com.lorealba.gsonGetterSetter.JCPGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.JourneyPlan;
import ba.cpm.com.lorealba.gsonGetterSetter.NonPromotionReason;
import ba.cpm.com.lorealba.gsonGetterSetter.NonStockReason;
import ba.cpm.com.lorealba.gsonGetterSetter.NonStockReasonGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.NonWorkingReason;
import ba.cpm.com.lorealba.gsonGetterSetter.NonWorkingReasonGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.NotificationData;
import ba.cpm.com.lorealba.gsonGetterSetter.ProductMaster;
import ba.cpm.com.lorealba.gsonGetterSetter.PromotionMaster;
import ba.cpm.com.lorealba.gsonGetterSetter.StockDataGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockDatum;
import ba.cpm.com.lorealba.gsonGetterSetter.StockPwpGwpDataGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockPwpGwpDatum;
import ba.cpm.com.lorealba.gsonGetterSetter.StockSampleDataGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockSampleDatum;
import ba.cpm.com.lorealba.gsonGetterSetter.StockTesterDataGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockTesterDatum;
import ba.cpm.com.lorealba.gsonGetterSetter.TableStructureGetterSetter;

/**
 * /**
 * Created by jeevanp on 15-12-2017.
 */

public class Lorealba_Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "LorealBa_Data";
    public static final int DATABASE_VERSION = 3;
    private SQLiteDatabase db;
    Context context;

    public Lorealba_Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void open() {
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //jeevan
            db.execSQL(CommonString.CREATE_TABLE_COVERAGE_DATA);
            db.execSQL(CommonString.Create_Table_Sale_Tracking);
            db.execSQL(CommonString.Create_Table_promotion);
            db.execSQL(CommonString.CREATE_TABLE_ATTENDENCE_TABLE);
            db.execSQL(CommonString.CREATE_TABLE_STORE_GEOTAGGING);

            /////////////////////USK
            db.execSQL(CommonString.CREATE_TABLE_FOCUS_PRODUCT_OPENINGHEADER_DATA);
            db.execSQL(CommonString.CREATE_TABLE_FOCUS_PRODUCT_STOCK_DATA);
            db.execSQL(CommonString.CREATE_TABLE_INWARD_REASON_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STOCK_TESTER_DATA_OPENINGHEADER_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STOCK_TESTER_CHILD_DATA);
            db.execSQL(CommonString.CREATE_TESTER_IMAGE_DATA);
            db.execSQL(CommonString.CREATE_TABLE_NOTIFICATION_DATA);
            db.execSQL(CommonString.CREATE_TABLE_ISTORE_PWP_GWP_DATA);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteSpecificStoreData(String storeid) {
        try {
            db.delete(CommonString.TABLE_COVERAGE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteAllTables() {
        try {
            db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
            db.delete(CommonString.TABLE_STORE_GEOTAGGING, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePreviousUploadedData(String visit_date) {
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from COVERAGE_DATA where VISIT_DATE < '" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_GEOTAGGING, null, null);
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d(" Coverage Data!!!!!", e.toString());

        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int createtable(String sqltext) {
        try {
            db.execSQL(sqltext);
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }


    public boolean insertJCPData(JCPGetterSetter data) {
        db.delete("Journey_Plan", null, null);
        List<JourneyPlan> jcpList = data.getJourneyPlan();

        ContentValues values = new ContentValues();
        try {
            if (jcpList.size() == 0) {
                return false;
            }

            for (int i = 0; i < jcpList.size(); i++) {

                values.put("StoreId", jcpList.get(i).getStoreId());
                values.put("CounterId", jcpList.get(i).getCounterId());
                values.put("VisitDate", jcpList.get(i).getVisitDate());
                values.put("StoreName", jcpList.get(i).getStoreName());
                values.put("DistributorName", jcpList.get(i).getDistributorName());
                values.put("Address", jcpList.get(i).getAddress());
                values.put("Landmark", jcpList.get(i).getLandmark());
                values.put("Pincode", jcpList.get(i).getPincode());
                values.put("ContactPerson", jcpList.get(i).getContactPerson());
                values.put("ContactNo", jcpList.get(i).getContactNo());
                values.put("CityName", jcpList.get(i).getCityName());
                values.put("StoreTypeName", jcpList.get(i).getStoreTypeName());
                values.put("StoreCategoryName", jcpList.get(i).getStoreCategoryName());
                values.put("StateId", jcpList.get(i).getStateId());
                values.put("StoreTypeId", jcpList.get(i).getStoreTypeId());
                values.put("ClassificationId", jcpList.get(i).getClassificationId());
                values.put("StoreCategoryId", jcpList.get(i).getStoreCategoryId());
                values.put("ReasonId", jcpList.get(i).getReasonId());
                values.put("Upload_Status", jcpList.get(i).getUploadStatus());
                values.put("Geo_Tag", jcpList.get(i).getGeoTag());
                values.put("DistributorId", jcpList.get(i).getDistributorId());
                values.put("GeoFencing", jcpList.get(i).getGeoFencing());
                values.put("StoreCode", jcpList.get(i).getStoreCode());
                values.put("ClassificationName", jcpList.get(i).getClassificationName());
                values.put("Latitude", jcpList.get(i).getLatitude());
                values.put("Longitude", jcpList.get(i).getLongitude());
                long id = db.insert("Journey_Plan", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Exception in Jcp", ex.toString());
            return false;
        }
    }


    public boolean insertproductmasterdata(TableStructureGetterSetter data) {
        db.delete("Product_Master", null, null);
        List<ProductMaster> productList = data.getProductMaster();

        ContentValues values = new ContentValues();
        try {
            if (productList.size() == 0) {
                return false;
            }
            for (int i = 0; i < productList.size(); i++) {
                values.put("ProductId", productList.get(i).getProductId());
                values.put("LorealCode", productList.get(i).getLorealCode());
                values.put("ProductName", productList.get(i).getProductName());
                values.put("ProductType", productList.get(i).getProductType());
                values.put("NuanceId", productList.get(i).getNuanceId());
                values.put("NuanceName", productList.get(i).getNuanceName());
                values.put("SubAxeId", productList.get(i).getSubAxeId());
                values.put("SubAxeName", productList.get(i).getSubAxeName());
                values.put("AxeId", productList.get(i).getAxeId());
                values.put("AxeName", productList.get(i).getAxeName());
                values.put("ReferenceId", productList.get(i).getReferenceId());
                values.put("ReferenceName", productList.get(i).getReferenceName());
                values.put("SubBrandId", productList.get(i).getSubBrandId());
                values.put("SubBrandName", productList.get(i).getSubBrandName());
                values.put("BrandId", productList.get(i).getBrandId());
                values.put("BrandName", productList.get(i).getBrandName());
                values.put("SignatureId", productList.get(i).getSignatureId());
                values.put("SignatureName", productList.get(i).getSignatureName());
                values.put("EanCode", productList.get(i).getEanCode());
                values.put("Mrp", productList.get(i).getMrp());
                long id = db.insert("Product_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Exception in Jcp", ex.toString());
            return false;
        }
    }

    public ArrayList<ProductMaster> getcategory_fromproduct() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select distinct SubAxeName from Product_Master", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setSubAxeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SubAxeName")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<ProductMaster> getcategory_wise_brand_fromproduct(String category_name) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select distinct BrandName,BrandId from Product_Master where SubAxeName='" + category_name + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setBrandName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BrandName")));
                    sb.setBrandId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("BrandId")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<ProductMaster> getbrand_wise_sku_fromproduct(String brand_Id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select distinct ProductName,ProductId,EanCode,Mrp from Product_Master where BrandId='" + brand_Id + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setEanCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("EanCode")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<ProductMaster> getsku_fromproductusing_eancode(String ean_code) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select distinct ProductName,ProductId,EanCode,Mrp from Product_Master where EanCode='" + ean_code + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setEanCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("EanCode")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }
        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public boolean insertNonWorkingData(NonWorkingReasonGetterSetter nonWorkingdata) {
        db.delete("Non_Working_Reason", null, null);
        ContentValues values = new ContentValues();
        List<NonWorkingReason> data = nonWorkingdata.getNonWorkingReason();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("ReasonId", data.get(i).getReasonId());
                values.put("Reason", data.get(i).getReason());
                values.put("Entry_Allow", data.get(i).getEntryAllow());
                values.put("Image_Allow", data.get(i).getImageAllow());
                values.put("GPS_Mandatory", data.get(i).getGPSMandatory());

                long id = db.insert("Non_Working_Reason", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }


    public ArrayList<JourneyPlan> getStoreData(String date) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM Journey_Plan WHERE VisitDate ='" + date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();
                    sb.setStoreId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreId"))));
                    sb.setCounterId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("CounterId"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("VisitDate"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreName")));
                    sb.setDistributorName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DistributorName")));
                    sb.setAddress((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));

                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ContactPerson")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ContactNo")));
                    sb.setCityName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CityName")));

                    sb.setStoreTypeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreTypeName")));
                    sb.setStoreCategoryName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreCategoryName")));
                    sb.setStateId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StateId")));

                    sb.setStoreTypeId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreTypeId"))));
                    sb.setStoreCategoryId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreCategoryId"))));


                    sb.setReasonId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ReasonId"))));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setDistributorId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("DistributorId")));


                    sb.setClassificationId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ClassificationId"))));
                    sb.setLatitude(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Latitude")));
                    sb.setLongitude(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Longitude")));
                    sb.setGeoFencing(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("GeoFencing")));
                    sb.setStoreCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreCode")));
                    sb.setClassificationName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ClassificationName")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }

    //jeevan   nmjnmn,
    @SuppressLint("LongLogTag")
    public boolean isCoverageDataFilled(String visit_date) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM COVERAGE_DATA " + "where " + CommonString.KEY_VISIT_DATE + "<>'" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }

    //jeevan   nmjnmn,
    @SuppressLint("LongLogTag")
    public long InsertCoverageData(CoverageBean data) {
        db.delete(CommonString.TABLE_COVERAGE_DATA, "STORE_ID" + "='" + data.getStoreId() + "' AND VISIT_DATE='" + data.getVisitDate() + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            values.put(CommonString.KEY_STORE_ID, data.getStoreId());
            values.put(CommonString.KEY_USER_ID, data.getUserId());
            values.put(CommonString.KEY_VISIT_DATE, data.getVisitDate());
            values.put(CommonString.KEY_LATITUDE, data.getLatitude());
            values.put(CommonString.KEY_LONGITUDE, data.getLongitude());
            values.put(CommonString.KEY_IMAGE, data.getImage());
            values.put(CommonString.KEY_COVERAGE_REMARK, data.getRemark());
            values.put(CommonString.KEY_REASON_ID, data.getReasonid());
            values.put(CommonString.KEY_REASON, data.getReason());
            values.put(CommonString.KEY_CHECKOUT_IMAGE, data.getCkeckout_image());
            l = db.insert(CommonString.TABLE_COVERAGE_DATA, null, values);
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Closes Data ", ex.toString());
        }
        return l;
    }

    //jeevan   nmjnmn,
    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getSpecificCoverageData(String visitdate, String store_cd) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "' AND " +
                    CommonString.KEY_STORE_ID + "='" + store_cd + "'", null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!", e.toString());
        }

        return list;

    }


    //jeevan   nmjnmn,
    public long updateJaurneyPlanSpecificStoreStatus(String storeid, String visit_date, String status) {
        long l = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("Upload_Status", status);
            l = db.update("Journey_Plan", values, " StoreId ='" + storeid + "' AND VisitDate ='" + visit_date + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    //jeevan   nmjnmn,
    public ArrayList<JourneyPlan> getSpecificStoreData(String store_cd) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from Journey_Plan  " + "where StoreId ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();
                    sb.setStoreId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreId"))));
                    sb.setCounterId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("CounterId"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("VisitDate"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreName")));
                    sb.setDistributorName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DistributorName")));
                    sb.setAddress((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));

                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ContactPerson")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ContactNo")));
                    sb.setCityName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CityName")));

                    sb.setStoreTypeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreTypeName")));
                    sb.setStoreCategoryName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreCategoryName")));
                    sb.setStateId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StateId")));

                    sb.setStoreTypeId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreTypeId"))));
                    sb.setStoreCategoryId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreCategoryId"))));


                    sb.setReasonId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ReasonId"))));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setDistributorId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("DistributorId")));


                    sb.setClassificationId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ClassificationId"))));
                    sb.setLatitude(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Latitude")));
                    sb.setLongitude(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Longitude")));
                    sb.setGeoFencing(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("GeoFencing")));
                    sb.setStoreCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreCode")));
                    sb.setClassificationName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ClassificationName")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }


    public ArrayList<AuditQuestion> getStoreAuditHeaderData() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  DISTINCT Question_Category , Question_Category_Id from Audit_Question ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion sb = new AuditQuestion();
                    sb.setQuestionCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Category")));
                    sb.setQuestionCategoryId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Category_Id"))));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<AuditQuestion> getStoreAuditChildData(int ques_category_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT Question,Question_Id FROM Audit_Question WHERE Question_Category_Id =" + ques_category_cd + " ", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion sb = new AuditQuestion();
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question")));
                    sb.setQuestionId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Id"))));
//                    sb.setImageAllow(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow")));
                    //changessssssssss
                    sb.setImageAllowforanswer("");
                    sb.setCurrectanswer("");
                    sb.setCurrectanswerCd("0");
                    sb.setAudit_cam("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<AuditQuestion> getauditAnswerData(String question_id) {
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select DISTINCT Answer_Id,Answer,ImageAllow from Audit_Question where Question_Id ='" + question_id + "' ", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion df = new AuditQuestion();
                    df.setAnswerId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer_Id"))));
                    df.setAnswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer")));
                    df.setImageAllowforanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ImageAllow")));
                    list.add(df);
                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;

    }


    public void insertStoreAuditData(String storeid,
                                     HashMap<AuditQuestion,
                                             List<AuditQuestion>> data, List<AuditQuestion> save_listDataHeader) {
        db.delete(CommonString.TABLE_INSERT_AUDIT_OPENINGHEADER_DATA, " STORE_CD='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STORE_AUDIT_DATA, " STORE_CD='" + storeid + "'", null);

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("QUESTION_CATEGORY_CD", save_listDataHeader.get(i).getQuestionCategoryId());
                values.put("QUESTION_CATEGORY", save_listDataHeader.get(i).getQuestionCategory());
                long l = db.insert(CommonString.TABLE_INSERT_AUDIT_OPENINGHEADER_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("QUESTION_CATEGORY_CD", save_listDataHeader.get(i).getQuestionCategoryId());
                    values1.put("QUESTION_CATEGORY", save_listDataHeader.get(i).getQuestionCategory());
                    values1.put("QUESTION", data.get(save_listDataHeader.get(i)).get(j).getQuestion());
                    values1.put("QUESTION_CD", data.get(save_listDataHeader.get(i)).get(j).getQuestionId());
                    values1.put("CURRECT_ANSWER", data.get(save_listDataHeader.get(i)).get(j).getCurrectanswer());
                    values1.put("ANSWER_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getCurrectanswerCd()));
                    values1.put("AUDIT_IMG", data.get(save_listDataHeader.get(i)).get(j).getAudit_cam());


                    values1.put("IMAGE_ALLOW", data.get(save_listDataHeader.get(i)).get(j).getImageAllow());
                    values1.put("IMAGEALLOW_ANS", data.get(save_listDataHeader.get(i)).get(j).getImageAllowforanswer());

                    db.insert(CommonString.TABLE_STORE_AUDIT_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }

    public ArrayList<AuditQuestion> getStoreAuditInsertedData(String store_cd, int questCategory_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_AUDIT_DATA WHERE STORE_CD ='" + store_cd + "' AND QUESTION_CATEGORY_CD=" + questCategory_cd + "", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion sb = new AuditQuestion();
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setQuestionId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_CD"))));
                    sb.setImageAllow(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_ALLOW")));
                    sb.setCurrectanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));
                    sb.setCurrectanswerCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));
                    sb.setAudit_cam(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AUDIT_IMG")));
                    sb.setImageAllowforanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGEALLOW_ANS")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    @SuppressLint("LongLogTag")
    public boolean isStoreAuditFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT QUESTION_CD FROM STORE_AUDIT_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }


    public long updateCoverageCheckoutIMG(String storeid, String visit_date, String checkout_img) {
        long l = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_CHECKOUT_IMAGE, checkout_img);
            l = db.update("COVERAGE_DATA", values, " STORE_ID ='" + storeid + "' AND VISIT_DATE ='" + visit_date + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    @SuppressLint("LongLogTag")
    public ArrayList<NonWorkingReason> getNonWorkingDataByFlag(boolean flag) {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<NonWorkingReason> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Non_Working_Reason", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (flag) {
                        NonWorkingReason sb = new NonWorkingReason();
                        String entry_allow_fortest = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                        if (entry_allow_fortest.equals("1")) {
                            sb.setReasonId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ReasonId"))));
                            sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                            String entry_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                            if (entry_allow.equals("1")) {
                                sb.setEntryAllow(true);
                            } else {
                                sb.setEntryAllow(false);
                            }
                            String image_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"));
                            if (image_allow.equals("1")) {
                                sb.setImageAllow(true);
                            } else {
                                sb.setImageAllow(false);
                            }
                            String gps_mendtry = dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"));
                            if (gps_mendtry.equals("1")) {
                                sb.setGPSMandatory(true);
                            } else {
                                sb.setGPSMandatory(false);
                            }

                            list.add(sb);
                        }


                    } else {
                        NonWorkingReason sb = new NonWorkingReason();
                        sb.setReasonId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ReasonId"))));
                        sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                        String entry_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                        if (entry_allow.equals("1")) {
                            sb.setEntryAllow(true);
                        } else {
                            sb.setEntryAllow(false);
                        }
                        String image_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"));
                        if (image_allow.equals("1")) {
                            sb.setImageAllow(true);
                        } else {
                            sb.setImageAllow(false);
                        }
                        String gps_mendtry = dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"));
                        if (gps_mendtry.equals("1")) {
                            sb.setGPSMandatory(true);
                        } else {
                            sb.setGPSMandatory(false);
                        }

                        list.add(sb);
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getCoverageData(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_COVERAGE_DATA +
                    " WHERE " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }


    public ArrayList<JourneyPlan> getSpecificStoreDatawithdate(String visit_date, String store_cd) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from Journey_Plan where VisitDate ='" + visit_date + "' AND StoreId='" + store_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();
                    sb.setStoreId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreId"))));
                    sb.setCounterId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("CounterId"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("VisitDate"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreName")));
                    sb.setDistributorName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DistributorName")));
                    sb.setAddress((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));

                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ContactPerson")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ContactNo")));
                    sb.setCityName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CityName")));

                    sb.setStoreTypeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreTypeName")));
                    sb.setStoreCategoryName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreCategoryName")));
                    sb.setStateId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StateId")));

                    sb.setStoreTypeId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreTypeId"))));
                    sb.setStoreCategoryId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreCategoryId"))));


                    sb.setReasonId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ReasonId"))));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setDistributorId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("DistributorId")));


                    sb.setClassificationId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ClassificationId"))));
                    sb.setLatitude(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Latitude")));
                    sb.setLongitude(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Longitude")));
                    sb.setGeoFencing(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("GeoFencing")));
                    sb.setStoreCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreCode")));
                    sb.setClassificationName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ClassificationName")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }

    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getcoverageDataPrevious(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + "<>'" + visitdate + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }

    public JourneyPlan getSpecificStoreDataPrevious(String date, String store_id) {
        JourneyPlan sb = new JourneyPlan();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * from Journey_Plan where VisitDate <> '" + date + "' AND StoreId='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setStoreId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreId"))));
                    sb.setCounterId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("CounterId"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("VisitDate"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreName")));
                    sb.setDistributorName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DistributorName")));
                    sb.setAddress((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));

                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ContactPerson")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ContactNo")));
                    sb.setCityName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CityName")));

                    sb.setStoreTypeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreTypeName")));
                    sb.setStoreCategoryName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreCategoryName")));
                    sb.setStateId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StateId")));

                    sb.setStoreTypeId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreTypeId"))));
                    sb.setStoreCategoryId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("StoreCategoryId"))));


                    sb.setReasonId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ReasonId"))));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setDistributorId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("DistributorId")));


                    sb.setClassificationId((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ClassificationId"))));
                    sb.setLatitude(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Latitude")));
                    sb.setLongitude(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Longitude")));
                    sb.setGeoFencing(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("GeoFencing")));
                    sb.setStoreCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StoreCode")));
                    sb.setClassificationName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ClassificationName")));

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }

        return sb;
    }

//upendra 26 dec

    public void updateStatus(String id, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            db.update("Journey_Plan", values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
        }
    }

    public long InsertSTOREgeotag(String storeid, double lat, double longitude, String path, String status) {

        db.delete(CommonString.TABLE_STORE_GEOTAGGING, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();
        try {
            values.put("STORE_ID", storeid);
            values.put("LATITUDE", Double.toString(lat));
            values.put("LONGITUDE", Double.toString(longitude));
            values.put("FRONT_IMAGE", path);
            values.put("GEO_TAG", status);
            values.put("STATUS", status);

            return db.insert(CommonString.TABLE_STORE_GEOTAGGING, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
            return 0;
        }
    }

    public ArrayList<GeotaggingBeans> getinsertGeotaggingData(String storeid, String status) {
        ArrayList<GeotaggingBeans> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from " + CommonString.TABLE_STORE_GEOTAGGING + "" + " where " + CommonString.KEY_STORE_ID + " ='" + storeid + "' and " + CommonString.KEY_STATUS + " = '" + status + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    GeotaggingBeans geoTag = new GeotaggingBeans();
                    geoTag.setStoreid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    geoTag.setLatitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE))));
                    geoTag.setLongitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE))));
                    geoTag.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FRONT_IMAGE")));
                    list.add(geoTag);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;

    }

    public long updateInsertedGeoTagStatus(String id, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            values.put("STATUS", status);
            return db.update(CommonString.TABLE_STORE_GEOTAGGING, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
            return 0;
        }
    }

    public long insertsaleTrackingdata(String user_Id, String visit_date, String store_Cd, ArrayList<InvoiceGetterSetter> invoiceList, String total_value, boolean pos_sale_flag) {
        db.delete(CommonString.Table_Sale_Tracking, "VISIT_DATE<>'" + visit_date + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            for (int k = 0; k < invoiceList.size(); k++) {

                values.put("USER_ID", user_Id);
                values.put("POS_SALE_FLAG", pos_sale_flag);
                values.put("TOTAL_AMOUNT", total_value);
                values.put(CommonString.KEY_STORE_ID, store_Cd);
                values.put(CommonString.KEY_VISIT_DATE, visit_date);
                values.put("STORE_NAME", invoiceList.get(k).getStore_name());
                values.put("STORE_ADDRESS", invoiceList.get(k).getStore_address());

                values.put("BUYER_NAME", invoiceList.get(k).getCustomer_name());
                values.put("CONTACT_NUMBER", invoiceList.get(k).getMobile_no());
                values.put("PRODUCT_ID", invoiceList.get(k).getProduct_Id());
                values.put("PRODUCT", invoiceList.get(k).getProduct());
                values.put("QUANTITY", invoiceList.get(k).getQuantity());
                values.put("PRODUCT_RATE", invoiceList.get(k).getProduct_rate());
                values.put("GENDER", invoiceList.get(k).getCustomer_gender());
                values.put("EAN_CODE", invoiceList.get(k).getScan_ean_code_or_enterd_ean_code());

                l = db.insert(CommonString.Table_Sale_Tracking, null, values);
            }
        } catch (Exception ex) {
            Log.d("Database Exception", ex.toString());
        }
        return l;
    }

    public ArrayList<InvoiceGetterSetter> getsalestrackingList(String storeid, String visite_date) {
        ArrayList<InvoiceGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from " + CommonString.Table_Sale_Tracking + "" + " where " + CommonString.KEY_STORE_ID + " ='" + storeid + "' and " + CommonString.KEY_VISIT_DATE + " = '" + visite_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    InvoiceGetterSetter sale = new InvoiceGetterSetter();
                    sale.setStore_name(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_NAME")));
                    sale.setStore_address(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ADDRESS")));
                    sale.setCustomer_name(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BUYER_NAME")));
                    sale.setMobile_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CONTACT_NUMBER")));
                    sale.setProduct_Id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRODUCT_ID")));
                    sale.setProduct(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRODUCT")));
                    sale.setQuantity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUANTITY")));
                    sale.setProduct_rate(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRODUCT_RATE")));
                    sale.setTotal_amount(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TOTAL_AMOUNT")));
                    sale.setScan_ean_code_or_enterd_ean_code(dbcursor.getString(dbcursor.getColumnIndexOrThrow("EAN_CODE")));
                    sale.setCustomer_gender(dbcursor.getString(dbcursor.getColumnIndexOrThrow("GENDER")));
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("POS_SALE_FLAG")).equals("1")) {
                        sale.setPos_sale_flag(true);
                    } else {
                        sale.setPos_sale_flag(false);
                    }
                    list.add(sale);

                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;
    }

    public long insertAttendenceData(String user_id, String visit_date, String image, String reason) {
        db.delete(CommonString.TABLE_ATTENDENCE_TABLE, null, null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            values.put(CommonString.KEY_USER_ID, user_id);
            values.put(CommonString.KEY_VISIT_DATE, visit_date);
            values.put(CommonString.KEY_IMAGE, image);
            values.put(CommonString.KEY_REASON, reason);

            l = db.insert(CommonString.TABLE_ATTENDENCE_TABLE, null, values);

        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        return l;
    }

    public boolean insert_dashboard_data(NonWorkingReasonGetterSetter dashboard) {
        db.delete("Dashboard_Data", null, null);
        ContentValues values = new ContentValues();
        List<DashboardDataGetter> data = dashboard.getDashboardData();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Value", data.get(i).getValue());
                values.put("Label", data.get(i).getLabel());
                values.put("Url", data.get(i).getUrl());

                long id = db.insert("Dashboard_Data", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public ArrayList<DashboardDataGetter> getdashboard_data() {
        ArrayList<DashboardDataGetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from Dashboard_Data", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    DashboardDataGetter sale = new DashboardDataGetter();
                    sale.setValue(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Value")));
                    sale.setLabel(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Label")));
                    sale.setUrl(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Url")));
                    list.add(sale);
                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;
    }

    public boolean insert_promotion_master(NonWorkingReasonGetterSetter promo) {
        db.delete("Promotion_Master", null, null);
        ContentValues values = new ContentValues();
        List<PromotionMaster> data = promo.getPromotionMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("PromotionName", data.get(i).getPromotionName());
                values.put("PromotionId", data.get(i).getPromotionId());

                values.put("PromotionType", data.get(i).getPromotionType());
                values.put("PromotionTypeId", data.get(i).getPromotionTypeId());

                values.put("CounterGroupName", data.get(i).getCounterGroupName());
                values.put("CounterGroupId", data.get(i).getCounterGroupId());

                values.put("ProductCluster", data.get(i).getProductCluster());
                values.put("ProductClusterId", data.get(i).getProductClusterId());

                values.put("ImageName", data.get(i).getImageName());
                values.put("Url", data.get(i).getUrl());
                values.put("StartDate", data.get(i).getStartDate());
                values.put("EndDate", data.get(i).getEndDate());

                long id = db.insert("Promotion_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insert_non_promotion_reason(NonWorkingReasonGetterSetter promo) {
        db.delete("Non_Promotion_Reason", null, null);
        ContentValues values = new ContentValues();
        List<NonPromotionReason> data = promo.getNonPromotionReason();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("PReasonId", data.get(i).getPReasonId());
                values.put("PReason", data.get(i).getPReason());

                long id = db.insert("Non_Promotion_Reason", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }


    public ArrayList<PromotionMaster> getpromotion_master_data() {
        ArrayList<PromotionMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from Promotion_Master", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PromotionMaster sale = new PromotionMaster();
                    sale.setPromotionName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PromotionName")));
                    sale.setPromotionId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("PromotionId")));

                    sale.setPromotionType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PromotionType")));
                    sale.setPromotionTypeId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("PromotionTypeId")));

                    sale.setCounterGroupName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CounterGroupName")));
                    sale.setCounterGroupId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("CounterGroupId")));

                    sale.setProductCluster(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductCluster")));
                    sale.setProductClusterId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductClusterId")));

                    sale.setImageName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ImageName")));
                    sale.setUrl(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Url")));
                    sale.setStartDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow("StartDate")));
                    sale.setEndDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow("EndDate")));
                    sale.setPromotion_exists_state("");
                    sale.setPromotion_img("");
                    sale.setPromotion_currect_ans("");
                    sale.setPromotion_currect_ans_Id("0");

                    list.add(sale);
                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;
    }

    public long insertpromotion_captured_data(String store_Cd, String visit_date, ArrayList<PromotionMaster> promotion_List) {
        db.delete(CommonString.Table_promotion, CommonString.KEY_STORE_ID + "='" + store_Cd + "' AND VISIT_DATE='" + visit_date + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            for (int k = 0; k < promotion_List.size(); k++) {


                values.put(CommonString.KEY_STORE_ID, store_Cd);
                values.put(CommonString.KEY_VISIT_DATE, visit_date);
                values.put("PROMOTION_NAME", promotion_List.get(k).getPromotionName());
                values.put("PROMOTION_ID", promotion_List.get(k).getPromotionId().toString());

                values.put("PROMOTION_TYPE", promotion_List.get(k).getPromotionType());
                values.put("PROMOTION_TYPE_ID", promotion_List.get(k).getPromotionTypeId());
                values.put("PRODUCT_CLUSTER", promotion_List.get(k).getProductCluster());
                values.put("PRODUCT_CLUSTER_ID", promotion_List.get(k).getProductClusterId());
                values.put("START_DATE", promotion_List.get(k).getStartDate());
                values.put("END_DATE", promotion_List.get(k).getEndDate());
                values.put("PROMOTION_EXIST", promotion_List.get(k).getPromotion_exists_state());
                values.put("PROMO_IMG", promotion_List.get(k).getPromotion_img());
                values.put("REASON", promotion_List.get(k).getPromotion_currect_ans());
                values.put("REASON_ID", promotion_List.get(k).getPromotion_currect_ans_Id());

                l = db.insert(CommonString.Table_promotion, null, values);
            }
        } catch (Exception ex) {
            Log.d("Database Exception", ex.toString());
        }
        return l;
    }

    public ArrayList<PromotionMaster> getpromotion_inserted_data(String store_Id, String visit_date) {
        ArrayList<PromotionMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select distinct Pt.PROMOTION_NAME,Pt.PROMOTION_ID,Pt.PROMOTION_TYPE,Pt.PROMOTION_TYPE_ID,Pt.PRODUCT_CLUSTER,Pt.PRODUCT_CLUSTER_ID," +
                    "Pt.START_DATE,Pt.END_DATE,Pt.PROMOTION_EXIST,Pt.PROMO_IMG,Pt.REASON,Pt.REASON_ID,Pm.ImageName,Pm.Url from PROMOTION_TABLE Pt Inner Join Promotion_Master Pm " +
                    "On Pt.PROMOTION_ID=Pm.PromotionId where Pt.STORE_ID='" + store_Id + "' and " + CommonString.KEY_VISIT_DATE + " = '" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PromotionMaster sale = new PromotionMaster();
                    sale.setPromotionName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTION_NAME")));
                    sale.setPromotionId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("PROMOTION_ID")));

                    sale.setPromotionType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTION_TYPE")));
                    sale.setPromotionTypeId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("PROMOTION_TYPE_ID")));

                    sale.setProductCluster(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PRODUCT_CLUSTER")));
                    sale.setProductClusterId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("PRODUCT_CLUSTER_ID")));
                    sale.setPromotion_exists_state(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMOTION_EXIST")));
                    sale.setPromotion_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_IMG")));
                    sale.setPromotion_currect_ans(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));
                    sale.setPromotion_currect_ans_Id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON_ID")));

                    sale.setImageName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ImageName")));
                    sale.setUrl(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Url")));
                    sale.setStartDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow("START_DATE")));
                    sale.setEndDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow("END_DATE")));

                    list.add(sale);
                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<NonPromotionReason> getnon_promotion_reasonList() {
        ArrayList<NonPromotionReason> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from Non_Promotion_Reason", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NonPromotionReason sale = new NonPromotionReason();
                    sale.setPReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PReason")));
                    sale.setPReasonId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("PReasonId")));
                    list.add(sale);
                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;
    }


    ///////////////////////upendra code start////////////////////////////////////////////
    public ArrayList<ProductMaster> getcategory_fromStockData(String sigature_id) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("select distinct AxeName from Product_Master where SignatureId ='" + sigature_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    //  sb.setAxeId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("AxeId")));
                    sb.setAxeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AxeName")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public ArrayList<ProductMaster> getcategory_wise_brand_fromStockData(String category_name, String sigature_id) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select distinct SubAxeName from Product_Master where AxeName ='" + category_name + "' AND SignatureId='" + sigature_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setSubAxeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SubAxeName")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public ArrayList<ProductMaster> getbrand_wise_sku_fromStockData(String subaxeName, String sigature_id, String storeId) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("select pm.ProductName,pm.ProductId,pm.EanCode,pm.Mrp,sd.Stock from Product_Master pm " +
                    "inner join Stock_Tester_Data sd on sd.ProductId=pm.ProductId where SubAxeName ='" + subaxeName + "' AND SignatureId='" + sigature_id + "'and sd.StoreId='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();

                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setEanCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("EanCode")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Stock")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public void insertStockData(String store_cd, String sigature_id, String visit_date, String category_id,
                                HashMap<ProductMaster,
                                        List<ProductMaster>> data, List<ProductMaster> save_listDataHeader) {
        db.delete(CommonString.TABLE_INSERT_HEADER_STOCK_DATA, "AxeName" + "='" + category_id + "' AND SignatureId='" + sigature_id + "'AND STORE_ID='" + store_cd + "'", null);
        db.delete(CommonString.TABLE_STORE_STOCK_CHILD_DATA, "AxeName" + "='" + category_id + "' AND SignatureId='" + sigature_id + "'AND STORE_ID='" + store_cd + "'", null);

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_ID", store_cd);
                values.put("AxeName", category_id);
                values.put("SignatureId", sigature_id);
                values.put(CommonString.KEY_VISIT_DATE, visit_date);
                values.put("SubAxeName", save_listDataHeader.get(i).getSubAxeName());
                long l = db.insert(CommonString.TABLE_INSERT_HEADER_STOCK_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_ID", store_cd);
                    values1.put("AxeName", category_id);
                    values1.put("SignatureId", sigature_id);
                    values1.put(CommonString.KEY_VISIT_DATE, visit_date);
                    values1.put("SubAxeName", save_listDataHeader.get(i).getSubAxeName());
                    values1.put("ProductName", data.get(save_listDataHeader.get(i)).get(j).getProductName());
                    values1.put("ProductId", data.get(save_listDataHeader.get(i)).get(j).getProductId());
                    values1.put("STOCK", data.get(save_listDataHeader.get(i)).get(j).getStock());
                    values1.put("Mrp", data.get(save_listDataHeader.get(i)).get(j).getMrp());
                    values1.put("STOCK_RECEIVED", data.get(save_listDataHeader.get(i)).get(j).getStock_receive());
                    values1.put("REASON_ID", data.get(save_listDataHeader.get(i)).get(j).getResion_id());
                    values1.put("REASON", data.get(save_listDataHeader.get(i)).get(j).getResion());

                    db.insert(CommonString.TABLE_STORE_STOCK_CHILD_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {

        }
    }

    public ArrayList<HeaderGetterSetter> getHeaderStock(String storeId) {
        ArrayList<HeaderGetterSetter> list = new ArrayList<HeaderGetterSetter>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT ID FROM DR_STOCK_HEADER_DATA WHERE STORE_ID= '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    HeaderGetterSetter sb = new HeaderGetterSetter();
                    sb.setKeyId(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ID")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            return list;
        }
        return list;
    }


    public ArrayList<ProductMaster> getStockInsertedData(String storeId, String subaxeName, String signatureId) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * from DR_STOCK_CHILD_DATA where SubAxeName ='" + subaxeName + "' AND SignatureId='" + signatureId + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public ArrayList<ProductMaster> getcategory_wise_brand_fromStockInword() {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select distinct SubAxeName from Product_Master ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setSubAxeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SubAxeName")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public ArrayList<ProductMaster> getStockInwordInsertedData(String storeId, String subaxeName) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * from DR_STOCK_CHILD_DATA where SubAxeName ='" + subaxeName + "' AND STORE_D='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setStock_receive(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("STOCK_RECEIVED")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    sb.setResion_id(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("REASON_ID")));
                    sb.setResion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public ArrayList<ProductMaster> getbrand_wise_sku_fromStockInwordData(String subaxeName) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select distinct ProductName,ProductId,EanCode,Mrp from Product_Master where SubAxeName='" + subaxeName + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();

                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setEanCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("EanCode")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public boolean insertInwordSalesPoData(TableStructureGetterSetter nonWorkingdata) {
        db.delete("InwardSales_PO", null, null);
        ContentValues values = new ContentValues();
        List<InwardSalesPO> data = nonWorkingdata.getInwardSalesPO();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("CounterCode", data.get(i).getCounterCode());
                values.put("CounterId", data.get(i).getCounterId());
                values.put("GrnDate", data.get(i).getGrnDate());
                values.put("GrnRefNo", data.get(i).getGrnRefNo());
                values.put("GrnType", data.get(i).getGrnType());
                values.put("MRP", data.get(i).getMRP());
                values.put("ProductCode", data.get(i).getProductCode());
                values.put("ProductId", data.get(i).getProductId());
                values.put("QTY", data.get(i).getQTY());
                values.put("StoreCode", data.get(i).getStoreCode());
                values.put("StoreId", data.get(i).getStoreId());
                values.put("UOM", data.get(i).getUOM());
                values.put("ProductName", data.get(i).getProductName());

                long id = db.insert("InwardSales_PO", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }


    public ArrayList<InwardSalesPO> getInwordData(String date, String GrnType, String storeId) {

        ArrayList<InwardSalesPO> list = new ArrayList<InwardSalesPO>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("select  ProductName,ProductId,QTY,MRP,GrnDate,GrnType from InwardSales_PO where GrnType ='" + GrnType + "' AND StoreId='" + storeId + "' AND GrnDate = '" + date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();

                while (!dbcursor.isAfterLast()) {
                    InwardSalesPO sb = new InwardSalesPO();

                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setQTY((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("QTY"))));
                    sb.setStock((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("QTY"))));
                    sb.setMRP((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("MRP"))));
                    sb.setGrnDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("GrnDate"))));
                    sb.setGrnDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("GrnType"))));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public void UpdatInwardStockData(String storeid, List<InwardSalesPO> inwardStockList) {
        ContentValues values1 = new ContentValues();
        try {

            for (int i = 0; i < inwardStockList.size(); i++) {
                InwardSalesPO productMaster = getProductStockData(storeid, inwardStockList.get(i).getProductId().toString());
                productMaster.getStock();
                int sumStock = productMaster.getStock() + inwardStockList.get(i).getStock();
                values1.put("STOCK", sumStock);

                db.update(CommonString.TABLE_STORE_STOCK_CHILD_DATA, values1, "STORE_ID" + "='" + storeid + "' AND ProductId " + "='" + inwardStockList.get(i).getProductId() + "'", null);
            }
        } catch (Exception ex) {

        }
    }

    public void UpdatInwardTesterStockData(String storeid, List<InwardSalesPO> inwardStockList) {
        ContentValues values1 = new ContentValues();
        try {

            for (int i = 0; i < inwardStockList.size(); i++) {
                InwardSalesPO productMaster = getProductStockTesterData(storeid, inwardStockList.get(i).getProductId().toString());
                productMaster.getStock();
                int sumStock = productMaster.getStock() + inwardStockList.get(i).getStock();
                values1.put("STOCK", sumStock);

                db.update(CommonString.TABLE_STORE_STOCK_TESTER_CHILD_DATA, values1, "STORE_ID" + "='" + storeid + "' AND ProductId " + "='" + inwardStockList.get(i).getProductId() + "'", null);
            }
        } catch (Exception ex) {

        }
    }


    public InwardSalesPO getProductStockData(String storeId, String ProductId) {

        InwardSalesPO sb = new InwardSalesPO();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from DR_STOCK_CHILD_DATA where ProductId ='" + ProductId + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setMRP(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }
        } catch (Exception e) {

            return sb;
        }
        return sb;
    }


    public InwardSalesPO getProductStockTesterData(String storeId, String ProductId) {

        InwardSalesPO sb = new InwardSalesPO();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * from DR_STOCK_TESTER__CHILD_DATA where ProductId ='" + ProductId + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setMRP(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }
        } catch (Exception e) {

            return sb;
        }

        return sb;
    }

    public void insertInwardData(String store_cd, String visit_date, ArrayList<InwardSalesPO> inwardStock) {
        db.delete(CommonString.TABLE_STORE_INWARD_REASON_DATA, " STORE_ID='" + store_cd + "'", null);
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();

            for (int j = 0; j < inwardStock.size(); j++) {

                values1.put("STORE_ID", store_cd);
                values1.put(CommonString.KEY_VISIT_DATE, visit_date);
                values1.put("ProductName", inwardStock.get(j).getProductName());
                values1.put("ProductId", inwardStock.get(j).getProductId());
                values1.put("STOCK", inwardStock.get(j).getStock());
                values1.put("REASON_ID", inwardStock.get(j).getReasonId());
                values1.put("REASON", inwardStock.get(j).getReason());
                values1.put("mrp", inwardStock.get(j).getMRP());

                db.insert(CommonString.TABLE_STORE_INWARD_REASON_DATA, null, values1);

            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {

        }
    }

    public boolean insertNonStockReasonData(NonStockReasonGetterSetter nonWorkingdata) {
        db.delete("Non_Stock_Reason", null, null);
        ContentValues values = new ContentValues();
        List<NonStockReason> data = nonWorkingdata.getNonStockReason();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {

                values.put("ReasonId", data.get(i).getReasonId());
                values.put("Reason", data.get(i).getReason());

                long id = db.insert("Non_Stock_Reason", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public ArrayList<NonStockReason> getNonStockData() {

        ArrayList<NonStockReason> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select * from Non_Stock_Reason", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NonStockReason sb = new NonStockReason();

                    sb.setReasonId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ReasonId")));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public boolean insertStockData(StockDataGetterSetter nonWorkingdata) {
        db.delete("Stock_Data", null, null);
        ContentValues values = new ContentValues();
        List<StockDatum> data = nonWorkingdata.getStockData();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("StoreId", data.get(i).getStoreId());
                values.put("Stock", data.get(i).getStock());
                values.put("ProductId", data.get(i).getProductId());

                long id = db.insert("Stock_Data", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertStockTesterData(StockTesterDataGetterSetter nonWorkingdata) {
        db.delete("Stock_Tester_Data", null, null);
        ContentValues values = new ContentValues();
        List<StockTesterDatum> data = nonWorkingdata.getStockTesterData();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("ProductId", data.get(i).getProductId());
                values.put("Stock", data.get(i).getStock());
                values.put("StoreId", data.get(i).getStoreId());

                long id = db.insert("Stock_Tester_Data", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }


    public boolean insertPwpGwpData(StockPwpGwpDataGetterSetter nonWorkingdata) {
        db.delete("Stock_PwpGwp_Data", null, null);
        ContentValues values = new ContentValues();
        List<StockPwpGwpDatum> data = nonWorkingdata.getStockPwpGwpData();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("ProductId", data.get(i).getProductId());
                values.put("ProductName", data.get(i).getProductName());
                values.put("Stock", data.get(i).getStock());
                values.put("StoreId", data.get(i).getStoreId());
                values.put("StoreName", data.get(i).getStoreName());


                long id = db.insert("Stock_PwpGwp_Data", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertsampleData(StockSampleDataGetterSetter nonWorkingdata) {
        db.delete("Stock_Sample_Data", null, null);
        ContentValues values = new ContentValues();
        List<StockSampleDatum> data = nonWorkingdata.getStockSampleData();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("ProductId", data.get(i).getProductId());
                values.put("Stock", data.get(i).getStock());
                values.put("StoreId", data.get(i).getStoreId());

                long id = db.insert("Stock_Sample_Data", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }


    public ArrayList<ProductMaster> getbrand_wise_sku_fromStockTesterData(String subaxeName, String sigature_id, String storeId) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("select pm.ProductName,pm.ProductId,pm.EanCode,pm.Mrp,sd.Stock from Product_Master pm " +
                    "inner join Stock_Data sd on sd.ProductId=pm.ProductId where SubAxeName ='" + subaxeName + "' AND SignatureId='" + sigature_id + "'and sd.StoreId='" + storeId + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();

                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setEanCode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("EanCode")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Stock")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public ArrayList<ProductMaster> getStockTesterInsertedData(String storeId, String subaxeName, String signatureId) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * from DR_STOCK_TESTER__CHILD_DATA where SubAxeName ='" + subaxeName + "' AND SignatureId='" + signatureId + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public void insertStockTesterData(String store_cd, String sigature_id, String visit_date, String category_id,
                                      HashMap<ProductMaster,
                                              List<ProductMaster>> data, List<ProductMaster> save_listDataHeader) {
        db.delete(CommonString.TABLE_INSERT_HEADER_STOCK_TESTER_DATA, "AxeName" + "='" + category_id + "' AND SignatureId='" + sigature_id + "'AND STORE_ID='" + store_cd + "'", null);
        db.delete(CommonString.TABLE_STORE_STOCK_TESTER_CHILD_DATA, "AxeName" + "='" + category_id + "' AND SignatureId='" + sigature_id + "'AND STORE_ID='" + store_cd + "'", null);

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_ID", store_cd);
                values.put("AxeName", category_id);
                values.put("SignatureId", sigature_id);
                values.put(CommonString.KEY_VISIT_DATE, visit_date);
                values.put("SubAxeName", save_listDataHeader.get(i).getSubAxeName());
                long l = db.insert(CommonString.TABLE_INSERT_HEADER_STOCK_TESTER_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_ID", store_cd);
                    values1.put("AxeName", category_id);
                    values1.put("SignatureId", sigature_id);
                    values1.put(CommonString.KEY_VISIT_DATE, visit_date);
                    values1.put("SubAxeName", save_listDataHeader.get(i).getSubAxeName());
                    values1.put("ProductName", data.get(save_listDataHeader.get(i)).get(j).getProductName());
                    values1.put("ProductId", data.get(save_listDataHeader.get(i)).get(j).getProductId());
                    values1.put("STOCK", data.get(save_listDataHeader.get(i)).get(j).getStock());
                    values1.put("Mrp", data.get(save_listDataHeader.get(i)).get(j).getMrp());
                    values1.put("STOCK_RECEIVED", data.get(save_listDataHeader.get(i)).get(j).getStock_receive());
                    values1.put("REASON_ID", data.get(save_listDataHeader.get(i)).get(j).getResion_id());
                    values1.put("REASON", data.get(save_listDataHeader.get(i)).get(j).getResion());

                    db.insert(CommonString.TABLE_STORE_STOCK_TESTER_CHILD_DATA, null, values1);

                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {

        }
    }

    public long insertGroomingImgData(GroomingGetterSetter groomingImg, String storeId, String visitDate, String sigature_id, String mCategory_id) {
        db.delete(CommonString.TABLE_TESTER_IMAGE_DATA, " STORE_ID" + "='" + storeId + "' AND VISIT_DATE='" + visitDate + "'AND Sigature_id='" + sigature_id + "'AND Category_id='" + mCategory_id + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            db.beginTransaction();

            values.put(CommonString.KEY_STORE_ID, storeId);
            values.put(CommonString.KEY_VISIT_DATE, visitDate);
            values.put("Sigature_id", sigature_id);
            values.put("Category_id", mCategory_id);
            values.put("IMAGE1", groomingImg.getImage1());
            values.put("IMAGE2", groomingImg.getImage2());
            values.put("IMAGE3", groomingImg.getImage3());
            values.put("IMAGE4", groomingImg.getImage4());

            l = db.insert(CommonString.TABLE_TESTER_IMAGE_DATA, null, values);

            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {

        }
        return l;
    }


    public GroomingGetterSetter getStockTestetImageData(String storeId, String Sigature_id, String Category_id) {
        GroomingGetterSetter sb = new GroomingGetterSetter();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from TESTER_IMAGE_DATA where Category_id ='" + Category_id + "' AND Sigature_id='" + Sigature_id + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    sb.setImage1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE1")));
                    sb.setImage2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE2")));
                    sb.setImage3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE3")));
                    sb.setImage4(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE4")));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIT_DATE")));
                    sb.setStoreCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    sb.setSigature_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sigature_id")));
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_id")));

                    dbcursor.moveToNext();
                }
                dbcursor.close();

            }
        } catch (Exception e) {


        }
        return sb;
    }

    //  Upendra Cpm, [06.05.19 13:08]
    public ArrayList<ProductMaster> getStockUploadData(String store_id, String visit_date) {
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM DR_STOCK_CHILD_DATA WHERE STORE_ID =" + store_id + "  AND VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setAxeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AxeName")));
                    sb.setSignatureId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("SignatureId")));
                    sb.setSignatureName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SubAxeName")));
                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    sb.setResion_id(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("REASON_ID")));
                    sb.setResion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public ArrayList<ProductMaster> getTesterUploadData(String store_id, String visit_date) {
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM DR_STOCK_TESTER__CHILD_DATA WHERE STORE_ID =" + store_id + "  AND VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();
                    sb.setAxeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AxeName")));
                    sb.setSignatureId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("SignatureId")));
                    sb.setSignatureName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SubAxeName")));
                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    sb.setResion_id(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("REASON_ID")));
                    sb.setResion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public ArrayList<GroomingGetterSetter> getTesterImageUploadData(String store_id, String visit_date) {
        ArrayList<GroomingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM TESTER_IMAGE_DATA WHERE STORE_ID =" + store_id + "  AND VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    GroomingGetterSetter sb = new GroomingGetterSetter();
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AxeName")));
                    sb.setSigature_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SignatureId")));
                    sb.setImage1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SubAxeName")));
                    sb.setImage2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setImage3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setImage3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setImage4(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Mrp")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }
        return list;
    }

    public ArrayList<ProductMaster> getInwardUploadData(String store_id, String visit_date) {
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM DR_INWARD_REASON_DATA WHERE STORE_ID =" + store_id + "  AND VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();

                    sb.setMrp(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Mrp")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setStock(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setResion_id(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("REASON_ID")));
                    sb.setResion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }
        return list;
    }
    ///////////////////////upendra code end////

    public boolean insertcst_sales_histry(NonWorkingReasonGetterSetter cst) {
        db.delete("Consumer_Sales_History", null, null);
        ContentValues values = new ContentValues();
        List<ConsumerSalesHistory> data = cst.getConsumerSalesHistory();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("ProductId", data.get(i).getProductId());
                values.put("ProductName", data.get(i).getProductName());
                values.put("Qtysold", data.get(i).getQtysold());
                values.put("MobileNumber", data.get(i).getMobileNumber());
                values.put("ConsumerName", data.get(i).getConsumerName());
                values.put("VisitDate", data.get(i).getVisitDate());
                values.put("EanCode", data.get(i).getEan_Code());

                long id = db.insert("Consumer_Sales_History", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public ArrayList<ProductMaster> getConsumerSaleHistry(String mobile_no, String consumer_date) {
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from Consumer_Sales_History where MobileNumber='" + mobile_no + "'and VisitDate='" + consumer_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sale = new ProductMaster();
                    sale.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sale.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sale.setConsumer_name(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ConsumerName")));
                    sale.setConsumer_qty(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Qtysold")));
                    sale.setVisit_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VisitDate")));
                    list.add(sale);

                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;
    }


    public ArrayList<ProductMaster> getConsumer_date(String mobile_no) {
        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select distinct VisitDate,MobileNumber from Consumer_Sales_History where MobileNumber='" + mobile_no + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sale = new ProductMaster();
                    sale.setVisit_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VisitDate")));
                    sale.setMobile_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MobileNumber")));
                    list.add(sale);
                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;
    }


    ////usk today
    public boolean isInwoardFilledData(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT  STORE_ID " + "FROM " + CommonString.TABLE_STORE_INWARD_REASON_DATA + " WHERE STORE_ID = '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")) == null || dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {

            return filled;
        }

        return filled;
    }


    public ArrayList<ProductMaster> getSignatureDataList(String storeId, String visitDate) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * from DR_STOCK_CHILD_DATA where  VISIT_DATE='" + visitDate + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();

                    sb.setSignatureId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("SignatureId")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public ArrayList<ProductMaster> getCategoryDataList(String storeId, String visitDate, String signatureId) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * from DR_STOCK_CHILD_DATA where VISIT_DATE ='" + visitDate + "' AND SignatureId='" + signatureId + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();

                    sb.setAxeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AxeName")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public boolean isStockDataFilled(String storeId, String axeName, String visitDate) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from DR_STOCK_CHILD_DATA where AxeName ='" + axeName + "' AND VISIT_DATE='" + visitDate + "' AND STORE_ID='" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {

            return filled;
        }
        return filled;
    }
///////////////////////


    public ArrayList<ProductMaster> getSignatureTesterDataList(String storeId, String visitDate) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * from DR_STOCK_TESTER__CHILD_DATA where  VISIT_DATE='" + visitDate + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();

                    sb.setSignatureId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("SignatureId")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public ArrayList<ProductMaster> getCategoryTestrDataList(String storeId, String visitDate, String signatureId) {

        ArrayList<ProductMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * from DR_STOCK_TESTER__CHILD_DATA where VISIT_DATE ='" + visitDate + "' AND SignatureId='" + signatureId + "' AND STORE_ID='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    ProductMaster sb = new ProductMaster();

                    sb.setAxeName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AxeName")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }


    public boolean isStockTestrDataFilled(String storeId, String axeName, String visitDate) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from DR_STOCK_TESTER__CHILD_DATA where AxeName ='" + axeName + "' AND VISIT_DATE='" + visitDate + "' AND STORE_ID='" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {

            return filled;
        }
        return filled;
    }

    public ArrayList<StockPwpGwpDatum> getPwpGwpData(String storeId) {

        ArrayList<StockPwpGwpDatum> list = new ArrayList<StockPwpGwpDatum>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("select  ProductName,ProductId,Stock from Stock_PwpGwp_Data where StoreId ='" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();

                while (!dbcursor.isAfterLast()) {
                    StockPwpGwpDatum sb = new StockPwpGwpDatum();

                    sb.setProductName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ProductName")));
                    sb.setProductId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("ProductId")));
                    sb.setStock((dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Stock"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public void insertPwpGwpData(String store_cd, String visit_date, ArrayList<StockPwpGwpDatum> pwpGwpStock) {
        db.delete(CommonString.TABLE_STORE_PWP_GWP_DATA, " STORE_ID='" + store_cd + "'", null);
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();

            for (int j = 0; j < pwpGwpStock.size(); j++) {

                values1.put("STORE_ID", store_cd);
                values1.put(CommonString.KEY_VISIT_DATE, visit_date);
                values1.put("ProductName", pwpGwpStock.get(j).getProductName());
                values1.put("ProductId", pwpGwpStock.get(j).getProductId());
                values1.put("STOCK", pwpGwpStock.get(j).getStock());

                db.insert(CommonString.TABLE_STORE_PWP_GWP_DATA, null, values1);

            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {

        }
    }

    public boolean isPwpGwpFilledData(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT  STORE_ID " + "FROM " + CommonString.TABLE_STORE_PWP_GWP_DATA + " WHERE STORE_ID = '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")) == null || dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {

            return filled;
        }

        return filled;
    }


    public long insertNotificationData(String title, String body, String path, String visited_date, String type) {
        long id = 0;
        ContentValues values = new ContentValues();
        try {
            values.put(CommonString.KEY_BODY, body);
            values.put(CommonString.KEY_TITLE, title);
            values.put(CommonString.KEY_VISIT_DATE,visited_date);
            values.put(CommonString.KEY_PATH, path);
            values.put(CommonString.KEY_TYPE, type);

            id = db.insert(CommonString.TABLE_NOTIFICATION_DATA, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
            return 0;
        }
        return id;
    }

    public ArrayList<NotificationData> getNotificationList() {
        ArrayList<NotificationData> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = getReadableDatabase().rawQuery("Select * from " + CommonString.TABLE_NOTIFICATION_DATA +"", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NotificationData notifyData = new NotificationData();
                    notifyData.setBody(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BODY)));
                    notifyData.setPath(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PATH)));
                    notifyData.setTitle(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TITLE)));
                    notifyData.setVisited_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    notifyData.setType(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TYPE)));
                    list.add(notifyData);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;
    }
}
