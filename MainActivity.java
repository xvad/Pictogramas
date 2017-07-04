package ucn.disc.cl.pictogramas;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Variables globales
    ImageView respuesta1 = null;
    ImageView respuesta2 = null;
    ImageView respuesta3 = null;
    ImageView cuadro1 = null;
    ImageView cuadro2 = null;
    ImageView cuadro3 = null;
    List<Integer> numRandoms;
    //Lista que contiene los cuadros yas respondidos
    final List<Integer> numerosCuadros = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imagenRespuesta = (ImageView) findViewById(R.id.imagenRespuesta);
        respuesta1 = (ImageView) findViewById(R.id.respuesta1);
        respuesta2 = (ImageView) findViewById(R.id.respuesta2);
        respuesta3 = (ImageView) findViewById(R.id.respuesta3);
        cuadro1 = (ImageView) findViewById(R.id.cuadro1);
        cuadro2 = (ImageView) findViewById(R.id.cuadro2);
        cuadro3 = (ImageView) findViewById(R.id.cuadro3);

        //Forzamos la orientaciones a horizontal
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.d("Orientacion", String.valueOf(getRequestedOrientation()));

        //inicializacion de la base de datos
        {
            FlowManager.init(new FlowConfig.Builder(getApplicationContext())
                    .openDatabasesOnInit(true)
                    .build());
        }

        //Vemos si la base de datos ya tiene oraciones
        List<Oracion> oracionList = SQLite.select().from(Oracion.class).queryList();
        if (oracionList.size() == 0) {

            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.laniniabaila);

            //cuadro2.setImageBitmap(image);
            Oracion ora = new Oracion();
            ora.setRespuesta("La niña baila");
            ora.setImagen(getEncodedImage(image));
            ora.save();
            Bitmap imageCuadro = BitmapFactory.decodeResource(getResources(), R.drawable.cuadrovacio);
            image = BitmapFactory.decodeResource(getResources(), R.drawable.la);
            Palabra palabra = new Palabra();
            palabra.setImagen(getEncodedImage(image));
            palabra.setNombre("La");
            palabra.oracionID = ora.getId();
            palabra.save();

            image = BitmapFactory.decodeResource(getResources(), R.drawable.ninia);
            palabra = new Palabra();
            palabra.setImagen(getEncodedImage(image));
            palabra.setNombre("niña");
            palabra.oracionID = ora.getId();
            palabra.save();

            image = BitmapFactory.decodeResource(getResources(), R.drawable.baila);
            palabra = new Palabra();
            palabra.setImagen(getEncodedImage(image));
            palabra.setNombre("baila");
            palabra.oracionID = ora.getId();
            palabra.save();

        }

        //Recuperamos la lista de oraciones
        //Una buena opcion es tomar solo la oracion q se ocupara mediante un WHERE esto no se hizo por ser solo un demo
        oracionList = SQLite.select().from(Oracion.class).queryList();


        numRandoms = new ArrayList<Integer>();
        int aleatorio;
        int k = 0;

        //Recorremos la lista de oraciones (Al pasar a otro nivel ese cero se debe aumentar para la siguiente oracion)
        for (final Palabra palabrasOracion : oracionList.get(0).palabras) {

            while (true) {
                aleatorio = generaNumeroAleatorio(0, oracionList.get(0).palabras.size() - 1);
                if (!numRandoms.contains(aleatorio)) {
                    break;
                }
            }

            numRandoms.add(aleatorio);
            Log.d("Numero Aletorio ", String.valueOf(aleatorio));
            if (aleatorio == 0) {
                respuesta1.setImageBitmap(getDecodeImage(palabrasOracion.getImagen()));
                respuesta1.setTag(palabrasOracion.getNombre());
            }

            if (aleatorio == 1) {
                respuesta2.setImageBitmap(getDecodeImage(palabrasOracion.getImagen()));
                respuesta2.setTag(palabrasOracion.getNombre());
            }

            if (aleatorio == 2) {
                respuesta3.setImageBitmap(getDecodeImage(palabrasOracion.getImagen()));
                respuesta3.setTag(palabrasOracion.getNombre());
            }

            k++;

        }

        //Al presionar la respuesta 1
        respuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!numerosCuadros.contains(1)) {
                    respuesta1.setVisibility(View.INVISIBLE);
                    if (numerosCuadros.size() == 0) {
                        cuadro1.setImageDrawable(respuesta1.getDrawable());
                    }
                    if (numerosCuadros.size() == 1) {
                        cuadro2.setImageDrawable(respuesta1.getDrawable());
                    }
                    if (numerosCuadros.size() == 2) {
                        cuadro3.setImageDrawable(respuesta1.getDrawable());
                    }

                    if (isCorrecta(numerosCuadros.size(), respuesta1.getId(), respuesta1)) {
                        numerosCuadros.add(1);
                    }
                }
            }
        });

        //Al presionar la respuesta 2
        respuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!numerosCuadros.contains(2)) {
                    respuesta2.setVisibility(View.INVISIBLE);
                    if (numerosCuadros.size() == 0) {
                        cuadro1.setImageDrawable(respuesta2.getDrawable());
                    }
                    if (numerosCuadros.size() == 1) {
                        cuadro2.setImageDrawable(respuesta2.getDrawable());
                    }
                    if (numerosCuadros.size() == 2) {
                        cuadro3.setImageDrawable(respuesta2.getDrawable());
                    }
                    if (isCorrecta(numerosCuadros.size(), respuesta2.getId(), respuesta2)) {
                        numerosCuadros.add(2);
                    }
                }
            }
        });

        //Al presionar la respuesta 3
        respuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!numerosCuadros.contains(3)) {
                    respuesta3.setVisibility(View.INVISIBLE);
                    if (numerosCuadros.size() == 0) {
                        cuadro1.setImageDrawable(respuesta3.getDrawable());
                    }
                    if (numerosCuadros.size() == 1) {
                        cuadro2.setImageDrawable(respuesta3.getDrawable());
                    }
                    if (numerosCuadros.size() == 2) {
                        cuadro3.setImageDrawable(respuesta3.getDrawable());
                    }
                    if (isCorrecta(numerosCuadros.size(), respuesta3.getId(), respuesta3)) {
                        numerosCuadros.add(3);
                    }
                }

            }
        });

    }


    //Metodo que verifica se la respuesta seleccionada es la correcta
    public boolean isCorrecta(final int posicion, int idRespuesta, final ImageView respuesta) {
        List<Oracion> oracionList = SQLite.select().from(Oracion.class).queryList();
        Handler handler = new Handler();
        if (oracionList.get(0).palabras.get(posicion).getNombre().equals(respuesta.getTag())) {

            if (posicion == 2) {

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Felicidades");
                alertDialog.setMessage("A completado el demo del juego!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            return true;
        } else {
            //Obtenemos la ruta del recuerdo vacio
            String uri = "@drawable/cuadrovacio";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            final Drawable res = getResources().getDrawable(imageResource);

            //Se utiliza un pequeño hilo para cambiar la respuesta
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (posicion == 0 && numerosCuadros.size() == 0) {
                        cuadro1.setImageDrawable(res);
                    }
                    if (posicion == 1 && numerosCuadros.size() == 1) {
                        cuadro2.setImageDrawable(res);
                    }
                    if (posicion == 2 && numerosCuadros.size() == 2) {
                        cuadro3.setImageDrawable(res);
                    }
                respuesta.setVisibility(View.VISIBLE);

                }
            }, 1200);

            return false;
        }

    }

    //Decodifica un String imagen en base64 para obtener su imagen
    public Bitmap getDecodeImage(String imagen) {
        byte[] dataDec = Base64.decode(imagen, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(dataDec, 0,
                dataDec.length);
        return bitmap;
    }

    //Codifica una imagen en base64 para guardarla en la base de datoss
    public String getEncodedImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    //Metodo que genera un numero aleatorio
    public static int generaNumeroAleatorio(int minimo, int maximo) {
        int num = (int) Math.floor(Math.random() * (maximo - minimo + 1) + (minimo));
        return num;
    }
}
