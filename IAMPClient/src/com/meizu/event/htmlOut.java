package com.meizu.event;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.litepal.crud.DataSupport;

import com.meizu.info.BluetoothInfo;
import com.meizu.litepal.Data;
import com.meizu.litepal.DataNew;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class htmlOut {
	static ArrayList<String> temp = new ArrayList<String>();
	static ArrayList<ArrayList<String>> group_name = new ArrayList<ArrayList<String>>();
	static String sdPath = Environment.getExternalStorageDirectory().toString();

	@SuppressLint("SdCardPath")
	public static void creathtml() {

		// DataSupport.deleteAll(Data.class);// test
		// for (int i = 0; i < 3; i++) {
		// Data mData = new Data();
		// mData.setFrom_mac("mac" + i);
		// mData.setFrom_name("name" + i);
		// mData.setTo_mac("tomac" + i);
		// mData.setTo_name("toname" + i);
		// mData.setMsg("msg" + i);
		// mData.setTime("20160101" + i);
		// mData.save();
		// }

		// List<Data> dataList = dataProcessing();

		List<Data> dataList = DataSupport.findAll(Data.class);// ��ѯ������Ϣ��δ�������
		try {
			savefile(dataList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("null")
	private static List<Data> dataProcessing() {
		// ��ͨѶ��¼��ȡΪ�������ͷ�-���շ�-������Ϣ-��Ӧ��Ϣ-ʱ�䡱 ����ʽ
		// q1,��Ϣ��Ӧ����ʱ��Ѹ�ٷ������������������Ӧ�ų���
		// Q2�����Խ����ͷ�����շ��ĵ�Ԫ��ϲ���
		String localMac = BluetoothInfo.getOneAddress();
		String localName = BluetoothInfo.getOneName();
		List<Data> dataList = DataSupport.where("from_mac = ? and msg != ? or to_mac = ? and msg = ?", localMac, "�õģ����Ѳ���", localMac, "�õģ����Ѳ���").find(
				Data.class);// ɸѡ���������еļ�¼
		List<DataNew> dataNewList = null;
		// List<ArrayList<String>> newList = new ArrayList<ArrayList<String>>();
		// ArrayList<String> itemList = new ArrayList<String>();
		// Data mData = new Data();

		DataNew dataNew = new DataNew();
		for (int i = 0; i < dataNewList.size(); i++) {// ������¼�б�
			dataNew = dataNewList.get(i);
			if (dataNew.getFrom_mac().equals(localMac)) {
				dataNewList.add(dataNew);

			} else {

			}
		}
		return dataList;
	}

	private static void savefile(List<Data> datasList) throws IOException {
		BufferedWriter f = null;

		// f = new BufferedWriter(new OutputStreamWriter(new
		// FileOutputStream(sdPath + "/AReporter/Dialog.html", false)));
		f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sdPath + "/Dialog.html", false)));
		f.write(htmltop());
		f.write("<br>\n");
		f.write(addfileitem());// �ļ�ͷ

		Data mData = new Data();
		for (int i = 0; i < datasList.size(); i++) {
			Log.e("newsList.size()", "" + datasList.size());
			mData = datasList.get(i);
			f.write("<tr>\n");
			f.write(addHtmlItem(mData));
			f.write("</tr>\n");
		}
		f.write(htmlend());
		f.close();
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
		// a1 += "<td>" + mdata.getFrom_mac() + "</td>\n";
		// a1 += "<td>" + mdata.getFrom_name() + "</td>\n";
		// a1 += "<td>" + mdata.getTo_mac() + "</td>\n";
		// a1 += "<td>" + mdata.getTo_name() + "</td>\n";

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
		String filestr = "�ҵ������¼" + "<p>�����ǳƣ�&#9" + BluetoothInfo.getOneName() + "<br>������ַ��&#9" + BluetoothInfo.getOneAddress();
		a1 += "<th colspan=\"4\"><strong>" + filestr + "</strong></th>\n";
		a1 += "</tr>\n";
		a1 += "<tr >\n";
		// a1 += "<th width=\"150\"><strong>���ͷ���ַ</strong></th>\n";
		// a1 += "<th width=\"150\"><strong>���ͷ��ǳ� </strong></th>\n";
		// a1 += "<th width=\"150\"><strong>���շ���ַ</strong></th>\n";
		// a1 += "<th width=\"150\"><strong>���շ��ǳ� </strong></th>\n";

		a1 += "<th width=\"150\"><strong>���ͷ�</strong></th>\n";
		a1 += "<th width=\"150\"><strong>���շ� </strong></th>\n";
		a1 += "<th width=\"200\"><strong>��Ϣ</strong></th>\n";
		a1 += "<th width=\"200\"><strong>ͨ��ʱ��</strong></th>\n";// ���в����Ǳ�ʾ��������WiFi��ͨ��
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