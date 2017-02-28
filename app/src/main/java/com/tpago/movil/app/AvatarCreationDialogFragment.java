package com.tpago.movil.app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tpago.movil.R;
import com.tpago.movil.io.Files;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class AvatarCreationDialogFragment extends DialogFragment {
  private static final String KEY_AVATAR_FILE = "outputFile";

  private static final int REQUEST_CODE_GALLERY = 0;
  private static final int REQUEST_CODE_CAMERA = 1;
  private static final int REQUEST_CODE_EDITOR = CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

  private static final int RESULT_CODE_OK = Activity.RESULT_OK;

  private File outputFile;
  private File temporaryFile;

  private View chooseFromGalleryActionView;
  private View useTheCameraActionView;

  private boolean shouldBeDismissed = false;

  public static AvatarCreationDialogFragment create(File avatarFile) {
    Preconditions.checkNotNull(avatarFile, "outputFile == null");
    final Bundle args = new Bundle();
    args.putSerializable(KEY_AVATAR_FILE, avatarFile);
    final AvatarCreationDialogFragment fragment = new AvatarCreationDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private Intent createChooser(List<Intent> intents) {
    final Intent target;
    if (intents.isEmpty()) {
      target = new Intent();
    } else {
      final int size = intents.size();
      target = intents.get(size - 1);
      intents.remove(size - 1);
    }
    final Intent chooser = Intent.createChooser(target, getString(R.string.using));
    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));
    return chooser;
  }

  private void requestImageUsingGallery() {
    final PackageManager packageManager = getContext().getPackageManager();
    final List<Intent> intentList = CropImage
      .getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, false);
    if (intentList.isEmpty()) {
      intentList.addAll(CropImage.getGalleryIntents(packageManager, Intent.ACTION_PICK, false));
    }
    startActivityForResult(createChooser(intentList), REQUEST_CODE_GALLERY);
  }

  private void requestImageUsingCamera() {
    final Context context = getContext();
    if (Objects.isNull(temporaryFile)) {
      try {
        temporaryFile = Files.createExternalPictureFile(context);
      } catch (IllegalStateException exception) {
        Timber.w(exception, "Creating a temporary image file for camera output");
      }
    }
    if (Objects.isNull(temporaryFile)) {
      // TODO: Let the user know that the temporary image couldn't be created.
    } else {
      final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      final List<Intent> intentList = new ArrayList<>();
      final PackageManager packageManager = context.getPackageManager();
      final List<ResolveInfo> resolveInfoList = packageManager
        .queryIntentActivities(captureIntent, 0);
      if (Objects.isNull(temporaryFile)) {
        temporaryFile = Files.createExternalPictureFile(context);
      }
      for (ResolveInfo info : resolveInfoList) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
          info.activityInfo.packageName,
          info.activityInfo.name));
        intent.setPackage(info.activityInfo.packageName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temporaryFile));
        intentList.add(intent);
      }
      startActivityForResult(createChooser(intentList), REQUEST_CODE_CAMERA);
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
      } else {
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
        startEditor(Uri.fromFile(temporaryFile));
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
    final Bundle args = Preconditions.checkNotNull(getArguments(), "getArguments() == null");
    if (!args.containsKey(KEY_AVATAR_FILE)) {
      throw new IllegalArgumentException("args.containsKey('outputFile') == false");
    }
    outputFile = (File) args.getSerializable(KEY_AVATAR_FILE);
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
    builder.setView(R.layout.dialog_avatar_creation);
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
        requestImageUsingGallery();
      }
    });
    // Adds the listener that gets notified every time the user the camera action is clicked.
    useTheCameraActionView = ButterKnife.findById(d, R.id.relative_layout_use_the_camera);
    useTheCameraActionView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final Context context = getContext();
        if (Permissions.checkIfGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)
          && Permissions.checkIfGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          requestImageUsingCamera();
        } else {
          Permissions.requestPermissions(
            AvatarCreationDialogFragment.this,
            REQUEST_CODE_CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
    Timber.d("Clearing the temporary file");
    if (Objects.isNotNull(temporaryFile)) {
      if (temporaryFile.exists()) {
        temporaryFile.delete();
      }
      temporaryFile = null;
    }
  }
}
