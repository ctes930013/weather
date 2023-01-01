package com.example.weather.model;

import com.example.weather.data.Location;
import com.example.weather.utils.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中華民國各縣市及鄉鎮市區處理相關
 *
 */
public class CityModel {

    /**
     * 使用者當前設定的縣市
     *
     */
    private String myCountry;

    /**
     * 使用者當前設定的鄉鎮市區
     *
     */
    private String myCity;

    public String getMyCountry(){
        return myCountry;
    }

    public void setMyCountry(String myCountry) {
        this.myCountry = myCountry;
    }

    public String getMyCity(){
        return myCity;
    }

    public void setMyCity(String myCity) {
        this.myCity = myCity;
    }


    /**
     * 各鄉鎮市區的map
     *
     */
    Map<String, List<String>> cityData = new HashMap<String, List<String>>() {{
        put("新北市", Arrays.asList(
                "板橋區", "三重區", "中和區", "永和區", "新莊區", "新店區", "樹林區", "鶯歌區", "三峽區",
                "淡水區", "汐止區", "瑞芳區", "土城區", "蘆洲區", "五股區", "泰山區", "林口區", "深坑區",
                "石碇區", "坪林區", "三芝區", "石門區", "八里區", "平溪區", "雙溪區", "貢寮區", "金山區",
                "萬里區", "烏來區"
                ));
        put("臺北市", Arrays.asList(
                "松山區","信義區", "大安區", "中山區", "中正區", "大同區", "萬華區", "文山區", "南港區",
                "內湖區", "士林區", "北投區"
        ));
        put("桃園市", Arrays.asList(
                "桃園區", "中壢區", "大溪區", "楊梅區", "蘆竹區", "大園區", "龜山區", "八德區", "龍潭區",
                "平鎮區", "新屋區", "觀音區", "復興區"
        ));
        put("臺中市", Arrays.asList(
                "中區", "東區", "南區", "西區", "北區", "西屯區", "南屯區", "北屯區", "豐原區",
                "東勢區", "大甲區", "清水區", "沙鹿區", "梧棲區", "后里區", "神岡區", "潭子區",
                "大雅區", "新社區", "石岡區", "外埔區", "大安區", "烏日區", "大肚區", "龍井區",
                "霧峰區", "太平區", "大里區", "和平區"
        ));
        put("臺南市", Arrays.asList(
                "新營區", "鹽水區", "白河區", "柳營區", "後壁區", "東山區", "麻豆區", "下營區", "六甲區",
                "官田區", "大內區", "佳里區", "學甲區", "西港區", "七股區", "將軍區", "北門區", "新化區",
                "善化區", "新市區", "安定區", "新化區", "善化區", "新市區", "安定區", "山上區", "玉井區",
                "楠西區", "南化區", "左鎮區", "仁德區", "歸仁區", "關廟區", "龍崎區", "永康區", "東區",
                "南區", "北區", "安南區", "安平區", "中西區"
        ));
        put("高雄市", Arrays.asList(
                "鹽埕區", "鼓山區", "左營區", "楠梓區", "三民區", "新興區", "前金區", "苓雅區", "前鎮區",
                "旗津區", "小港區", "林園區", "鳳山區", "大寮區", "大樹區", "大社區", "仁武區", "鳥松區",
                "岡山區", "橋頭區", "燕巢區", "田寮區", "阿蓮區", "路竹區", "湖內區", "茄萣區", "永安區",
                "彌陀區", "梓官區", "旗山區", "美濃區", "六龜區", "甲仙區", "杉林區", "內門區", "茂林區",
                "桃源區", "那瑪夏區"
        ));
        put("基隆市", Arrays.asList(
                "中正區", "七堵區", "暖暖區", "仁愛區", "中山區", "安樂區", "信義區"
        ));
        put("新竹市", Arrays.asList(
                "東區", "北區", "香山區"
        ));
        put("嘉義市", Arrays.asList(
                "東區", "西區"
        ));
        put("宜蘭縣", Arrays.asList(
                "宜蘭市", "羅東鎮", "蘇澳鎮", "頭城鎮", "礁溪鄉", "壯圍鄉", "員山鄉", "冬山鄉", "五結鄉",
                "三星鄉", "大同鄉", "南澳鄉"
        ));
        put("新竹縣", Arrays.asList(
                "竹北市", "關西鎮", "新埔鎮", "竹東鎮", "湖口鄉", "橫山鄉", "新豐鄉", "芎林鄉", "寶山鄉",
                "北埔鄉", "峨眉鄉", "尖石鄉", "五峰鄉"
        ));
        put("苗栗縣", Arrays.asList(
                "苗栗市", "頭份市", "苑裡鎮", "通霄鎮", "竹南鎮", "後龍鎮", "卓蘭鎮", "大湖鄉", "公館鄉",
                "銅鑼鄉", "南庄鄉", "頭屋鄉", "三義鄉", "西湖鄉", "造橋鄉", "三灣鄉", "獅潭鄉", "泰安鄉"
        ));
        put("彰化縣", Arrays.asList(
                "彰化市", "員林市", "鹿港鎮", "和美鎮", "北斗鎮", "溪湖鎮", "田中鎮", "二林鎮", "線西鄉",
                "伸港鄉", "福興鄉", "秀水鄉", "花壇鄉", "芬園鄉", "大村鄉", "埔鹽鄉", "埔心鄉", "永靖鄉",
                "社頭鄉", "二水鄉", "田尾鄉", "埤頭鄉", "芳苑鄉", "大城鄉", "竹塘鄉", "溪州鄉"
        ));
        put("南投縣", Arrays.asList(
                "南投市", "埔里鎮", "草屯鎮", "竹山鎮", "集集鎮", "名間鄉", "鹿谷鄉", "中寮鄉", "魚池鄉",
                "國姓鄉", "水里鄉", "信義鄉", "仁愛鄉"
        ));
        put("雲林縣", Arrays.asList(
                "斗六市", "斗南鎮", "虎尾鎮", "西螺鎮", "土庫鎮", "北港鎮", "古坑鄉", "大埤鄉", "莿桐鄉",
                "林內鄉", "二崙鄉", "崙背鄉", "麥寮鄉", "東勢鄉", "褒忠鄉", "臺西鄉", "元長鄉", "四湖鄉",
                "口湖鄉", "水林鄉"
        ));
        put("嘉義縣", Arrays.asList(
                "太保市", "朴子市", "布袋鎮", "大林鎮", "民雄鄉", "溪口鄉", "新港鄉", "六腳鄉", "東石鄉",
                "義竹鄉", "鹿草鄉", "水上鄉", "中埔鄉", "竹崎鄉", "梅山鄉", "番路鄉", "大埔鄉", "阿里山鄉"
        ));
        put("屏東縣", Arrays.asList(
                "屏東市", "潮州鎮", "東港鎮", "恆春鎮", "萬丹鄉", "長治鄉", "麟洛鄉", "九如鄉", "里港鄉",
                "鹽埔鄉", "高樹鄉", "萬巒鄉", "內埔鄉", "竹田鄉", "新埤鄉", "枋寮鄉", "新園鄉", "崁頂鄉",
                "林邊鄉", "南州鄉", "佳冬鄉", "琉球鄉", "車城鄉", "滿州鄉", "枋山鄉", "三地門鄉", "霧臺鄉",
                "瑪家鄉", "泰武鄉", "來義鄉", "春日鄉", "獅子鄉", "牡丹鄉"
        ));
        put("臺東縣", Arrays.asList(
                "臺東市", "成功鎮", "關山鎮", "卑南鄉", "大武鄉", "太麻里鄉", "東河鄉", "長濱鄉", "鹿野鄉",
                "池上鄉", "綠島鄉", "延平鄉", "海端鄉",  "達仁鄉", "金峰鄉", "蘭嶼鄉"
        ));
        put("花蓮縣", Arrays.asList(
                "花蓮市", "鳳林鎮", "玉里鎮", "新城鄉", "吉安鄉", "壽豐鄉", "光復鄉", "豐濱鄉", "瑞穗鄉",
                "富里鄉", "秀林鄉", "萬榮鄉", "卓溪鄉"
        ));
        put("澎湖縣", Arrays.asList(
                "馬公市", "湖西鄉", "白沙鄉", "西嶼鄉", "望安鄉", "七美鄉"
        ));
        put("金門縣", Arrays.asList(
                "金城鎮", "金湖鎮", "金沙鎮", "金寧鄉", "烈嶼鄉", "烏坵鄉"
        ));
        put("連江縣", Arrays.asList(
                "南竿鄉", "北竿鄉", "莒光鄉", "東引鄉"
        ));
    }};

