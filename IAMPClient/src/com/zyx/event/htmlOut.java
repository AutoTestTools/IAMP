package com.zyx.event;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.zyx.bluetooth.CurReqPage;
import com.zyx.info.BluetoothInfo;
import com.zyx.litepal.Data;
import com.zyx.litepal.DataNew;

public class htmlOut {
	static ArrayList<String> temp = new ArrayList<String>();
	static ArrayList<ArrayList<String>> group_name = new ArrayList<ArrayList<String>>();
	static String sdPath = Environment.getExternalStorageDirectory().toString();
	static String strFilePath = sdPath + "/IAMP/report";

	@SuppressLint("SdCardPath")
	public static boolean creathtml() {

		// List<Data> dataList = dataProcessing();
		List<Data> dataList;
		if (CurReqPage.isTalking()) {
			dataList = DataSupport.where("from_mac = ? or to_mac = ?", CurReqPage.getTalker_mac(), CurReqPage.getTalker_mac()).find(Data.class);
			Log.e("====", "=" + CurReqPage.getTalker_mac() + "=");
		} else {
			dataList = DataSupport.findAll(Data.class);// 查询所有信息，未经处理得

		}
		try {
			return savefile(dataList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@SuppressWarnings("null")
	private static List<Data> dataProcessing() {
		// 将通讯记录提取为：“发送方-接收方-请求消息-响应消息-时间” 的形式
		// q1,消息响应不及时，迅速发了两条请求后，两条响应才出现
		// Q2，可以将发送方与接收方的单元格合并吗
		String localMac = BluetoothInfo.getOneAddress();
		String localName = BluetoothInfo.getOneName();
		List<Data> dataList = DataSupport.where("from_mac = ? and msg != ? or to_mac = ? and msg = ?", localMac, "好的，我已拨打", localMac, "好的，我已拨打").find(
				Data.class);// 筛选出本机主叫的记录
		List<DataNew> dataNewList = null;
		// List<ArrayList<String>> newList = new ArrayList<ArrayList<String>>();
		// ArrayList<String> itemList = new ArrayList<String>();
		// Data mData = new Data();

		DataNew dataNew = new DataNew();
		for (int i = 0; i < dataNewList.size(); i++) {// 遍历记录列表
			dataNew = dataNewList.get(i);
			if (dataNew.getFrom_mac().equals(localMac)) {
				dataNewList.add(dataNew);

			} else {

			}
		}
		return dataList;
	}

	private static boolean savefile(List<Data> datasList) throws IOException {
		if (datasList.size() > 0) {
			BufferedWriter bufferedWriter = null;
			File file = new File(strFilePath);
			if (!file.exists()) {
				Log.e("----", "into make file");
				file.mkdirs();
				File f = new File(file, "Message.html");
				f.createNewFile();
			}
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strFilePath + "/Message.html", false)));
			bufferedWriter.write(htmltop());
			bufferedWriter.write("<br>\n");
			bufferedWriter.write(addfileitem());// 文件头
			Data mData = new Data();
			for (int i = 0; i < datasList.size(); i++) {
				Log.e("newsList.size()", "" + datasList.size());
				mData = datasList.get(i);
				bufferedWriter.write("<tr>\n");
				bufferedWriter.write(addHtmlItem(mData));
				bufferedWriter.write("</tr>\n");
			}
			bufferedWriter.write(htmlend());
			bufferedWriter.close();
			return true;
		} else {
			return false;
		}
	}

