package com.tpago.movil.session;


//import com.tpago.movil.io.FileHelper;
import android.net.Uri;

import com.tpago.movil.util.ObjectHelper;

import java.io.File;

/**
 * @author hecvasro
 */
public final class UpdateUserPictureJob extends SessionJob {

    public static final String TYPE = "UpdateUserPictureJob";

    static UpdateUserPictureJob create(File picture) {
        return new UpdateUserPictureJob(picture);
    }

    final File picture;

    private UpdateUserPictureJob(File picture) {
        super(TYPE);

        this.picture = picture;
    }

    @Override
    public void onAdded() {
        if (this.picture != null && this.picture.exists()) {
            this.sessionManager.getUser()
                    .updatePicture(Uri.fromFile(this.picture));
        } else {
            this.sessionManager.getUser()
                    .updatePicture(Uri.EMPTY);
        }
    }

    @Override
    public void onRun() throws Throwable {
//    final User user = this.sessionManager.getUser();
//    final Uri newPicture = this.api.updateUserPicture(user, this.picture)
//      .blockingGet();
//    user.updatePicture(newPicture);
//    FileHelper.deleteFile(this.picture);
    }
}
