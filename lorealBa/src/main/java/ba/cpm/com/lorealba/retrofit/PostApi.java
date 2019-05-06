package ba.cpm.com.lorealba.retrofit;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import ba.cpm.com.lorealba.constant.CommonString;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;


/**
 * Created by jeevanp on 19-05-2017.
 */


//using interface for post data
public interface PostApi {
    @retrofit2.http.POST(CommonString.KEY_LOGIN_DETAILS)
    retrofit2.Call<ResponseBody> getLogindetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("DownloadJson")
    Call<String> getDownloadAll(@Body RequestBody request);

    @retrofit2.http.POST("DownloadJson")
    Call<ResponseBody> getDownloadAllUSINGLOGIN(@Body RequestBody request);


    @retrofit2.http.POST("CoverageDetail")
    retrofit2.Call<ResponseBody> getCoverageDetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("SendOTP")
    retrofit2.Call<ResponseBody> getOTPMethod(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("UploadJCPDetail")
    retrofit2.Call<ResponseBody> getUploadJCPDetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("UploadJson")
    retrofit2.Call<ResponseBody> getUploadJsonDetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("CoverageStatusDetail")
    retrofit2.Call<ResponseBody> getCoverageStatusDetail(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("Upload_StoreGeoTag_IMAGES")
    retrofit2.Call<ResponseBody> getGeoTagImage(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("CheckoutDetail")
    retrofit2.Call<ResponseBody> getCheckout(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("DeleteCoverage")
    retrofit2.Call<ResponseBody> deleteCoverageData(@retrofit2.http.Body okhttp3.RequestBody request);

    @retrofit2.http.POST("CoverageNonworking")
    retrofit2.Call<ResponseBody> setCoverageNonWorkingData(@retrofit2.http.Body okhttp3.RequestBody request);

    @POST("Uploadimageswithpath")
    retrofit.Call<String> getUploadDataBaseBackup(@Body RequestBody body1);
    @POST("UploadJsonDetail")
    Call<JsonObject> getGeotag(@Body RequestBody request);
    @retrofit2.http.POST("TokenDetail")
    Call<ResponseBody> uploadTokenDetails(@Body RequestBody request);


    @POST("UploadJsonDetail")
    Call<JSONObject> getUploadJsonDetailForFileList(@Body RequestBody request);
    @retrofit2.http.POST("UploadAttendanceDetail")
    retrofit2.Call<ResponseBody> getAttendanceDetails(@retrofit2.http.Body okhttp3.RequestBody request);
}

