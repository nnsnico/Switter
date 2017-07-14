package com.excercise.nns.androidex.viewmodel;

import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.model.data.Token;

import java.util.IllegalFormatCodePointException;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by nns on 2017/06/19.
 */

public class TimelineViewModel {
    private TimelineContract contract;

    public TimelineViewModel(TimelineContract contract) {
        this.contract = contract;

        // open Realm
        final Realm realm = Realm.getDefaultInstance();     //1
        try {
            RealmQuery<Token> query = realm.where(Token.class);
            RealmResults<Token> result = query.findAll();
            Token token = result.first();
            // AccessTokenがあるか
            if (!token.hasAccessToken()) {
                realm.close();                              //1
                contract.onStartOAuth();
            } else {
                realm.close();                              //1
                // TODO: タイムラインの取得
            }
        } catch (IllegalArgumentException e) {
            realm.close();
            contract.onStartOAuth();
        }
    }
}
