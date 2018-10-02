package com.tpago.movil.app.ui.main.settings.primaryPaymentMethod;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.auto.value.AutoValue;
import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.FragmentActivityBase;
import com.tpago.movil.app.ui.FragmentCreator;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.app.ui.item.ItemHelper;
import com.tpago.movil.app.ui.main.settings.primaryPaymentMethod.list.PaymentMethodViewViewHolderAdapter;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.domain.ProductManager;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PrimaryPaymentMethodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrimaryPaymentMethodFragment extends FragmentBase implements PrimaryPaymentMethodPresentation {
    RecyclerView recyclerView;

    @Inject
    ProductManager productManager;

    @Inject
    StringMapper stringMapper;

    @Inject
    CompanyHelper companyHelper;


    private PaymentMethodViewViewHolderAdapter adapter;
    private PrimaryPaymentMethodPresenter presenter;


    public static FragmentCreator creator() {
        return new AutoValue_PrimaryPaymentMethodFragment_Creator();
    }


    public PrimaryPaymentMethodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PrimaryPaymentMethodFragment.
     */
    public static PrimaryPaymentMethodFragment newInstance() {
        PrimaryPaymentMethodFragment fragment = new PrimaryPaymentMethodFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieves the dependency injector.
        FragmentActivityBase.get(this.getActivity()).component().inject(this);
        this.presenter = PrimaryPaymentMethodPresenter.create(this, FragmentActivityBase.get(this.getActivity()).component());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Sets the title.
        FragmentActivityBase.get(this.getActivity())
                .setTitle(R.string.altUnlockMethod);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_primary_payment_method, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Context context = this.getContext();
        this.adapter = new PaymentMethodViewViewHolderAdapter(getContext(), this.productManager, companyHelper, this);
        this.recyclerView = getView().findViewById(R.id.payment_recycler_view);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(ItemHelper.layoutManagerLinearVertical(context));
        this.recyclerView.addItemDecoration(ItemHelper.dividerLineHorizontal(context));
    }


    @Override
    public void finish() {
        getActivity().finish();
    }

    @AutoValue
    public static abstract class Creator extends FragmentCreator {

        Creator() {
        }

        @Override
        public Fragment create() {
            return new PrimaryPaymentMethodFragment();
        }
    }
}