    /**
     * 各縣市的未來一週天氣代碼的map
     *
     */
    Map<String, String> cityFutureCodeData = new HashMap<String, String>() {{
        put("新北市", "F-D0047-071");
        put("臺北市", "F-D0047-063");
        put("桃園市", "F-D0047-007");
        put("臺中市", "F-D0047-075");
        put("臺南市", "F-D0047-079");
        put("高雄市", "F-D0047-067");
        put("基隆市", "F-D0047-051");
        put("新竹市", "F-D0047-055");
        put("嘉義市", "F-D0047-059");
        put("宜蘭縣", "F-D0047-003");
        put("新竹縣", "F-D0047-011");
        put("苗栗縣", "F-D0047-015");
        put("彰化縣", "F-D0047-019");
        put("南投縣", "F-D0047-023");
        put("雲林縣", "F-D0047-027");
        put("嘉義縣", "F-D0047-031");
        put("屏東縣", "F-D0047-035");
        put("臺東縣", "F-D0047-039");
        put("花蓮縣", "F-D0047-043");
        put("澎湖縣", "F-D0047-047");
        put("金門縣", "F-D0047-087");
        put("連江縣", "F-D0047-083");
    }};

    /**
     * 各縣市的未來2天天氣代碼的map
     *
     */
    Map<String, String> cityCodeData = new HashMap<String, String>() {{
        put("新北市", "F-D0047-069");
        put("臺北市", "F-D0047-061");
        put("桃園市", "F-D0047-005");
        put("臺中市", "F-D0047-073");
        put("臺南市", "F-D0047-077");
        put("高雄市", "F-D0047-065");
        put("基隆市", "F-D0047-049");
        put("新竹市", "F-D0047-053");
        put("嘉義市", "F-D0047-057");
        put("宜蘭縣", "F-D0047-001");
        put("新竹縣", "F-D0047-009");
        put("苗栗縣", "F-D0047-013");
        put("彰化縣", "F-D0047-017");
        put("南投縣", "F-D0047-021");
        put("雲林縣", "F-D0047-025");
        put("嘉義縣", "F-D0047-029");
        put("屏東縣", "F-D0047-033");
        put("臺東縣", "F-D0047-037");
        put("花蓮縣", "F-D0047-041");
        put("澎湖縣", "F-D0047-045");
        put("金門縣", "F-D0047-085");
        put("連江縣", "F-D0047-081");
    }};

