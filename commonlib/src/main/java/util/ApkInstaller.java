package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;



/**
 * 弹出提示框，下载服务组件
 */
public class ApkInstaller {
	private Activity mActivity ;
	
	public ApkInstaller(Activity activity) {
		mActivity = activity;
	}

	public void install(){
//		Builder builder = new Builder(mActivity);
//		builder.setMessage("检测到您未安装语记！\n是否前往下载语记？");
//		builder.setTitle("下载提示");
//		builder.setPositiveButton("确认前往", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				String url = SpeechUtility.getUtility().getComponentUrl();
//				String assetsApk="SpeechService.apk";
//				processInstall(mActivity, url,assetsApk);
//			}
//		});
//		builder.setNegativeButton("残忍拒绝", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().toast();
		return;
	}
	/**
	 * 如果服务组件没有安装打开语音服务组件下载页面，进行下载后安装。
	 */
	private boolean processInstall(Context context ,String url,String assetsApk){
		//直接下载方式
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
		return true;		
	}
}
