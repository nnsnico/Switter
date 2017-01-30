package com.example.user.lskiller.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;

import java.io.File;
import java.io.FileNotFoundException;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by USER on 2016/08/16.
 */
public class TweetActivity extends FragmentActivity {

    private EditText mInputText;
    private Twitter mTwitter;
    private TextView countText;
    private TextView countMedia;
    private Intent saveData;
    private final static int REQUEST_PICK = 1001;
    private StatusUpdate statusUpdate;
    private File path;
    private long[] mediaIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        getWindow().setEnterTransition(new Slide());

        mTwitter = TwitterUtils.getTwitterInstance(this);
        mInputText = (EditText) findViewById(R.id.input_text);
        countTweet();
        countText = (TextView) findViewById(R.id.countText);
        countMedia = (TextView) findViewById(R.id.uploadedMediaCount);

        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet();
            }
        });

        findViewById(R.id.action_media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // 選択可能にする
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode != REQUEST_PICK || resultCode != RESULT_OK) {
            return;
        }
        saveData = data;
        countStatusMedia(saveData);
    }

    private void countStatusMedia(final Intent data) {

        if (data.getData() != null) {
            final Uri uri = data.getData();
            /** path 変換 */
            final String fileName = getPathFromUri(TweetActivity.this, uri);
            path = new File(fileName);
            countMedia.setText("1枚アップロードされています");

        } else {
            // setMedia
            int count = saveData.getClipData().getItemCount();
            countMedia.setText(count + "枚アップロードされています");
        }
    }

    private void countTweet() {
        mInputText.addTextChangedListener(new TextWatcher() {
            int textLength = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int textColor = Color.WHITE;
                countText.setTextColor(textColor);

                // 入力文字数の表示
                String s = charSequence.toString();
                textLength = s.length();

                // 指定文字数オーバーで文字色を赤くする
                if (textLength > 140) {
                    textColor = Color.RED;
                    countText.setTextColor(textColor);
                }
                countText.setText(String.format("%s/140", Integer.toString(140 - textLength)));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //
    private void tweet() {
        final ProgressDialog progressDialog = new ProgressDialog(TweetActivity.this);
        progressDialog.setMessage("ツイート中・・・");
        progressDialog.show();

        final AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                statusUpdate = new StatusUpdate(params[0]);
                try {
                    if (saveData != null) {
                        if (saveData.getData() != null) {
                            statusUpdate.setMedia(path);
                            mTwitter.updateStatus(statusUpdate);
                        } else {
                            Log.d("saveData", "media has already saved");
                            if (saveData.getClipData().getItemCount() <= 4) {
                                mediaIds = uploadMediaIds(saveData);
                                statusUpdate.setMediaIds(mediaIds);
                                mTwitter.updateStatus(statusUpdate);
                            } else {
                                return false;
                            }
                        }
                    } else {
                        Log.d("saveData", "media not saved");
                        statusUpdate = new StatusUpdate(params[0]);
                        mTwitter.updateStatus(statusUpdate);
                    }
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast("ツイートしました");
                    progressDialog.dismiss();
                    finish();
                } else {
                    showToast("ツイート出来ませんでした");
                    progressDialog.dismiss();
                }
            }
        };
        task.execute(mInputText.getText().toString());
    }

    private long[] uploadMediaIds(Intent saveData) {
        mediaIds = new long[saveData.getClipData().getItemCount()];
        for (int i = 0, length = saveData.getClipData().getItemCount(); i < length; i++) {
            ClipData.Item item = saveData.getClipData().getItemAt(i);
            try {
                mediaIds[i] = mTwitter.uploadMedia(
                        String.format("[filename_%d]", i + 1),
                        getContentResolver().openInputStream(item.getUri())
                ).getMediaId();
            } catch (TwitterException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mediaIds;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * URIをpathに変換する
     */
    public String getPathFromUri(final Context context, final Uri uri) {
        boolean isAfterKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        Log.e("TAG", "uri:" + uri.getAuthority());
        if (isAfterKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(
                    uri.getAuthority())) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    return "/stroage/" + type + "/" + split[1];
                }
            } else if ("com.android.providers.downloads.documents".equals(
                    uri.getAuthority())) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(
                    uri.getAuthority())) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                contentUri = MediaStore.Files.getContentUri("external");
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {//MediaStore
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {MediaStore.Files.FileColumns.DATA};
        try {
            cursor = context.getContentResolver().query(
                    uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int cindex = cursor.getColumnIndexOrThrow(projection[0]);
                return cursor.getString(cindex);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
