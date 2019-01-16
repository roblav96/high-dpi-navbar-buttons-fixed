
package com.roblav96.highdpinavbarbuttonsfixed;

import android.util.Log;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Surface;
import android.view.Display;
import android.util.SparseArray;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.*;



public class HighDpiNavbarButtonsFixed implements IXposedHookLoadPackage {
	
	private static final String TAG = "HighDpiNavbarButtonsFixed";
	private static int DIDIT = 0;
	
	// public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
	// 	if (resparam.packageName.equals("com.android.systemui")) {
	// 		resparam.res.hookLayout(CLASSNAME_SYSTEMUI, "layout", "navigation_bar", new XC_LayoutInflated() {});
	// 	}
	// }
	
	public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		Log.d(TAG, "handleLoadPackage ->");
		
		if(lpparam.packageName.equals("android")) {
			try {
				
				XposedHelpers.findAndHookMethod("com.android.server.policy.PhoneWindowManager", lpparam.classLoader, "navigationBarPosition", int.class, int.class, int.class, new XC_MethodReplacement() {
					@Override
					protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
						// Log.d(TAG, "com.android.server.policy.PhoneWindowManager: navigationBarPosition -> 4");
						return 4;
					}
				});
				XposedHelpers.findAndHookMethod("com.android.server.policy.PhoneWindowManager", lpparam.classLoader, "getNavBarPosition", new XC_MethodReplacement() {
					@Override
					protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
						// Log.d(TAG, "com.android.server.policy.PhoneWindowManager: getNavBarPosition -> 4");
						return 4;
					}
				});
				XposedHelpers.findAndHookMethod("com.android.server.policy.PhoneWindowManager", lpparam.classLoader, "getNavigationBarWidth", int.class, int.class, new XC_MethodReplacement() {
					@Override
					protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
						// Log.d(TAG, "com.android.server.policy.PhoneWindowManager: getNavigationBarWidth -> 192");
						return 192;
					}
				});
				XposedHelpers.findAndHookMethod("com.android.server.policy.PhoneWindowManager", lpparam.classLoader, "getNavigationBarHeight", int.class, int.class, new XC_MethodReplacement() {
					@Override
					protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
						// Log.d(TAG, "com.android.server.policy.PhoneWindowManager: getNavigationBarHeight -> 192");
						return 192;
					}
				});

			} catch(Throwable t) {
				XposedBridge.log(TAG + "ERROR ->" + t);
			}
		}
		
		if(lpparam.packageName.equals("com.android.systemui")) {
			try {
				
				XposedHelpers.findAndHookConstructor("com.android.systemui.statusbar.phone.NavigationBarInflaterView", lpparam.classLoader, Context.class, AttributeSet.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						try {
							Log.d(TAG, "com.android.systemui.statusbar.phone.NavigationBarInflaterView: Constructor ->");
							XposedHelpers.setObjectField(param.thisObject, "isRot0Landscape", false);
						} catch (Throwable t) {
							XposedBridge.log(TAG + "ERROR ->" + t);
						}
					}
				});
				XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.phone.NavigationBarInflaterView", lpparam.classLoader, "inflateLayout", String.class, new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						try {
							Log.d(TAG, "com.android.systemui.statusbar.phone.NavigationBarInflaterView: inflateLayout param.args -> " + param.args[0]);
							XposedHelpers.setObjectField(param.thisObject, "isRot0Landscape", false);
						} catch (Throwable t) {
							XposedBridge.log(TAG + "ERROR ->" + t);
						}
					}
				});
				XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.phone.NavigationBarInflaterView", lpparam.classLoader, "inflateButton", String.class, ViewGroup.class, boolean.class, boolean.class, new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						try {
							Log.d(TAG, "com.android.systemui.statusbar.phone.NavigationBarInflaterView: inflateButton param.args -> " + param.args[0]);
							param.args[2] = true;
						} catch (Throwable t) {
							XposedBridge.log(TAG + "ERROR ->" + t);
						}
					}
				});
				
				// XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.phone.NavigationBarView", lpparam.classLoader, "updateRotatedViews", new XC_MethodReplacement() {
				// 	@Override
				// 	protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				// 		Log.d(TAG, "com.android.systemui.statusbar.phone.NavigationBarView: updateRotatedViews ->");
				// 		View[] mRotatedViews = (View[]) XposedHelpers.getObjectField(param.thisObject, "mRotatedViews");
				// 		mRotatedViews[Surface.ROTATION_0] = mRotatedViews[Surface.ROTATION_180] =
				// 		mRotatedViews[Surface.ROTATION_270] = mRotatedViews[Surface.ROTATION_90] = findViewById(liparam.res.getIdentifier("nav_buttons", "id", );
				// 		XposedHelpers.callMethod(param.thisObject, "updateCurrentView");
				// 		return null;
				// 	}
				// });
				
				XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.phone.NavigationBarView", lpparam.classLoader, "updateCurrentView", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						try {
							Log.d(TAG, "com.android.systemui.statusbar.phone.NavigationBarView updateCurrentView ->");
							Log.d(TAG, "Surface.ROTATION_0->" + Surface.ROTATION_0 + " Surface.ROTATION_90->" + Surface.ROTATION_90 + " Surface.ROTATION_180->" + Surface.ROTATION_180 + " Surface.ROTATION_270->" + Surface.ROTATION_270);
							
							Display mDisplay = (Display) XposedHelpers.getObjectField(param.thisObject, "mDisplay");
							int rot = mDisplay.getRotation();
							Log.d(TAG, "rot -> " + rot);
							
							boolean mVertical = (boolean) XposedHelpers.getObjectField(param.thisObject, "mVertical");
							Log.d(TAG, "mVertical -> " + mVertical);
							
							// Log.d(TAG, "DIDIT -> " + DIDIT);
							// if (DIDIT == 0) {
							// 	DIDIT++;
							View[] mRotatedViews = (View[]) XposedHelpers.getObjectField(param.thisObject, "mRotatedViews");
							for (int i=0; i<4; i++) {
								mRotatedViews[i].setVisibility(View.GONE);
							}
							mRotatedViews[Surface.ROTATION_270] = mRotatedViews[Surface.ROTATION_0];
							mRotatedViews[Surface.ROTATION_90] = mRotatedViews[Surface.ROTATION_180];
							// mRotatedViews[Surface.ROTATION_0] = mRotatedViews[Surface.ROTATION_270];
							// XposedHelpers.setObjectField(param.thisObject, "mVertical", true);
							// }
							// XposedHelpers.setObjectField(param.thisObject, "mVertical", true);
							
						} catch (Throwable t) {
							XposedBridge.log(TAG + "ERROR ->" + t);
						}
					}
				});

				// XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.phone.NavigationBarView", lpparam.classLoader, "updateCurrentView", new XC_MethodReplacement() {
				// 	@Override
				// 	protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				// 		try {
				// 			Log.d(TAG, "com.android.systemui.statusbar.phone.NavigationBarView: updateCurrentView ->");
				// 			Display mDisplay = (Display) XposedHelpers.getObjectField(param.thisObject, "mDisplay");
				// 			final int rot = mDisplay.getRotation();
				// 			View[] mRotatedViews = (View[]) XposedHelpers.getObjectField(param.thisObject, "mRotatedViews");
				// 			for (int i=0; i<4; i++) {
				// 				mRotatedViews[i].setVisibility(View.GONE);
				// 			}
				// 			// mRotatedViews[Surface.ROTATION_0] = mRotatedViews[Surface.ROTATION_180] = mRotatedViews[Surface.ROTATION_90];
				// 			boolean mVertical = (boolean) XposedHelpers.getObjectField(param.thisObject, "mVertical");
				// 			if (rot == Surface.ROTATION_0 && mVertical) {
				// 				XposedHelpers.setObjectField(param.thisObject, "mCurrentView", mRotatedViews[Surface.ROTATION_90]);
				// 			} else {
				// 				XposedHelpers.setObjectField(param.thisObject, "mCurrentView", mRotatedViews[rot]);
				// 			}
				// 			View mCurrentView = (View) XposedHelpers.getObjectField(param.thisObject, "mCurrentView");
				// 			mCurrentView.setVisibility(View.VISIBLE);
				// 			View mNavigationInflaterView = (View) XposedHelpers.getObjectField(param.thisObject, "mNavigationInflaterView");
				// 			mNavigationInflaterView.setAlternativeOrder(rot == Surface.ROTATION_90);
				// 			SparseArray<ButtonDispatcher> mButtonDispatchers = (SparseArray<ButtonDispatcher>) XposedHelpers.getObjectField(param.thisObject, "mButtonDispatchers");
				// 			for (int i = 0; i < mButtonDispatchers.size(); i++) {
				// 				mButtonDispatchers.valueAt(i).setCurrentView(mCurrentView);
				// 			}
				// 		} catch(Throwable t) {
				// 			XposedBridge.log(TAG + "ERROR ->" + t);
				// 		}
				// 		return null;
				// 	}
				// });
				
			} catch(Throwable t) {
				XposedBridge.log(TAG + "ERROR ->" + t);
			}
		}
	}
}
