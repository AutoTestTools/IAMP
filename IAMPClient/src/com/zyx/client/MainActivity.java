package com.zyx.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.zyx.bluetooth.BluetoothActivity;
import com.zyx.event.Model;
import com.zyx.iamp.client.R;
import com.zyx.info.Properties;
import com.zyx.socket.NewRoomActivity;

public class MainActivity extends Activity {

	private File PRO_DIR = new File(Environment.getExternalStorageDirectory().toString() + "/IAMP");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

	}

	public void model(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.wifi_model:
			intent.setClass(MainActivity.this, NewRoomActivity.class);
			Model.setCurModel(Properties.WIFI_MODEL);
			break;
		case R.id.buletooth_model:
			intent.setClass(MainActivity.this, BluetoothActivity.class);
			Model.setCurModel(Properties.BLUETOOTH_MODEL);
			break;
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void demo(View view) {
		
		installApk(getAssetFile());
		
	}

	/**
	 * 获取assets资源文件夹中的文件
	 * 
	 * @return
	 */
	private File getAssetFile() {
		File f = null;
		AssetManager asset = MainActivity.this.getAssets();
		String fileName = "demo.apk";
		try {
			PRO_DIR.mkdirs();
			InputStream is = asset.open(fileName);
			f = new File(PRO_DIR, fileName);
			f.createNewFile();
			FileOutputStream fOut = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fOut.write(buffer, 0, len);
			}
			fOut.flush();
			is.close();
			fOut.close();
			return f;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void installApk(File file) {
		boolean isinstall = false;
		PackageManager pManager = this.getPackageManager();
		List<PackageInfo> appList = pManager.getInstalledPackages(0);
		for (int i = 0; i < appList.size(); i++) {
			PackageInfo pak = (PackageInfo) appList.get(i);
			String mPak = pak.packageName;
			if (mPak.contains("com.iamp.demo")) {
				isinstall = true;
				break;
			}
		}
		if (!isinstall) {
			Toast.makeText(this, "您未安装demo,请先安装!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setClassName("com.android.packageinstaller", "com.android.packageinstaller.PackageInstallerActivity");
			String type = "android/vnd.android.package-archive";
			intent.setDataAndType(Uri.fromFile(file), type);
			this.startActivity(intent);
		}else{
			Intent intent = new Intent(Intent.ACTION_MAIN);  
			intent.addCategory(Intent.CATEGORY_LAUNCHER);   
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ComponentName cn = new ComponentName("com.iamp.demo", "com.iamp.demo.MainActivity");              
			intent.setComponent(cn);  
			startActivity(intent);  
		}
	}

}