    /**
     * 根據鄉鎮市區取得對應的縣市
     *
     * @param city  鄉鎮市區
     * @return String  縣市
     */
    public String getCountyByCity(String city){
        String county = Constants.defaultCounty;
        for (Map.Entry<String, List<String>> entry : cityData.entrySet()) {
            String findCity = "";
            for(String item : entry.getValue()){
                if(item.equals(city)){
                    //有找到對應的城市了
                    findCity = city;
                    break;
                }
            }
            if(!"".equals(findCity)){
                county = entry.getKey();
                break;
            }
        }
        return county;
    }

    /**
     * 根據縣市取得對應的未來一週天氣代碼
     *
     * @param county  縣市
     * @return String  代碼
     */
    public String getFutureCodeByCounty(String county){
        String code = "";
        for (Map.Entry<String, String> entry : cityFutureCodeData.entrySet()) {
            if(entry.getKey().equals(county)){
                code = entry.getValue();
                break;
            }
        }
        if("".equals(code)){
            return getFutureCodeByCounty(Constants.defaultCounty);
        }
        return code;
    }

    /**
     * 根據縣市取得對應的未來2天天氣代碼
     *
     * @param county  縣市
     * @return String  代碼
     */
    public String getCodeByCounty(String county){
        String code = "";
        for (Map.Entry<String, String> entry : cityCodeData.entrySet()) {
            if(entry.getKey().equals(county)){
                code = entry.getValue();
                break;
            }
        }
        if("".equals(code)){
            return getCodeByCounty(Constants.defaultCounty);
        }
        return code;
    }
}
