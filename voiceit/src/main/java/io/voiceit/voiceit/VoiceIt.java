package io.voiceit.voiceit;

/**
 * Created by armaanbindra on 6/16/17.
 */


        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import android.media.MediaRecorder;
        import android.os.CountDownTimer;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import cz.msebera.android.httpclient.entity.ByteArrayEntity;


/**
 * Created by armaanbindra on 6/16/17.
 */
public class VoiceIt {

    private static final String BASE_URL = "https://siv.voiceprintportal.com/sivservice/api";
    private AsyncHttpClient aSyncClient;
    private String developerId;

    private String GetSHA256(String data) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(data.getBytes());
            byte[] hash = sha.digest();

            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public VoiceIt(String developerId){
        this.aSyncClient = new AsyncHttpClient();
        this.developerId = developerId;
    }

    public void createUser(String userId, String password, AsyncHttpResponseHandler responseHandler) {
        this.aSyncClient.removeAllHeaders();
        this.aSyncClient.addHeader("VsitDeveloperId", this.developerId);
        this.aSyncClient.addHeader("UserId", userId);
        this.aSyncClient.addHeader("VsitPassword", GetSHA256(password));
        this.aSyncClient.post(getAbsoluteUrl("/users"), null, responseHandler);
    }

    public void getUser(String userId, String password, AsyncHttpResponseHandler responseHandler) {
        this.aSyncClient.removeAllHeaders();
        this.aSyncClient.addHeader("VsitDeveloperId", this.developerId);
        this.aSyncClient.addHeader("UserId", userId);
        this.aSyncClient.addHeader("VsitPassword", GetSHA256(password));
        this.aSyncClient.get(getAbsoluteUrl("/users"), null, responseHandler);
    }

    public void deleteUser(String userId, String password, AsyncHttpResponseHandler responseHandler) {
        this.aSyncClient.removeAllHeaders();
        this.aSyncClient.addHeader("VsitDeveloperId", this.developerId);
        this.aSyncClient.addHeader("UserId", userId);
        this.aSyncClient.addHeader("VsitPassword", GetSHA256(password));
        this.aSyncClient.delete(getAbsoluteUrl("/users"), null, responseHandler);
    }

    public void getEnrollments(String userId, String password, AsyncHttpResponseHandler responseHandler) {
        this.aSyncClient.removeAllHeaders();
        this.aSyncClient.addHeader("VsitDeveloperId", this.developerId);
        this.aSyncClient.addHeader("UserId", userId);
        this.aSyncClient.addHeader("VsitPassword", GetSHA256(password));
        this.aSyncClient.get(getAbsoluteUrl("/enrollments"), null, responseHandler);
    }

    public void getEnrollmentsCount(String userId, String password, String vppText, AsyncHttpResponseHandler responseHandler) {
        this.aSyncClient.removeAllHeaders();
        this.aSyncClient.addHeader("VsitDeveloperId", this.developerId);
        this.aSyncClient.addHeader("UserId", userId);
        this.aSyncClient.addHeader("VppText", vppText);
        this.aSyncClient.addHeader("VsitPassword", GetSHA256(password));
        this.aSyncClient.get(getAbsoluteUrl("/enrollments/count"), null, responseHandler);
    }

    public void deleteEnrollment(String userId, String password, Integer enrollmentId, AsyncHttpResponseHandler responseHandler) {
        this.aSyncClient.removeAllHeaders();
        this.aSyncClient.addHeader("VsitDeveloperId", this.developerId);
        this.aSyncClient.addHeader("UserId", userId);
        this.aSyncClient.addHeader("VsitPassword", GetSHA256(password));
        this.aSyncClient.delete(getAbsoluteUrl("/enrollments/" + enrollmentId.toString()), null, responseHandler);
    }

    public void createEnrollment(String userId, String password, String contentLanguage, final AsyncHttpResponseHandler responseHandler) {
        this.aSyncClient.removeAllHeaders();
        this.aSyncClient.addHeader("VsitDeveloperId", this.developerId);
        this.aSyncClient.addHeader("UserId", userId);
        this.aSyncClient.addHeader("ContentLanguage", contentLanguage);
        this.aSyncClient.addHeader("VsitPassword", GetSHA256(password));

        try{
            final File file =  File.createTempFile("tempfile", ".wav");
            final MediaRecorder myRecorder = new MediaRecorder();
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            myRecorder.setAudioSamplingRate(11250);
            myRecorder.setAudioChannels(1);
            myRecorder.setAudioEncodingBitRate(8000);
            myRecorder.setOutputFile(file.getAbsolutePath());
            myRecorder.prepare();
            myRecorder.start();
            byte[] myData = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(myData);
                for (int i = 0; i < myData.length; i++) {
                    System.out.print((char)myData[i]);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found.");
                e.printStackTrace();
            }
            CountDownTimer countDowntimer = new CountDownTimer(4800, 1000) {
                public void onTick(long millisUntilFinished) {}
                public void onFinish() {
                    try{
                        myRecorder.stop();
                        myRecorder.reset();
                        myRecorder.release();
                        byte[] myData = new byte[(int) file.length()];
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            fileInputStream.read(myData);
                        } catch (FileNotFoundException e) {
                            System.out.println("File Not Found.");
                            e.printStackTrace();
                        }
                        ByteArrayEntity fileBytes = new ByteArrayEntity(myData);
                        file.delete();
                        aSyncClient.post(null,getAbsoluteUrl("/enrollments"), fileBytes, "audio/wav", responseHandler );
                    } catch(Exception ex){
                        System.out.println("Exception Error:"+ex.getMessage());
                    }
                }
            };
            countDowntimer.start();
        }
        catch(Exception ex)
        {
            System.out.println("Recording Error:" + ex.getMessage());
        }

    }

    public void authentication(String userId, String password, String contentLanguage, final AsyncHttpResponseHandler responseHandler) {
        this.aSyncClient.removeAllHeaders();
        this.aSyncClient.addHeader("VsitDeveloperId", this.developerId);
        this.aSyncClient.addHeader("UserId", userId);
        this.aSyncClient.addHeader("ContentLanguage", contentLanguage);
        this.aSyncClient.addHeader("VsitPassword", GetSHA256(password));

        try {
            final File file = File.createTempFile("tempfile", ".wav");
            final MediaRecorder myRecorder = new MediaRecorder();
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            myRecorder.setAudioSamplingRate(11250);
            myRecorder.setAudioChannels(1);
            myRecorder.setAudioEncodingBitRate(8000);
            myRecorder.setOutputFile(file.getAbsolutePath());
            myRecorder.prepare();
            myRecorder.start();
            byte[] myData = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(myData);
                for (int i = 0; i < myData.length; i++) {
                    System.out.print((char) myData[i]);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found.");
                e.printStackTrace();
            }
            CountDownTimer countDowntimer = new CountDownTimer(4800, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    try {
                        myRecorder.stop();
                        myRecorder.reset();
                        myRecorder.release();
                        byte[] myData = new byte[(int) file.length()];
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            fileInputStream.read(myData);
                        } catch (FileNotFoundException e) {
                            System.out.println("File Not Found.");
                            e.printStackTrace();
                        }
                        ByteArrayEntity fileBytes = new ByteArrayEntity(myData);
                        file.delete();
                        aSyncClient.post(null, getAbsoluteUrl("/authentications"), fileBytes, "audio/wav", responseHandler);
                    } catch (Exception ex) {
                        System.out.println("Exception Error:" + ex.getMessage());
                    }
                }
            };
            countDowntimer.start();
        } catch (Exception ex) {
            System.out.println("Recording Error:" + ex.getMessage());
        }

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}