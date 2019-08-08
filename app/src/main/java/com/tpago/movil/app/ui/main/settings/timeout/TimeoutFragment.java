package com.tpago.movil.app.ui.main.settings.timeout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.auto.value.AutoValue;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.FragmentActivityBase;
import com.tpago.movil.app.ui.FragmentCreator;
import com.tpago.movil.app.ui.InjectableFragment;
import com.tpago.movil.app.ui.item.ItemHelper;
import com.tpago.movil.dep.ConfigManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public final class TimeoutFragment extends InjectableFragment {
    @BindView(R.id.session_timeout_options)
    RecyclerView sessionTimeoutOptionsRv;

    public static FragmentCreator creator() {
        return new AutoValue_TimeoutFragment_Creator();
    }


    @Override
    protected int layoutResId() {
        return R.layout.fragment_timeout_settings;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionTimeoutOptionsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        List<TimeoutSessionOption> items = new ArrayList<>();
        int[] minutes = new int[]{1, 2, 3, 4, 5, 10};
        for (int minute : minutes) {
            items.add(new TimeoutSessionOption(minute));
        }
        sessionTimeoutOptionsRv.addItemDecoration(ItemHelper.dividerLineHorizontal(getContext()));
        sessionTimeoutOptionsRv.setAdapter(new TimeoutSessionOptionsAdapter(
                items,
                item -> {
                    ConfigManager.setTimeOut(getContext(), item);
                    getActivity().finish();
                }));
    }

    @Override
    public void onStart() {
        super.onStart();

        // Sets the title.
        FragmentActivityBase.get(this.getActivity())
                .setTitle(R.string.sessionTimeoutTitle);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @AutoValue
    public static abstract class Creator extends FragmentCreator {

        Creator() {
        }

        @Override
        public Fragment create() {
            return new TimeoutFragment();
        }
    }
}
