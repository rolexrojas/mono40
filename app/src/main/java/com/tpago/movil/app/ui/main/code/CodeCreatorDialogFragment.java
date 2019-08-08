package com.tpago.movil.app.ui.main.code;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.InjectableDialogFragment;
import com.tpago.movil.app.ui.DNumPad;
import com.tpago.movil.d.ui.view.BaseAnimatorListener;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import io.codetail.animation.ViewAnimationUtils;

/**
 * @author hecvasro
 */
public final class CodeCreatorDialogFragment extends InjectableDialogFragment
        implements CodeCreatorPresentation, DialogInterface.OnShowListener {

    private boolean wasVisible;

    static CodeCreatorDialogFragment create() {
        return new CodeCreatorDialogFragment();
    }

    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.subtitleTextView)
    TextView subtitleTextView;
    @BindView(R.id.valueTextView)
    TextView valueTextView;
    @BindView(R.id.num_pad)
    DNumPad DNumPad;
    @BindView(R.id.linear_layout_container)
    LinearLayout containerLinearLayout;
    @BindInt(android.R.integer.config_shortAnimTime)
    int enterDuration;
    @BindView(R.id.frame_layout_container)
    FrameLayout containerFrameLayout;
    @BindInt(android.R.integer.config_shortAnimTime)
    int exitDuration;

    private static final String KEY_ORIGIN_X = "originX";
    private static final String KEY_ORIGIN_Y = "originY";

    private int originX;
    private int originY;

    @Inject
    CodeCreatorPresenter presenter;

    private Consumer<Integer> numPadDigitConsumer;
    private Action numPadDeleteAction;

    @LayoutRes
    @Override
    protected int layoutResId() {
        return R.layout.code_creator;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CodeCreatorTheme);

        final Bundle bundle
                = ObjectHelper.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();

        originX = bundle.getInt(KEY_ORIGIN_X);
        originY = bundle.getInt(KEY_ORIGIN_Y);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.parentComponentBuilderSupplier
                .get(CodeCreatorDialogFragment.class, CodeCreatorPresentationComponent.Builder.class)
                .codeCreatorModule(CodeCreatorPresentationModule.create(this))
                .build()
                .inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!wasVisible) {
            wasVisible = true;
            animate();
        }
    }

    public void animate() {
        final Display display = getActivity().getWindowManager()
                .getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        final int originX = size.x / 2;
        final int originY = size.y / 2;

        // Prepares the foreground for the animation.
        containerLinearLayout.setAlpha(0.0F);
        containerLinearLayout.setScaleX(0.9F);
        containerLinearLayout.setScaleY(0.9F);
        // Prepares the foreground animator.
        final AnimatorSet foregroundAnimator = new AnimatorSet();
        foregroundAnimator.setDuration(enterDuration / 2);
        foregroundAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        foregroundAnimator.play(ObjectAnimator.ofFloat(containerLinearLayout, "alpha", 1.0F))
                .with(ObjectAnimator.ofFloat(containerLinearLayout, "scaleX", 1.0F))
                .with(ObjectAnimator.ofFloat(containerLinearLayout, "scaleY", 1.0F))
                .after(enterDuration / 2);
        // Prepares the background animator.
        final int radius = (int) Math.hypot(
                containerFrameLayout.getWidth(),
                containerLinearLayout.getHeight()
        );
        final Animator backgroundAnimator = ViewAnimationUtils
                .createCircularReveal(containerFrameLayout, originX, originY, 0, radius)
                .setDuration(enterDuration);
        // Prepares the combined animator.
        final AnimatorSet animator = new AnimatorSet();
        animator.play(foregroundAnimator)
                .with(backgroundAnimator);
        // Starts the combined animator.
        animator.start();
    }

    @Override
    public void onResume() {
        super.onResume();

        this.numPadDigitConsumer = this.presenter::onNumPadDigitButtonClicked;
        this.DNumPad.addDigitConsumer(this.numPadDigitConsumer);
        this.numPadDeleteAction = this.presenter::onNumPadDeleteButtonClicked;
        this.DNumPad.addDeleteAction(this.numPadDeleteAction);

        this.presenter.onPresentationResumed();
    }

    @Override
    public void onPause() {
        this.presenter.onPresentationPaused();

        this.DNumPad.removeDeleteAction(this.numPadDeleteAction);
        this.numPadDeleteAction = null;
        this.DNumPad.removeDigitConsumer(this.numPadDigitConsumer);
        this.numPadDigitConsumer = null;

        super.onPause();
    }

    @Override
    public void setTitle(String text) {
        this.titleTextView.setText(text);
    }

    @Override
    public void setSubtitle(String text) {
        this.subtitleTextView.setText(text);
    }

    @Override
    public void setValue(String text) {
        this.valueTextView.setText(text);
    }

    private void finish() {
        if (ObjectHelper.isNotNull(containerFrameLayout)) {
            // Prepares the background animator.
            final int radius = (int) Math.hypot(
                    containerFrameLayout.getWidth(),
                    containerLinearLayout.getHeight()
            );
            final Animator animator = ViewAnimationUtils
                    .createCircularReveal(containerFrameLayout, originX, originY, radius, 0)
                    .setDuration(exitDuration);
            animator.addListener(new BaseAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    dismiss();
                }
            });
            // Starts the background animator.
            animator.start();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                finish();
            }
        };
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        animate();
    }
}
