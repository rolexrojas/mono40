package com.tpago.movil;

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
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tpago.movil.app.PermissionRequestResult;
import com.tpago.movil.app.Permissions;
import com.tpago.movil.io.Files;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.io.File;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class AvatarCreationDialogFragment extends DialogFragment {
  private static final String KEY_OUTPUT_FILE = "outputFile";

  private static final String PERMISSION_CAMERA
    = Manifest.permission.CAMERA;
  private static final String PERMISSION_READ_EXTERNAL_STORAGE
    = Manifest.permission.READ_EXTERNAL_STORAGE;
  private static final String PERMISSION_WRITE_EXTERNAL_STORAGE
    = Manifest.permission.WRITE_EXTERNAL_STORAGE;

  private static final int REQUEST_CODE_GALLERY = 0;
  private static final int REQUEST_CODE_CAMERA = 1;
  private static final int REQUEST_CODE_EDITOR = CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

  private static final int RESULT_CODE_OK = Activity.RESULT_OK;

  public static AvatarCreationDialogFragment create(File outputFile) {
    Preconditions.assertNotNull(outputFile, "outputFile == null");
    final Bundle args = new Bundle();
    args.putSerializable(KEY_OUTPUT_FILE, outputFile);
    final AvatarCreationDialogFragment fragment = new AvatarCreationDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private File outputFile;
  private File temporaryFile;

  private View chooseFromGalleryActionView;
  private View useTheCameraActionView;

  private boolean shouldBeDismissed = false;

  private void requestImageUsingGallery() {
    final Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    i.setType(MimeType.IMAGE);
    startActivityForResult(i, REQUEST_CODE_GALLERY);
  }

  private void requestImageUsingCamera() {
    final Context context = getContext();
    if (Objects.checkIfNull(temporaryFile)) {
      try {
        temporaryFile = Files.createExternalPictureFile(context);
      } catch (IllegalStateException exception) {
        Timber.w(exception, "Creating a temporary image file for camera output");
      }
    }
    if (Objects.checkIfNull(temporaryFile)) {
      // TODO: Let the user know that the temporary image couldn't be created.
    } else {
      final Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      i.putExtra(MediaStore.EXTRA_OUTPUT, Files.getFileUri(getContext(), temporaryFile));
      startActivityForResult(i, REQUEST_CODE_CAMERA);
    }
  }

  private void startEditor(Uri uri) {
    CropImage.activity(uri)
      .setActivityTitle(getString(R.string.dialog_avatar_creation_editor_title))
      .setAllowRotation(true)
      .setCropShape(CropImageView.CropShape.RECTANGLE)
      .setFixAspectRatio(true)
      .setGuidelines(CropImageView.Guidelines.ON)
      .setOutputUri(Uri.fromFile(outputFile))
      .start(getContext(), this);
  }

  @Override
  public void onRequestPermissionsResult(
    int requestCode,
    @NonNull String[] permissions,
    @NonNull int[] results) {
    super.onRequestPermissionsResult(requestCode, permissions, results);
    final PermissionRequestResult result = PermissionRequestResult.create(permissions, results);
    if (result.isSuccessful()) {
      if (requestCode == REQUEST_CODE_GALLERY) {
        requestImageUsingGallery();
      } else if (requestCode == REQUEST_CODE_CAMERA) {
        requestImageUsingCamera();
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_GALLERY) {
      if (resultCode == RESULT_CODE_OK) {
        startEditor(data.getData());
      }
    } else if (requestCode == REQUEST_CODE_CAMERA) {
      if (resultCode == RESULT_CODE_OK) {
        startEditor(Files.getFileUri(temporaryFile));
      }
    } else if (requestCode == REQUEST_CODE_EDITOR) {
      if (resultCode == RESULT_CODE_OK) {
        shouldBeDismissed = true;
      }
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Retrieves the avatar file from the arguments.
    final Bundle args = Preconditions.assertNotNull(getArguments(), "getArguments() == null");
    if (!args.containsKey(KEY_OUTPUT_FILE)) {
      throw new IllegalArgumentException("args.containsKey('outputFile') == false");
    }
    outputFile = (File) args.getSerializable(KEY_OUTPUT_FILE);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    if (outputFile.exists()) {
      builder.setTitle(R.string.dialog_avatar_creation_label_title_replace);
    } else {
      builder.setTitle(R.string.dialog_avatar_creation_label_title_create);
    }
    builder.setView(R.layout.d_dialog_avatar_creation);
    return builder.create();
  }

  @Override
  public void onStart() {
    super.onStart();
    if (shouldBeDismissed) {
      dismiss();
      shouldBeDismissed = false;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    final Dialog d = getDialog();
    // Adds the listener that gets notified every time the choose from gallery action is clicked.
    chooseFromGalleryActionView = ButterKnife.findById(d, R.id.relative_layout_choose_from_gallery);
    chooseFromGalleryActionView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final Context context = getContext();
        if (Permissions.checkIfGranted(context, PERMISSION_READ_EXTERNAL_STORAGE)
          && Permissions.checkIfGranted(context, PERMISSION_WRITE_EXTERNAL_STORAGE)) {
          requestImageUsingGallery();
        } else {
          Permissions.requestPermissions(
            AvatarCreationDialogFragment.this,
            REQUEST_CODE_GALLERY,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
      }
    });
    // Adds the listener that gets notified every time the user the camera action is clicked.
    useTheCameraActionView = ButterKnife.findById(d, R.id.relative_layout_use_the_camera);
    useTheCameraActionView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final Context context = getContext();
        if (Permissions.checkIfGranted(context, PERMISSION_CAMERA)
          && Permissions.checkIfGranted(context, PERMISSION_READ_EXTERNAL_STORAGE)
          && Permissions.checkIfGranted(context, PERMISSION_WRITE_EXTERNAL_STORAGE)) {
          requestImageUsingCamera();
        } else {
          Permissions.requestPermissions(
            AvatarCreationDialogFragment.this,
            REQUEST_CODE_CAMERA,
            PERMISSION_CAMERA,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
      }
    });
  }

  @Override
  public void onPause() {
    super.onPause();
    // Removes the listener that gets notified every time the choose from gallery action is clicked.
    chooseFromGalleryActionView.setOnClickListener(null);
    chooseFromGalleryActionView = null;
    // Removes the listener that gets notified every time the user the camera action is clicked.
    useTheCameraActionView.setOnClickListener(null);
    useTheCameraActionView = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // Clears the temporary file.
    if (Objects.checkIfNotNull(temporaryFile)) {
      if (temporaryFile.exists()) {
        temporaryFile.delete();
      }
      temporaryFile = null;
    }
  }
}
