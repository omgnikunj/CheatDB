package com.niksoft.android.cheatdatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.niksoft.android.cheatdatabase.db.GVCCDB;

public class MainActivity extends AppCompatActivity {

    private static GVCCDB mDbHelper;
    private Cursor effectList;

    AutoCompleteTextView actv_effect;
    TextView tv_effect, tv_cheat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new GVCCDB(this);
        mDbHelper.open();

        actv_effect = (AutoCompleteTextView) findViewById(R.id.actv_effect);
        tv_effect = (TextView) findViewById(R.id.tv_effect);
        tv_cheat = (TextView) findViewById(R.id.tv_cheats);

        tv_cheat.setText("");
        tv_effect.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpAutoCompleteCursor();
    }

    private void setUpAutoCompleteCursor() {

        String[] from = new String[]{GVCCDB.KEY_CHEAT_EFFECT};
        int[] to = new int[]{R.id.text1};

        effectList = mDbHelper.getAllEffects();

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.layout_effect_row, effectList, from, to);

        actv_effect.setAdapter(adapter);

        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndexOrThrow(GVCCDB.KEY_CHEAT_EFFECT));
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint != null) {
                    Cursor matchCursor = mDbHelper.getMatchingEffects(constraint.toString());
                    return matchCursor;
                }
                return null;
            }
        });

        actv_effect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor selectedItem = (Cursor) adapterView.getItemAtPosition(i);
                String mEffect = selectedItem.getString(selectedItem.getColumnIndexOrThrow(GVCCDB.KEY_CHEAT_EFFECT));
                String mCheat = selectedItem.getString(selectedItem.getColumnIndexOrThrow(GVCCDB.KEY_CHEAT_CODE));

                tv_effect.setText(mEffect);
                tv_cheat.setText(mCheat);
            }
        });

        actv_effect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_cheat.setText("");
                tv_effect.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }
}
