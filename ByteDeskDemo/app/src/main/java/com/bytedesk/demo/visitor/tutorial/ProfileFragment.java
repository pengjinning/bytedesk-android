package com.bytedesk.demo.visitor.tutorial;

import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bytedesk.core.api.BDCoreApi;
import com.bytedesk.core.callback.BaseCallback;
import com.bytedesk.demo.R;
import com.bytedesk.demo.common.BaseFragment;
import com.bytedesk.ui.util.BDUiUtils;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.topbar) QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView) QMUIGroupListView mGroupListView;

    private String mDefaultNickname = "";
    private String mTagName;
    private String mTagKey;
    private String mTagValue = "";

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_profile, null);
        ButterKnife.bind(this, root);

        mTagName = "客服后台显示名";
        mTagKey = "myKey"; // 开发者可以自行改变key，并设置相应的value，此处只为演示目的

        initTopBar();
        initGroupListView();

        return root;
    }

    private void initTopBar() {
        mTopBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_color_blue));
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {popBackStack();
            }
        });
        mTopBar.setTitle("设置用户信息接口");
    }

    private void initGroupListView() {

        final QMUICommonListItemView nicknameItem = mGroupListView.createItemView("昵称");
        nicknameItem.setDetailText(mDefaultNickname);

        QMUIGroupListView.newSection(getContext())
                .setTitle("默认用户信息接口")
//                .setDescription("默认描述")
                .addItemView(nicknameItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //
                        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
                        builder.setTitle("标题")
                                .setPlaceholder("在此输入您的昵称")
                                .setInputType(InputType.TYPE_CLASS_TEXT)
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {dialog.dismiss();
                                    }
                                })
                                .addAction("确定", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        final CharSequence text = builder.getEditText().getText();
                                        if (text != null && text.length() > 0) {
//                                            Toast.makeText(getActivity(), "您的昵称: " + text, Toast.LENGTH_SHORT).show();

                                              mDefaultNickname = text.toString();
                                              // 调用接口设置昵称
                                              BDCoreApi.visitorSetNickname(getContext(), mDefaultNickname, new BaseCallback() {
                                                  @Override
                                                  public void onSuccess(JSONObject object) {
                                                      //解析 object
//                                                      try {
//                                                          String nickname = object.getJSONObject("data").getString("nickname");
//                                                      } catch (JSONException e) {
//                                                          e.printStackTrace();
//                                                      }
                                                      nicknameItem.setDetailText(mDefaultNickname);
                                                  }

                                                  @Override
                                                  public void onError(JSONObject object) {
                                                      BDUiUtils.showTipDialog(getContext(), "设置昵称失败");
                                                  }
                                              });

                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), "请填入昵称", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
                    }
                })
                .addTo(mGroupListView);

        final QMUICommonListItemView selfDefineItem = mGroupListView.createItemView("自定义标签");
        selfDefineItem.setDetailText(mTagValue);

        QMUIGroupListView.newSection(getContext())
                .setTitle("自定义用户信息接口")
//                .setDescription("自定义描述")
                .addItemView(selfDefineItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//
                        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
                        builder.setTitle("标题")
                                .setPlaceholder("在此输入自定义标签")
                                .setInputType(InputType.TYPE_CLASS_TEXT)
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction("确定", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        final CharSequence text = builder.getEditText().getText();
                                        if (text != null && text.length() > 0) {

                                            // 调用接口设置自定义标签
                                            mTagValue = text.toString();
                                            BDCoreApi.visitorSetUserInfo(getContext(), "", mTagKey, mTagValue, new BaseCallback() {
                                                @Override
                                                public void onSuccess(JSONObject object) {
                                                    selfDefineItem.setDetailText(mTagValue);
                                                }

                                                @Override
                                                public void onError(JSONObject object) {
                                                    BDUiUtils.showTipDialog(getContext(), "设置自定义标签失败");
                                                }
                                            });

                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), "请填入自定义标签值", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
                    }
                })
                .addTo(mGroupListView);


        ///////
        BDCoreApi.visitorGetUserInfo(getContext(), new BaseCallback() {

            @Override
            public void onSuccess(JSONObject object) {
                try {
                    Logger.d("get userinfo success message: " + object.get("message")
                            + " status_code:" + object.get("status_code")
                            + " data:" + object.get("data"));

                    //
                    String nickname = object.getJSONObject("data").getString("nickname");
                    nicknameItem.setDetailText(nickname);

                    // 解析自定义key/value
                    JSONArray jsonArray = object.getJSONObject("data").getJSONArray("fingerPrints");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String key = jsonObject.getString("key");
                        String value = jsonObject.getString("value");
                        if (key.equals(mTagKey)) {
                            selfDefineItem.setDetailText(value);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(JSONObject object) {
                try {
                    Logger.e("获取用户信息错误: " + object.get("message")
                            + " status_code:" + object.get("status_code")
                            + " data:" + object.get("data"));

                    BDUiUtils.showTipDialog(getContext(), "获取个人资料失败");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    @Override
    public QMUIFragment.TransitionConfig onFetchTransitionConfig() {
        return SCALE_TRANSITION_CONFIG;
    }


}
