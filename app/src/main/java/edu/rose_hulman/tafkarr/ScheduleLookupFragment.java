package edu.rose_hulman.tafkarr;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;



public class ScheduleLookupFragment extends Fragment {
    public static final String SHARED_PREF_AUTH_KEY = "auth";
    private static final String RESULTS_BUNDLE_KEY = "results";

    private SharedPreferences msharedPrefs;
    private EditText mSearch;
    private Spinner mTermSelect;
    private Button mSubmit;
    private ProgressBar mProgress;
    private RecyclerView mResultsList;
    private ScheduleLookupResultsListAdapter mResultsListAdapter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(RESULTS_BUNDLE_KEY,
                mResultsListAdapter.getDataset());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_schedule_lookup, container, false);

        msharedPrefs = getActivity().getSharedPreferences(
                getString(R.string.shared_prefs_file), getActivity().MODE_PRIVATE);

        ArrayList<String> results = savedInstanceState == null ? null
                : savedInstanceState.getStringArrayList(RESULTS_BUNDLE_KEY);
        if (results == null) {
            results = new ArrayList<String>();
        }

        mResultsList = (RecyclerView) root.findViewById(R.id.resultsList);
        mResultsList.setHasFixedSize(true);
        LayoutManager lm = new LinearLayoutManager(getActivity());
        mResultsList.setLayoutManager(lm);
        mResultsListAdapter = new ScheduleLookupResultsListAdapter(results);
        mResultsList.setAdapter(mResultsListAdapter);

        mProgress = (ProgressBar) root.findViewById(R.id.results_progress);

        mSearch = (EditText) root.findViewById(R.id.usernameSearchInput);

        mTermSelect = (Spinner) root.findViewById(R.id.termSelect);
        mTermSelect.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.terms)));

        mSubmit = (Button) root.findViewById(R.id.submit);
        mSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id,
                                          KeyEvent keyEvent) {
                if (id == R.id.searchIME || id == EditorInfo.IME_NULL) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        return root;
    }


    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private void performSearch() {
        try {
            if (mSearch.getText().toString().isEmpty()) {
                mSearch.setError(getString(R.string.schedule_lookup_field_required));
                return;
            }
            closeKeyboard();
            mProgress.setVisibility(View.VISIBLE);
            mResultsList.setVisibility(View.GONE);

            String term = (String) mTermSelect.getSelectedItem();
            String search = mSearch.getText().toString();

            // ResultsTask asyncRTask = new ResultsTask();
            ScheduleLookupRequest asyncRTask = new ScheduleLookupRequest(getActivity(),
                    mResultsList, mResultsListAdapter, mProgress);
            asyncRTask.execute(
                    msharedPrefs.getString(SHARED_PREF_AUTH_KEY, ""), term,
                    search);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
