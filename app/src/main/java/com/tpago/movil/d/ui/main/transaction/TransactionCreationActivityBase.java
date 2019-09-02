package com.tpago.movil.d.ui.main.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.SwitchableContainerActivityBase;
import com.tpago.movil.d.ui.main.transaction.bills.BillTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.contacts.PhoneNumberTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.products.CreditCardTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.products.LoanTransactionCreationFragment;
import com.tpago.movil.dep.App;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class TransactionCreationActivityBase
        extends SwitchableContainerActivityBase<TransactionCreationComponent>
        implements TransactionCreationContainer {

    private static final String KEY_RECIPIENT = "recipient";
    private static final String KEY_TRANSACTION_ID = "transactionId";
    private static final String KEY_TRANSACTION_CATEGORY = "transactionCategory";
    private static String recipientName;

    private Unbinder unbinder;
    private TransactionCreationComponent component;

    @Inject
    TransactionCategory transactionCategory;
    @Inject
    Recipient recipient;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static Intent serializeResult(Recipient recipient, String transactionId, TransactionCategory transactionCategory) {
        final Intent intent = new Intent();
        intent.putExtra(KEY_RECIPIENT, recipient);
        intent.putExtra(KEY_TRANSACTION_ID, transactionId);
        intent.putExtra(KEY_TRANSACTION_CATEGORY, transactionCategory);
        return intent;
    }

    @NonNull
    public static Intent getLaunchIntent(
            @NonNull Context context,
            @NonNull TransactionCategory transactionCategory,
            @NonNull Recipient recipient
    ) {
        final Intent intent = new Intent(context, TransactionCreationActivityBase.class);
        intent.putExtra(KEY_TRANSACTION_CATEGORY, transactionCategory.name());
        intent.putExtra(KEY_RECIPIENT, recipient);
        return intent;
    }


    public static TransactionResult deserializeTransactionResult(Intent intent) {
        if (ObjectHelper.isNull(intent)) {
            return null;
        } else {
            final Recipient recipient = intent.getParcelableExtra(KEY_RECIPIENT);
            final String transactionId = intent.getStringExtra(KEY_TRANSACTION_ID);
            final TransactionCategory transactionCategory = TransactionCategory.TRANSFER;
            return new TransactionResult(recipient, transactionId, transactionCategory);
        }
    }

    public static Pair<Recipient, String> deserializeResult(Intent intent) {
        if (ObjectHelper.isNull(intent)) {
            return null;
        } else {
            final Recipient recipient = intent.getParcelableExtra(KEY_RECIPIENT);
            final String transactionId = intent.getStringExtra(KEY_TRANSACTION_ID);
            return Pair.create(recipient, transactionId);
        }
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String name) {
        recipientName = name;
    }

    @Override
    protected int layoutResId() {
        return R.layout.d_activity_transaction_creation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        // Asserts all the required arguments.
        final Bundle bundle = ObjectHelper.firstNonNull(
                savedInstanceState,
                this.getIntent()
                        .getExtras()
        );
        if (ObjectHelper.isNull(bundle)) {
            throw new NullPointerException("bundle == null");
        } else if (!bundle.containsKey(KEY_TRANSACTION_CATEGORY)) {
            throw new NullPointerException(
                    "Argument '" + KEY_TRANSACTION_CATEGORY + "' must be provided");
        } else if (!bundle.containsKey(KEY_RECIPIENT)) {
            throw new NullPointerException("Argument '" + KEY_RECIPIENT + "' must be provided");
        } else {
            this.transactionCategory = TransactionCategory
                    .valueOf(bundle.getString(KEY_TRANSACTION_CATEGORY));
            final Recipient recipient = bundle.getParcelable(KEY_RECIPIENT);
            // Injects all the annotated dependencies.
            component = DaggerTransactionCreationComponent.builder()
                    .appComponent(((App) getApplication()).component())
                    .activityModule(ActivityModule.create(this))
                    .transactionCreationModule(new TransactionCreationModule(transactionCategory, recipient))
                    .build();
            component.inject(this);
            // Prepares the app bar.
            setSupportActionBar(toolbar);
            final ActionBar actionBar = getSupportActionBar();
            if (ObjectHelper.isNotNull(actionBar)) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
            }
            // Resolves the initial sub-screen.
            ChildFragment<TransactionCreationContainer> fragment = null;
            switch (recipient.getType()) {
                case PHONE_NUMBER:
                case NON_AFFILIATED_PHONE_NUMBER:
                    fragment = PhoneNumberTransactionCreationFragment.newInstance();
                    break;
                case BILL:
                    fragment = BillTransactionCreationFragment.create();
                    break;
                case PRODUCT:
                    final ProductRecipient productRecipient = (ProductRecipient) recipient;
                    if (Product.checkIfCreditCard(productRecipient.getProduct())) {
                        fragment = CreditCardTransactionCreationFragment.create();
                    } else {
                        fragment = LoanTransactionCreationFragment.create();
                    }
                    break;
                case ACCOUNT:
                    fragment = PhoneNumberTransactionCreationFragment.newInstance();
                    break;
                case USER:
                    fragment = PhoneNumberTransactionCreationFragment.newInstance();
                    break;
                case MERCHANT:
                    fragment = PhoneNumberTransactionCreationFragment.newInstance();
            }
            this.setChildFragment(fragment, false, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Sets the title and subtitle.
        final String title;
        final String subtitle;
        final String label = recipient.getLabel();

        if (ObjectHelper.isNotNull(label)) {
            title = label;
            subtitle = recipient.getIdentifier();
        } else {
            title = recipient.getIdentifier();
            subtitle = null;
        }
        recipientName = title;
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(
                        getString(
                                R.string.format_transaction_title,
                                getString(transactionCategory.stringId),
                                title
                        )
                );
                toolbar.setSubtitle(subtitle);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public TransactionCreationComponent getComponent() {
        return component;
    }

    @Override
    public void setChildFragment(ChildFragment<TransactionCreationContainer> fragment) {
        setChildFragment(fragment, true, true);
    }

    @Override
    public void finish(boolean succeeded, String transactionId) {
        if (succeeded) {
            setResult(RESULT_OK, serializeResult(recipient, transactionId, transactionCategory));
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