	private static String htmlend() {
		// TODO Auto-generated method stub
		String a2;
		a2 = "</tr>\n";
		a2 += "</tbody>\n";
		a2 += "</table>\n";
		a2 += "<SCRIPT type=text/javascript>\n";
		a2 += "    $(function(){\n";
		a2 += "       $(\".jqzoom\").jqueryzoom({\n";
		a2 += "xzoom:400,\n";
		a2 += "yzoom:355,\n";
		a2 += "offset:10,\n";
		a2 += "position:\"right\",\n";
		a2 += "preload:1,\n";
		a2 += "lens:1\n";
		a2 += "        });\n";
		a2 += "        $(\"#spec-list\").jdMarquee({\n";
		a2 += "deriction:\"left\",\n";
		a2 += "width:350,\n";
		a2 += "height:56,\n";
		a2 += "step:2,\n";
		a2 += "speed:4,\n";
		a2 += "delay:10,\n";
		a2 += "control:true,\n";
		a2 += "_front:\"#spec-right\",\n";
		a2 += "_back:\"#spec-left\"\n";
		a2 += "        });\n";
		a2 += "        $(\"#spec-list img\").bind(\"mouseover\",function(){\n";
		a2 += "var src=$(this).attr(\"src\");\n";
		a2 += "$(\"#spec-n1 img\").eq(0).attr({\n";
		a2 += "    src:src.replace(\"\\/n5\\/\",\"\\/n1\\/\"),\n";
		a2 += "    jqimg:src.replace(\"\\/n5\\/\",\"\\/n0\\/\")\n";
		a2 += "});\n";
		a2 += "$(this).css({\n";
		a2 += "    \"border\":\"2px solid #ff6600\",\n";
		a2 += "    \"padding\":\"1px\"\n";
		a2 += "});\n";
		a2 += "        }).bind(\"mouseout\",function(){\n";
		a2 += "$(this).css({\n";
		a2 += "    \"border\":\"1px solid #ccc\",\n";
		a2 += "    \"padding\":\"2px\"\n";
		a2 += "});\n";
		a2 += "        });\n";
		a2 += "    })\n";
		a2 += "    </SCRIPT>\n";
		a2 += "<SCRIPT src=\"js/lib.js\" type=text/javascript></SCRIPT>\n";
		a2 += "<SCRIPT src=\"js/163css.js\" type=text/javascript></SCRIPT>\n";
		a2 += "</body>\n";
		a2 += "</html>\n";
		return a2;
	}

	private static String htmltop() {
		// TODO Auto-generated method stub
		String a1;
		a1 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
		a1 += "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n";
		a1 += "<head>\n";
		a1 += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n";
		a1 += "<title>MEIZU AutoTest</title>\n";
		a1 += "<link href=\"js/css.css\" type=\"text/css\" rel=\"stylesheet\">\n";
		a1 += "<SCRIPT src=\"js/jquery-1.2.6.pack.js\" type=text/javascript></SCRIPT>\n";
		a1 += "<SCRIPT src=\"js/base.js\" type=text/javascript></SCRIPT>\n";
		a1 += "</head>\n";
		a1 += "<body>\n";
		return a1;
	}

	private static String addHtmlItem(Data mdata) {
		String a1 = "";

		a1 += "<td>" + mdata.getFrom_name() + "<br>" + mdata.getFrom_mac() + "</td>\n";
		a1 += "<td>" + mdata.getTo_name() + "<br>" + mdata.getTo_mac() + "</td>\n";
		a1 += "<td>" + mdata.getMsg() + "</td>\n";
		a1 += "<td>" + mdata.getTime() + "</td>\n";
		return a1;
	}

	private static String addfileitem() {
		// TODO Auto-generated method stub
		String a1;
		a1 = "<table height=\"199\" table border=\"1\" cellspacing=\"0px\" style=\"border-collapse:collapse\">\n";
		a1 += "<tbody>\n";
		a1 += "<tr bordercolor=\"#000000\" bgcolor=\"#FFFFFF\">\n";
		String filestr = "我的聊天记录" + "<p>本机昵称：&#9" + BluetoothInfo.getOneName() + "<br>本机地址：&#9" + BluetoothInfo.getOneAddress();
		a1 += "<th colspan=\"4\"><strong>" + filestr + "</strong></th>\n";
		a1 += "</tr>\n";
		a1 += "<tr >\n";

		a1 += "<th width=\"150\"><strong>发送方</strong></th>\n";
		a1 += "<th width=\"150\"><strong>接收方 </strong></th>\n";
		a1 += "<th width=\"200\"><strong>消息</strong></th>\n";
		a1 += "<th width=\"200\"><strong>通信时间</strong></th>\n";// 还有参数是表示蓝牙还是WiFi的通信
		a1 += "</tr>\n";
		return a1;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// SQLiteDatabase db = Connector.getDatabase();
		for (int i = 0; i < 3; i++) {
			Data mData = new Data();
			mData.setFrom_mac("mac" + i);
			mData.setFrom_name("name" + i);
			mData.save();
		}
		List<Data> newsList = DataSupport.findAll(Data.class);

		for (int i = 0; i < newsList.size(); i++) {
			Data mData = newsList.get(i);
			System.out.println(mData.getFrom_mac());
			System.out.println(mData.getFrom_name());
		}
	}
}