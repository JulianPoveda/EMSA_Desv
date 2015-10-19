package ws_connect;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sistema.SQLite;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class UploaderFoto extends AsyncTask<String,Void,String> {

    URL url;
    HttpURLConnection conn = null;
    DataOutputStream dos = null;
    int serverResponseCode = 0;
    String upLoadServerUri = null;
    FileInputStream fileInputStream;

    private Context context;
    private SQLite FcnSQL;
    
    private String FolderAplicacion;
    

    public UploaderFoto(Context ctx, String folderAplicacion){
        this.context = ctx;
        this.FolderAplicacion = folderAplicacion;
        this.FcnSQL     = new SQLite(this.context,this.FolderAplicacion );
    }

    protected void onPreExecute() {
        upLoadServerUri = "http://192.168.0.16/galeriaOne/demo/UploadImage.php";
    }

    @Override
    protected String doInBackground(String... params) {

        String fileName      = params[0];
        String sourceFileUri = params[0];
        String orden         = params[1];
        String acta          = params[2];
        String cuenta        = params[3];
        String codigo        = this.FcnSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='equipo'");

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        try {
            // open a URL connection to the Servlet
            fileInputStream = new FileInputStream(sourceFile);
            url = new URL(upLoadServerUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs -- Permita el envio de datos.s
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"cuenta\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(cuenta);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"codigo\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(codigo);
            dos.writeBytes(lineEnd);
            
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"orden\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(orden);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"acta\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(acta);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){
                   /* String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                            +" http://www.androidexample.com/media/uploads/"
                            +uploadFileName;

                    messageText.setText(msg);*/
                //Toast.makeText(this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                //Se debe borrar el registro fotografico.
                Log.i("uploadFile", "File Upload Complete");
            }
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Upload file", "Exception : " + e.getMessage(), e);
        }finally {
            conn.disconnect();
        }
        return serverResponseCode+"";
    }

    protected void onPostExecute(Void result) {
       /* super.onPostExecute(result);
        pDialog.dismiss();*/
    }
}

