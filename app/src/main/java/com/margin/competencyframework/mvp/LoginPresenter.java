package com.margin.competencyframework.mvp;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.margin.competencyframework.CompetencyFrameworkApplication;
import com.margin.competencyframework.Listener.MailValidityChecker;
import com.margin.competencyframework.R;
import com.margin.competencyframework.model.APIError;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import rx.Subscription;

/**
 * Created by Martha on 1/7/2017.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private static final String KEY_BARCODE_EDITTEXT = "key_barcode_edittext";

    private final Context mContext;
    private final LoginContract.View mView;
    private final LoginContract.Model mModel;
    private final ArrayMap<String, Subscription> mSubscriptionList;
    private final MailValidityChecker mMailValidityChecker;
    private List<Call> mCallList = new ArrayList<>();
    private boolean mCanLogin = true;

    public LoginPresenter(LoginContract.View view) {
        mContext = CompetencyFrameworkApplication.getAppComponent().getAppContext();
        mView = view;
        mModel = new LoginModel(this);
        mSubscriptionList = new ArrayMap<>();
        mMailValidityChecker = new MailValidityChecker() {

            Reason reason = Reason.OK;

            @Override
            public boolean check(String pin) {
                boolean isEmpty = TextUtils.isEmpty(pin);
                if (isEmpty) {
                    reason = Reason.WRONG_LENGTH;
                }
                return !isEmpty;
            }

            @Override
            public Reason getReason() {
                return reason;
            }
        };
    }

    @Override
    public void create() {
        if (PrefsUtils.isUserSignedIn() && null != mView) {
            mView.launchTaskManager();
        }
        delegateAddBarcodeTextChangeListener();
    }

    @Override
    public void destroy() {
        for (Subscription s : mSubscriptionList.values()) s.unsubscribe();

        cancelCalls();
    }

    @Override
    public void verifyPin(String pin) {
        if (null != mView) {
            if (!mMailValidityChecker.check(pin)) {
                String errorMsg = mContext.getString(mMailValidityChecker.getReason().stringResId);
                mView.setErrorMessage(errorMsg);
                return;
            }
            mView.showProgress(true);
            performLogin(pin);
        }
    }

    @Override
    public void performLogin(String pin) {
        if (ConnectivityUtils.isNetworkAvailable(mContext)) {
            setCanLogin(false);
            AuthenticateRequest authenticateRequest = new AuthenticateRequest();
            authenticateRequest.pin = pin;
            mModel.authenticate(authenticateRequest);
        } else {
            if (null != mView) {
                mView.showToast(mContext.getString(R.string.message_no_internet));
            }
        }
    }

    @Override
    public void addCall(Call call) {
        mCallList.add(call);
    }

    @Override
    public void onAuthError(APIError apiError, int responseCode) {
        setCanLogin(true);
        if (null != mView) {
            mView.showProgress(false);
            switch (responseCode) {
                case HttpResponseCode.CODE_401:
                case HttpResponseCode.CODE_403:
                    mView.setErrorMessage(mContext.getString(R.string.error_invalid_pin));
                    break;
                case HttpResponseCode.CODE_200:
                    mView.showToast(apiError.getMessage(),
                            apiError.getName(),
                            Integer.toString(apiError.getStatusCode()));
                    break;
            }
        }
    }

    @Override
    public void onAuthSuccess(AccessToken accessToken) {
        if (null != mView) {
            mView.showProgress(false);
            PrefsUtils.putAuthData(accessToken.getId(), accessToken.getUserId());
            mModel.getUserInfo(accessToken.getUserId(), null, accessToken.getId());
        }
    }

    @Override
    public void onAuthFailure(Throwable t) {
        setCanLogin(true);
        if (null != mView) {
            mView.showToast(t.getMessage());
            mView.showProgress(false);
        }
    }

    @Override
    public void onGetUserError(APIError apiError, int responseCode) {
        setCanLogin(true);
        LogUtils.e("Error getting user: " + apiError.getMessage() + ", response code: " +
                responseCode + ".");
    }

    @Override
    public void onGetUserSuccess(User user) {
        PrefsUtils.putUserData(user.getUsername(), user.getDefaultGateway(), user.getEmail());
        if (null != mView) mView.launchTaskManager();
        setCanLogin(true);
    }

    @Override
    public void onGetUserFailure(Throwable t) {
        setCanLogin(true);
        LogUtils.e("Failure getting user: " + t.getMessage() + ".");
    }

    @Override
    public void cancelCalls() {
        for (Call call : mCallList) call.cancel();
    }

    @Override
    public void setCanLogin(boolean canLogin) {
        this.mCanLogin = canLogin;
    }

    @Override
    public boolean canLogin() {
        return mCanLogin;
    }

    @Override
    public void onLoginClicked(String pin) {
        if (canLogin()) verifyPin(pin);
    }
}
