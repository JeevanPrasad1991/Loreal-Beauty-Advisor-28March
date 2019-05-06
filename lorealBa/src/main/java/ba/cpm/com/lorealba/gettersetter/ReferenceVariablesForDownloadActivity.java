package ba.cpm.com.lorealba.gettersetter;

import ba.cpm.com.lorealba.gsonGetterSetter.AuditQuestionGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.JCPGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.NonStockReasonGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.NonWorkingReasonGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockDataGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockPwpGwpDataGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockSampleDataGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockTesterDataGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.TableStructureGetterSetter;

/**
 * Created by jeevanp on 15-12-2017.
 */

public class ReferenceVariablesForDownloadActivity {
    protected TableStructureGetterSetter tableStructureObj,product_masterObject,inwardSales_POObject;
    protected JCPGetterSetter jcpObject;
    protected NonWorkingReasonGetterSetter nonWorkingObj,dashboardObject,promotionMasterObject,nonpromotionReason,cst_sales_histryObject;
    protected NonStockReasonGetterSetter reasonObj;
    protected StockDataGetterSetter stockDataObj;
    protected StockPwpGwpDataGetterSetter stockPowObj;
    protected StockTesterDataGetterSetter stockTesterObj;
    protected StockSampleDataGetterSetter stockSampleObj;
}
