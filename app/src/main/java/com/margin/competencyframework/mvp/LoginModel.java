package com.margin.competencyframework.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.margin.competencyframework.CompetencyFrameworkApplication;
import com.margin.competencyframework.model.APIError;
import com.margin.competencyframework.model.AccessToken;
import com.margin.competencyframework.model.AuthenticateRequest;
import com.margin.competencyframework.model.User;
import com.margin.competencyframework.rest.RestAPI;
import com.margin.competencyframework.util.Constants;
import com.margin.competencyframework.util.GATrackerUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Martha on 1/7/2017.
 */
public class LoginModel  implements LoginContract.Model {

    private final LoginContract.Presenter mPresenter;
    private final RestAPI mRestAPI;
    private final Converter<ResponseBody, APIError> mErrorConverter;

    public LoginModel(LoginContract.Presenter presenter) {
        mPresenter = presenter;
        mRestAPI = CompetencyFrameworkApplication.appConnectivityServices().getOrdinaryRestApi();
        mErrorConverter = CompetencyFrameworkApplication.appConnectivityServices().getErrorConverter();
    }

    @Override
    public void authenticate(AuthenticateRequest authenticateRequest) {

        Call<AccessToken> call = mRestAPI.authenticate(authenticateRequest);

        // TODO for google analytics later to be implemented
//        GATrackerUtils.trackEvent(CompetencyFrameworkApplication.appConnectivityServices().getAppContext(),
//                Constants.EventCategory.REST_API_REQUEST,
//                API_VERSION + CLIENTS + "/authenticate",
//                GsonUtil.getGson().toJson(authenticateRequest), 0);
        mPresenter.addCall(call);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response != null) {
                    if (!response.isSuccessful() && response.errorBody() != null) {
                        try {
                            APIError error = mErrorConverter.convert(response.errorBody());
                            int responseCode = error.getStatus();
                            mPresenter.onAuthError(error, responseCode);
                            // TODO for google analytics later to be implemented
//                            GATrackerUtils.trackEvent(CompetencyFrameworkApplication.appConnectivityServices().getAppContext(),
//                                    Constants.EventCategory.REST_API_RESPONSE,
//                                    API_VERSION + CLIENTS + "/authenticate",
//                                    error.toString(), 0);
                        } catch (IOException e) {
                            mPresenter.onAuthFailure(e.getCause());
                            e.printStackTrace();
                            // TODO for google analytics later to be implemented
//                            GATrackerUtils.trackException(
//                                    CompetencyFrameworkApplication.getAppComponent().getAppContext(), e);
                        }
                    } else {
                        if (response.isSuccessful()) {
                            AccessToken loginResponse = response.body();
                            mPresenter.onAuthSuccess(loginResponse);
//                            GATrackerUtils.trackEvent(CompetencyFrameworkApplication.appConnectivityServices().getAppContext(),
//                                    Constants.EventCategory.REST_API_RESPONSE,
//                                    API_VERSION + CLIENTS + "/authenticate",
//                                    loginResponse.toString(), 0);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                mPresenter.onAuthFailure(t);
                GATrackerUtils.trackException(
                        CompetencyFrameworkApplication.appConnectivityServices().getAppContext(), t);
            }
        });
    }

    @Override
    public void getUserInfo(@NonNull String id, @Nullable String filter, @NonNull String accessToken) {
        Call<User> call = mRestAPI.getUser(id, filter, accessToken);
        UserRequest request = new UserRequest(filter, accessToken);
        GATrackerUtils.trackEvent(CompetencyFrameworkApplication.getAppComponent().getAppContext(),
                Constants.EventCategory.REST_API_REQUEST,
                API_VERSION + CLIENT + "/" + id,
                GsonUtil.getGson().toJson(request), 0);
        mPresenter.addCall(call);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        APIError error = mErrorConverter.convert(response.errorBody());
                        int responseCode = error.getStatus();
                        mPresenter.onGetUserError(error, responseCode);
                        GATrackerUtils.trackEvent(CompetencyFrameworkApplication.getAppComponent().getAppContext(),
                                Constants.EventCategory.REST_API_RESPONSE,
                                API_VERSION + CLIENT + "/" + id,
                                error.toString(), 0);
                    } catch (IOException e) {
                        mPresenter.onGetUserFailure(e.getCause());
                        e.printStackTrace();
                        GATrackerUtils.trackException(
                                CompetencyFrameworkApplication.getAppComponent().getAppContext(), e);
                    }
                } else if (null != response) {
                    if (response.isSuccessful()) {
                        User loginResponse = response.body();
                        mPresenter.onGetUserSuccess(loginResponse);
                        GATrackerUtils.trackEvent(CompetencyFrameworkApplication.getAppComponent().getAppContext(),
                                Constants.EventCategory.REST_API_RESPONSE,
                                API_VERSION + CLIENT + "/" + id,
                                GsonUtil.getGson().toJson(loginResponse), 0);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mPresenter.onGetUserFailure(t);
                GATrackerUtils.trackException(
                        CompetencyFrameworkApplication.getAppComponent().getAppContext(), t);
            }
        });
    }
}
