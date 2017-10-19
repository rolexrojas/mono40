package com.tpago.movil.app.ui.picture;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tpago.movil.R;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.permission.PermissionHelper;
import com.tpago.movil.app.ui.permission.PermissionRequestResult;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.dep.MimeType;
import com.tpago.movil.io.FileHelper;
import com.tpago.movil.util.ObjectHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class PictureCreatorDialogFragment extends DialogFragment {

  private static final List<String> REQUIRED_PERMISSIONS_GALLERY;
  private static final List<String> REQUIRED_PERMISSIONS_CAMERA;

  static {
    REQUIRED_PERMISSIONS_GALLERY = new ArrayList<>();
    REQUIRED_PERMISSIONS_GALLERY.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    REQUIRED_PERMISSIONS_GALLERY.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    REQUIRED_PERMISSIONS_CAMERA = new ArrayList<>();
    REQUIRED_PERMISSIONS_CAMERA.addAll(REQUIRED_PERMISSIONS_GALLERY);
    REQUIRED_PERMISSIONS_CAMERA.add(Manifest.permission.CAMERA);
  }

  private static final int REQUEST_CODE_GALLERY = 0;
  private static final int REQUEST_CODE_CAMERA = 1;
  private static final int REQUEST_CODE_EDITOR = CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

  private static final int RESULT_CODE_OK = Activity.RESULT_OK;

  static PictureCreatorDialogFragment create() {
    return new PictureCreatorDialogFragment();
  }

  protected ComponentBuilderSupplier parentComponentBuilderSupplier;

  @Inject StringMapper stringMapper;
  @Inject AlertManager alertManager;

  @Inject PictureCreator pictureCreator;

  private File temporaryFile;
  private File outputFile;

  private View chooseFromGalleryActionView;
  private View useTheCameraActionView;

  private boolean shouldResolve = false;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // Checks that its container is a component builder supplier.
    final Fragment parentFragment = this.getParentFragment();
    if (!(parentFragment instanceof ComponentBuilderSupplierContainer)) {
      throw new ClassCastException("!(parentFragment instanceof ComponentBuilderSupplierContainer)");
    }
    this.parentComponentBuilderSupplier = ((ComponentBuilderSupplierContainer) parentFragment)
      .componentBuilderSupplier();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all annotated dependencies.
    this.parentComponentBuilderSupplier
      .get(PictureCreatorDialogFragment.class, PictureCreatorPresentationComponent.Builder.class)
      .build()
      .inject(this);

    // Checks that there is an active request.
    if (!this.pictureCreator.isRequestActive()) {
      throw new IllegalStateException("!this.pictureCreator.isRequestActive()");
    }

    // Creates the output file.
    this.outputFile = FileHelper.createInternalPictureFile(this.getContext());
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final PictureCreator.Request request = this.pictureCreator.currentRequest();
    final int actionStringId;
    if (request.creation()) {
      actionStringId = R.string.set;
    } else {
      actionStringId = R.string.update;
    }
    return new AlertDialog.Builder(this.getActivity())
      .setTitle(
        String.format(
          this.stringMapper.apply(R.string.pictureCreatorTitleFormat),
          this.stringMapper.apply(actionStringId)
        )
      )
      .setView(R.layout.picture_creator)
      .create();
  }

  @Override
  public void onStart() {
    super.onStart();

    if (this.shouldResolve) {
      this.shouldResolve = false;

      this.pictureCreator.resolveRequest(this.outputFile);
      this.outputFile = null;
    }
  }

  private void requestImageUsingGallery() {
    final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType(MimeType.IMAGE);
    this.startActivityForResult(intent, REQUEST_CODE_GALLERY);
  }

  private void onChooseFromGalleryOptionClicked(View view) {
    if (PermissionHelper.areGranted(this.getContext(), REQUIRED_PERMISSIONS_GALLERY)) {
      this.requestImageUsingGallery();
    } else {
      PermissionHelper.requestPermissions(this, REQUEST_CODE_GALLERY, REQUIRED_PERMISSIONS_GALLERY);
    }
  }

  private void requestImageUsingCamera() {
    final Context context = getContext();
    if (ObjectHelper.isNull(this.temporaryFile)) {
      try {
        this.temporaryFile = FileHelper.createExternalPictureFile(context);
      } catch (IllegalStateException exception) {
        Timber.w(exception, "Creating a temporary image file for camera output");
      }
    }
    if (ObjectHelper.isNull(this.temporaryFile)) {
      final AlertData alertData = AlertData.builder(this.stringMapper)
        .positiveButtonAction(this::dismiss)
        .build();
      this.alertManager.show(alertData);
    } else {
      final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      intent.putExtra(
        MediaStore.EXTRA_OUTPUT,
        FileHelper.getFileUri(
          this.getContext(),
          this.temporaryFile
        )
      );
      this.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
  }

  private void onUseTheCameraOptionClicked(View view) {
    if (PermissionHelper.areGranted(this.getContext(), REQUIRED_PERMISSIONS_CAMERA)) {
      this.requestImageUsingCamera();
    } else {
      PermissionHelper.requestPermissions(this, REQUEST_CODE_CAMERA, REQUIRED_PERMISSIONS_CAMERA);
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    final Dialog dialog = this.getDialog();

    // Adds the listener that gets notified every time the choose from gallery action is clicked.
    this.chooseFromGalleryActionView = ButterKnife
      .findById(dialog, R.id.relative_layout_choose_from_gallery);
    this.chooseFromGalleryActionView.setOnClickListener(this::onChooseFromGalleryOptionClicked);
    // Adds the listener that gets notified every time the user the camera action is clicked.
    this.useTheCameraActionView = ButterKnife
      .findById(dialog, R.id.relative_layout_use_the_camera);
    this.useTheCameraActionView.setOnClickListener(this::onUseTheCameraOptionClicked);
  }

  @Override
  public void onPause() {
    // Removes the listener that gets notified every time the user the camera action is clicked.
    this.useTheCameraActionView.setOnClickListener(null);
    this.useTheCameraActionView = null;
    // Removes the listener that gets notified every time the choose from gallery action is clicked.
    this.chooseFromGalleryActionView.setOnClickListener(null);
    this.chooseFromGalleryActionView = null;

    super.onPause();
  }

  @Override
  public void onDestroy() {
    // Deletes the temporary file.
    if (ObjectHelper.isNotNull(this.temporaryFile)) {
      FileHelper.deleteFile(this.temporaryFile);
      this.temporaryFile = null;
    }

    // Deletes the output file.
    if (ObjectHelper.isNotNull(this.outputFile)) {
      FileHelper.deleteFile(this.outputFile);
      this.outputFile = null;
    }

    // Notifies the creator that the fragment is been destroyed.
    this.pictureCreator.onDestroy();

    super.onDestroy();
  }

  @Override
  public void onDetach() {
    this.parentComponentBuilderSupplier = null;

    super.onDetach();
  }

  @Override
  public void onRequestPermissionsResult(
    int requestCode,
    @NonNull String[] permissions,
    @NonNull int[] results
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, results);

    final PermissionRequestResult result = PermissionRequestResult.create(permissions, results);
    if (result.isSuccessful()) {
      if (requestCode == REQUEST_CODE_GALLERY) {
        this.requestImageUsingGallery();
      } else if (requestCode == REQUEST_CODE_CAMERA) {
        this.requestImageUsingCamera();
      }
    }
  }

  private void launchEditor(Uri uri) {
    CropImage.activity(uri)
      .setActivityTitle(this.stringMapper.apply(R.string.edit))
      .setAllowRotation(true)
      .setCropShape(CropImageView.CropShape.RECTANGLE)
      .setFixAspectRatio(true)
      .setGuidelines(CropImageView.Guidelines.ON)
      .setOutputUri(Uri.fromFile(this.outputFile))
      .start(getContext(), this);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_GALLERY) {
      if (resultCode == RESULT_CODE_OK) {
        this.launchEditor(data.getData());
      }
    } else if (requestCode == REQUEST_CODE_CAMERA) {
      if (resultCode == RESULT_CODE_OK) {
        this.launchEditor(Uri.fromFile(this.temporaryFile));
      }
    } else if (requestCode == REQUEST_CODE_EDITOR) {
      if (resultCode == RESULT_CODE_OK) {
        this.shouldResolve = true;
      }
    }
  }
}
