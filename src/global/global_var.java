package global;

import android.os.Environment;
import java.io.File;

public interface global_var {
    final int DIALOG_WARNING        = 1;
    final int DIALOG_ERROR          = 2;
    final int DIALOG_INFORMATIVE    = 3;


    final int _GET_HOUR             = 1;
    final int _GET_DATE             = 2;

    final String _ERROR_DATE        ="Bad_1";
    final String _ERROR_TIME        ="Bad_2";


    /**
     * Constantes para el manejo de las fotos
     */
    final int   _SIZZE_BUFFER       = 5;
    final int   _WIDTH_PICTURE      = 500;
    final int   _HEIGHT_PICTURE     = 500;


    public static String name_database      = "BdDesviaciones_Lecturas";
    public static String path_files_app     = Environment.getExternalStorageDirectory() + File.separator + "EMSA";
    //public static String sub_path_pictures  = "Fotos";

}
